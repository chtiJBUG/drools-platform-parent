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

