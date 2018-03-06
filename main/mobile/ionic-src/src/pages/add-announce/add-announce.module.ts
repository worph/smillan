import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {AddAnnouncePage} from './add-announce';
import {ImagePickerComponentModule} from '../../components/image-picker/image-picker.module';
import {MasonryModule} from '../../components/angular2-masonry/angular2-masonry';

@NgModule({
  declarations: [
    AddAnnouncePage,
  ],
  imports: [
    IonicPageModule.forChild(AddAnnouncePage),
    ImagePickerComponentModule,
    MasonryModule
  ],
  exports: [
    AddAnnouncePage
  ]
})
export class AddAnnouncePageModule {}
