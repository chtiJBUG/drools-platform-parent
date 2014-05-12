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
        when('/runtimeAnalysis', {
            templateUrl: 'modules/runtime-monitoring/runtime-analysis/runtime-analysis.html',
            controller: 'runtimeAnalysisController'
        }).

        otherwise({
            redirectTo: '/home'
        });
});
