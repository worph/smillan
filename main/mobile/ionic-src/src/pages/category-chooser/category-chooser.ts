import {Component, Pipe, PipeTransform} from '@angular/core';
import {
  AlertController, IonicPage, ModalController, NavController, NavParams, ToastController,
  ViewController
} from 'ionic-angular';
import {CategoriesProvider, Category} from "../../providers/categories/categories";
import {DynamicCustomizerProvider} from "../../providers/dynamic-customizer/dynamic-customizer";

@Pipe({name: 'values', pure: false})
export class ValuesPipe implements PipeTransform {
  transform(value: any, args: any[] = null): any {
    return Object.keys(value).map(key => value[key]);
  }
}

export interface CatNode {
  id?: string;
  children?:{};
  category?:SplitedCategory;
}

export interface SplitedCategory{
  value?:string;
  fullcategory?:Category;
}

@IonicPage()
@Component({
  selector: 'page-category-chooser',
  templateUrl: 'category-chooser.html',
})
export class CategoryChooserPage {

  thisCategory:CatNode ={
    id:"All",
    children:{}
  };

  available: Category[] = [];
  SEPARATOR: string = "/"
  validateCallback: (test: string[]) => string[] = null;
  private rootNode: CatNode;
  private popnumber: number;
  private modaleMode:boolean=true;

  constructor(public navCtrl: NavController,
              public modalCtrl: ModalController,
              public navParams: NavParams,
              public viewCtrl: ViewController,
              public categoryService:CategoriesProvider,
              private toastCtrl: ToastController,
              private alertCtrl: AlertController,
              private custo:DynamicCustomizerProvider) {
    this.validateCallback = navParams.get("callback");
    this.rootNode = navParams.get("rootnode");
    this.popnumber = navParams.get("popnumber");
    categoryService.getCategory().then((catList:Category[])=>{
      this.available=[];
      this.available=this.available.concat(catList);
      if(this.popnumber==undefined){
        this.popnumber=1;
      }
      if (this.rootNode == null) {
        this.updateRootNode();
      }
    });
  }

  ionViewDidLoad() {

  }

  updateRootNode(): void {
    this.rootNode = this.convertToHierarchy(this.splitCat(this.available));
    if(this.custo.persoTagPresent("utc")){
      this.rootNode = this.rootNode.children["UTC"];
    }
    if(this.custo.persoTagPresent("hec")){
      this.rootNode = this.rootNode.children["HEC"];
    }
    if(this.custo.persoTagPresent("generic")){
      this.rootNode = this.rootNode.children["generic"];
    }
  }

  splitCat(input: Category[]): SplitedCategory[][] {
    let ret: SplitedCategory[][] = [];
    input.forEach((item: Category) => {
      let arrayToPush = [];
      let newValue = item.value.split(this.SEPARATOR);
      newValue.forEach((itemCat)=>{
        arrayToPush.push({
          value:itemCat,
          fullcategory:item
        });
      });
      ret.push(arrayToPush);
    });
    return ret;
  }

  convertToHierarchy(arry: SplitedCategory[][] /* array of array of strings */): CatNode {
    // Build the node structure
    let rootNode: CatNode = {
        id: "root",
        children: {}
    }
    for (let i = 0; i < arry.length; i++) {
      let path:SplitedCategory[] = arry[i];
      this.buildNodeRecursive(rootNode, path, 0);
    }
    return rootNode;
  }

  buildNodeRecursive(node: CatNode, path: SplitedCategory[], idx: number): void {
    if (idx < path.length) {
      //add child that allow self selection of this category
      node.children[this.thisCategory.id]={
        id: this.thisCategory.id,
        children: {},
        category:node.category
      };

      //add this category to the tree
      let item = path[idx];
      let itemid = item.value;
      if (!node.children[itemid]) {
        node.children[itemid] = {
          id: itemid,
          children: {},
          category:item
        };
      }

      //add all children of this category recursively
      this.buildNodeRecursive(node.children[itemid], path, idx + 1);
    }
  }

  keysLength(item) {
    return Object.keys(item).length;
  }

  validateCatPassword(category:string):Promise<boolean>{
    return new Promise<boolean>((resolve, reject) => {
      if(!this.categoryService.isAuth(category)){
      let alert = this.alertCtrl.create({
        title: 'Type password for category',
        inputs: [
          {
            name: 'password',
            placeholder: 'Password',
            type: 'password'
          }
        ],
        buttons: [
          {
            text: 'Cancel',
            role: 'cancel',
            handler: data => {
              console.info('Cancel clicked');
            }
          },
          {
            text: 'OK',
            handler: (data) => {
              console.info('ok clicked',data);
              this.categoryService.authCategory(category,data.password).then((auth)=>{
                resolve(auth);
              }).catch((err)=>{
                console.error(err);
                resolve(false);
              });
              return true;
            }
          }
        ]
      });
      alert.present();
      }else{
        resolve(true);
      }
    });
  }

  validate(event, value: CatNode) {
    let authOk:Promise<boolean>;
    if(value.category.fullcategory.password){
      //popup password and auth
      authOk=this.validateCatPassword(value.category.fullcategory.value);
    }else {
      authOk=Promise.resolve(true);
    }
    authOk.then((authok)=>{
      if(!authok){
        let toast = this.toastCtrl.create({
          message: "Invalid password",
          duration: 5000,
          showCloseButton: true,
          dismissOnPageChange: true,
          position: 'top'
        });
        toast.present();
      }else{
        if(value.category.fullcategory==this.thisCategory){
          if (this.validateCallback != null) {
            this.validateCallback([]);
          }
          for (let i = 0; i < this.popnumber; i++) {
            this.navCtrl.pop();
          }
        }else if (Object.keys(value.children).length == 0) {
          //leaf
          if (this.validateCallback != null) {
            this.validateCallback([value.category.fullcategory.value]);//return the real path of the category
          }
          for (let i = 0; i < this.popnumber; i++) {
            this.navCtrl.pop();
          }
        } else {
          if(this.modaleMode){
            let modal = this.modalCtrl.create(CategoryChooserPage, {
              rootnode: value,
              popnumber: this.popnumber + 1,
              callback: this.validateCallback
            });
            modal.present();
          }else {
            this.navCtrl.push(CategoryChooserPage, {
              rootnode: value,
              popnumber: this.popnumber + 1,
              callback: this.validateCallback
            })
          }
        }
      }
    });
  }

  dismiss() {
    this.viewCtrl.dismiss();
  }

}
