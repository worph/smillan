import {Component} from '@angular/core';
import {IonicPage, NavController, NavParams} from 'ionic-angular';
import {DynamicCustomizerProvider} from "../../providers/dynamic-customizer/dynamic-customizer";

@IonicPage()
@Component({
  selector: 'page-poster-display',
  templateUrl: 'poster-display.html',
})
export class PosterDisplayPage {

  constructor(
    public navCtrl: NavController,
    public navParams: NavParams,
    public custo:DynamicCustomizerProvider) {
  }

  ionViewDidLoad() {
  }

}
