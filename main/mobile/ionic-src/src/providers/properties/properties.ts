import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';

declare let GLOBAL_PROPERTIES:any;

@Injectable()
export class PropertiesProvider {

  constructor() {
  }

  getProperty(pptName:string):string {
    return GLOBAL_PROPERTIES[pptName];
  }

}
