var droolsPlatformApp = angular.module('droolsPlatformApp', [
    'ngRoute',
    'http-auth-interceptor',
    'drools-platform.services',
    'drools-platform.controllers',
    'ui.select2',
    'ui.bootstrap',
    'ui.codemirror',
    'ng-context-menu',
    'duScroll',
    'paginator'

]);

droolsPlatformApp.config(function ($locationProvider) {
    //___ Enable Html5 mode
    $locationProvider.html5Mode(true);
});


droolsPlatformApp.factory('httpInterceptor', ['$q', '$rootScope', function ($q, $rootScope) {

    function success(response) {
        return response;
    }

    function error(response) {
        var errorId;
        if (response.status != 404 && response.status != 401 && response.status != 403)
            errorId = null;
        if (response.status == 500)
            errorId = response.headers('x-exception-id');
        $rootScope.$broadcast('event:serverError', errorId)
        $q.reject(response)
    }

    return function (promise) {
        return promise.then(success, error);
    };

}]);

droolsPlatformApp.run(function ($rootScope, $log, $location) {

    $rootScope.login = {};
    $rootScope.$on('event:auth-loginRequired', function () {
        $rootScope.loginRequired = true;
        $rootScope.login.focus = new Date().getTime();
        $log.log('Login required')
    });
    $rootScope.$on('event:auth-loginConfirmed', function () {
        $rootScope.loginRequired = false;
        /* When login confirmed : redirect to homepage */
        $location.path('/home');
        $log.log('Login confirmed');
    });

});

//___ Pagination system found on FRAngular.com
var paginator = angular.module('paginator', []);
paginator.directive('paginator', function () {
    var pageSizeLabel = "Page size";
    return {
        priority: 0,
        restrict: 'A',
        scope: {items: '&'},
        template:
            '<button ng-disabled="isFirstPage()" ng-click="decPage()">&lt;</button> '
            + '{{paginator.currentPage+1}}/{{numberOfPages()}} '
            + '<button ng-disabled="isLastPage()" ng-click="incPage()">&gt;</button> '
            + '<span>' + pageSizeLabel + '</span> '
            + '<select ng-model="paginator.pageSize" ng-options="size for size in pageSizeList"></select> ',
        replace: false,
        compile: function compile(tElement, tAttrs, transclude) {
            return {
                pre: function preLink(scope, iElement, iAttrs, controller) {
                    scope.pageSizeList = [5, 10, 20, 50, 100];
                    scope.paginator = {
                        pageSize: 5,
                        currentPage: 0
                    };

                    scope.isFirstPage = function () {
                        return scope.paginator.currentPage == 0;
                    };
                    scope.isLastPage = function () {
                        return scope.paginator.currentPage
                            >= scope.items().length / scope.paginator.pageSize - 1;
                    };
                    scope.incPage = function () {
                        if (!scope.isLastPage()) {
                            scope.paginator.currentPage++;
                        }
                    };
                    scope.decPage = function () {
                        if (!scope.isFirstPage()) {
                            scope.paginator.currentPage--;
                        }
                    };
                    scope.firstPage = function () {
                        scope.paginator.currentPage = 0;
                    };
                    scope.numberOfPages = function () {
                        return Math.ceil(scope.items().length / scope.paginator.pageSize);
                    };
                    scope.$watch('paginator.pageSize', function(newValue, oldValue) {
                        if (newValue != oldValue) {
                            scope.firstPage();
                        }
                    });

                    // ---- Functions available in parent scope -----

                    scope.$parent.firstPage = function () {
                        scope.firstPage();
                    };
                    // Function that returns the reduced items list, to use in ng-repeat
                    scope.$parent.pageItems = function () {
                        var start = scope.paginator.currentPage * scope.paginator.pageSize;
                        var limit = scope.paginator.pageSize;
                        return scope.items().slice(start, start + limit);
                    };
                },
                post: function postLink(scope, iElement, iAttrs, controller) {}
            };
        }
    };
});



var DroolsPlatformControllers = angular.module('drools-platform.controllers', []);

var ServicesModule = angular.module('drools-platform.services', []);

DroolsPlatformControllers.controller('MainCtrl', ['$rootScope', '$scope', '$window', '$http', 'LoginService','$log',
    function ($rootScope, $scope, $window, $http, LoginService, $log) {
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
            $scope.alerts = [
                {
                    type: "error",
                    title: "Oups, server error occurred ",
                    content: content
                }
            ];
        });
        $http.get('./server/username')
            .success(function (data) {
                $rootScope.username = data
            })
            .error(function (cause) {
                $log.log("Error. Caused by : " + cause);
                $rootScope.username = "default";
            });
    }
]);

