DroolsPlatformControllers.controller('runtimeBuilderController', function ($rootScope, $scope, $route) {
    $rootScope.runtimeGenerationModule = true;
    $scope.isSettingsEnabled = true;
    $scope.isArtifactsEnabled = false;
    $scope.isGenerationsEnabled = false;
    $scope.isDeploymentEnabled = false;

    var subView = 'settings';
    if ($route.current.action != null) {
        var subView = $route.current.action.replace("runtime.builder.", "");
    }

    $scope.subview = subView;

    $scope.$on(
        "routeChangeSuccess",
        function ($currentRoute, $previousRoute) {
            if ($currentRoute.action == null)
                return;
            $currentRoute.action.startsWith("runtime.builder.")

        }
    );
});

