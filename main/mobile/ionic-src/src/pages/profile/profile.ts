import {Component, ViewChild} from '@angular/core';
import {AlertController, IonicPage, NavController, NavParams, ToastController} from 'ionic-angular';
import {AuthPage} from "../auth/auth";
import {UserOptionPage} from "../user-option/user-option";
import {AnnounceGridComponent} from "../../components/announce-grid/announce-grid";
import {AuthentifiedUserProvider} from "../../providers/authentified-user/authentified-user";
import {ProfilesProvider} from "../../providers/profiles/profiles";

/**
 * Generated class for the ProfilePage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html',
})
export class ProfilePage {
  display: string = "profile"
  anounymous: boolean = true;
  user: any = null;
  userService: AuthentifiedUserProvider;
  profileID: number = 0;
  editImage: boolean = false;
  @ViewChild(AnnounceGridComponent) announceGridComponent: AnnounceGridComponent;
  connected: boolean = false;

  constructor(public navCtrl: NavController,
              public navParams: NavParams,
              userService: AuthentifiedUserProvider,
              public  profilesService: ProfilesProvider,
              private toastCtrl: ToastController,
              private alertCtrl: AlertController) {
    this.userService = userService;
  }

  ionViewWillEnter() {
    this.editImage = false;
  }

  ionViewDidLoad() {
    this.userService.getConnectionEvent().onPublish(()=>{
      this.user = this.userService.getUserData();
      this.connected = true;
    });
  }

  signIn() {
    this.user.login = "";
    //connect
    this.navCtrl.push(AuthPage,
      {
        callback: (login, password, dname, controller) => {
          this.user.login = login;
          this.userService.login(login, password).then(
            () => {
              this.navCtrl.pop();
              controller.clearError();
            },
            (error) => {
              controller.displayError(error);
            }
          );
        },
        mode: "signin"
      });
  };

  signUp() {
    this.user.login = "";
    //register / upgrade
    this.navCtrl.push(AuthPage,
      {
        callback: (login, password, displayname, controller) => {
          console.log("Sign Up received : "+login);
          this.user.login = login;
          this.userService.upgrade(login, password, displayname).then(() => {
            console.log("Upgrade completed : "+login);
            this.navCtrl.pop();
            controller.clearError();
          }, (error) => {
            controller.displayError(error);
          });
        },
        mode: "signup"
      });
  };

  goToSettings(event: any) {
    this.navCtrl.push(UserOptionPage);
  }

  imageReceived(event: any) {
    let image = event.image;
    let profile = this.user.profiles[this.profileID];
    profile.avatar = {
      id: null,
      type: "image",
      url: image.distantUrl
    };
    console.info("profile sent", profile);
    this.profilesService.defineProfile(profile).then((profile) => {
      //update profile from server data
      console.info("profile received", profile);
      this.user.profiles[this.profileID] = profile;
      this.editImage = false;
    });
  }

  changeAvatar(event: any) {
    if (!this.user.anonymous) {
      this.editImage = true;
    }
  }

  changeProfileName(event: any) {
    let profile = this.user.profiles[this.profileID];
    let alert = this.alertCtrl.create({
      title: 'Change display name',
      inputs: [
        {
          name: 'dname',
          placeholder: 'Display name'
        }
      ],
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          handler: data => {
            console.log('Cancel clicked');
          }
        },
        {
          text: 'Change',
          handler: (data) => {
            let minLenghtForName = 3;
            if (data.dname.length < minLenghtForName) {
              let toast = this.toastCtrl.create({
                message: "name should be at least " + minLenghtForName + " characters",
                duration: 5000,
                showCloseButton: true,
                dismissOnPageChange: true,
                position: 'top'
              });
              toast.present();
              return false;
            } else {
              profile.profileName = data.dname;
              console.info("profile sent", profile);
              this.profilesService.defineProfile(profile).then((profile) =>{
                //update profile from server data
                console.info("profile received", profile);
                this.user.profiles[this.profileID] = profile;
              });
              return true;
            }
          }
        }
      ]
    });
    alert.present();
  }

  segmentChanged(event: any) {
    setTimeout(() => {
      if (this.announceGridComponent) {
        let profileID = this.user.profiles[this.profileID].id;
        this.announceGridComponent.setOrderDateFilter();
        this.announceGridComponent.setProfileIDFilter(profileID);
        this.announceGridComponent.doRefresh();
      }
    });
  }

}
