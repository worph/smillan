import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {ChatComponent} from './chat';

@NgModule({
  declarations: [
    ChatComponent,
  ],
  imports: [
    IonicPageModule.forChild(ChatComponent),
  ],
  exports: [
    ChatComponent
  ]
})
export class ChatComponentModule {}
