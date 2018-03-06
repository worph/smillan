import {Component, ViewChild} from '@angular/core';
import {IonicPage, ModalController, NavController, NavParams} from 'ionic-angular';
import {AddAnnouncePage} from '../add-announce/add-announce';
import {AnnounceGridComponent} from "../../components/announce-grid/announce-grid";
import {CategoryChooserPage} from "../category-chooser/category-chooser";
import {DynamicCustomizerProvider} from "../../providers/dynamic-customizer/dynamic-customizer";
import {CategoriesProvider} from "../../providers/categories/categories";


/**
 * Generated class for the AnnounceFeedPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-announce-feed',
  templateUrl: 'announce-feed.html',
})
export class AnnounceFeedPage {

  selected: string[] = [];
  @ViewChild(AnnounceGridComponent) announceGridComponent: AnnounceGridComponent;

  constructor(
    public navCtrl: NavController,
    public navParams: NavParams,
    public modalCtrl: ModalController,
    public category: CategoriesProvider,
    public custo: DynamicCustomizerProvider
  ) {
  }

  ngAfterViewInit(): void {
    setTimeout(()=>{
      let tagFilter = []
      if(this.custo.persoTagPresent("utc")){
        tagFilter = [].concat(["UTC"]);
      }
      if(this.custo.persoTagPresent("hec")){
        tagFilter = [].concat(["HEC"]);
      }
      if(this.custo.persoTagPresent("generic")){
        tagFilter = [].concat(["generic"]);
      }
      this.announceGridComponent.setOrderDateFilter();
      this.announceGridComponent.setCategoryFilter(tagFilter);
      this.announceGridComponent.doRefresh();
      this.goToCategory(null);
    },200);
  }

  doRefresh(refresher){
    let self = this;
    setTimeout(() => {
      self.announceGridComponent.doRefresh();
      refresher.complete();
    });
  }

  goToAddAnnounce(event){
    this.navCtrl.push(AddAnnouncePage,{
      onadd:(announce)=>{
          this.announceGridComponent.doRefresh();
      }
    });
  }

  goToCategory(event){
      let self= this;
      let modal = self.modalCtrl.create(CategoryChooserPage,{
        callback:function(selected){
          self.selected = [].concat(selected);
          self.announceGridComponent.setOrderDateFilter();
          self.announceGridComponent.setCategoryFilter(selected);
          self.announceGridComponent.doRefresh();
        },
        initialSelection:[].concat(self.selected)
      })
      modal.present();
    }

  /*goToCategory(event){
    let self= this;
    let modal = self.modalCtrl.create(FilterSelectPage,{
      callback:function(selected){
        self.selected = [].concat(selected);
        self.announceGridComponent.setCategoryFilter(selected);
        self.announceGridComponent.doRefresh();
      },
      initialSelection:[].concat(self.selected)
    })
    modal.present();
  }*/
}
