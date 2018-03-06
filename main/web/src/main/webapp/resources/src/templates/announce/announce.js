(function () {
    'use strict';

    var mapTemplate = angular.module('template.announce', ['ui.router', 'module.announces', 'ngMessages', 'ngMaterial','angularGrid']);


    mapTemplate.filter('unsafe', ['$sce',
        function ($sce) {
            return $sce.trustAsHtml;
        }]);

    mapTemplate.controller("template.announce.controller", ['$scope', '$http', 'AnnouncesService',
        function ($scope, $http, AnnouncesService) {
            var self = this;
            $scope.card = {};
            $scope.card.title = 'test';
            self.page = 0;
            self.shots = [];
            self.loadingMore = false;

            self.loadMoreShots = function () {
                if (self.loadingMore)
                    return;
                self.page++;
                self.loadingMore = true;
                
                var requestedAnnounces = { 
                    optionService:{
                        searchOption:{
                            number:10,page:self.page-1
                        }
                    },
                    entities: [
                        {}
                    ]
                };
                var promise = AnnouncesService.search(requestedAnnounces);
                //var promise = $http.get('https://api.dribbble.com/v1/shots/?per_page=24&page=' + self.page + '&access_token=3df6bcfc60b54b131ac04f132af615e60b0bd0b1cadca89a4761cd5d125d608f');

                promise.then(function (data) {
                    data = data.entities;
                    var shotsTmp = angular.copy(self.shots);
                    shotsTmp = shotsTmp.concat(data);
                    //shotsTmp = shotsTmp.concat(data.data);
                    self.shots = shotsTmp;
                    self.loadingMore = false;

                }, function () {
                    self.loadingMore = false;
                });
                return promise;
            };

            self.loadMoreShots();
        }]);

})();
