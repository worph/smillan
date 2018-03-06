(function () {
    'use strict';

    var module = angular.module('socket.service', ['ngSanitize', 'btford.socket-io']);

    module.factory('socket', ['socketFactory', function (socketFactory) {
            //Create socket and connect to http://chat.socket.io 
            var myIoSocket = io.connect('http://chat.socket.io');
            //var myIoSocket = io.connect('http://localhost:3000/');

            var mySocket = socketFactory({
                ioSocket: myIoSocket
            });

            return mySocket;
        }]);

})();
