DroolsPlatformControllers.controller('artifactsDefinitionsController', function ($rootScope, $scope, $location) {

    $scope.artifact = {
        host: 'http://www.facebook.com',
        appName: 'web app context'
    }

    $scope.validateDefinitions = function (artifactsDefinitionsForm) {
        if (artifactsDefinitionsForm.$valid) {
            // alert("artifact");
            $location.path( "/runtimeBuilder/runtimeBuilding" );
        }

    };
    $scope.gotoPreviousStep = function () {
        $location.path( "/runtimeBuilder/guvnorSettings");
    };
});
