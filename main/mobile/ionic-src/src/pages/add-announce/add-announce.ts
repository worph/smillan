import {Component, Inject} from '@angular/core';
import {FabContainer, IonicPage, ModalController, NavController, NavParams, ToastController} from 'ionic-angular';
import {Device} from '@ionic-native/device';
import {FilterSelectPage} from "../filter-select/filter-select";
import {CategoriesProvider} from "../../providers/categories/categories";
import {AuthentifiedUserProvider} from "../../providers/authentified-user/authentified-user";
import {Announce, AnnouncesProvider} from "../../providers/announces/announces";
import {CategoryChooserPage} from "../category-chooser/category-chooser";
import {ChatRoomsProvider} from "../../providers/chat-rooms/chat-rooms";

@IonicPage()
@Component({
  selector: 'page-add-announce',
  templateUrl: 'add-announce.html',
})
export class AddAnnouncePage {

  items: any = [];
  menuItems: any = [];
  ITEM_TEXT: string = "text";
  ITEM_IMAGE: string = "image";
  ITEM_CATEGORY: string = "category";
  ITEM_CATEGORY2: string = "category2";
  imagePresent: boolean;
  textPresent: boolean;
  title: string = "title";
  selectedProfileId;
  user;
  profiles;
  postAnnounceLoading:boolean=false;

  suucessCallback:(announce)=>void;

  constructor(public navCtrl: NavController,
              public navParams: NavParams,
              public device: Device,
              public modalCtrl: ModalController,
              private toastCtrl: ToastController,
              private category: CategoriesProvider,
              public  announcesService: AnnouncesProvider,
              public authentifiedUserService: AuthentifiedUserProvider,
              public chatRoomService: ChatRoomsProvider) {
    this.suucessCallback = this.navParams.get("onadd");
  }

  ionViewDidLoad() {
    this.clear();
    this.authentifiedUserService.connect().then(() => {
      this.user = this.authentifiedUserService.getUserData();
      this.profiles = this.user.profiles;
      if (this.profiles.length === 1) {
        this.selectedProfileId = this.profiles[0].id;
      }
    });
  }

  clear() {
    this.items = [];
    this.imagePresent = false;
    this.textPresent = false;
    this.addText();
    this.addCategory2();
    this.addImageMenuItem();
  }

  /**
   * Item management
   */
  addText() {
    this.items.push({
      type: this.ITEM_TEXT,
      value: ""
    });
    this.textPresent = true;
  }

  addCategory2() {
    let self = this;
    this.items.push({
      type: this.ITEM_CATEGORY2,
      selected: [],
      onDelete: function (item) {
        this.selected = this.selected.filter((filereditem) => {
          return filereditem != item;
        });
      },
      onAdd: function () {
        let itemthis = this;
        let modal = self.modalCtrl.create(CategoryChooserPage, {
          callback: (selected) => {
            itemthis.selected = [].concat(selected);
          },
          initialSelection: [].concat(itemthis.selected)
        });
        modal.present();
      }
    });
  }

  addCategory() {
    let self = this;
    this.items.push({
      type: this.ITEM_CATEGORY,
      selected: [],
      onDelete: function (item) {
        this.selected = this.selected.filter((filereditem) => {
          return filereditem != item;
        });
      },
      onAdd:  function () {
        let itemthis = this;
        let modal = self.modalCtrl.create(FilterSelectPage, {
          callback: (selected) => {
            itemthis.selected = [].concat(selected);
          },
          initialSelection: [].concat(itemthis.selected)
        });
        modal.present();
      }
    });
  }

  internalTakePicture(fab: FabContainer,source:string) {
    if (fab != undefined) {
      fab.close();
    }
    let imageItem = {
      type: this.ITEM_IMAGE,
      source: source,
      distantUrl: '',
      callback: null
    };
    imageItem.callback = ($event) => {
      imageItem.distantUrl = $event.image.distantUrl;
    };
    this.items.push(imageItem);
    this.imagePresent = true;
  };

  takePicture(parent: any, fab: FabContainer) {
    parent.internalTakePicture(fab,"camera");
  };

  addPicture(parent: any, fab: FabContainer) {
    parent.internalTakePicture(fab,"photolib");
  };

  removeItem(item) {

  }

  /**
   * announce button management
   */

  addImageMenuItem() {
    if(this.device.platform!="browser" && this.device.platform!=null) {//return null on browser
      this.menuItems.push({
        icon: "camera",
        callback: this.takePicture,
        parent: this
      });
    }
    this.menuItems.push({
      icon: "image",
      callback: this.addPicture,
      parent: this
    });
  }

  postAnnounce() {
    let mediaUrlTable = [];
    let geolocTable = [];
    let text = "";
    let type = "";
    let announceCategoriesString:string[]=[];
    //
    //parse item to construct announce data
    //
    for (let item of this.items) {
      switch (item.type) {
        case "geo": {
          geolocTable.push({
            lat: item.marker.getPosition().lat(),
            lon: item.marker.getPosition().lng()
          });
          console.log(item.marker.getPosition().lat() + "," + item.marker.getPosition().lng());
        }
          break;
        case "announce-type": {
          type = item.value;
        }
          break;
        case this.ITEM_IMAGE: {
          if (item.distantUrl !== "" && item.distantUrl !== undefined) {
            mediaUrlTable.push({
              type: "image1-1",
              url: item.distantUrl
            });
          }
        }
          break;
        case this.ITEM_TEXT: {
          text = item.value;
        }
          break;
        case this.ITEM_CATEGORY2:
        case this.ITEM_CATEGORY: {
          item.selected.forEach((category) => {
            let split = category.split("/");
            let current:string="";
            for(let i=0;i<split.length;i++){
              current = current+split[i]+"/";
              let categoryToAdd = current.substring(0, current.length - 1);
              announceCategoriesString.push(categoryToAdd);
            }
          });
        }
        break;
      }
    }
    //add default main category
    announceCategoriesString.push(this.category.getMainCategory());
    //remove duplicate category
    announceCategoriesString = announceCategoriesString.filter((elem, index, self) => {
      return index == self.indexOf(elem);
    });
    let announceCategories = [];
    announceCategoriesString.forEach(value => {
      announceCategories.push({
        value: value
      });
    });
    //create the announce
    this.title = "title";
    let announce:Announce = {
      id:null,
      profileId: this.selectedProfileId,
      title: this.title,
      text: text,
      type: type,
      media: mediaUrlTable,
      categories: announceCategories,
      locations: geolocTable
    };
    console.log("announce posted:" + JSON.stringify(announce));
    //
    //perform announce validity check
    //
    if (this.selectedProfileId === "" || this.selectedProfileId === undefined) {
      console.log("invalid profile id");
      return;
    }
    let textPresent = announce.text !== "" && announce.text !== null;
    let mediaPresent = announce.media.length !== 0;
    if (!textPresent && !mediaPresent) {
      let toast = this.toastCtrl.create({
        message: '"Required at least a message or an image"',
        duration: 3000,
        position: 'top'
      });
      toast.present();
    } else {
      this.postAnnounceLoading = true;
      this.announcesService.post(announce).then((announceReceived)=>{
        if(this.chatRoomService.isConnected()) {
          this.chatRoomService.getRoomHandle(announceReceived.chatId);
        }
        this.navCtrl.pop();
        this.postAnnounceLoading=false;
        this.suucessCallback(announceReceived);
        this.clear();
      }).catch((error)=>{
        console.error(error);
        if(error=="ERR_WRONG_LOGIN_PASSWORD"){
          //invalid category password
          this.category.checkCategoryAuthIntegrity();
        }
        this.postAnnounceLoading=false;
        let toast = this.toastCtrl.create({
          message: '"Error From Server"',
          duration: 3000,
          position: 'top'
        });
        toast.present();
      });
    }
  }

  cancel() {
    this.navCtrl.pop();
  }

}
