var droolsPlatformApp = angular.module('droolsPlatformApp', [
    'ngRoute',
    'http-auth-interceptor',
    'drools-platform.services',
    'drools-platform.controllers'
]);

droolsPlatformApp.config(function ($locationProvider) {
    //___ Enable Html5 mode
    $locationProvider.html5Mode(true);
});

droolsPlatformApp.config(function ($routeProvider, $httpProvider) {

    //___ Defines the routes
    $routeProvider
        .when('/home', {
            templateUrl: 'home/home.html',
            controller: HomeController
        }).
        otherwise({
            redirectTo: '/home'
        });
    //____ Intercepts the possibly error from the server
    var interceptor = function ($rootScope, $q) {
        var success = function (response) {
            response;
        };
        var error = function (response) {
            var errorId;
            if (response.status != 404 && response.status != 401 && response.status != 403)
                errorId = null;
            if (response.status == 500)
                errorId = response.headers('x-exception-id');
            $rootScope.$broadcast('event:serverError', errorId)
            $q.reject(response)
        };
        return function(promise){
            promise.then(success, error);
        };
    };
    $httpProvider.responseInterceptors.push(interceptor);
});

droolsPlatformApp.run(function ($rootScope, $log) {

    $rootScope.login = {};
    $rootScope.$on('event:auth-loginRequired', function () {
        $rootScope.loginRequired = true;
        $rootScope.login.focus = new Date().getTime();
        $log.log('Login required')
    });
    $rootScope.$on('event:auth-loginConfirmed', function () {
        $rootScope.loginRequired = false;
        $log.log('Login confirmed');
    });
});

var DroolsPlatformControllers = angular.module('drools-platform.controllers', ['drools-platform.services']);

var ServicesModule = angular.module('drools-platform.services', []);

DroolsPlatformControllers.controller('MainCtrl', ['$rootScope', '$scope', '$window', '$http', 'LoginService',
    function ($rootScope, $scope, $window, $http, LoginService) {
        $scope.logout = function () {
            var loginRequired = function () {
                $rootScope.loginRequired = true;
            };
            LoginService.logout().then(loginRequired);
        };
        $rootScope.$on('event:serverError', function (event, errorId) {
            var content = "";
            if (errorId != null)
                content = "<p>Error #" + errorId + "</p>";
            $scope.alerts = [{
                    type: "error",
                    title: "Oups, server error occurred ",
                    content: content
            }];
        });
        $http.get('./server/username')
            .success(function (data) {
                $rootScope.username = data
            })
            .error(function(cause) {
                $log.log("Error. Caused by : "+cause);
            });
    }
]);