import {Component} from '@angular/core';
import {IonicPage, NavController, NavParams} from 'ionic-angular';
import {AuthPage} from "../auth/auth";
import {AuthentifiedUserProvider} from "../../providers/authentified-user/authentified-user";

/**
 * Generated class for the UserOptionPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-user-option',
  templateUrl: 'user-option.html',
})
export class UserOptionPage {
  user: any;

  constructor(public navCtrl: NavController, public navParams: NavParams,
              public userService: AuthentifiedUserProvider) {
    this.user={};
  }

  ionViewDidLoad() {
    this.userService.getConnectionEvent().onPublish(() => {
      this.user = this.userService.getUserData();
    });
  }

  clearuser() {
    this.userService.clearCache().then(() => {
      this.navCtrl.pop();
    });
  };

  signIn() {
    //connect
    this.navCtrl.push(AuthPage,
      {
        callback: (login, password, dname, controller) => {
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
  changeEmail() {
    //connect
    this.navCtrl.push(AuthPage,
      {
        callback: (login, password, dname, controller) => {
          this.userService.changeCred(login, password).then(
            () => {
              this.navCtrl.pop();
              controller.clearError();
            },
            (error) => {
              controller.displayError(error);
            }
          );
        },
        mode: "email_change"
      });
  };
  changePassword() {
    //connect
    this.navCtrl.push(AuthPage,
      {
        callback: (login, password, dname, controller) => {
          this.userService.changeCred(login, password).then(
            () => {
              this.navCtrl.pop();
              controller.clearError();
            },
            (error) => {
              controller.displayError(error);
            }
          );
        },
        mode: "password_change"
      });
  };

}
