import {AfterContentInit, Component, EventEmitter, Input, Output} from '@angular/core';
import {NavController} from 'ionic-angular';
import {Announce} from "../../providers/announces/announces";
import {Profile, ProfilesProvider} from "../../providers/profiles/profiles";

declare var moment:any;

export interface AnnounceDisplay extends Announce{
  profile?: Profile;
  displayType?:string;
  removeRight:boolean;
}

export interface AnnounceEvent{
  announce:AnnounceDisplay;
  event:any;
}

export interface ProfileEvent{
  profile:any;
  event:any;
}

@Component({
  selector: 'announce',
  templateUrl: 'announce.html'
})
export class AnnounceComponent implements AfterContentInit{
  @Input('announce') announce:AnnounceDisplay;
  @Output('announceClick') onClick: EventEmitter<AnnounceEvent> = new EventEmitter();
  @Output('announceRemoveClick') onRemoveClick: EventEmitter<any> = new EventEmitter();
  @Output('announceUserClick') onUserClick: EventEmitter<ProfileEvent> = new EventEmitter();

  constructor(
    public navCtrl: NavController,
    public profilesProvider:ProfilesProvider
  ) {
  }

  computeAndSetAnnounceType(announce) {
      //small/medium/long
        var textPresent = announce.text !== "" && announce.text !== null;
      var mediaPresent = announce.media.length !== 0;
      if (!textPresent && !mediaPresent) {
          announce.displayType = "none";
          console.log("invalid announce format");
        } else if (mediaPresent) {
          announce.displayType = "media_display";
        } else {
          announce.displayType = "message_display";
        }
    }

  ngAfterContentInit(): void{
    let self = this;
    self.computeAndSetAnnounceType(self.announce);
    if(this.announce.profile==undefined || this.announce.profile==null){
      this.profilesProvider.getProfile(this.announce.profileId).then(profile => {
        this.announce.profile=profile;
      });
    }
  }

  prettyDate(date:any){
    return moment(date).format('DD-MM-YYYY HH:mm');
  }

  removeItem($event:any,announce:any){
    console.log("remove:" + announce)
    this.onRemoveClick.emit({
      event:event,
      announce:announce
    });
  }

  popupAnnounce = function (announce, $event) {
    this.onClick.emit({
      event:$event,
      announce:announce
    });
  }

  userPopup = function (profile, $event) {
    this.onUserClick.emit({
      event:$event,
      profile:profile
    });
  }



  getRandomColor = function() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  };

}
