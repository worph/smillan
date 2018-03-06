(function () {
  'use strict';

  var module = angular.module('module.properties.service', []);

  module.service('PropertiesService', [function () {
    return {
      getProperty: function (pptName) {
        return GLOBAL_PROPERTIES[pptName];
      }
    };
  }]);
})();
