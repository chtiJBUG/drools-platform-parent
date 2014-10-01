/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

DroolsPlatformControllers.controller('loginController', ['$scope', '$rootScope', 'authService', 'LoginService', function ($scope, $rootScope, authService, LoginService) {
    $scope.submit = function () {
        var success = function (username) {
            authService.loginConfirmed();
            $scope.authFailure = false;
            $scope.password = null;
            $rootScope.username = username;
        };
        var failure = function () {
            $scope.authFailure = true;
        };
        LoginService.login($scope.username, $scope.password).then(success, failure);
    };
}]);

