import {Injectable} from '@angular/core';
import 'rxjs/add/operator/map';
import {Storage} from '@ionic/storage';
import { CookieService } from 'ngx-cookie';
import {PropertiesProvider} from "../properties/properties";

@Injectable()
export class StorageProvider {
  prefix: string = "smlv4.";
  cookiesMode:boolean;
  constructor(
    private cookies: CookieService,
    private storage: Storage,
    private properties: PropertiesProvider
  ) {
    let display = {};
    let pptCookies = properties.getProperty("app.storage.mode.cookies");
    this.cookiesMode = pptCookies==null?false:pptCookies=="true";
    if(!this.cookiesMode){
      this.storage.forEach((value, key) => {
        display[key] = value;
      });
      storage.ready().then(()=>{
        console.info("storage driver", storage.driver);
        console.info("storage info", display);
      })
    }else{
      console.info("storage driver cookies");
      console.info("storage info", this.cookies.getAll());
    }
  }

  store(key:string, value): Promise<any> {
    if(!this.cookiesMode){
      return this.storage.set(this.prefix + key, value);
    }else{
      this.cookies.put(this.prefix + key, JSON.stringify(value));
      return Promise.resolve(true);
    }
  }

  load(key:string): Promise<any> {
    if(!this.cookiesMode){
      return this.storage.get(this.prefix + key);
    }else{
      let value = this.cookies.get(this.prefix + key);
      if(value==null){
        return Promise.resolve(null);
      }else{
        return Promise.resolve(JSON.parse(value));
      }
    }
  }

  deleteKey(key:string): Promise<any> {
    if(!this.cookiesMode){
      return this.storage.remove(this.prefix + key);
    }else{
      this.cookies.remove(this.prefix + key);
      return Promise.resolve(true);
    }
  }

  clearAll(): Promise<any> {
    if(!this.cookiesMode){
      return this.storage.clear();
    }else{
      this.cookies.removeAll();
      return Promise.resolve(true);
    }
  }

  loadOrCreateObject(key:string): Promise<any> {
    return this.storage.get(this.prefix + key).then((value) => {
        if (value == null) {
          this.store(key, {});
          return {};
        } else {
          return this.load(key);
        }
      }
    );
  }

  loadOrCreateArray(key:string): Promise<any> {
    return this.storage.get(this.prefix + key).then((value) => {
        if (value == null) {
          this.store(key, []);
          return [];
        } else {
          return this.load(key);
        }
      }
    );
  }

}
