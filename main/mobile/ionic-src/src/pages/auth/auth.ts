import {Component, Inject} from '@angular/core';
import {IonicPage, NavController, NavParams, ToastController} from 'ionic-angular';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/catch';

/**
 * Generated class for the AuthPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-auth',
  templateUrl: 'auth.html',
})
export class AuthPage {
  private process: boolean;
  private error: string;
  private dname: string;
  private login: string;
  private password: string;
  private callback: any;
  private mode: any;
  private askForPassword: boolean;
  private askForDisplayName: boolean;
  private askForEmail: boolean;
  private displayForgottentPasswordButton: boolean;
  private instruction: string;
  validButonText: string;
  titleText: string;

  constructor(public navCtrl: NavController,
              public navParams: NavParams,
              public http: Http,
              private toastCtrl: ToastController,
              @Inject('SmilanHostService') public smilanHostService: any) {
    let self:AuthPage = this;
    self.callback = navParams.get("callback");
    self.mode = navParams.get("mode");
    if (self.mode === "signup") {
      self.askForDisplayName = true;
      self.askForEmail = true;
      self.askForPassword = true;
      self.displayForgottentPasswordButton = false;
      self.instruction = null;
      self.validButonText = "Sign Up";
      self.titleText = "Register";
    }
    if (self.mode === "signin") {
      self.askForDisplayName = false;
      self.askForEmail = true;
      self.askForPassword = true;
      self.displayForgottentPasswordButton = true;
      self.instruction = null;
      self.validButonText = "Sign In";
      self.titleText = self.validButonText;
    }
    if (self.mode === "email_change") {
      self.askForDisplayName = false;
      self.askForEmail = true;
      self.askForPassword = false;
      self.displayForgottentPasswordButton = false;
      self.instruction = "Please enter your new email";
      self.validButonText = "Change Email";
      self.titleText = self.validButonText;
    }
    if (self.mode === "password_change") {
      self.askForDisplayName = false;
      self.askForEmail = false;
      self.askForPassword = true;
      self.displayForgottentPasswordButton = false;
      self.instruction = "Please enter your new password";
      self.validButonText = "Change Password";
      self.titleText = self.validButonText;
    }
    self.password = "";
    self.login = "";
    self.dname = "";
    self.error = "";
    self.process = false;
  }

  ionViewDidLoad() {

  }

  displayError(error:any):void {
    let self:AuthPage = this;
    console.log(error);
    if (error.optionService === undefined || error.optionService === null) {
      if (error.statusText === undefined) {
        self.error = error;//error is directly a text to display
      } else {
        //presence of status text means it is a server error
        self.error = error.statusText;
        self.error = "Server Error please try again later. (5)";
      }
    } else {
      if (error.optionService.errorOption === undefined) {
        //it comes from the server but has no option error
        self.error = "Server Error please try again later. (6)";
      } else {
        self.error = error.optionService.errorOption.error;
        switch (self.error) {
          case "ERR_USER_NOT_ANNONYMOUS_A":
            self.error = "User must not be anonymous";
            break;
          case "ERR_USER_ID_NOT_EXSIST":
            self.error = "User login does not exist";
            break;
          case "ERR_USER_NOT_ANNONYMOUS_B":
            self.error = "User must not be anonymous";
            break;
          case "ERR_LOGIN_ALREADY_TAKEN":
            self.error = "User login is already taken";
            break;
          case "ERR_WRONG_LOGIN_OR_PASSWORD":
            self.error = "Invalid login or password";
            break;
          default:
            self.error = "invalid format";
            break;
        }
      }
    }

    let toast = this.toastCtrl.create({
      message: self.error,
      duration: 5000,
      showCloseButton: true,
      dismissOnPageChange: true,
      position: 'top'
    });
    toast.present();
    self.process = false;
  };

  clearError():void {
    let self:AuthPage = this;
    self.error = "";
    self.process = false;
  };

  onlineEmailVerification(email:string): Promise<boolean>{
    let self:AuthPage = this;
    return new Promise<boolean>((resolve, fail) => {
      self.http.get(self.smilanHostService.getAPIServerHost() + "/api/v1/email/verify?email=" + email)
        .retry(4)
        .retryWhen(error => error.delay(500))
        .timeoutWith(2000, Observable.throw(new Error("Timeout has occurred")))
        .map(function (res: Response) {
          let resultData = res.json();
          console.log("onlineEmailVerification", resultData);
          if (resultData.result === "invalid") {
            return false;
          } else {
            return true;
          }
        }).catch((error: Response | any) => {
          return Observable.create(true);
        }).subscribe((data:boolean) => {
          resolve(data);
        });
      });
  }

  verifyEmailForSignUp(email:string):Promise<boolean> {
    let self:AuthPage = this;
    return new Promise((resolve, fail) => {
      self.verifyEmailForSignIn(email).then((result:boolean) => {
        if (result==true) {
          self.onlineEmailVerification(email).then((data:boolean)=>{
            resolve(data);
          })
        } else {
          resolve(true);
        }
      });
    });
  }

  verifyEmailForSignIn(email:string):Promise<boolean> {
    return new Promise((resolve, fail) => {
      let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      //let re = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
      resolve(re.test(email));
    });
  }

  continue_():void {
    let self:AuthPage = this;
    if (self.mode === "signup") {
      if (self.dname === "" || self.dname === null || self.login === "" || self.login === null || self.login === undefined || self.password === "" || self.password === null) {
        self.displayError("Email, Display name and password must be filled");
        return;
      }
    } else if (self.mode === "signin") {
      if (self.login === "" || self.login === null || self.login === undefined || self.password === "" || self.password === null) {
        self.displayError("Email and password must be filled");
        return;
      }
    } else if (self.mode === "email_change") {
      if (self.login === "" || self.login === null || self.login === undefined) {
        self.displayError("Email must be filled");
      }
    } else if (self.mode === "password_change") {
      if (self.password === "" || self.password === null || self.password === undefined) {
        self.displayError("Password must be filled");
      }
    }
    self.clearError();
    self.process = true;
    let promise;
    if (self.mode === "signup") {
      //sign up mode
      promise = self.verifyEmailForSignUp(self.login);
    } else if (self.mode === "signin") {
      //sign in mode
      promise = self.verifyEmailForSignIn(self.login);
    } else if (self.mode === "email_change") {
      //change mail mode
      promise = self.verifyEmailForSignIn(self.login);
    } else {
      //immediate
      promise = new Promise((resolve,fail) => {
        resolve(true);
      });
    }

    promise
      .then((result:boolean) => {
      if (result === true) {
        self.callback(self.login, self.password, self.dname, self);
      } else {
        self.displayError("Email is invalid");
      }
    })
      .catch((error:any) => {
        console.log(error);
        self.displayError("Server Error please try again later. (2)");
      });
  };

  retreivePassword(login: string):void {
    let self:AuthPage = this;
    self.http
      .get(self.smilanHostService.getAPIServerHost() + "/api/v1/password/forgotten?email=" + login)
      .retry(4)
      .retryWhen(error => error.delay(500))
      .timeoutWith(2000, Observable.throw(new Error("Timeout has occurred")))
      .map(
        (res: Response) => {
          let status = res.json().status;
          if (status === "OK") {
            self.displayError("An email has been send to " + self.login + ". Check your email to recover the password");
          } else if (status === "ERR_UNKOWN_USER") {
            self.displayError("Provided mail is not associated with an account");
          } else {
            self.displayError("Server Error please try again later. (3)");
          }
          return status;
        })
      .catch((error: Response | any) => {
        console.log(error);
        self.displayError("Server Error please try again later. (4)");
        return Observable.throw(error);
      })
      .subscribe(() => {});//subscribe to trigger the call
  }

  passwordForgoten():void {
    let self:AuthPage = this;
    if (self.login === "" || self.login === null || self.login === undefined) {
      self.displayError("Email must be filled");
    } else {
      self.clearError();
      self.process = true;
      self.retreivePassword(self.login);
    }
  };

}
