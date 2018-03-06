var GLOBAL_PROPERTIES=[];
(function () {
    'use strict';

    var module = angular.module('module.properties.service', []);

    var pptList = GLOBAL_PROPERTIES;
            //smillan
            pptList["app.mode"] = "production";
            pptList["server.host.api"] = "https://mobile.smillan.com:8443/smilan-web";//"https://api.smillan.com:8080/smilan-web";
            pptList["server.host.chat"] = "https://mobile.smillan.com:5280/http-bind";//"https://chat.smillan.com:5280/http-bind";
            pptList["server.host.upload"] = "https://media.smillan.com/image/upload_server.php";
            pptList["server.host.template.image"] = "https://media.smillan.com/image/template/";
            //google map API
            pptList["api.google.map.key"] = "AIzaSyDKoirvt8Ytrl-pzHGem-juM6Ye68zqQ38";
            pptList["api.google.map.version"] = "3.25";
            pptList["api.google.map.api.url"] = "https://maps.google.com/maps/api/js";
            pptList["api.google.map.api.url.full"] = "https://maps.googleapis.com/maps/api/js?v=" + pptList["api.google.map.version"] 
                    + "&key=" + pptList["api.google.map.key"];

    module.service('PropertiesService', [function () {


            return {
                getProperty: function (pptName) {
                    return pptList[pptName];
                }
            };
        }]);
})();
