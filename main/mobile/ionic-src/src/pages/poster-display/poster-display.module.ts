import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {PosterDisplayPage} from './poster-display';

@NgModule({
  declarations: [
    PosterDisplayPage,
  ],
  imports: [
    IonicPageModule.forChild(PosterDisplayPage),
  ],
  exports: [
    PosterDisplayPage
  ]
})
export class PosterDisplayPageModule {}
