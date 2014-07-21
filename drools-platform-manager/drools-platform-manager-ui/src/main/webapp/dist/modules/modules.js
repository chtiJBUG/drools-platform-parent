var droolsPlatformApp = angular.module('droolsPlatformApp', [
    'ngRoute',
    'http-auth-interceptor',
    'drools-platform.services',
    'drools-platform.controllers',
    'ui.select2'
]);

droolsPlatformApp.config(function ($locationProvider) {
    //___ Enable Html5 mode
    $locationProvider.html5Mode(true);
});

droolsPlatformApp.config(function ($routeProvider) {

    //___ Defines the routes
    $routeProvider
        .when('/home', {
            templateUrl: 'modules/home/home.html',
            controller: 'homeController'
        }).
        when('/runtimeBuilder', {
            templateUrl: 'modules/runtime-builder/runtime-builder.html',
            controller: 'runtimeBuilderController',
            action: "runtime.builder.settings"
        }).
        when("/runtimeBuilder/guvnorSettings", {
            templateUrl: 'modules/runtime-builder/runtime-builder.html',
            controller: 'runtimeBuilderController',
            action: "runtime.builder.settings"
        }).
        when("/runtimeBuilder/runtimeDefinition", {
            templateUrl: 'modules/runtime-builder/runtime-builder.html',
            controller: 'runtimeBuilderController',
            action: "runtime.builder.definition"
        }).
        when("/runtimeBuilder/runtimeBuilding", {
            templateUrl: 'modules/runtime-builder/runtime-builder.html',
            controller: 'runtimeBuilderController',
            action: "runtime.builder.building"
        }).
        when("/runtimeBuilder/runtimeDeployment", {
            templateUrl: 'modules/runtime-builder/runtime-builder.html',
            controller: 'runtimeBuilderController',
            action: "runtime.builder.deployment"
        }).
        when('/changeAssetStatus', {
            templateUrl: 'modules/asset-management/status-modification/status-modification.html',
            controller: 'homeController'
        }).
        when('/buildAssetPackage', {
            templateUrl: 'modules/asset-management/package-building/package-building.html',
            controller: 'assetPackagingController'
        }).
        otherwise({
            redirectTo: '/home'
        });
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
            });
    }
]);


DroolsPlatformControllers.controller('homeController',  function ($scope) {
    $scope.message = "World";
});


ServicesModule.service('LoginService', ['$http', '$q', function ($http, $q) {
    this.login = function (username, password) {
        var deferred = $q.defer();
        var data = $.param({username: username, password: password});
        $http.post('./server/login', data, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
                deferred.resolve(data);
            })
            .error(function () {
                deferred.reject();
            });
        return deferred.promise;
    };

    this.logout = function () {
        var deferred = $q.defer();
        $http.get('./server/logout')
            .success(function () {
                deferred.resolve();
            })
            .error(function () {
                deferred.reject();
            });
        return deferred.promise;
    };
}
]);


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


DroolsPlatformControllers.controller('runtimeBuilderController', function ($rootScope, $scope, $route) {
    $rootScope.runtimeGenerationModule = true;
    $scope.isSettingsEnabled = true;
    $scope.isArtifactsEnabled = false;
    $scope.isGenerationsEnabled = false;

    var subView = 'settings';
    if ($route.current.action != null) {
        var subView = $route.current.action.replace("runtime.builder.", "");
    }

    $scope.subview = subView;

    $rootScope.$on(
        "routeChangeSuccess",
        function ($current, $previous) {
            if ($current.action == null)
                return;
            $scope.subview = $current.action.replace("runtime.builder.", "");

        }
    );
});


DroolsPlatformControllers.controller('guvnorSettingsController', function ($rootScope, $scope, $route) {
    $scope.validateSettings = function () {
        $scope.isSettingsEnabled = false;
        $scope.isArtifactsEnabled = true;
        $scope.isGenerationsEnabled = false;

    };
});

DroolsPlatformControllers.controller('assetStatusController', function ($rootScope, $scope, $http) {
    $scope.filters = {};
    $scope.assets = undefined;
    $http.get('./server/rule_status/all')
        .success(function (data) {
            $scope.statuses = data;
        })
        .error(function (error) {
            console.log(error);
        });

    $scope.filtersOptions = {
        width: '600px',
        selectOnBlur: true
    };

    $scope.search = function () {
        var filters = $scope.filters;
        if (filters == undefined || filters.length == 0)
            return;
        $http.post('./server/rule_status', JSON.stringify(filters))
            .success(function (data) {
                $scope.assets = data;
            })
            .error(function (error) {
                console.log(error);
            });
    };

    $scope.promoteAssetsStatus = function () {
        var assetsToPromote = _.where($scope.assets, {selected: true});
        var data = {assetsToPromote: assetsToPromote, assetStatuses: $scope.filters};
        $http.post('./server/rule_status/promote', data)
            .success(function (data) {
                $scope.assets = data;
            })
            .error(function (error) {
                console.log(error);
            });
    };

    $scope.demoteAssetsStatus = function () {
        var assetsToDemote = _.where($scope.assets, {selected: true});
        var data = {assetsToDemote: assetsToDemote, assetStatuses: $scope.filters};
        $http.post('./server/rule_status/demote', data)
            .success(function (data) {
                $scope.assets = data;
            })
            .error(function (error) {
                console.log(error);
            });
    };

});

DroolsPlatformControllers.controller('assetPackagingController', function ($rootScope, $scope, $http, $log) {
    $scope.filters = {};
    $scope.isRelease = false;
    $scope.version = undefined;

    $http.get('./server/rule_status/all')
        .success(function (data) {
            $scope.statuses = data;
        })
        .error(function (error) {
            console.log(error);
        });


    $scope.createVersion = function() {
        var packageSnapshotRequest = {
            assetStatuses: $scope.filters,
            version: $scope.version,
            isRelease: $scope.isRelease
        };

        $http.post('./server/rules_package/build', packageSnapshotRequest)
            .success(function (data) {
                $scope.assets = data;
            })
            .error(function (error) {
                console.log(error);
            });
    };
});
