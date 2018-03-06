import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import { LocalNotifications } from '@ionic-native/local-notifications';
import {DynamicCustomizerProvider} from "../dynamic-customizer/dynamic-customizer";
import {DeviceProvider} from "../device/device";
import {ToastController} from "ionic-angular";
declare var window;

export interface Notification {
  title:string,
  message:string,
  iconUrl?:string
}

@Injectable()
export class NotificationProvider {
  notificationSupport:string[]=[];
  SUPPORT_HTML5="html5";
  SUPPORT_DEVICE="device";
  displayedNotif:any[]=[];
  maxHtmlNotifCount:number = 2;

  constructor(
    private localNotifications: LocalNotifications,
    private device:DeviceProvider,
    private toastCtrl: ToastController
  ) {
    if(device.getMode()==this.device.MODE_MOBILE_APP){
      this.notificationSupport.push(this.SUPPORT_DEVICE);
    }else if(device.getMode()==this.device.MODE_BROWSER){
      if (!("Notification" in window)) {
        console.log("This browser does not support desktop notification");
      }else{
        if (window.Notification.permission !== "denied") {
          this.notificationSupport.push(this.SUPPORT_HTML5);
        }
      }
    }
  }

  html5CheckPermission():Promise<boolean>{
    return new Promise<boolean>((resolve, reject) => {
      // Otherwise, we need to ask the user for permission
      if (window.Notification.permission !== "denied") {
        Notification.requestPermission((permission) => {
          // If the user accepts, let's create a notification
          if (permission === "granted") {
            resolve(true);
          }else{
            resolve(false);
          }
        });
      }
      // Let's check whether notification permissions have already been granted
      else if (window.Notification.permission === "granted") {
        // If it's okay let's create a notification
        resolve(true);
      }else{
        resolve(false);
      }
    });
  }

  html5Notification(notif:Notification) {
    this.html5CheckPermission().then((status)=>{
      if(!status){
        console.log("impossible to notify html5");
      }
      // If it's okay let's create a notification
      let options = {
        body: notif.message,
        vibrate:true,
        onclick:()=>{
          console.log("click");
        },
        icon: notif.iconUrl?notif.iconUrl:"assets/image/default_announce.png"
      }
      while(this.maxHtmlNotifCount<this.displayedNotif.length){
        let notif = this.displayedNotif[0];
        this.displayedNotif.shift();
        notif.close();
      }
      let notification = new Notification(notif.title,options);
      this.displayedNotif.push(notification);
      notification.onclick = (e:any) => {
        window.focus();
        notification.close();
        //window.open(window.location.href);//ok keep that for background worker
      };
      notification.onerror = () =>{
        this.displayedNotif.filter((element)=>{
          return element!=notification;
        });
      }
      notification.onclose = () => {
        this.displayedNotif.filter((element)=>{
          return element!=notification;
        });
        console.log("close notif");
      };

    });
  }

  logNotification(notif:Notification):void{
      console.log("Notfification : "+notif.title+" => "+notif.message);
  }

  deviceNotification(notif:Notification):void{
    this.localNotifications.schedule({
      id:0,
      title:notif.title,
      text: notif.message
    });
  }

  notifyExternalSimple(title:string, message:string):void{
    this.notifyExternal(title,message,null);
  }

  notifyExternal(title:string, message:string,iconUrl:string):void{
    let notif:Notification = {
      title:title,
      message:message,
      iconUrl:iconUrl
    };
    this.logNotification(notif)
    if(this.notificationSupport.indexOf(this.SUPPORT_DEVICE)!=-1){
      this.deviceNotification(notif);
    }

    if(this.notificationSupport.indexOf(this.SUPPORT_HTML5)!=-1){
      this.html5Notification(notif);
    }
  }

}
