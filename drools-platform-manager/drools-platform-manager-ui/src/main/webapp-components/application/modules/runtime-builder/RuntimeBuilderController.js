/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        if (subView == "settings") {
            /* Define enability of the link */
            $scope.isSettingsEnabled = true;
            $scope.isArtifactsEnabled = false;
            $scope.isGenerationsEnabled = false;

        } else if (subView == "definition") {
            /* Define enability of the link */
            $scope.isSettingsEnabled = false;
            $scope.isArtifactsEnabled = true;
            $scope.isGenerationsEnabled = false;


        } else if (subView == "building") {
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

