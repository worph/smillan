import {Component} from '@angular/core';
import {IonicPage, NavController, NavParams} from 'ionic-angular';

@IonicPage()
@Component({
  selector: 'page-filter-select',
  templateUrl: 'filter-select.html',
})
export class FilterSelectPage {

  selected : string[] = [];
  available : string[] = [];
  originalSet : string[] = ["hello","hello3","omg","zomg","hello4"];
  searchText :string =  "";
  validateCallback : Function = null;

  constructor(public navCtrl: NavController, public navParams: NavParams) {
    this.validateCallback = navParams.get("callback");
    this.selected = navParams.get("initialSelection");
    if(this.selected==undefined){
      this.selected = [];
    }
    this.updateAvailableWithData();
  }

  ionViewDidLoad() {
  }

  updateAvailableWithData(){
    this.available = [];
    this.available = this.available.concat(this.originalSet);//list copy to avoid issue
    //remove selected
    this.available = this.available.filter((filereditem)=>{
      let contains = false;
      this.selected.forEach((selectedItem)=>{
        if(selectedItem===filereditem){
          contains=true;
        }
      })
      return !contains;
    })
    if(this.searchText!==""){
      this.available = this.available.filter((filereditem)=>{
        return filereditem.indexOf(this.searchText)!=-1;
      })
    }
  }

  delete(item){
    this.selected = this.selected.filter((filereditem)=>{
      return filereditem!=item;
    });
    this.updateAvailableWithData();
  }

  add(item){
    this.selected.push(item);
    this.updateAvailableWithData();
  }

  search(event){
    this.updateAvailableWithData();
  }

  validate(event){
    if(this.validateCallback!=null){
      this.validateCallback([].concat(this.selected));
    }
    this.navCtrl.pop();
  }

}
