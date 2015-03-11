/*
 * Copyright 2015 Pymma Software
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

DroolsPlatformControllers.controller('runtimeManagementController', function ($rootScope, $scope, $http) {
    /* Select package */
    $scope.selectPackage = {
        allowClear: true
    };
    $scope.productionRuntimes = [];
    $scope.integrationRuntimes = [];
    $scope.developmentRuntimes = [];
    $scope.package="all";
    $http.get('./server/rules_package/list')
        .success(function (data) {
            $scope.packagesList = data;
        })
        .error(function (error, status) {
            console.log("[Error] Error HTTP " + status);
            console.log(error);
        });


   loadRuntimePanel("all")
    /* Input select */
    $scope.filters = {};
    $scope.assets = undefined;
    $http.get('./server/rule_status/all')
        .success(function (data) {
            $scope.statuses = data;
        })
        .error(function (error, status) {
            console.log("[Error] Error HTTP " + status);
            console.log(error);
        });

    $scope.filtersOptions = {
        selectOnBlur: true
    };
    function sortByRuleBaseID (a, b) {
        if (a.ruleBaseId > b.ruleBaseId)
            return 1;
        if (a.ruleBaseId < b.ruleBaseId)
            return -1;
        // a doit être égale à b
        return 0;
    }
    function loadRuntimePanel(packageName) {
        $http.get('./server/runtime/all')
            .success(function (data) {
                $scope.productionRuntimes = [];
                $scope.integrationRuntimes = [];
                $scope.developmentRuntimes = [];
                for (var runtime in data){
                    var aRuntime =data[runtime];
                    if (packageName=="all" || packageName==aRuntime.rulePackage) {
                        if (aRuntime.environment == "PROD") {
                            $scope.productionRuntimes.push(aRuntime)
                        }
                        if (aRuntime.environment == "INT") {
                            $scope.integrationRuntimes.push(aRuntime)
                        }
                        if (aRuntime.environment == "DEV") {
                            $scope.developmentRuntimes.push(aRuntime)
                        }
                    }
                }
                $scope.developmentRuntimes.sort(sortByRuleBaseID);
                $scope.integrationRuntimes.sort(sortByRuleBaseID);
                $scope.productionRuntimes.sort(sortByRuleBaseID);
            })
            .error(function (error, status) {
                console.log("[Error] Error HTTP " + status);
                console.log(error);
            });
    }
    $scope.search = function () {
        var packageSelected = $scope.package;
        if (packageSelected == "") {
            /* When mistake happens display the tooltip */
            $scope.namePackageSelectClass = "form-group has-error has-feedback";
            return;
        } else {
            $scope.namePackageSelectClass = "form-group";
            loadRuntimePanel(packageSelected);
            $scope.package=packageSelected;
            $scope.showCancelButton = true;
            //$("#popoverBtn").popover('destroy');

        }
    };
    $scope.reset = function () {
        $scope.showCancelButton = false;
        $scope.namePackageSelectClass = "form-group";
        loadRuntimePanel("all");
        $scope.package="all";
        console.log("[Info] Values reset");
    }

    $scope.changeRuntimeMode = function (runtime) {
        var newMode='';
        if (runtime.mode=="Debug"){
            newMode="Info";
        }else{
            newMode="Debug";
        }
        $http.post('./server/runtime/mode/' + runtime.ruleBaseId + '/' + newMode)
            .success(function (data) {
                var selectedpackage=$scope.package;
                if( typeof selectedpackage === 'undefined' || selectedpackage === null || selectedpackage==""){
                    loadRuntimePanel("all");
                }else{
                    loadRuntimePanel($scope.package);
                }
                console.log('[Success] ' + data);
            })
            .error(function (error, status) {
                console.log("[Error] Error HTTP " + status);
                console.log(error);
            });

    };
    $scope.changeRuntimeEnv = function (runtime,newEnv) {

        $http.post('./server/runtime/env/' + runtime.ruleBaseId + '/' + newEnv)
            .success(function (data) {
                var selectedpackage=$scope.package;
                if( typeof selectedpackage === 'undefined' || selectedpackage === null || selectedpackage==""){
                    loadRuntimePanel("all");
                }else{
                    loadRuntimePanel($scope.package);
                }
                console.log('[Success] ' + data);
            })
            .error(function (error, status) {
                console.log("[Error] Error HTTP " + status);
                console.log(error);
            });

    };

});
