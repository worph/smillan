//usage
ionic build android
ionic build browser
ionic run android
cordova build --release android

//setup project and add plugin
ionic cordova resources
ionic cordova platform add android ios browser

//plugins (to install)
ionic cordova plugin add cordova-plugin-camera --save
npm install --save @ionic-native/camera
ionic cordova plugin add cordova-plugin-device --save
npm install --save @ionic-native/device
ionic cordova plugin add cordova-plugin-network-information --save
npm install --save @ionic-native/network
ionic cordova plugin add cordova-plugin-splashscreen --save
npm install --save @ionic-native/splash-screen
ionic cordova plugin add cordova-plugin-statusbar --save
npm install --save @ionic-native/status-bar
ionic cordova plugin add ionic-plugin-keyboard
npm install --save @ionic-native/keyboard
ionic cordova plugin add cordova-plugin-whitelist --save
ionic cordova plugin add cordova-sqlite-storage --save
npm install --save @ionic/storage
ionic cordova plugin save


ionic cordova plugin add cordova.plugins.diagnostic --save
ionic cordova plugin cordova-plugin-request-location-accuracy --save


//Other just for info (do not install)
ionic cordova plugin add cordova-plugin-keyboard --save
cordova plugin add https://github.com/b-alidra/-cordova-imagePickerEx.git
cordova plugin add https://github.com/ratkop/-cordova-imagePickerEx.git
cordova plugin rm cordova-plugin-image-picker


////////////////////////////////////////////////////////
Android AndroidManifest.xml
////////////////////////////////////////////////////////
add : 
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
