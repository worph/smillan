import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {AnnounceComponent} from './announce';

@NgModule({
  declarations: [
    AnnounceComponent,
  ],
  imports: [
    IonicPageModule.forChild(AnnounceComponent),
  ],
  exports: [
    AnnounceComponent
  ]
})
export class AnnounceComponentModule {}
