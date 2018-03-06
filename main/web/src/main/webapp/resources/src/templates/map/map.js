(function () {
    'use strict';

    var mapTemplate = angular.module('template.map', ["ui.router","module.googleapi"]);

    mapTemplate.controller("template.map.controller", ["$scope", "GoogleAPIService",
        function ($scope,GoogleAPIService) {
            $scope.googleMapsUrl = "https://maps.googleapis.com/maps/api/js?key="+GoogleAPIService.getMapsAPIKey();
        }]);

})();