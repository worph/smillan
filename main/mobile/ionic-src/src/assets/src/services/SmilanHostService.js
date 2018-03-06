(function () {
    'use strict';

    var service = angular.module('module.smilanhostservice', ['module.properties.service']);

    service.service('SmilanHostService', ["PropertiesService", function (PropertiesService) {
            this.chatIsOnline = false;
            this.apiIsOnline = false;
            var apiKey = null;
            return {
                getAPIServerHost: function () {
                    return PropertiesService.getProperty("server.host.api");
                },
                getChatServerHost: function () {
                    return PropertiesService.getProperty("server.host.chat");
                },
                getUploadServerHost: function () {
                    return PropertiesService.getProperty("server.host.upload");
                },
                getImageTemplateHost: function () {
                    return PropertiesService.getProperty("server.host.template.image");
                },
                getAPIServerOnline: function () {
                    return this.apiIsOnline;
                },
                getChatServerOnline: function () {
                    return this.chatIsOnline;
                },
                setAPIServerOnline: function (value) {
                    this.apiIsOnline = value;
                },
                setChatServerOnline: function (value) {
                    this.chatIsOnline = value;
                },
                getAPIKey: function () {
                    return apiKey;
                },
                setAPIKey: function (key) {
                    apiKey = key;
                },
                getAPIKeyHTTPParam: function(){
                    return (apiKey===null || apiKey==="")?"":"?apikey="+apiKey;
                }
            };
        }]);


})();



