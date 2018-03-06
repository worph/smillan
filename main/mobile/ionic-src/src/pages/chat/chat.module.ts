import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {ChatPage} from './chat';
import {ChatComponentModule} from '../../components/chat/chat.module';
import {ChatSendComponentModule} from '../../components/chat-send/chat-send.module';

@NgModule({
  declarations: [
    ChatPage,
  ],
  imports: [
    IonicPageModule.forChild(ChatPage),
    ChatComponentModule,
    ChatSendComponentModule
  ],
  exports: [
    ChatPage
  ]
})
export class ChatPageModule {}
