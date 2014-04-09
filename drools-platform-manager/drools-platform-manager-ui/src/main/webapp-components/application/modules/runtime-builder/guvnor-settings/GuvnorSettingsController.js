DroolsPlatformControllers.controller('guvnorSettingsController', function ($rootScope, $scope, $location) {

    $scope.guvnor = {
        host: 'http://www.google.fr',
        appName: 'web app context'
    }

    $scope.validateSettings = function (guvnorSettingsForm) {


        if (guvnorSettingsForm.$valid) {
            // alert("settings");
            $location.path( "/runtimeBuilder/runtimeDefinition" );
        }

    };


});
