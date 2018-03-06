import {AfterViewInit, Component, EventEmitter, Inject, Input, Output} from '@angular/core';
import {Camera} from '@ionic-native/camera';
import {ToastController} from "ionic-angular";
import {DeviceProvider} from "../../providers/device/device";

@Component({
  selector: 'image-picker',
  templateUrl: 'image-picker.html'
})
export class ImagePickerComponent implements AfterViewInit{

  item = {
    loading: false,
    localUrl: "",
    distantUrl: "",
    inputID: "",
    type:"image"
  };
  validMimeTypes = ["image/png", "image/jpeg", "image/gif"];
  maxFileSize = 3 * 1024 * 1024;//3mb

  @Output('imageReceived') imageReceived: EventEmitter<any> = new EventEmitter();

  SOURCE_PHOTOLIB = "photolib"
  SOURCE_CAMERA = "camera"
  //SOURCE_AUTO="auto" => TODO for later display button for photo lib or camera
  // if photolib on browser display drop windows
  @Input() source: string;
  @Input() initialUrl: string;

  constructor(@Inject('UploadService') public  uploadService: any,
              public camera: Camera,
              public device: DeviceProvider,
              private toastCtrl: ToastController) {
    this.item.inputID = 'input-id-' + Math.random();
  }

  ngAfterViewInit(): void {
    let self = this;
    setTimeout(()=>{
      /*
       * RUN
       */
      if(this.source!==this.SOURCE_PHOTOLIB && this.source!==this.SOURCE_CAMERA){
        console.error("invalid source type : "+this.source);
      }
      if (self.device.getMode() == self.device.MODE_MOBILE_APP) {
        //in this case we directly triger the image select process
        this.selectPicture(this.source);
      }
    });
  }

  displayError(error: string): void {
    console.log(error);
    let toast = this.toastCtrl.create({
      message: error,
      duration: 5000,
      showCloseButton: true,
      dismissOnPageChange: true,
      position: 'top'
    });
    toast.present();
  };

  trigerInputFile(triggerID) {
    //helper methode
    setTimeout(function () {
      document.getElementById(triggerID).click();
    }, 0);
  }

  checkSize(size) {
    let self = this;
    let _ref;
    let actualSize = (size / 1024) / 1024;
    if (((_ref = this.maxFileSize) === (void 0) || _ref === "") || actualSize < this.maxFileSize) {
      return true;
    } else {
      self.displayError("File must be smaller than " + this.maxFileSize + " MB is of size : " + actualSize + " MB");
      return false;
    }
  }

  isTypeValid(type) {
    let self = this;
    if ((this.validMimeTypes === (void 0)) || this.validMimeTypes.indexOf(type) > -1) {
      return true;
    } else {
      self.displayError("Invalid file type.  File must be one of following types " + this.validMimeTypes + " is of type : " + type);
      return false;
    }
  }

  fileBrowserEvent(event) {
    console.log("fileBrowserEvent");
    let file = event.target.files[0];
    this.onFileReceived(file, this.item);
  }

  notify(image) {
    this.imageReceived.emit({
      event: event,
      image: image
    });
    console.log("image uploaded : " + image.distantUrl);
  }

  changeImage(triggerID) {
    if (this.device.getMode() == this.device.MODE_MOBILE_APP) {
      this.selectPicture(this.source);
    } else {
      this.trigerInputFile(triggerID);
    }
  }

  onFileReceived(file, itemImage) {
    let self = this;
    if (file === undefined) {
      return;
    }
    //let name = file.name;
    let type = file.type;
    let size = file.size;
    if (this.isTypeValid(type) && this.checkSize(size)) {
      itemImage.loading = true;
      let reader = new FileReader();
      reader.onload = function (evt) {
        let stringDataURL = reader.result;
        itemImage.localUrl = stringDataURL;
        let dataBase64 = stringDataURL.slice(stringDataURL.indexOf(",") + 1);
        //TODO manage mime type in a cleaner way (jpg is hardcoded)
        self.uploadService.upload({
          data: dataBase64,
          ext: "jpg"
        }).then(function (data) {
          itemImage.loading = false;
          itemImage.distantUrl = data;
          self.notify(itemImage);
        });
      };
      reader.readAsDataURL(file);
    }
  }

  /**
   * displays the camera for the user to select/take a photo
   * @param {type} sourceType value Camera.PictureSourceType.PHOTOLIBRARY/Camera.PictureSourceType.CAMERA/Camera.PictureSourceType.SAVEDPHOTOALBUM
   * @returns {undefined}
   */
  selectPicture(sourceType: string): void {
    console.info("selectPicture: ");
    if (this.device.getMode() != this.device.MODE_MOBILE_APP) {
      console.error("illegal use");
      return;
    }
    let convertedSourceType;
    if (sourceType == this.SOURCE_CAMERA) {
      convertedSourceType = this.camera.PictureSourceType.CAMERA;
    } else {
      convertedSourceType = this.camera.PictureSourceType.PHOTOLIBRARY;
    }
    let self = this;
    let picOptions = {
      destinationType: this.camera.DestinationType.DATA_URL, //FILE_URI / NATIVE_URI
      sourceType: convertedSourceType,
      allowEdit: false,
      encodingType: this.camera.EncodingType.JPEG,
      mediaType: this.camera.MediaType.PICTURE,
      saveToPhotoAlbum: false,
      correctOrientation: true  //Corrects Android orientation quirks
    }

    this.camera.getPicture(picOptions).then((imageURI) => {
      //console.info("dataimg: "+imageURI);
      let url = "";
      //url = imageURI; //deactivate because this causes bug on android
      //url = "data:image/jpeg;base64," + imageURI;
      let itemImage = this.item;
      itemImage.type = "image";
      itemImage.localUrl = url;
      itemImage.distantUrl = "";
      itemImage.loading = true;
      this.uploadService.upload({
        data: imageURI,
        ext: "jpg"
      }).then(function (data) {
        console.info("upload complete: ",data);
        itemImage.loading = false;
        itemImage.distantUrl = data;
        self.notify(itemImage);
      });
    }, function (err) {
      console.error(err);
    });
  }


}
