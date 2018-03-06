import {Component, Inject} from '@angular/core';
import {ProfilePage} from '../profile/profile';
import {ChatListPage} from '../chat-list/chat-list';
import {AnnounceFeedPage} from "../announce-feed/announce-feed";
import {PosterDisplayPage} from "../poster-display/poster-display";
import {SplashScreen} from '@ionic-native/splash-screen';
import {GroupChatIdStorageProvider} from "../../providers/group-chat-id-storage/group-chat-id-storage";
import {AuthentifiedUserProvider} from "../../providers/authentified-user/authentified-user";
import {NetworkProvider} from "../../providers/network/network";
import {ChatMessage, ChatRoomsProvider, RoomHandle} from "../../providers/chat-rooms/chat-rooms";
import {NotificationProvider} from "../../providers/notification/notification";
import {DynamicCustomizerProvider} from "../../providers/dynamic-customizer/dynamic-customizer";
import {ProfilesProvider} from "../../providers/profiles/profiles";

@Component({
  templateUrl: 'tabs.html'
})
export class TabsPage {

  tab1Root:any = PosterDisplayPage;
  tab1RootTitle:string = "Home";
  tab1RootIcon:string = "home";

  totalUnseen:number=null;
  atLesatOneChat:boolean=false;
  chatPageTitle:string="Chat";

  tabs:any[];

  constructor(
    private chatRoomService: ChatRoomsProvider,
    @Inject('PropertiesService') public propertiesService:any,
    private networkService:NetworkProvider,
    private userService:AuthentifiedUserProvider,
    private splashScreen: SplashScreen,
    groupChatIdStorage:GroupChatIdStorageProvider,
    private localNotifications: NotificationProvider,
    private custo:DynamicCustomizerProvider,
    private profiles:ProfilesProvider) {
    let self = this;
    if(custo.persoTagPresent("generic")){
      this.tab1Root = AnnounceFeedPage;
      this.tab1RootTitle = "Home";
      this.tab1RootIcon = "home";
      this.tabs= [
        { title: this.chatPageTitle, root: ChatListPage, icon: "chatboxes" },
        { title: "Profile", root: ProfilePage, icon: "contacts" },
      ];
    }else {
      this.tab1Root = PosterDisplayPage;
      this.tab1RootTitle = "Home";
      this.tab1RootIcon = "home";
      this.tabs= [
        { title: "Newsfeed", root: AnnounceFeedPage, icon: "logo-octocat",badge:null },
        { title: this.chatPageTitle, root: ChatListPage, icon: "chatboxes" ,badge:null},
        { title: "Profile", root: ProfilePage, icon: "contacts",badge:null },
      ];
    }
    this.chatRoomService.getConnectionEvent().onPublishOnce( ()=>{
      this.chatRoomService.getRoomsHandles().onElement((roomHandle:RoomHandle) => {
        self.localAddHandle(roomHandle);
        self.changeChatState();
      });
    });
    this.appBootstrap();
  }

  setBadgeValue(title:string,value:number){
    this.tabs.forEach((elem)=>{
      if(elem.title==title){
        elem.badge=value;
      }
    })
  }

  appBootstrap(){
    console.info('Smillan app bootstrap on : '+this.propertiesService.getProperty("server.host.api"));
    this.networkService.connect();
    this.splashScreen.hide();
  }

  changeChatState(){
    if(!this.atLesatOneChat) {
      this.atLesatOneChat = true;
    }
  }

  localAddHandle(roomHandle:RoomHandle) {
    let self = this;
    roomHandle.getMsgs().onElement((message:ChatMessage) => {
      if (message.stamp.valueOf() > roomHandle.getLastTimeUserSawThisHandle().valueOf()) {
        if(self.totalUnseen==null){
          self.totalUnseen=0;//enable badge
        }
        if(this.chatRoomService.getUserToken().username!=message.from){
          self.totalUnseen++;
          this.profiles.searchWithChatId(message.from).then(profile => {
            this.localNotifications.notifyExternal(profile.profileName,message.text,profile.avatar.url);
          });
        }
        this.setBadgeValue(this.chatPageTitle,self.totalUnseen);
      }
    });
    roomHandle.getUnseenEvent().on((unseennumber) => {
        self.totalUnseen = 0;
        this.chatRoomService.getRoomsHandles().get().forEach((handle:RoomHandle) => {
          self.totalUnseen += handle.getMessageUnseenNumber();
        });
        if(self.totalUnseen==0){
          self.totalUnseen=null;//disable badge
        }
        this.setBadgeValue(this.chatPageTitle,self.totalUnseen);
    });
  };
}
