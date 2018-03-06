(function () {
    'use strict';

    var mapTemplate = angular.module('template.user', ['ui.router','module.users','ngMessages','ngMaterial']);

    mapTemplate.controller("template.user.controller", ['$scope', 'UserService',
        function ($scope, UserService) {
            var self = this;
            self.user = {id: null, username: '', address: '', email: ''};
            self.users = [];

            self.fetchAllUsers = function () {
                var searchData={
                    entities:[{}]
                };
                UserService.search(searchData)
                        .then(
                                function (d) {
                                    self.users = d.entities;
                                },
                                function (errResponse) {
                                    console.error('Error while fetching Currencies');
                                }
                        );
            };

            self.defineUser = function (user) {
                UserService.defineUser(user)
                        .then(
                                self.fetchAllUsers,
                                function (errResponse) {
                                    console.error('Error while creating User.');
                                }
                        );
            };

            self.deleteUser = function (id) {
                UserService.deleteUser(id)
                        .then(
                                self.fetchAllUsers,
                                function (errResponse) {
                                    console.error('Error while deleting User.');
                                }
                        );
            };

            self.fetchAllUsers();

            self.submit = function () {
                if (self.user.id === null) {
                    console.log('Saving New User', self.user);
                } else {
                    console.log('User updated with id ', self.user.id);
                }
                self.defineUser(self.user);
                self.reset();
            };

            self.edit = function (id) {
                console.log('id to be edited', id);
                for (var i = 0; i < self.users.length; i++) {
                    if (self.users[i].id === id) {
                        self.user = angular.copy(self.users[i]);
                        break;
                    }
                }
            };

            self.remove = function (id) {
                console.log('id to be deleted', id);
                if (self.user.id === id) {//clean form if the user to be deleted is shown there.
                    self.reset();
                }
                self.deleteUser(id);
            };

            self.reset = function () {
                self.user = {id: null, username: '', address: '', email: ''};
                $scope.userForm.$setUntouched();
                $scope.userForm.$setValidity();
                $scope.userForm.$setPristine(); //reset Form
            };
        }]);

})();
