import {Component, Inject, Input, OnChanges, SimpleChange} from '@angular/core';
import {ProfilesProvider} from "../../providers/profiles/profiles";
import {ChatRoomsProvider, RoomHandle} from "../../providers/chat-rooms/chat-rooms";

/**
 * Generated class for the ChatSendComponent component.
 *
 * See https://angular.io/docs/ts/latest/api/core/index/ComponentMetadata-class.html
 * for more info on Angular Components.
 */
@Component({
  selector: 'chat-send',
  templateUrl: 'chat-send.html'
})
export class ChatSendComponent implements OnChanges {

  @Input() chatid: string = "";
  chatHandle: RoomHandle;
  msg: string;

  constructor(
              public chatRoomService: ChatRoomsProvider,
              public  profilesService: ProfilesProvider) {
  }

  sendMessage() {
    if(this.msg=="" || this.msg==null || this.msg==" "){
      return;
    }
    this.chatHandle.sendMsg(this.msg);
    this.msg = "";
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    for (let propName in changes) {
      let changedProp = changes[propName];
      let to = JSON.stringify(changedProp.currentValue);
      if (changedProp.isFirstChange()) {
        console.log(`Initial value of ${propName} set to ${to}`);
        this.init();
      } else {
        console.error("invalid case");
      }
    }
  }

  init() {
    this.chatRoomService.getConnectionEvent().onPublishOnce((chatsServiceHandle) => {
      this.chatHandle = chatsServiceHandle.getRoomHandle(this.chatid);
    });
  }

  updateTyping() {

  }

  keypressed(keycode) {
    if (keycode == 13) {//13 is keycode for enter
      this.sendMessage();
    }
  }

}
