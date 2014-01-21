

DroolsPlatformControllers.controller('loginController', ['$scope', '$rootScope','authService', 'LoginService', function ($scope, $rootScope, authService, LoginService) {
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

