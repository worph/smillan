import {Component, Inject, ViewChild} from '@angular/core';
import {Content, IonicPage, NavController, NavParams} from 'ionic-angular';
import {ChatPage} from "../chat/chat";
import {ProfileEvent} from "../../components/announce/announce";
import {AuthentifiedUserProvider} from "../../providers/authentified-user/authentified-user";
import {ProfilesProvider} from "../../providers/profiles/profiles";
import {ChatRoomsProvider, RoomHandle} from "../../providers/chat-rooms/chat-rooms";

@IonicPage()
@Component({
  selector: 'page-display-announce',
  templateUrl: 'display-announce.html',
})
export class DisplayAnnouncePage {
  @ViewChild(Content) content: Content;

  chatHandle: RoomHandle;
  id: string;
  msg: string;
  item: any;
  currentUser: any = null;

  constructor(public navCtrl: NavController,
              public navParams: NavParams,
              public chatRoomService: ChatRoomsProvider,
              public  profilesService: ProfilesProvider,
              public  authentifiedUserService: AuthentifiedUserProvider) {
    this.item = navParams.get("announce");
    this.id = this.item.chatId;
    chatRoomService.getConnectionEvent().onPublishOnce(()=>{
      let owner = chatRoomService.getUserToken().username;
      if (this.id != null) {
        if (owner == this.id) {
          console.error("can't speak to yourself");
          navCtrl.pop();
        }
        this.chatHandle = chatRoomService.getRoomHandle(this.id);
      }

    });
    this.authentifiedUserService.connect().then(() => {
      this.currentUser = this.authentifiedUserService.getUserData();
    });
  }

  ionViewDidEnter() {
    console.info("enter room");
    this.chatHandle.updateUnseenMessage();
  }

  ionViewWillLeave() {
    console.info("exit room");
    this.chatHandle.updateUnseenMessage();
  }

  chatWhithUser($event: ProfileEvent) {
    let profile = $event.profile;
    let pass: boolean = true;
    if (this.currentUser == null) {
      pass = false;
    } else {
      this.currentUser.profiles.forEach((profileItem) => {
        if (profile.id === profileItem.id) {
          console.warn("can't speak to yourthis");
          pass = false;
          return;
        }
      });
    }
    if (pass) {
      this.navCtrl.push(ChatPage, {
        id: profile.xmppId
      })
    }
  }

  sendMessage() {
    this.chatHandle.sendMsg(this.msg);
    this.msg = "";
  }

  scrollBottom() {
    setTimeout(() => {
      this.content.scrollToBottom();
    })
  }
}
