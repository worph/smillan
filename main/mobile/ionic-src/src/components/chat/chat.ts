import {Component, EventEmitter, Input, OnChanges, Output, SimpleChange} from '@angular/core';
import {ProfilesProvider} from "../../providers/profiles/profiles";
import {ChatMessage, ChatRoomsProvider, RoomHandle} from "../../providers/chat-rooms/chat-rooms";

declare let moment: any;

export interface ChatDisplayMessage {
  msg: string;
  owned: boolean;
  username: string;
  formatedTime: string;
  time: Date;
}

export interface ProfileEvent {
  profile: any;
  event: any;
}

@Component({
  selector: 'chat',
  templateUrl: 'chat.html'
})
export class ChatComponent implements OnChanges {
  msgs: ChatDisplayMessage[] = [];
  profilesData: any = [];
  ownerUsername: string = "";
  chatHandle: RoomHandle;

  @Input() chatid: string = "";
  @Output('scollBottomNedded') scollBottomNedded: EventEmitter<any> = new EventEmitter();
  @Output('userClick') onUserClick: EventEmitter<ProfileEvent> = new EventEmitter();

  constructor(public chatRoomService: ChatRoomsProvider,
              public  profilesService: ProfilesProvider) {
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

  loadProfile(jid):void{
    if(this.profilesData[jid]==null){
      this.profilesService.searchWithChatId(jid).then((data) => {
        if (data === undefined) {
          console.error("unknown user");
        } else {
          this.profilesData[jid] = data;
        }
      },  (err) => {
        console.error("profile data not found : ", err);
      });
    }
  }

  init() {
    this.chatRoomService.getConnectionEvent().onPublishOnce(()=>{
      this.ownerUsername = this.chatRoomService.getUserToken().username;
      if (this.chatid != null && this.chatid != "") {
        this.chatHandle = this.chatRoomService.getRoomHandle(this.chatid);

        /* retreive user data */
        this.chatHandle.getUsers().onElement((userjid) => {
          this.loadProfile(userjid);
        });

        /* retreive handle message (and mark as seen) */
        this.chatHandle.updateUnseenMessage();

        /* register chat callback */
        this.chatHandle.getMsgs().onElement( (message:ChatMessage) => {
          this.loadProfile(message.from);
          this.displayMsg(message.from, message.text, message.stamp);
        });

      }
    });
  }

  userPopup (profile, $event) {
    this.onUserClick.emit({
      event: event,
      profile: profile
    });
  }

  updateTyping() {

  }

  static prettyDate(date: any) {
    return moment(date).format('DD-MM-YYYY HH:mm');
  }


  displayMsg(username: string, text: string, stamp: Date) {
    let message: ChatDisplayMessage = {
      msg: text,
      owned: username === this.ownerUsername,
      username: username,
      formatedTime: ChatComponent.prettyDate(stamp),
      time: stamp
    };
    this.msgs.push(message);
    if (this.scollBottomNedded != null) {
      this.scollBottomNedded.emit({});
    }
  }

}
