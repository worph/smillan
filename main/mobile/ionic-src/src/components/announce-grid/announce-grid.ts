import {AfterViewInit, Component, Inject, ViewChild} from '@angular/core';
import {CategoriesProvider} from "../../providers/categories/categories";
import {NavController} from 'ionic-angular';
import {DisplayAnnouncePage} from "../../pages/display-announce/display-announce";
import {AngularMasonry} from "../angular2-masonry/src/masonry";
import {AuthentifiedUserProvider} from "../../providers/authentified-user/authentified-user";
import {Announce, AnnouncesAPIRequest, AnnouncesProvider} from "../../providers/announces/announces";
import {OptionService} from "../../app/app.component";
import {ProfilesProvider} from "../../providers/profiles/profiles";

declare let moment: any;

export interface GridAnnounce extends Announce {
  backgroundColor: string;
  numberInList: number;
  removeRight: boolean;
  ready: boolean;
  profile: any;
}

@Component({
  selector: 'announce-grid',
  templateUrl: 'announce-grid.html'
})
export class AnnounceGridComponent implements AfterViewInit {

  text: string;
  loadingMore: boolean = false;
  moreDataCanBeLoadedBoolean: boolean = false;
  page: number = 0;
  filter: AnnouncesAPIRequest = {
    entities: [{id: null}]
  };
  loadBatch: number = 10;
  counter: number = 0;
  globalNumberInList: number = 0;
  items: GridAnnounce[] = [];
  optionService: OptionService = null;
  currentUser: any;

  @ViewChild(AngularMasonry) masonry: AngularMasonry;

  constructor(public navCtrl: NavController,
              public  profilesService: ProfilesProvider,
              public  announcesService: AnnouncesProvider,
              public  authentifiedUserService: AuthentifiedUserProvider,
              @Inject('LocationService') public  locationService: any,
              @Inject('BackGroundWorker') public backGroundWorker: any,
              public categoryProvider: CategoriesProvider) {
  }

  ngAfterViewInit(): void {
    this.authentifiedUserService.connect().then(() => {
      this.currentUser = this.authentifiedUserService.getUserData();
      this.items.forEach((announce) => {
        this.updateRemoveRight(announce);
        this.checkReady(announce);
      });
    });
  }

  prettyDate(date: any) {
    return moment(date).format('DD-MM-YYYY HH:mm');
  }

  getRandomColor () {
    let letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  };

  doRefresh () {
    this.counter = 0;
    this.loadingMore = false;
    this.moreDataCanBeLoadedBoolean = true;
    this.page = 0;
    this.items = [];
    this.loadMore(null);
  };

  onRemoveAnnounce($event) {
    let announce = $event.announce;
    this.items = this.items.filter((element) =>{
      return element.id !== announce.id;
    });
    this.announcesService.delete(announce.id);
    $event.event.stopPropagation();
  }

  updateRemoveRight(announce: GridAnnounce) {
    announce.removeRight = false;
    this.currentUser.profiles.forEach((profile) => {
      if (this.currentUser.roles.indexOf("admin") !== -1) {
        //administrator
        announce.removeRight = true;
        return;
      }
      if (announce.profileId === profile.id) {
        announce.removeRight = true;
        return;
      }
    });
    return announce.removeRight;
  };

  addItem(item: GridAnnounce) {
    this.items.push(item);
  }

  popupAnnounce($event) {
    this.navCtrl.push(DisplayAnnouncePage, {
      announce: $event.announce
    })
  }

  setFilter(filter: any) {
    this.filter = filter;
  }

  setProfileIDFilter(profileID: string) {
    this.filter = {
      entities: [{
        id: null,
        profileId: profileID
      }],
      optionService: this.optionService
    };
  }

  setOrderDateFilter() {
    if (this.optionService == undefined) {
      this.optionService = {};
    }
    if (this.optionService.searchOption == undefined) {
      this.optionService.searchOption = {};
    }
    this.optionService.searchOption.order = [{
      item: "created",
      direction: "desc"
    }]

  }

  setCategoryFilter(categories: string[]) {
    let enties = [];
    categories.forEach((item) => {
      enties.push({
        categories: [
          {
            value: item
          }
        ]
      })
    });
    if (enties.length == 0) {
      //entities must have at least one item
      enties.push({});
    }
    this.filter = {
      entities: enties,
      optionService: this.optionService
    };
  }

  checkReady(announce: GridAnnounce) {
    if (announce.profile != undefined) {
      if (announce.removeRight != undefined) {
        announce.ready = true;
        return;
      }
    }
    announce.ready = false;
  }

  loadMore(infiniteScroll: any) {
    this.locationService.getLocation().then((geo) => {
      if (this.loadingMore)
        console.log("synchronisation error");
      this.page++;
      this.loadingMore = true;
      //$log.info("announce number : "+this.controller.elements.length);
      let requestedAnnounces = this.filter;
      if (this.optionService != null) {
        requestedAnnounces.optionService = this.optionService;
      }
      if (requestedAnnounces.optionService == undefined) {
        requestedAnnounces.optionService = {};
      }
      if (requestedAnnounces.optionService.searchOption == undefined) {
        requestedAnnounces.optionService.searchOption = {};
      }
      requestedAnnounces.optionService.searchOption.number = this.loadBatch;
      requestedAnnounces.optionService.searchOption.page = this.page - 1;
      /*order: [{
       item: "geodist",
       parameters: geo.lat + "," + geo.lng + ",100", //<lat>,<lon>,<radius (m)>
       direction: "asc"
       }]*/
      let promise = this.announcesService.search(requestedAnnounces);
      promise.then((data) => {
        //data = data.entities;
        if (data.length === 0) {
          this.moreDataCanBeLoadedBoolean = false;
        }
        data.forEach((announceR) => {
          let announce: any = announceR;
          announce.ready = false;
          announce.profile = null;
          announce.numberInList = this.globalNumberInList;
          this.globalNumberInList++;
          announce.removeRight = false;//waitting for true value
          //custom background
          //let random = Math.floor(Math.random()*15);
          //an.backgroundURL="https://media.smillan.com/image/template/background/background-"+pad(random,3)+".jpg";
          announce.backgroundColor = this.getRandomColor();
          this.profilesService.getProfile(announce.profileId).then((data) => {
            announce.profile = data;
            if (this.currentUser !== undefined) {
              this.updateRemoveRight(announce);
            }
            this.checkReady(announce);
          });
          this.addItem(announce);
        });
        //this.controller.elements = this.controller.elements.concat(data);
        this.loadingMore = false;
        if (infiniteScroll != null) {
          infiniteScroll.complete();
        }
        if (this.masonry != null) {
          setTimeout(() => {
            //this.masonry._msnry.reloadItems();//hack due to graphic bug (only on prod server
            this.masonry._msnry._resetLayout();
            console.log("refresh masonry layout");
          }, 2000);
        }
      }, (err) => {
        console.error(err);
        this.loadingMore = false;
        if (infiniteScroll != null) {
          infiniteScroll.complete();
        }
      });
    });
  };


}
