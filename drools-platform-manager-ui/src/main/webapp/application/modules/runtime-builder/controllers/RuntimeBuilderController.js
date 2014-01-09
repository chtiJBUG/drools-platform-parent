
DroolsPlatformControllers.controller('runtimeBuilderController',  function ($rootScope, $scope) {
    $rootScope.runtimeGenerationModule = true;
    $scope.isSettingsEnabled = true;
    $scope.isArtifactsEnabled = false;
    $scope.isGenerationsEnabled = false;
    $scope.isDeploymentEnabled = false;
});

