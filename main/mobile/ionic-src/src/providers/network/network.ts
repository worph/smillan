import {Inject, Injectable} from '@angular/core';
import 'rxjs/add/operator/map';
import {Network} from '@ionic-native/network';
import {DeviceProvider} from "../device/device";
import {PublishEvent} from "../../app/app.component";

@Injectable()
export class NetworkProvider {
  onConnectEventEmiter: PublishEvent<string> = new PublishEvent<string>();
  onDisconnectEventEmiter: PublishEvent<boolean> = new PublishEvent<boolean>();
  connected:boolean;

  constructor(
    private network: Network,
    device:DeviceProvider) {
    this.onConnectEventEmiter.onPublish((value)=>{
      console.log("network connected : "+value);
    });
    this.onDisconnectEventEmiter.onPublish(()=>{
      console.log("network disconnected");
    });
    let self = this;
    // watch network for a disconnect
    this.network.onDisconnect().subscribe(() => {
      self.onConnectEventEmiter.unpublish();
      self.onDisconnectEventEmiter.publish(true);
      self.connected=false;
    });

    // watch network for a connection
    this.network.onConnect().subscribe(() => {
      // We just got a connection but we need to wait briefly
      // before we determine the connection type. Might need to wait.
      // prior to doing any api requests as well.
      setTimeout(() => {
        self.connected=true;
        self.onDisconnectEventEmiter.unpublish();
        self.onConnectEventEmiter.publish(this.network.type);
      }, 4000);
    });
    if(device.getMode()==device.MODE_BROWSER){
      self.connected=true;
      this.onConnectEventEmiter.publish("BROWSER");
    }else{
      if(this.network.type!="none"){
        this.onConnectEventEmiter.publish(this.network.type);
      }
    }
  }

  onConnectOnce(listener:(string)=>void){
    this.onConnectEventEmiter.onPublishOnce(listener);
  }


  onConnect(listener:(string)=>void){
    this.onConnectEventEmiter.onPublish(listener);
  }

  getOnDisconnectEmmiter():PublishEvent<boolean>{
    return this.onDisconnectEventEmiter;
  }

  connect() {
  }
}
