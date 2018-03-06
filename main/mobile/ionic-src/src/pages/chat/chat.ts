import {Component, Inject, ViewChild} from '@angular/core';
import {Content, IonicPage, NavController, NavParams} from 'ionic-angular';
import {ProfilesProvider} from "../../providers/profiles/profiles";
import {ChatRoomsProvider, RoomHandle} from "../../providers/chat-rooms/chat-rooms";

@IonicPage()
@Component({
  selector: 'page-chat',
  templateUrl: 'chat.html',
})
export class ChatPage {
  chatHandle: RoomHandle;
  id: string;
  msg: string;

  @ViewChild(Content) content: Content;

  otherProfile: any;

  constructor(public navCtrl: NavController,
              public navParams: NavParams,
              public chatRoomService: ChatRoomsProvider,
              public  profilesService: ProfilesProvider) {
    this.id = navParams.get("id");

    profilesService.searchWithChatId(this.id).then((data) => {
      this.otherProfile = data;
    });
    chatRoomService.getConnectionEvent().onPublishOnce(()=>{
      let owner = chatRoomService.getUserToken().username;
      if (this.id != null) {
        if (owner == this.id) {
          console.error("can't speak to yourthis");
          navCtrl.pop();
        }
        this.chatHandle = chatRoomService.getRoomHandle(this.id);
      }

    });
  }

  sendMessage() {
    this.chatHandle.sendMsg(this.msg);
    this.msg = "";
  }

  ionViewDidLoad() {
  }

  updateTyping() {

  }

  scrollBottom() {
    setTimeout(() => {
      this.content.scrollToBottom();
    })
  }

}
