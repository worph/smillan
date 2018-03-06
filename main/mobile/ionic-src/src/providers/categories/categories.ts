import {Inject, Injectable} from '@angular/core';
import {Http} from '@angular/http';
import 'rxjs/add/operator/map';
import {Observable} from "rxjs/Observable";
import {StorageProvider} from "../storage/storage";
import {AuthentifiedUserProvider} from "../authentified-user/authentified-user";
import {DynamicCustomizerProvider} from "../dynamic-customizer/dynamic-customizer";

export interface Category {
  value: string;
  password?: boolean;
}

@Injectable()
export class CategoriesProvider {

  mainCategory: string = "generic";

  apiHost: string;
  apipath: string;
  authentifiedCategory: string[] = [];
  STORAGE_CATEGORY: string = "app.category.auth";

  constructor(@Inject('SmilanHostService') public smilanHostService: any,
              public http: Http,
              public storageService: StorageProvider,
              public authUserService: AuthentifiedUserProvider,
              public custo: DynamicCustomizerProvider) {
    if (custo.persoTagPresent("generic")) {
      this.mainCategory = "generic";
    } else if (custo.persoTagPresent("hec")) {
      this.mainCategory = "HEC";
    } else if (custo.persoTagPresent("utc")) {
      this.mainCategory = "UTC";
    }
    this.apiHost = smilanHostService.getAPIServerHost();
    this.apipath = this.apiHost + "/api/v1/categories/";
    this.loadCategoryAuth();
    authUserService.getConnectionEvent().onPublish(() => {
      this.checkCategoryAuthIntegrity();//sync permission with server
    })
  }

  loadCategoryAuth() {
    this.storageService.loadOrCreateArray(this.STORAGE_CATEGORY).then((list) => {
      this.authentifiedCategory = list;
    });
  }

  storeCategoryAuth() {
    this.storageService.store(this.STORAGE_CATEGORY, this.authentifiedCategory).then((list) => {
      this.loadCategoryAuth();
    });
  }

  prety(value:string):string{
    let split = value.split("/");
    return split[split.length-1];
  }

  apikey(): string {
    return this.smilanHostService.getAPIKeyHTTPParam();
  }

  getMainCategory(): string {
    return this.mainCategory;
  }

  getCategory(): Promise<Category[]> {
    return new Promise<Category[]>((resolve, reject) => {
      this.http.get(this.apipath + this.apikey(), {})
        .map((res: any) => {
          let resultData: Category[] = res.json().entities;
          return resultData;
        })
        .catch((error: Response | any) => {
          reject(error);
          return Observable.create([]);
        })
        .subscribe((data: Category[]) => {
          resolve(data);
        });
      /*let available: Category[] = [
        {value:"hello"},
        {value:"hello3"},
        {value:"omg"},
        {value:"zomg/omg"},
        {value:"hello4"},
        {value:"zomg/omg3"},
        {value:"zomg2/omg3"},
        {value:"zomg2/password",password:true},
        {value:"password",password:true},
      ];
      resolve(available);*/
    });
  }

  authCategory(category: string, password: string): Promise<boolean> {
    let self = this;
    return new Promise<boolean>((resolve, reject) => {
      try {
        self.http.post(self.apipath + "auth" + self.apikey(),
          {
            category: category,
            password: password
          }
          , {})
          .map((res: any) => {
            return res.json().auth;
          })
          .catch((error: Response | any) => {
            reject(error);
            return Observable.create(false);
          })
          .subscribe((authok: boolean) => {
            if (authok) {
              this.authentifiedCategory.push(category);
              this.storeCategoryAuth();
            }
            resolve(authok);
          });
      } catch (err) {
        reject(err);
      }
    });
  }

  isAuth(category: string): boolean {
    let ret = false;
    this.authentifiedCategory.forEach((categoryItem) => {
      if (category == categoryItem) {
        ret = true;
      }
    });
    return ret;
  }

  checkCategoryAuthIntegrity() {
    this.authUserService.connect().then(() => {
      this.authentifiedCategory = [];
      this.authUserService.getUserData().permissions.forEach(value => {
        let splited = value.split(":");
        if (splited[0] == "category") {
          let cat = splited[1];
          if (this.authentifiedCategory.indexOf(cat) == -1) {
            this.authentifiedCategory.push(cat);
          }
        }
      })
      this.storeCategoryAuth();
    });
  }

}
