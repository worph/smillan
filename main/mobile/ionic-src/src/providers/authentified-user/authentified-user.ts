import {Inject, Injectable} from '@angular/core';
import 'rxjs/add/operator/map';
import {StorageProvider} from "../storage/storage";
import {NetworkProvider} from "../network/network";
import {PublishEvent, promiseTimeout} from "../../app/app.component";
import {Profile, ProfilesProvider} from "../profiles/profiles";
import {ChatRoomsProvider} from "../chat-rooms/chat-rooms";
import {UserToken} from "../chats/chats";

export interface Token {
  login: string;
  password: string;
}

export interface User {
  id: string;
  email: string;
  login: string;
  password: string;
  xmppPassword: string;
  apiKey: string;
  roles: string[];
  permissions: string[];
  anonymous: boolean;
  profiles: Profile[];
}

@Injectable()
export class AuthentifiedUserProvider {

  private onConnectEventEmiter: PublishEvent<AuthentifiedUserProvider> = new PublishEvent<AuthentifiedUserProvider>();
  private onDisconectEventEmiter: PublishEvent<string> = new PublishEvent<string>();
  STORAGE_USER_LOGIN: string = "app.user.login";
  STORAGE_USER_PASSWORD: string = "app.user.password";
  recoveryRetryCounter: number = 0;
  MAX_RECOVERY: number = 5;
  private connectInProgress: Promise<boolean> = null;
  private connected:boolean = false;
  private userData:User;

  constructor(
              public chatRoomService: ChatRoomsProvider,
              public  profilesService: ProfilesProvider,
              public storageService: StorageProvider,
              private network: NetworkProvider,
              @Inject('SmilanHostService') public smilanHostService: any) {
    this.network.onConnect(() => {
      this.connect();
    });
  }

  getUserData(): User {
    return this.userData;
  }

  addNewProfile(): void {
    this.profilesService.createNewProfile(this.userData.id).then(() => {
      //reconnect to update evrythings
      this.reconnect();
    });
  };

  login(login: string, password: string): Promise<string> {
    return new Promise<string>((resolve, reject) => {
      //check login password with server
      this.profilesService.auth(login, password).then((userdata) => {
        if (userdata.login !== login) {
          reject(userdata);
        } else {
          this.storeLoginPassword(login, password);
          this.reconnect().then((result)=>{
            resolve("login succesful");
          }).catch((err)=>{
            reject("wrong login password");
          })
        }
      }).catch(() => {
        reject("wrong login password");
      });
    });
  };

  changeCred(login: string, password: string): Promise<string> {
    return new Promise((resolve, reject) => {
      if (login === "" && password === "") {
        resolve("nothing to do");
      }
      let pass = new Promise((resolve, reject) => {
        if (login === "") {
          this.storageService.load(this.STORAGE_USER_LOGIN).then((loginR) => {
            login = loginR;
            resolve(null);
          }).catch((err) => {
            reject(err);
          });
        } else if (password === "") {
          this.storageService.load(this.STORAGE_USER_PASSWORD).then((passwordR) => {
            password = passwordR;
            resolve(null);
          }).catch((err) => {
            reject(err);
          });
        } else {
          //nothing to do already ok
          resolve(null);
        }
      });
      pass.then(() => {
        this.profilesService.changeCred(login, password).then((userdata) => {
          if (userdata === null) {
            reject(userdata);
          } else {
            this.storeLoginPassword(login, password).then(() => {
              this.reconnect().then(() => {
                resolve("credential changed");
              }).catch((err) => {
                reject(err);
              });
            }).catch((err) => {
              reject(err);
            });
          }
        });
      }).catch((err) => {
        reject(err);
      });
    });
  };

  upgrade(login: string, password: string, displayname: string): Promise<string> {
    if (this.userData.anonymous === true) {
      return new Promise((resolve, reject) => {
        this.profilesService.upgrade(this.userData.id, login, password, displayname).then((data) => {
          console.log("upgrade completed (1)");
          this.storeLoginPassword(login, password);
          //reconnect to update evrythings
          this.reconnect().then(() => {
            resolve("user upgraded");
          }).catch((err) => {
            reject(err);
          });
        }).catch((err) => {
          reject(err);
        });
      });
    } else {
      return Promise.reject("user not anonymous");
    }
  };

  private storeLoginPassword(login: string, password: string): Promise<boolean> {
    return new Promise<boolean>((resolve) => {
      this.storageService.store(this.STORAGE_USER_LOGIN, login).then(() => {
        this.storageService.store(this.STORAGE_USER_PASSWORD, password).then(() => {
          resolve(true);
        });
      });
    });
  }

  private loadLoginPassword(): Promise<Token> {
    return new Promise<Token>((resolve, reject) => {
      let token: Token = {
        login: null,
        password: null
      };
      this.storageService.load(this.STORAGE_USER_LOGIN).then((login) => {
        token.login = login;
        this.storageService.load(this.STORAGE_USER_PASSWORD).then((password) => {
          token.password = password;
          resolve(token);
        }).catch((err) => {
          reject(err);
        });
      }).catch((err) => {
        reject(err);
      });
    });
  }

  clearCache(): Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
      this.userData=null;
      this.smilanHostService.setAPIKey(null);
      this.storageService.deleteKey(this.STORAGE_USER_LOGIN);
      this.storageService.deleteKey(this.STORAGE_USER_PASSWORD);
      this.storageService.clearAll();
      this.reconnect().then(() => {
        resolve(true);
      }).catch((err) => {
        reject(err);
      });
    });
  }

  private waitNetworkConnection(): Promise<String> {
    return new Promise((resolve, reject) => {
      this.network.onConnectOnce((type) => {
        resolve(type);
      });
    });
  }

  private readDataFromNVM(): Promise<Token> {
    //read user in nvm
    return new Promise<Token>((resolve, reject) => {
      let token: Token;
      promiseTimeout(2000,this.loadLoginPassword()).then((tokenReceived: Token) => {
        token = tokenReceived;
        console.info("token from nvm ", token);
        if (token.login == null) {
          let newProfilePromise = this.profilesService.createNewUser();
          newProfilePromise.then((data) => {
            console.info("new user created", data);
            let userData = data;
            promiseTimeout(2000,this.storeLoginPassword(userData.login, userData.password)).then(() => {
              promiseTimeout(2000,this.loadLoginPassword()).then((tokenReceived: Token) => {
                token = tokenReceived;
                if (token.login === undefined) {
                  throw new Error("Code error");
                }
                console.info("new user " + userData.id + " : " + token.login);
                resolve(token);
              }).catch((err) => {
                reject(err);
              });
            }).catch((err) => {
              reject(err);
            });
          }).catch((err) => {
            reject(err);
          });
        } else {
          console.info("user retreived: " + token.login);
          resolve(token);
        }
      }).catch((err) => {
        reject(err);
      });
    });
  }

  private readUserData(token: Token): Promise<User> {
    return new Promise<User>((resolve, reject) => {
      this.profilesService.auth(token.login, token.password).then((userdata) => {
        console.info("auth succes on API server as : " + token.login);
        userdata.profiles = [];//init profiles
        this.smilanHostService.setAPIKey(userdata.apiKey);
        resolve(userdata);
      }).catch(() => {
        reject("wrong login password");
      });
    });
  }

  private readProfileData(userdata: User): Promise<User> {
    return new Promise<User>((resolve, reject) => {
      this.profilesService.getUserProfiles(userdata.id).then((profiledata) => {
        userdata.profiles = profiledata;
        this.userData = userdata;
        resolve(userdata);
      }).catch((err) => {
        reject(err);
      });
    });
  }

  private connectToChat(userdata: User): void {
    let userTokens:UserToken[] = [];
    for (let profile in userdata.profiles) {
      userTokens = userTokens.concat([
        {
          username: userdata.profiles[profile].xmppId,
          password: userdata.xmppPassword
        }
      ]);
    }
    this.chatRoomService.setUserTokens(userTokens);
    this.chatRoomService.connect();
  }

  reportProblems(fault) {
    console.error(String(fault));
  }

  private tryRecovery() {
    this.recoveryRetryCounter++;
    if (this.recoveryRetryCounter <= this.MAX_RECOVERY) {
      setTimeout(() => {
        console.info("try recovery");
        this.clearCache();
      }, 1000);
    } else {
      console.error("account connect error");
    }
  }

  getConnectionEvent():PublishEvent<AuthentifiedUserProvider>{
    return this.onConnectEventEmiter;
  }

  getDisconectionEvent():PublishEvent<string>{
    return this.onDisconectEventEmiter;
  }

  reconnect():Promise<boolean> {
    this.connected=false;
    return this.connect();
  }
  connect():Promise<boolean> {
    if(this.connected){
      console.log("already connected");
      return Promise.resolve(true);
    }
    if(this.connectInProgress!=null){
      return this.connectInProgress;
    }
    this.connected=false;
    this.connectInProgress = new Promise<boolean>(((resolve, reject) => {
      promiseTimeout(120000, Promise.resolve(true)
        .then(() => {
          console.log("wait for network conection");
          return this.waitNetworkConnection();
        })
        .then(() => {
          console.log("read data from NVM");
          return this.readDataFromNVM();
        })
        .then((token: Token) => {
          console.log("read user data");
          return this.readUserData(token);
        })
        .then((user: User) => {
          console.log("read profile data");
          return this.readProfileData(user);
        })
        .then(() => {
          console.log("connect to chat");
          this.connectToChat(this.getUserData());
          //publish api
          console.log("auth user online");
          this.connected=true;
          this.onConnectEventEmiter.publish(this);
          this.connectInProgress = null;
          resolve(true);
        }))
        .catch((error) => {
          this.connected=false;
          this.connectInProgress = null;
          reject(error);
        });
    }));

    this.connectInProgress.catch((error) => {
      this.reportProblems(error);
      this.tryRecovery();
    });
    return this.connectInProgress;
  }

  disconnect():void{

  }

  isConnected():boolean{
    return this.connected;
  }
}
