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
            when('/packageVersionManagement', {
                templateUrl: 'modules/asset-management/package-version-management/package-version-management.html',
                controller: 'packageVersionManagementController'
            }).
            when('/runtimeAnalysis', {
                templateUrl: 'modules/runtime-monitoring/runtime-analysis/runtime-analysis.html',
                controller: 'runtimeAnalysisController'
            }).
            when('/runtimeManagement', {
                templateUrl: 'modules/runtime-management/runtime-management.html',
                controller: 'runtimeManagementController'
            }).
            otherwise({
                redirectTo: '/home'
            });
});
