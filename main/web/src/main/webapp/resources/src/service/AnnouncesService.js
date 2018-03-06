(function () {
    'use strict';

    var announces = angular.module('module.announces', []);

    announces.service('AnnouncesService', ['$http', '$q', AnnounceService]);

    /**
     * Users DataService
     * Uses embedded, hard-coded data model; acts asynchronously to simulate
     * remote data service call(s).
     *
     * @returns {{loadAll: Function}}
     * @constructor
     */
    function AnnounceService($http, $q) {
        // Promise-based API
        var apipath = "http://localhost:8080/smilan-web/api/v1/announces/"
        return {
            define: function (announce) {
                return $http.post(apipath, {
                    entities: [announce]}, {});
            },
            load: function (identifiant) {
                return $http.get(apipath + identifiant, {
                    identifiant: identifiant}, {});
            },
            search: function (requestedAnnounces) {
                return $http.post(apipath + 'search', requestedAnnounces)
                        .then(
                                function (response) {
                                    return response.data;
                                },
                                function (errResponse) {
                                    console.error('Error while fetching users');
                                    return $q.reject(errResponse);
                                }
                        );
            }
        };
    }

})();
