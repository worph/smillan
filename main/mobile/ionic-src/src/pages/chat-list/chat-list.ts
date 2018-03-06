import {Component, Inject} from '@angular/core';
import {IonicPage, NavController, NavParams} from 'ionic-angular';
import {ChatPage} from "../chat/chat";
import {DisplayAnnouncePage} from "../display-announce/display-announce";
import {AnnouncesProvider} from "../../providers/announces/announces";
import {ProfilesProvider} from "../../providers/profiles/profiles";
import {ChatRoomsProvider, RoomHandle} from "../../providers/chat-rooms/chat-rooms";

@IonicPage()
@Component({
  selector: 'page-chat-list',
  templateUrl: 'chat-list.html',
})
export class ChatListPage {
  chatHandleList: RoomHandle[] = [];
  profilesData: any[] = [];

  announceHandleList: RoomHandle[] = [];
  announcesData: any[] = [];

  constructor(public navCtrl: NavController,
              public navParams: NavParams,
              public chatRoomService: ChatRoomsProvider,
              public  profilesService: ProfilesProvider,
              public announcesService: AnnouncesProvider) {
    chatRoomService.getConnectionEvent().onPublishOnce(()=>{
      chatRoomService.getRoomsHandles().onElement((roomHandle)=> {
        this.localAddHandle(roomHandle);
      });
    });
  }

  getAnnounceIDfromJid(jid: string): string {
    //debug-announce-3@conference.mobile.smillan.com => 3
    let firstpart = jid.split("@")[0];
    let decomp = firstpart.split("-");
    return decomp[decomp.length - 1];
  }

  localAddHandle(roomHandle:RoomHandle):void {
    console.info("add room handle");
    if (roomHandle.getRoomId().indexOf("conference") !== -1) {
      let announceID = this.getAnnounceIDfromJid(roomHandle.getRoomId());
      this.announcesService.load(announceID).then((data) => {
        this.announcesData[roomHandle.getRoomId()] = data;
      }).catch((err)=>{
        console.error(err);
      })
      this.announceHandleList.push(roomHandle);
    } else {
      /* retreive user data */
      roomHandle.getUsers().onElement((chatid) => {
        this.profilesService.searchWithChatId(chatid).then((data) => {
          if (data === undefined) {
            console.error("unknown user");
          } else {
            this.profilesData[chatid] = data;
          }
        }).catch((err)=>{
          console.error(err);
        });
      });
      this.chatHandleList.push(roomHandle);
    }
  };

  gotoAnnounce(announce) {
    this.navCtrl.push(DisplayAnnouncePage, {announce: announce});
  }

  gotoChat(chatHandle) {
    this.navCtrl.push(ChatPage, {id: chatHandle.jid});
  };

  ionViewDidLoad() {
  }

}
