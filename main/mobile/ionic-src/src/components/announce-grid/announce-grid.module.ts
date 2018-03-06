import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {AnnounceGridComponent} from './announce-grid';
import {MasonryModule} from '../angular2-masonry';
import {AnnounceComponentModule} from "../announce/announce.module";

@NgModule({
  declarations: [
    AnnounceGridComponent
  ],
  imports: [
    IonicPageModule.forChild(AnnounceGridComponent),
    MasonryModule,
    AnnounceComponentModule
  ],
  exports: [
    AnnounceGridComponent
  ]
})
export class AnnounceGridComponentModule {
  constructor(){  }
}
