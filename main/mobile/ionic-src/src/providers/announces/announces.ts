import {Inject, Injectable} from '@angular/core';
import {Http} from '@angular/http';
import 'rxjs/add/operator/map';
import {Observable} from "rxjs/Observable";
import {Category} from "../categories/categories";
import {Media, OptionService, TypedCache} from "../../app/app.component";

export interface Announce {
  id: string;
  profileId?: string;
  title?: string;
  text?: string;
  chatId?: string;
  media?: Media[];
  categories?: Category[];
  locations?: any[];
  type?: string;
  created?: Date;
}

export interface AnnouncesAPIRequest {
  optionService?: OptionService;
  entities: Announce[];
}

@Injectable()
export class AnnouncesProvider {
  apiHost: string;
  apipath: string;
  cache: TypedCache<Announce> = new TypedCache<Announce>();

  constructor(@Inject('SmilanHostService') public  smilanHostService: any,
              public http: Http) {
    this.apiHost = smilanHostService.getAPIServerHost();
    this.apipath = this.apiHost + "/api/v1/announces/";
  }

  apikey(): string {
    return this.smilanHostService.getAPIKeyHTTPParam();
  }

  dtoManageError(dto:AnnouncesAPIRequest,rejectCallback):boolean{
    if(dto.optionService!=null){
      if(dto.optionService.errorOption!=null){
        rejectCallback(dto.optionService.errorOption.error);
        return true;
      }
    }
    return false;
  }

  post(announce: Announce): Promise<Announce> {
    let self = this;
    return new Promise<Announce>((resolve, reject) => {
      let subscription = self.http.post(self.apipath + self.apikey(),
        {
          entities: [announce]
        }
        , {})
        .map((res: any) => {
        let jsonres = res.json();
          if(this.dtoManageError(jsonres,reject)){
            return null;
          }
          let resultData: Announce = jsonres.entities[0];
          return resultData;
        })
        .catch((error: Response | any) => {
          return Observable.create(null);
        });

      subscription.subscribe((data: Announce) => {
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

  load(identifiant: string): Promise<Announce> {
    let self = this;
    let cached = self.cache.getCache(identifiant);
    if (cached !== null) {
      return new Promise(function (resolve) {
        resolve(cached);
      });
    }
    return new Promise<Announce>((resolve, reject) => {
      self.http.get(self.apipath + identifiant + self.apikey(), {})
        .map((res: any) => {
          let resultData: Announce = res.json().entities[0];
          self.cache.cacheElement(identifiant, resultData);
          return resultData;
        })
        .catch((error: Response | any) => {
          reject(error);
          return Observable.create(null);
        })
        .subscribe((data: Announce) => {
          resolve(data);
        });

    });
  }

  delete(announceid: string): Promise<boolean> {
    let self = this;
    let request = {
      optionService: {
        deleteOption: {
          "delete": true
        }
      },
      entities: [{
        id: announceid
      }]
    };
    return new Promise<boolean>((resolve, reject) => {
      self.http.post(self.apipath + 'delete' + self.apikey(), request)
        .map((res: any) => {
          self.cache.invalidateCache(announceid);
          return true;
        })
        .catch((error: Response | any) => {
          reject(false);
          return Observable.create(null);
        })
        .subscribe((data: any) => {
          resolve(data);
        });
    });
  }

  multiLoad(announceIDs: string[]): Promise<Announce[]> {
    let self = this;
    return new Promise<Announce[]>((resolve, reject) => {
      if (announceIDs.length === 0) {
        return [];
      }

      //search in cache
      let uncachedIdsList = [];
      announceIDs.forEach(function (id) {
        if (!self.cache.isInCache(id)) {
          uncachedIdsList.push(id);
        }
      });

      if (uncachedIdsList.length === 0) {
        let result: Announce[] = [];
        announceIDs.forEach((elementId) => {
          let cachedElm = self.cache.getCache(elementId);
          if (cachedElm === undefined) {
            console.error("invalid cache");
          } else {
            result.push(cachedElm);
          }
        });
        resolve(result);
      } else {
        //load announce from server
        let announceList: Announce[] = [];
        announceIDs.forEach((elementId) => {
          announceList.push({
            id: elementId
          });
        });
        let requestedAnnouncesId: AnnouncesAPIRequest = {
          optionService: {
            searchOption: {
              expression: "or" //or between the ids
            }
          },
          entities: announceList
        };
        self.http.post(self.apipath + 'search' + self.apikey(), requestedAnnouncesId)
          .map((res: any) => {
            let json = res.json()
            let announceList: Announce[] = json.entities;

            announceList.forEach((data: Announce) => {
              self.cache.cacheElement(data.id, data);
            });
            let result = [];
            announceIDs.forEach((elementId) => {
              let cachedElm = self.cache.getCache(elementId);
              if (cachedElm === undefined) {
                console.error("invalid self.cache");
              } else {
                result.push(cachedElm);
              }
            });
            return result;
          })
          .subscribe((data: any) => {
            resolve(data);
          });

      }
    });
  }

  searchByProfileID(profileId: string): Promise<Announce[]> {
    let self = this;
    return new Promise<Announce[]>((resolve, reject) => {
      self.http.post(self.apipath + 'idsearch' + self.apikey(), {profileId: profileId})
        .map((res: any) => {
          let resultData = res.json();
          let announceIDs = resultData;
          return announceIDs;
        })
        .subscribe((announceIDs: string[]) => {
          self.multiLoad(announceIDs).then((announces: Announce[]) => {
            resolve(announces);
          }).catch((err) => {
            reject(err);
          });
        });
    });
  }

  searchByCategory(category: string): Promise<Announce[]> {
    let self = this;
    return new Promise<Announce[]>((resolve, reject) => {
      self.http.post(self.apipath + 'idsearch' + self.apikey(), {category: category})
        .map((res: any) => {
          let resultData = res.json();
          let announceIDs = resultData;
          return announceIDs;
        })
        .subscribe((announceIDs: string[]) => {
          self.multiLoad(announceIDs).then((announces: Announce[]) => {
            resolve(announces);
          }).catch((err) => {
            reject(err);
          });
        });
    });
  }


  search(requestedAnnounces: AnnouncesAPIRequest): Promise<Announce[]> {
    if (requestedAnnounces.entities.length === 0) {
      console.error('must have at least one entities if no option search');
      return Promise.resolve([]);
    }
    let self = this;
    return new Promise<Announce[]>((resolve, reject) => {
      self.http.post(self.apipath + 'idsearch' + self.apikey(), requestedAnnounces)
        .map((res: any) => {
          let resultData = res.json();
          let announceIDs = resultData;
          return announceIDs;
        })
        .subscribe((announceIDs: string[]) => {
          self.multiLoad(announceIDs).then((announces: Announce[]) => {
            resolve(announces);
          }).catch((err) => {
            reject(err);
          });
        });
    });
  }

  /**
   *
   * @param {
             *          lat:float
             *          lon:float
             *          dist:float
             *          } parameters
   * @returns {announces[String]}
   */
  /*
  geosearch: function (parameters) {
    return $http.post(self.apipath + 'geosearch' + self.apikey(), parameters)
      .then(
        function (response) {
          return response.data;
        },
        function (errResponse) {
          console.error('Error while fetching users');
          return new Promise.reject(errResponse);
        }
      );
  }*/

}
