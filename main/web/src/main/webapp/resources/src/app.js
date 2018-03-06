                  
var app = angular.module('SmilanApp', [
    'ngMaterial','ui.router','ngMap','angularGrid',
    'template.map','template.user','template.announce','template.chat']);
app.config(
        [ "$stateProvider","$urlRouterProvider","$locationProvider", function($stateProvider, $urlRouterProvider,$locationProvider) {  
            //$locationProvider.html5Mode(true).hashPrefix('*');;
            
            $stateProvider.state("admin-user", {
                url: "/admin/user",
                templateUrl: "templates/admin/user/user.html",
                controller: "template.user.controller as ctrl"
            });
            
            
            $stateProvider.state("map", {
                url: "/map",
                templateUrl: "templates/map/map.html",
                controller: "template.map.controller"
            });
            
            $stateProvider.state("chat", {
                url: "/chat",
                templateUrl: "templates/chat/chat.html",
                controller: "template.chat.controller as chatCtrl"
            });
            
            $stateProvider.state("announce", {
                url: "/announce",
                templateUrl: "templates/announce/announce.html",
                controller: "template.announce.controller as ctrl"
            });
            
            // if none of the above states are matched, use this as the fallback
            $urlRouterProvider.otherwise('/admin/user');
        } ]);
        
app.config([ "$mdThemingProvider","$mdIconProvider", function($mdThemingProvider, $mdIconProvider) {
    var assetsFolder = "./../assets/"
    $mdIconProvider
            .defaultIconSet(assetsFolder+"svg/avatars.svg", 128)
            .icon("menu", assetsFolder+"svg/menu.svg", 24)
            .icon("share", assetsFolder+"svg/share.svg", 24)
            .icon("google_plus", assetsFolder+"svg/google_plus.svg", 512)
            .icon("hangouts", assetsFolder+"svg/hangouts.svg", 512)
            .icon("twitter", assetsFolder+"svg/twitter.svg", 512)
            .icon("phone", assetsFolder+"svg/phone.svg", 512);

    $mdThemingProvider.theme('default')
            .primaryPalette('brown');

    /*$mdThemingProvider.theme('default')
            .primaryPalette('blue')
            .accentPalette('red')
            .warnPalette('orange')
            .backgroundPalette('blue');*/
                        
}]);
