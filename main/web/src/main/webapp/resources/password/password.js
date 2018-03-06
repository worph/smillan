
var app = angular.module('SmilanApp', ['ngMaterial', 'ngMessages']);

app.controller("password.controller", ['$scope', '$log', '$http',"$location",
    function ($scope, $log, $http,$location) {
        $log.info("started");
        $scope.vm = {};
        $scope.vm.password1 = "";
        $scope.vm.otp = "";
        $scope.vm.email = "";
        $scope.vm.processed = false;
        $scope.vm.processedResultMessage = false;

        $scope.submit = function () {
            var otp =  $location.search().otp;
            $log.info("submit : " + $scope.vm.password1 + "otp:"+otp);
            var newPassword = $scope.vm.password1;
            var url = "https://mobile.smillan.com:8443/smilan-web/api/v1/password/change?otp=";//TODO parameter this
            var host = window.location.hostname;
            if(host==="localhost"){
                var url = "http://localhost:8084/smilan-web/api/v1/password/change?otp=";//TODO parameter this  
            }
            
            $http.get(url + otp + "&newPassword=" + newPassword,{}).then(
                function (data) {
                    var status = data.data.status;
                    if (status === "OK") {
                        $scope.vm.processedResultMessage="Password Succesfuly changed you can now login with your new pasword";
                    } else if (status === "ERR_INVALID_OTP") {
                        $scope.vm.processedResultMessage="This link has been deactivated please retry the password recovery procedure";
                    } else {
                        $log.error(data);
                        $log.error("server error");
                        $scope.vm.processedResultMessage="Server is not available for the moment please try later";
                    }
                    $scope.vm.processed = true;
                },
                function(err){
                    $log.error(err);
                    $log.error("server error");
                    $scope.vm.processedResultMessage="Server is not available for the moment please try later";
                    $scope.vm.processed = true;
                }
            );
        };

    }]);

