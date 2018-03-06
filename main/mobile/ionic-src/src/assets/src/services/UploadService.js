(function () {
    'use strict';

    var users = angular.module('module.uploads', ['module.uploads']);

    users.service('UploadService', ['$log','$http', '$q', 'SmilanHostService', service]);
    function service($log,$http, $q, SmilanHostService) {

        var apiHost = SmilanHostService.getUploadServerHost();
        return {
            /**
             * 
             * @param {{
                                data: dataURI,
                                ext: "jpg"
                            }} file
             * @returns {unresolved}
             */
            upload: function (file) {
                return $http.post(apiHost, file)
                        .then(
                                function (response) {
                                    return response.data;
                                },
                                function (errResponse) {
                                    $log.error('Error HTTP request');
                                    return $q.reject(errResponse);
                                }
                        );
            }
        };
    }

})();