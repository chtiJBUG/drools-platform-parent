DroolsPlatformControllers.controller('runtimeBuilderController', function ($rootScope, $scope, $route) {
    $rootScope.runtimeGenerationModule = true;
    $scope.isSettingsEnabled = true;
    $scope.isArtifactsEnabled = false;
    $scope.isGenerationsEnabled = false;

    var subView = 'settings';
    if ($route.current.action != null) {

        /* Warning : Must be called subView */
        var subView = $route.current.action.replace("runtime.builder.", "");
            /* Define actual colored badge */
            if(subView=="settings"){
                /* Define enability of the link */
                $scope.isSettingsEnabled = true;
                $scope.isArtifactsEnabled = false;
                $scope.isGenerationsEnabled = false;

            }else if(subView=="definition"){
                /* Define enability of the link */
                $scope.isSettingsEnabled = false;
                $scope.isArtifactsEnabled = true;
                $scope.isGenerationsEnabled = false;


            }else if(subView=="building"){
                /* Define enability of the link */
                $scope.isSettingsEnabled = false;
                $scope.isArtifactsEnabled = false;
                $scope.isGenerationsEnabled = true;

            }
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

