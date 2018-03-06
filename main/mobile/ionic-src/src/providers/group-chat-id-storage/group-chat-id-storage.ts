import {EventEmitter, Inject, Injectable} from '@angular/core';
import 'rxjs/add/operator/map';
import {StorageProvider} from "../storage/storage";
import {ChatRoomsProvider, RoomHandle} from "../chat-rooms/chat-rooms";

@Injectable()
export class GroupChatIdStorageProvider {
  chatIds: string[] = [];
  event: EventEmitter<string> = new EventEmitter();
  STORAGE_KEY: string = "app.chat.group.ids";

  constructor(private storage: StorageProvider,
              public chatRoomService: ChatRoomsProvider) {
    chatRoomService.getConnectionEvent().onPublishOnce(()=>{
      //notify chat service of room from storage and future ones
      this.onIdEvent((item: string) => {
        chatRoomService.getRoomHandle(item);//create all the handle for group chat
      });

      //listen to new rooms from chat service
      chatRoomService.getRoomsHandles().onElement((roomHandle) => {
        this.localAddHandle(roomHandle);
      });

      //fetch stored id in NVM => add id will trigger get room handle
      storage.loadOrCreateArray(this.STORAGE_KEY).then((val) => {
        try{
          if (val != null) {
            val.forEach((item) => {
              this.addIdd(item);
            });
          }
        }catch(e){
          console.error("invalid key for chat id");
          storage.deleteKey(this.STORAGE_KEY);
        }
      });
    });
  }

  localAddHandle(roomHandle:RoomHandle):void {
    let id = roomHandle.getRoomId();
    if (id.indexOf("conference") !== -1) {
      if (!this.alreadyExist(id)) {
        this.addIdd(id);
      }
    }
  }

  alreadyExist(id: string): boolean {
    let exist: boolean = false;
    this.chatIds.forEach((item) => {
      if (item == id) {
        exist = true;
      }
    });
    return exist;
  }

  addIdd(id: string) {
    if(!this.alreadyExist(id)){
      this.chatIds.push(id);
      this.event.emit(id);
      this.storage.store(this.STORAGE_KEY,this.chatIds);
    }
  }

  /*
  On call on this method all the ids curently present in the list will be emited
  afterward if a new item is added to the list the event will be notified
   */
  onIdEvent(callback: (item: string) => void) {
    this.event.subscribe(callback);
    this.chatIds.forEach(callback);
  }

}
