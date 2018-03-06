import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {DisplayAnnouncePage} from './display-announce';
import {ChatComponentModule} from '../../components/chat/chat.module';
import {ChatSendComponentModule} from '../../components/chat-send/chat-send.module';
import {AnnounceComponentModule} from "../../components/announce/announce.module";

@NgModule({
  declarations: [
    DisplayAnnouncePage,
  ],
  imports: [
    IonicPageModule.forChild(DisplayAnnouncePage),
    ChatComponentModule,
    ChatSendComponentModule,
    AnnounceComponentModule
  ],
  exports: [
    DisplayAnnouncePage
  ]
})
export class DisplayAnnouncePageModule {}
