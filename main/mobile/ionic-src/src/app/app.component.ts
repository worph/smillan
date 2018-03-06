import {Component, Inject, Pipe, PipeTransform} from '@angular/core';
import {Platform} from 'ionic-angular';
import {StatusBar} from '@ionic-native/status-bar';
import {SplashScreen} from '@ionic-native/splash-screen';

import {TabsPage} from '../pages/tabs/tabs';
import {DynamicCustomizerProvider} from "../providers/dynamic-customizer/dynamic-customizer";

export interface Listener<T> {
  (event: T): any;
}

export interface Disposable {
  dispose();
}

export class PublishEvent<T> {
  event:TypedEvent<T> = new TypedEvent<T>();
  last:T=null;

  onPublishOnce(listener:(string)=>void){
    if(this.last!=null){
      listener(this.last);
    }else{
      this.event.once(listener);
    }
  }

  onPublish(listener:(string)=>void){
    if(this.last!=null){
      listener(this.last);
    }
    this.event.on(listener);
  }

  unpublish(){
    this.last=null;
  }

  publish = (eventData: T) => {
    this.last=eventData;
    this.event.emit(eventData);
  }
}

export class ArrayEvent<T> {
  private event:TypedEvent<T> = new TypedEvent<T>();
  private array:T[]=[];

  /**
   *
   * @param {(string) => void} listener
   */
  onElement(listener:(string)=>void){
    this.array.forEach((item)=>{
      listener(item);
    });
    this.event.on(listener);
  }

  push = (eventData: T) => {
    this.array.push(eventData);
    this.event.emit(eventData);
  }

  get():T[]{
    return this.array;
  }
}

/** passes through events as they happen. You will not get events from before you start listening */
export class TypedEvent<T> {
  private listeners: Listener<T>[] = [];
  private listenersOncer: Listener<T>[] = [];

  on = (listener: Listener<T>): Disposable => {
    this.listeners.push(listener);
    return {
      dispose: () => this.off(listener)
    };
  }

  once = (listener: Listener<T>): void => {
    this.listenersOncer.push(listener);
  }

  off = (listener: Listener<T>) => {
    var callbackIndex = this.listeners.indexOf(listener);
    if (callbackIndex > -1) this.listeners.splice(callbackIndex, 1);
  }

  emit = (event: T) => {
    /** Update any general listeners */
    this.listeners.forEach((listener) => listener(event));

    /** Clear the `once` queue */
    this.listenersOncer.forEach((listener) => listener(event));
    this.listenersOncer = [];
  }

}

export class TypedCache<T> {
  cacheData: any = {};

  isInCache(id: string): boolean {
    return this.cacheData[id] !== undefined && this.cacheData[id] !== null;
  }

  getCache(id: string): T {
    let ret = this.cacheData[id];
    if(ret==undefined){
      return null;
    }
    return ret;
  }

  invalidateCache(id: string): boolean {
    return delete this.cacheData[id];
  }

  cacheElement(id: string, element: T): void {
    this.cacheData[id] = element;
  }
}

export interface ErrorOption {
  error: string;
}

export interface OptionService {
  searchOption?: any;
  errorOption?: ErrorOption;
  deleteOption?: any;
  timeFilterOption?: any;
}

export interface Media {
  id: string;
  type: string;
  url: string;
}

export function promiseTimeout(ms: number, promise: Promise<any>): Promise<any> {

  // Create a promise that rejects in <ms> milliseconds
  let timeout = new Promise((resolve, reject) => {
    let id = setTimeout(() => {
      clearTimeout(id);
      reject('Timed out in ' + ms + 'ms.')
    }, ms)
  })

  // Returns a race between our timeout and the passed in promise
  return Promise.race([
    promise,
    timeout
  ])
}

@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  rootPage: any = TabsPage;

  constructor(platform: Platform,
              statusBar: StatusBar,
              private splashScreen: SplashScreen,
              dynamicCustomiser : DynamicCustomizerProvider) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      splashScreen.hide();
    });
  }


}
