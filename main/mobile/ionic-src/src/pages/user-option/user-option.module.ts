import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {UserOptionPage} from './user-option';

@NgModule({
  declarations: [
    UserOptionPage,
  ],
  imports: [
    IonicPageModule.forChild(UserOptionPage),
  ],
  exports: [
    UserOptionPage
  ]
})
export class UserOptionPageModule {}
