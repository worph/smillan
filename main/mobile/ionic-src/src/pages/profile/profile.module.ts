import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {ProfilePage} from './profile';
import {ImagePickerComponentModule} from '../../components/image-picker/image-picker.module';
import {AnnounceGridComponentModule} from '../../components/announce-grid/announce-grid.module';

@NgModule({
  declarations: [
    ProfilePage,
  ],
  imports: [
    IonicPageModule.forChild(ProfilePage),
    ImagePickerComponentModule,
    AnnounceGridComponentModule
  ],
  exports: [
    ProfilePage
  ]
})
export class ProfilePageModule {}
