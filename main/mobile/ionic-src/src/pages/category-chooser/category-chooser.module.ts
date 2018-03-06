import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {CategoryChooserPage, ValuesPipe} from './category-chooser';

@NgModule({
  declarations: [
    CategoryChooserPage,
    ValuesPipe
  ],
  imports: [
    IonicPageModule.forChild(CategoryChooserPage),
  ],
  exports: [
    CategoryChooserPage
  ],
  providers:[
    ValuesPipe
  ]
})
export class CategoryChooserPageModule {}
