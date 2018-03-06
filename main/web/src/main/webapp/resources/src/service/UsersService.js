(function () {
    'use strict';

    var users = angular.module('module.users', []);

    users.service('UserService', ['$http', '$q', UsersService]);

    /**
     * Users DataService
     * Uses embedded, hard-coded data model; acts asynchronously to simulate
     * remote data service call(s).
     *
     * @returns {{loadAll: Function}}
     * @constructor
     */
    function UsersService($http, $q) {

        var apipath = "http://localhost:8080/smilan-web/api/v1/users/"
        return {
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
            },
            defineUser: function (user) {
                var dataSet={
                    entities:[
                        user
                    ]
                }
                return $http.post(apipath, dataSet)
                        .then(
                                function (response) {
                                    return response.data;
                                },
                                function (errResponse) {
                                    console.error('Error while creating user');
                                    return $q.reject(errResponse);
                                }
                        );
            },
            deleteUser: function (idr) {
                var dataSet={
                    entities:[
                        {id:idr}
                    ]
                }
                return $http.delete(apipath,dataSet)
                        .then(
                                function (response) {
                                    return response.data;
                                },
                                function (errResponse) {
                                    console.error('Error while deleting user');
                                    return $q.reject(errResponse);
                                }
                        );
            }

        };

    }

})();