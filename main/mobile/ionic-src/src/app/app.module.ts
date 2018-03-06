/* main */
import {NgModule, ErrorHandler} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {IonicApp, IonicModule, IonicErrorHandler} from 'ionic-angular';
import {MyApp} from './app.component';

/* angular */
import {HttpModule} from '@angular/http';
import {UpgradeModule} from '@angular/upgrade/static';
import { CookieModule } from 'ngx-cookie';

/* ionic */
import {StatusBar} from '@ionic-native/status-bar';
import {SplashScreen} from '@ionic-native/splash-screen';
import {IonicStorageModule} from '@ionic/storage';
import {Camera} from '@ionic-native/camera';
import {Device} from '@ionic-native/device';
import {Network} from '@ionic-native/network';
import { LocalNotifications } from '@ionic-native/local-notifications';

/* providers */
import {CategoriesProvider} from '../providers/categories/categories';
import {ImageProvider} from '../providers/image/image';
import {StorageProvider} from '../providers/storage/storage';
import {DeviceProvider} from '../providers/device/device';
import {AuthentifiedUserProvider} from '../providers/authentified-user/authentified-user';
import {GroupChatIdStorageProvider} from '../providers/group-chat-id-storage/group-chat-id-storage';
import {NetworkProvider} from '../providers/network/network';
import {AnnouncesProvider} from '../providers/announces/announces';
import {ChatsProvider} from '../providers/chats/chats';
import {ChatRoomsProvider} from '../providers/chat-rooms/chat-rooms';
import {ProfilesProvider} from '../providers/profiles/profiles';
import { NotificationProvider } from '../providers/notification/notification';
import { DynamicCustomizerProvider } from '../providers/dynamic-customizer/dynamic-customizer';

/* components */
import {AnnounceGridComponentModule} from '../components/announce-grid/announce-grid.module';
import {ImagePickerComponentModule} from '../components/image-picker/image-picker.module';
import {ChatComponentModule} from '../components/chat/chat.module';
import {ChatSendComponentModule} from '../components/chat-send/chat-send.module';
import {AnnounceComponentModule} from "../components/announce/announce.module";
import {MasonryModule} from '../components/angular2-masonry/angular2-masonry';

/* page */
import {TabsPage} from '../pages/tabs/tabs';

import {MapPageModule} from '../pages/map/map.module';
import {ProfilePageModule} from '../pages/profile/profile.module';
import {ChatListPageModule} from '../pages/chat-list/chat-list.module';
import {AuthPageModule} from '../pages/auth/auth.module';
import {AddAnnouncePageModule} from '../pages/add-announce/add-announce.module';
import {ChatPageModule} from "../pages/chat/chat.module";
import {UserOptionPageModule} from "../pages/user-option/user-option.module";
import {DisplayAnnouncePageModule} from "../pages/display-announce/display-announce.module";
import {FilterSelectPageModule} from "../pages/filter-select/filter-select.module";
import {AnnounceFeedPageModule} from "../pages/announce-feed/announce-feed.module";
import {PosterDisplayPageModule} from "../pages/poster-display/poster-display.module";
import {CategoryChooserPageModule} from "../pages/category-chooser/category-chooser.module";
import { PropertiesProvider } from '../providers/properties/properties';
/**/

/* angular 1 service export function*/
export function angular1GoogleAPIService(injector: any) {
  return injector.get('GoogleAPIService');
}

export function angular1BackGroundWorker(injector: any) {
  return injector.get('BackGroundWorker');
}

export function angular1LocationService(injector: any) {
  return injector.get('LocationService');
}

export function angular1PropertiesService(injector: any) {
  return injector.get('PropertiesService');
}

export function angular1SmilanHostService(injector: any) {
  return injector.get('SmilanHostService');
}

export function angular1UploadService(injector: any) {
  return injector.get('UploadService');
}

/* NG module */
@NgModule({
  declarations: [
    MyApp,
    TabsPage
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp, {
      // Configs for app
      tabsHideOnSubPages: true
    }),
    HttpModule,
    IonicStorageModule.forRoot({
      name: '__mydb',
      driverOrder: ['indexeddb', 'websql','sqlite']
    }),
    UpgradeModule,

    CookieModule.forRoot(),

    //component module
    AnnounceComponentModule,
    AnnounceGridComponentModule,
    ImagePickerComponentModule,
    ChatComponentModule,
    ChatSendComponentModule,
    MasonryModule,

    //page module
    MapPageModule,
    ProfilePageModule,
    ChatListPageModule,
    AuthPageModule,
    AddAnnouncePageModule,
    ChatPageModule,
    UserOptionPageModule,
    DisplayAnnouncePageModule,
    FilterSelectPageModule,
    AnnounceFeedPageModule,
    PosterDisplayPageModule,
    CategoryChooserPageModule
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    TabsPage
  ],
  providers: [
    /* ionic */
    StatusBar,
    SplashScreen,
    Camera,
    Device,
    Network,
    LocalNotifications,
    {provide: ErrorHandler, useClass: IonicErrorHandler},

    /* angular 1 module */
    {provide: 'BackGroundWorker', useFactory: angular1BackGroundWorker, deps: ['$injector']},
    {provide: 'GoogleAPIService', useFactory: angular1GoogleAPIService, deps: ['$injector']},
    {provide: 'LocationService', useFactory: angular1LocationService, deps: ['$injector']},
    {provide: 'PropertiesService', useFactory: angular1PropertiesService, deps: ['$injector']},
    {provide: 'SmilanHostService', useFactory: angular1SmilanHostService, deps: ['$injector']},
    {provide: 'UploadService', useFactory: angular1UploadService, deps: ['$injector']},

    /* providers */
    CategoriesProvider,
    ImageProvider,
    GroupChatIdStorageProvider,
    DeviceProvider,
    AuthentifiedUserProvider,
    StorageProvider,
    NetworkProvider,
    AnnouncesProvider,
    ChatsProvider,
    ChatRoomsProvider,
    ProfilesProvider,
    NotificationProvider,
    DynamicCustomizerProvider,
    PropertiesProvider
  ]
})
export class AppModule {

  constructor() {
  }
}
