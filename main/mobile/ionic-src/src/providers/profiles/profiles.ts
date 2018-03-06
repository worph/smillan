import {Inject, Injectable} from '@angular/core';
import {Http} from '@angular/http';
import 'rxjs/add/operator/map';
import {Media, OptionService} from "../../app/app.component";
import {Observable} from "rxjs/Observable";
import {User} from "../authentified-user/authentified-user";

export interface Profile {
  id?: string;
  userId?: string;
  profileName?: string;
  xmppId?: string;
  description?: string;
  avatar?: Media;
  background?: Media;
  created?: Date;
}

export interface ProfilesAPIRequest {
  optionService?: OptionService;
  entities: Profile[];
}

@Injectable()
export class ProfilesProvider {

  apiHost: string;
  apipath: string;

  constructor(@Inject('SmilanHostService') public  smilanHostService: any,
              public http: Http) {
    this.apiHost = smilanHostService.getAPIServerHost();
    this.apipath = this.apiHost + "/api/v1/profiles/";
  }

  apikey(): string {
    return this.smilanHostService.getAPIKeyHTTPParam();
  }

  dtoManageError(dto: ProfilesAPIRequest): boolean {
    if (dto.optionService != null) {
      if (dto.optionService.errorOption != null) {
        return true;
      }
    }
    return false;
  }


  searchWithChatId(chatId: string): Promise<Profile> {
    if (chatId === undefined) {
      console.error("searchWithChatId wrong param");
      return;
    }
    let requestedProfiles: ProfilesAPIRequest = {
      entities: [
        {xmppId: chatId}
      ]
    };
    return new Promise<Profile>((resolve, reject) => {
      let subscription = this.http.post(this.apipath + "search" + this.apikey(), requestedProfiles)
        .map((res: any) => {
          let jsonres = res.json();
          if (this.dtoManageError(jsonres)) {
            return reject(jsonres);
          }
          if (jsonres.entities.length === 1) {
            return jsonres.entities[0];
          } else {
            return reject(jsonres);
          }
        })
        .catch((error: Response | any) => {
            return Observable.create(null);
          }
        );
      subscription.subscribe((data: Profile) => {
          if (data == null) {
            reject("invalid data from server");
          } else {
            resolve(data);
          }
        },
        (error) => {
          reject("invalid data from server");
        });
    });
  }

  search(requestedProfiles: ProfilesAPIRequest): Promise<Profile[]> {
    return new Promise<Profile[]>((resolve, reject) => {
      let subscription = this.http.post(this.apipath + "search" + this.apikey(), requestedProfiles)
        .map((res: any) => {
          let jsonres = res.json();
          if (this.dtoManageError(jsonres)) {
            return reject(jsonres);
          }
          return jsonres.entities;
        })
        .catch((error: Response | any) => {
            return Observable.create(null);
          }
        );
      subscription.subscribe((data: Profile[]) => {
          if (data == null) {
            reject("invalid data from server");
          } else {
            resolve(data);
          }
        },
        (error) => {
          reject("invalid data from server");
        });
    });
  }

  createNewUser(): Promise<User> {
    return new Promise<User>((resolve, reject) => {
      let subscription = this.http.post(this.apipath + "createNewUser" + this.apikey(), {})
        .map((res: any) => {
          let jsonres = res.json();
          if (this.dtoManageError(jsonres)) {
            return reject(jsonres);
          }
          return jsonres.entities[0];
        })
        .catch((error: Response | any) => {
            return Observable.create(null);
          }
        );
      subscription.subscribe((data: User) => {
          if (data == null) {
            reject("invalid data from server");
          } else {
            resolve(data);
          }
        },
        (error) => {
          reject("invalid data from server");
        });
    });
  }

  createNewProfile(parentUserId: string): Promise<Profile> {
    return new Promise<User>((resolve, reject) => {
      let subscription = this.http.post(this.apipath + 'createNewProfile' + '/' + parentUserId + this.apikey(), {})
        .map((res: any) => {
          let jsonres = res.json();
          if (this.dtoManageError(jsonres)) {
            return reject(jsonres);
          }
          return jsonres;
        })
        .catch((error: Response | any) => {
            return Observable.create(null);
          }
        );
      subscription.subscribe((data: User) => {
          if (data == null) {
            reject("invalid data from server");
          } else {
            resolve(data);
          }
        },
        (error) => {
          reject("invalid data from server");
        });
    });
  }

  getUserProfiles(userId: string): Promise<Profile[]> {
    let requestedProfiles = {
      entities: [
        {
          userId: userId
        }
      ]
    };
    return this.search(requestedProfiles);
  }

  auth(login: string, password: string): Promise<User> {
    return new Promise<User>((resolve, reject) => {
      let subscription = this.http.post(this.apipath + 'auth' + this.apikey(), {
        login: login,
        password: password
      })
        .map((res: any) => {
          let jsonres = res.json();
          if (this.dtoManageError(jsonres)) {
            reject("invalid");
            return;
          }
          if (jsonres.entities.length !== 1) {
            reject("invalid entities number");
            return;
          }
          resolve(jsonres.entities[0]);
          return;
        })
        .catch((error: Response | any) => {
            return Observable.create(null);
          }
        );
      subscription.subscribe((data: User) => {
          if (data == null) {
            reject("invalid data from server");
          } else {
            resolve(data);
          }
        },
        (error) => {
          reject("invalid data from server");
        });
    });
  }

  /**
   *
   * @param {String} login
   * @param {String} password
   * @returns {User}
   */
  changeCred(login: string, password: string): Promise<Profile> {
    return new Promise<Profile>((resolve, reject) => {
      let subscription = this.http.post(this.apipath + '/change/credentials' + this.apikey(), {
        login: login,
        password: password
      })
        .map((res: any) => {
          let jsonres = res.json();
          if (this.dtoManageError(jsonres)) {
            return reject(jsonres);
          }
          return jsonres;
        })
        .catch((error: Response | any) => {
            return Observable.create(null);
          }
        );
      subscription.subscribe((data: Profile) => {
          if (data == null) {
            reject("invalid data from server");
          } else {
            resolve(data);
          }
        },
        (error) => {
          reject("invalid data from server");
        });
    });
  }

  upgrade(parentUserId: string, login: string, password: string, displayname: string): Promise<Profile> {
    return new Promise<Profile>((resolve, reject) => {
      let subscription = this.http.post(this.apipath + 'upgrade' + this.apikey(), {
        userID: parentUserId,
        login: login,
        password: password,
        displayName: displayname
      })
        .map((res: any) => {
          let jsonres = res.json();
          if (this.dtoManageError(jsonres)) {
            return reject(jsonres);
          }
          if (jsonres.entities.length === 1) {
            return jsonres.entities[0];
          } else {
            return reject(jsonres);
          }
        })
        .catch((error: Response | any) => {
            return Observable.create(null);
          }
        );
      subscription.subscribe((data: Profile) => {
          if (data == null) {
            reject("invalid data from server");
          } else {
            resolve(data);
          }
        },
        (error) => {
          reject("invalid data from server");
        });
    });
  }

  defineProfile(user: Profile): Promise<Profile> {
    let dataSet = {
      entities: [
        user
      ]
    };
    return new Promise<Profile>((resolve, reject) => {
      let subscription = this.http.post(this.apipath + this.apikey(), dataSet)
        .map((res: any) => {
          let jsonres = res.json();
          if (this.dtoManageError(jsonres)) {
            return reject(jsonres);
          }
          if (jsonres.entities.length === 1) {
            return jsonres.entities[0];
          } else {
            return reject(jsonres);
          }
        })
        .catch((error: Response | any) => {
            return Observable.create(null);
          }
        );
      subscription.subscribe((data: Profile) => {
          if (data == null) {
            reject("invalid data from server");
          } else {
            resolve(data);
          }
        },
        (error) => {
          reject("invalid data from server");
        });
    });
  }

  /**
   * API profile format
   * @param {string} idr
   * @returns {unresolved}
   */
  getProfile(idr: string): Promise<Profile> {
    return new Promise<Profile>((resolve, reject) => {
      let subscription = this.http.get(this.apipath + idr + this.apikey())
        .map((res: any) => {
          let jsonres = res.json();
          if (this.dtoManageError(jsonres)) {
            return reject(jsonres);
          }
          if (jsonres.entities.length === 1) {
            return jsonres.entities[0];
          } else {
            return reject(jsonres);
          }
        })
        .catch((error: Response | any) => {
            return Observable.create(null);
          }
        );
      subscription.subscribe((data: Profile) => {
          if (data == null) {
            reject("invalid data from server");
          } else {
            resolve(data);
          }
        },
        (error) => {
          reject("invalid data from server");
        });
    });
  }

}
