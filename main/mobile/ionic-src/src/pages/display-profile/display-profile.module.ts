import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {DisplayProfilePage} from './display-profile';

@NgModule({
  declarations: [
    DisplayProfilePage,
  ],
  imports: [
    IonicPageModule.forChild(DisplayProfilePage),
  ],
  exports: [
    DisplayProfilePage
  ]
})
export class DisplayProfilePageModule {}
