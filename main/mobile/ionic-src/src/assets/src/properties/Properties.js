var GLOBAL_PROPERTIES=[];
(function () {
  'use strict';
  var pptList = GLOBAL_PROPERTIES;  //smillan
  pptList["app.mode"] = "dev";
  pptList["app.perso.tags"] = '["hec","mobile","app"]';//json format
  pptList["app.storage.mode.cookies"] = "true";
  //pptList["app.dir.base"] = "/debug/debug/";
  //pptList["server.host.api"] = "http://localhost:8084/smilan-web/";
  pptList["server.host.api"] = "https://mobile.smillan.com:9443/smilan-web/";
  pptList["server.host.chat"] = "https://mobile.smillan.com:5280/http-bind";
  pptList["server.host.upload"] = "https://media.smillan.com/image/upload_server.php";
  pptList["server.host.template.image"] = "https://media.smillan.com/image/template/";
  //google map API
  pptList["api.google.map.key"] = "XXX";
  pptList["api.google.map.version"] = "3.25";
  pptList["api.google.map.api.url"] = "https://maps.google.com/maps/api/js";
  pptList["api.google.map.api.url.full"] = "https://maps.googleapis.com/maps/api/js?v=" + pptList["api.google.map.version"]
    + "&key=" + pptList["api.google.map.key"];
})();
