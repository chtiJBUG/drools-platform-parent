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

DroolsPlatformControllers.controller('assetStatusController', function ($rootScope, $scope, $http) {
    /* Select package */
    $scope.selectPackage = {
        allowClear: true
    };

    $http.get('./server/rules_package/list')
            .success(function (data) {
                $scope.packagesList = data;
            })
            .error(function (error, status) {
                console.log("[Error] Error HTTP " + status);
                console.log(error);
            });

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

    $scope.search = function () {
        var filters = $scope.filters;
        var packageSelected = $scope.package;
        if (filters == undefined) {
            $scope.assetStatusSelectClass = "form-group has-error has-feedback";
            console.log("'[Error] filters : any filters chosen");
        }
        else if (packageSelected == "" && filters.length == 0) {
            $scope.namePackageSelectClass = "form-group has-error has-feedback";
            $scope.assetStatusSelectClass = "form-group has-error has-feedback";
            console.log("'[Error] filters : you must choose the package");
        } else if (filters.length == 0) {
            /* When mistake happens display the tooltip */
            $scope.assetStatusSelectClass = "form-group has-error has-feedback";
            return;
        } else if (packageSelected == "") {
            /* When mistake happens display the tooltip */
            $scope.namePackageSelectClass = "form-group has-error has-feedback";
            $scope.assetStatusSelectClass = "form-group";
            return;
        } else {
            $scope.namePackageSelectClass = "form-group";
            $scope.assetStatusSelectClass = "form-group";
            //$("#popoverBtn").popover('destroy');
            $http.post('./server/rule_status/' + packageSelected, JSON.stringify(filters))
                    .success(function (data) {
                        $scope.showCancelButton = true;
                        $scope.assets = data;
                        console.log('[Success] ' + data);
                    })
                    .error(function (error, status) {
                        $scope.showCancelButton = true;
                        $scope.noAssetSent = true;
                        $scope.status = status;
                        console.log("[Error] Error HTTP " + status);
                        console.log(error);
                    });
        }
    };
    $scope.reset = function () {
        $scope.showCancelButton = false;
        $scope.package = undefined;
        $scope.filters = undefined;
        $scope.assets = undefined;
        $scope.namePackageSelectClass = "form-group";
        $scope.assetStatusSelectClass = "form-group";
        console.log("[Info] Values reset");
    }

    $scope.promoteAssetsStatus = function () {
        var packageSelected = $scope.package;
        var assetsToPromote = _.where($scope.assets, {selected: true});
        var data = {assetsToPromote: assetsToPromote, assetStatuses: $scope.filters};
        $http.post('./server/rule_status/' + packageSelected + '/promote', data)
                .success(function (data) {
                    $scope.assets = data;
                })
                .error(function (error, status) {
                    console.log("[Error] Error HTTP " + status + " (Promote Asset status)");
                    console.log(error);
                });
    };

    $scope.demoteAssetsStatus = function () {
        var packageSelected = $scope.package;
        var assetsToDemote = _.where($scope.assets, {selected: true});
        var data = {assetsToDemote: assetsToDemote, assetStatuses: $scope.filters};
        $http.post('./server/rule_status/' + packageSelected + '/demote', data)
                .success(function (data) {
                    $scope.assets = data;
                })
                .error(function (error, status) {
                    console.log("[Error] Error HTTP " + status + " (Demote Asset status)");
                    console.log(error);
                });
    };

});
