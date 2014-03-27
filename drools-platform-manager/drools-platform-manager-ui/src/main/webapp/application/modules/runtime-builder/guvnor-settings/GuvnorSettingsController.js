DroolsPlatformControllers.controller('guvnorSettingsController', function ($rootScope, $scope, $route) {
    $scope.validateSettings = function () {
        $scope.isSettingsEnabled = false;
        $scope.isArtifactsEnabled = true;
        $scope.isGenerationsEnabled = false;

    };
});
