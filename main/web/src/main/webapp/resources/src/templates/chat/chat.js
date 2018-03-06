(function () {
    'use strict';

    var mapTemplate = angular.module('template.chat', ['ngSanitize', 'btford.socket-io', 'socket.service']);

    mapTemplate.controller('template.chat.controller', ['$scope', '$stateParams', 'socket', '$sanitize', '$timeout', function ($scope, $stateParams, socket, $sanitize, $timeout) {

            $scope.msgs = [];
            $scope.typingStr = "";
            var self = this;
            var typing = false;
            var typingUsers = [];
            var lastTypingTime;
            var TYPING_TIMER_LENGTH = 400;
            var connected = false;

            //initializing messages array
            $stateParams.nickname = "morlock";

            //publish message
            $scope.sendMsg = function (text) {
                if (!text || text === '') {
                    return;
                }
                var msg = {username: "me", msg: text};
                socket.emit('new message', text);
                socket.emit('stop typing');
                $scope.input.msg = '';//clear the text area
                displayMsg(msg);
            };

            //function called on Input Change
            $scope.updateTyping = function () {
                sendUpdateTyping();
            };

            socket.on('connect', function () {

                connected = true;

                //Add user
                socket.emit('add user', $stateParams.nickname);

                // On login display welcome message
                socket.on('login', function (data) {
                    //Set the value of connected flag
                    self.connected = true;
                    self.number_message = message_string(data.numUsers);
                });

                // Whenever the server emits 'new message', update the chat body
                socket.on('new message', function (data) {
                    if (data.message && data.username)
                    {
                        var msg = {username: $sanitize(data.username), msg: $sanitize(data.message)};
                        msg.received = true;
                        displayMsg(msg);
                    }
                });

                // Whenever the server emits 'user joined', log it in the chat body
                socket.on('user joined', function (data) {
                    var msg = {username: data.username, msg: data.username + " joined"};
                    msg.received = true;
                    displayMsg(msg);
                });

                // Whenever the server emits 'user left', log it in the chat body
                socket.on('user left', function (data) {
                    var msg = {username: data.username, msg: data.username + " left"};
                    msg.received = true;
                    displayMsg(msg);
                });

                //Whenever the server emits 'typing', show the typing message
                socket.on('typing', function (data) {
                    addChatTyping($sanitize(data.username));
                });

                // Whenever the server emits 'stop typing', kill the typing message
                socket.on('stop typing', function (data) {
                    removeChatTyping($sanitize(data.username));
                });
            });

            //display our msg
            function displayMsg(msg) {
                $scope.msgs.push(msg);
            }

            // Updates the typing event
            function sendUpdateTyping() {
                if (connected) {
                    if (!typing) {
                        typing = true;
                        socket.emit('typing');
                    }
                }
                lastTypingTime = (new Date()).getTime();
                $timeout(function () {
                    var typingTimer = (new Date()).getTime();
                    var timeDiff = typingTimer - lastTypingTime;
                    if (timeDiff >= TYPING_TIMER_LENGTH && typing) {
                        socket.emit('stop typing');
                        typing = false;
                    }
                }, TYPING_TIMER_LENGTH);
            }

            function updateTyping() {
                if (typingUsers.length > 0) {
                    $scope.typingStr = typingUsers + " is typing";
                } else {
                    $scope.typingStr = "";
                }
            }

            // Adds the visual chat typing message
            function addChatTyping(username) {
                typingUsers.push(username);
                var uniqueNames = [];
                typingUsers.forEach(function (element) {
                    if (uniqueNames.indexOf(element) === -1) {
                        uniqueNames.push(element);
                    }
                });
                typingUsers = uniqueNames;
                updateTyping();
            }

            // Removes the visual chat typing message
            function removeChatTyping(username) {
                //removeFromArray(typingUsers,username);
                //typingUsers.remove(username);

                typingUsers = typingUsers.filter(function (element) {
                    return element !== username;
                });
                updateTyping();
            }

            // Return message string depending on the number of users
            function message_string(number_of_users)
            {
                return number_of_users === 1 ? "there's 1 participant" : "there are " + number_of_users + " participants";
            }
        }]);

})();
