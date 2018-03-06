import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {AnnounceFeedPage} from './announce-feed';
import {AnnounceGridComponentModule} from '../../components/announce-grid/announce-grid.module';

@NgModule({
  declarations: [
    AnnounceFeedPage,
  ],
  imports: [
    IonicPageModule.forChild(AnnounceFeedPage),
    AnnounceGridComponentModule
  ],
  exports: [
    AnnounceFeedPage
  ]
})
export class AnnounceFeedPageModule {}
