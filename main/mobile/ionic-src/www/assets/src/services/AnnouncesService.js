(function () {
    'use strict';

    var announces = angular.module('module.announces', ['module.smilanhostservice']);

    announces.service('AnnouncesService', ['$log', '$http', '$q', 'SmilanHostService', AnnounceService]);

    function AnnounceService($log, $http, $q, SmilanHostService) {
        // Promise-based API
        var cacheData = {};
        var cache = {
            isInCache:function(id){
                return cacheData[id]!==undefined;
            },
            getCache:function(id){
                return cacheData[id];
            },
            cacheElement:function(id,element){
                cacheData[id]=element;
            }
        };
        var apiHost = SmilanHostService.getAPIServerHost();
        var apipath = apiHost + "/api/v1/announces/";
        var online = false;
        {
            var img = new Image();
            img.onload = function () {
                online = true;
            };
            img.onerror = function () {
                online = true;
            };
            var start = new Date().getTime();
            img.src = apiHost;
            this.timer = setTimeout(function () {
                online = false;
            }, 10000);
        }
        var apikey = SmilanHostService.getAPIKeyHTTPParam;//alias for shortening
        return {
            isOnline: function () {
                return online;
            },
            post: function (announce) {
                return $http.post(apipath + apikey(), {
                    entities: [announce]}, {});
            },
            load: function (identifiant) {
                var cached = cache.getCache(identifiant);
                if(cached!==undefined){
                    return $q(function (resolve) {
                        resolve(cached);
                    });
                }
                return $http.get(apipath + identifiant + apikey(), {
                    identifiant: identifiant}, {}).then(
                        function (response) {
                            var element = response.data.entities[0];
                            cache.cacheElement(identifiant,element);
                            return element;
                        },
                        function (errResponse) {
                            $log.error('Error while fetching users');
                            return $q.reject(errResponse);
                        }
                );
            },
            delete: function (announceid) {
                var request = {
                    optionService: {
                        deleteOption: {
                            "delete": true
                        }
                    },
                    entities: [{
                            id: announceid
                        }]
                };
                return $http.post(apipath + 'delete' + apikey(), request)
                        .then(
                                function (response) {
                                    return response.data;
                                },
                                function (errResponse) {
                                    $log.error('Error while fetching users');
                                    return $q.reject(errResponse);
                                }
                        );
            },
            search: function (requestedAnnounces) {
                if (requestedAnnounces.entities.length === 0) {
                    $log.error('must have at least one entities if no option search');
                    return $q(function (resolve) {
                        resolve([]);
                    });
                }
                //search ids
                return $http.post(apipath + 'idsearch' + apikey(), requestedAnnounces)
                        .then(
                                function (response) {
                                    var announceIDs = response.data;
                                    if(announceIDs.length===0){
                                        return [];
                                    }
                                    //search in cache
                                    //fetch announces
                                    var requestedAnnouncesId = {
                                        optionService: {
                                            searchOption: {
                                                expression: "or" //or between the ids
                                            }
                                        },
                                        entities: []
                                    };
                                    announceIDs.forEach(function (element) {
                                        if(!cache.isInCache(element)){
                                            requestedAnnouncesId.entities.push({
                                                id: element
                                            });
                                        }
                                    });
                                    if(requestedAnnouncesId.entities.length===0){
                                        return $q(function (resolve) {
                                            var result = [];
                                            announceIDs.forEach(function (elementId) {
                                                var cachedElm = cache.getCache(elementId);
                                                if(cachedElm===undefined){
                                                    $log.error("invalid cache");
                                                }else{
                                                    result.push(cachedElm);
                                                }
                                            });
                                            resolve(result);
                                        });
                                    }else{
                                        //$log.info(requestedAnnouncesId);
                                        return $http.post(apipath + 'search' + apikey(), requestedAnnouncesId)
                                                .then(
                                                        function (response) {
                                                            response.data.entities.forEach(function(data){
                                                                cache.cacheElement(data.id,data);
                                                            });
                                                            var result = [];
                                                            announceIDs.forEach(function (elementId) {
                                                                var cachedElm = cache.getCache(elementId);
                                                                if(cachedElm===undefined){
                                                                    $log.error("invalid cache");
                                                                }else{
                                                                    result.push(cachedElm);
                                                                }
                                                            });
                                                            return result;
                                                        },
                                                        function (errResponse) {
                                                            $log.error('Error while fetching users');
                                                            return $q.reject(errResponse);
                                                        }
                                                );
                                    }
                                },
                                function (errResponse) {
                                    $log.error('Error while fetching users');
                                    return $q.reject(errResponse);
                                }
                        );
            },
            /**
             *
             * @param {
             *          lat:float
             *          lon:float
             *          dist:float
             *          } parameters
             * @returns {announces[String]}
             */
            geosearch: function (parameters) {
                return $http.post(apipath + 'geosearch' + apikey(), parameters)
                        .then(
                                function (response) {
                                    return response.data;
                                },
                                function (errResponse) {
                                    $log.error('Error while fetching users');
                                    return $q.reject(errResponse);
                                }
                        );
            }
        };
    }

})();
