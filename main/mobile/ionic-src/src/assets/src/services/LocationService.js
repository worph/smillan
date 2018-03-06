(function () {
  'use strict';

  var module = angular.module('module.location', [
    "ngCordova",
    'module.properties.service'
  ]);

  module.service('LocationService', ['$log', '$q', '$cordovaGeolocation', '$http', 'PropertiesService', function ($log, $q, $cordovaGeolocation, $http, PropertiesService) {
    //http://stackoverflow.com/questions/391979/how-to-get-clients-ip-address-using-javascript-only
    var lastGeo = null;//quality higher number is beter
    //timeout in ms
    var posOptionsAcurate = {timeout: 10000, enableHighAccuracy: true};
    var posOptionsFast = {timeout: 200, enableHighAccuracy: false};
    var INTERNET_QUALITY = 20;
    var FASTGEO_QUALITY = 30;
    var ACURATEGEO_QUALITY = 40;

    function updateLastGeo(newGeo) {
      if (lastGeo === null) {
        lastGeo = newGeo;
        return;
      }
      if (lastGeo.quality <= newGeo.quality) {
        lastGeo = newGeo;
        return;
      }
    }

    function triggerInternetGeoLoc() {
      return $q(function (resolve, fail) {
        $http.get("https://freegeoip.net/json/").then(function (data) {
          updateLastGeo({
            lat: data.data.latitude,
            lng: data.data.longitude,
            quality: INTERNET_QUALITY
          });
          resolve(lastGeo);
        }, function (err) {
          fail(err);
          // error no fall back fallback :/
        });
      });
    }

    function triggerGeoLoc(posOptions) {
      if (PropertiesService.getProperty("app.geo.enabled") !== "true") {
        return $q(function (resolve, fail) {
            resolve({
              lat: 1.0,
              lng: 1.0,
              quality: INTERNET_QUALITY
            });
        });
      } else {
        return $q(function (resolve, fail) {
          $cordovaGeolocation
            .getCurrentPosition(posOptions)
            .then(function (position) {
              var lat = position.coords.latitude;
              var lng = position.coords.longitude;
              updateLastGeo({
                lat: lat,
                lng: lng,
                quality: posOptions.enableHighAccuracy ? ACURATEGEO_QUALITY : FASTGEO_QUALITY
              });
              resolve(lastGeo);
            }, function (err) {
              triggerInternetGeoLoc().then(function (data) {
                resolve(data);
              }, function (err) {
                fail(err);
                // error no fall back fallback :/
              });
            });
        });
      }
    }

    triggerInternetGeoLoc().then(function (geo) {
      $log.info("internet geo:" + geo.lat + "/" + geo.lng);
    });
    triggerGeoLoc(posOptionsFast).then(function (geo) {
      $log.info("fast geo:" + geo.lat + "/" + geo.lng);
    });
    triggerGeoLoc(posOptionsAcurate).then(function (geo) {
      $log.info("acurate geo:" + geo.lat + "/" + geo.lng);
    });
    return {
      getLocation: function () {
        return $q(function (resolve, fail) {
          if (lastGeo === null) {
            triggerGeoLoc(posOptionsFast).then(function (geo) {
              resolve(geo);
            });
          } else {
            triggerGeoLoc(posOptionsAcurate);//update geo for the next time
            resolve(lastGeo);
          }
        });
      }
    };
  }]);

})();
