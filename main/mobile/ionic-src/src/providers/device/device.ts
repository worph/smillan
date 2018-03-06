import {Injectable} from '@angular/core';
import 'rxjs/add/operator/map';
import {Device} from "@ionic-native/device";

@Injectable()
export class DeviceProvider {

  public MODE_BROWSER = "browser";
  public MODE_MOBILE_APP = "mobile_app"
  mode: string = this.MODE_BROWSER;

  constructor(public device: Device) {
    console.log("device:",device);
    this.updateMode();
    console.log("device mode:"+this.getMode());
  }

  updateMode(): void {
    if (this.device.platform != null) {
      //cordova is loaded
      if (this.device.platform != "browser") {
        this.mode = this.MODE_MOBILE_APP;
      } else {
        this.mode = this.MODE_BROWSER;
      }
    } else {
      //cordova is not loaded fallback
      this.mode = this.MODE_BROWSER;
    }
  }

  getMode(){
    return this.mode;
  }

}
