(function () {
    'use strict';

    var announces = angular.module('module.googleapi', []);

    announces.service('GoogleAPIService', [GoogleAPIService]);
    function GoogleAPIService() {
        return {
            getMapsAPIKey: function () {
                return "AIzaSyCEZ3EbIkH7J7f8xNrj7j0e8m5RokxoRMs";
            }
        };
    }

})();
