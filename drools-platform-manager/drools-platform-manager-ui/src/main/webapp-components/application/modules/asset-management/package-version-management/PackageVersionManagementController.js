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

DroolsPlatformControllers.controller('packageVersionManagementController', ['$rootScope',
    '$scope',
    '$http',
    '$log',
    'growlNotifications',
    'StompService', function ($rootScope, $scope, $http, $log, growlNotifications, StompService) {
        initController();

        /* Functions to search the package's versions */
        //___ Quick search after any modification
        $scope.searchPackageByName = function () {
            $scope.noAssetSent = false;
            //___ Recovering the values
            var packageSelected = $scope.package;
            var versionSelected = "default";
            //___ Get the list according to the params chosen
            $http.get('./server/rules_package/' + packageSelected + '/' + versionSelected)
                    .success(function (data) {
                        $scope.showCancelButton = true;
                        $scope.isCreateButtonVisible = true;
                        $scope.packageVersionsList = data;
                        console.log("[Success] Package version list got successfully");
                    })
                    .error(function (error, status) {
                        $scope.showCancelButton = true;
                        $scope.noAssetSent = true;
                        $scope.status = status;
                        console.log("[Error] Error HTTP " + status);
                        console.log(error);
                    })
        };
        //___ Search all package's versions
        $scope.searchPackage = function () {
            $scope.noAssetSent = false;
            //___ Recovering the values
            var packageSelected = $scope.package;
            var versionSelected = $scope.version;

            //___ Test if the field is not empty
            if (packageSelected == "" || packageSelected == undefined) {
                //___ Red halo to show there's a mistake or an omission
                $scope.namePackageSelectClass = "form-group has-error has-feedback";
                console.log("[Error] Package name is mandatory for filtering");
            } else {
                //___ No red halo
                $scope.namePackageSelectClass = "form-group";
                console.log("[Info] Package selected : " + packageSelected);
                //___ Test if the field is empty
                if (versionSelected == "undefined" || versionSelected == "") {
                    console.log("[Info] No version chosen");
                    //___ if it is, value by default attributed
                    versionSelected = "default";
                    console.log("[Info] Keyword applied : " + versionSelected);
                } else {
                    console.log("[Info] Version selected : " + versionSelected);
                }
                if ($scope.isCheckedSearch) {
                    versionSelected += "-SNAPSHOT";
                    console.log("[Info] Prefix added : " + versionSelected);
                }
                //___ Get the list according to the params chosen
                $http.get('./server/rules_package/' + packageSelected + '/' + versionSelected)
                        .success(function (data) {
                            $scope.showCancelButton = true;
                            $scope.isCreateButtonVisible = true;
                            $scope.searchNotSuccessful = false;
                            $scope.packageVersionsList = data;
                            console.log("[Success] Package version list got successfully");
                        })
                        .error(function (error, status) {
                            $scope.showCancelButton = true;
                            $scope.noAssetSent = true;
                            $scope.status = status;
                            console.log("[Error] " + status);
                            console.log(error);
                        })
            }
            // growlNotifications.add('Hello world', 'warning', 2000);

        };

        /* Right click management */
        $scope.onRightClick = function (packageVersion) {
            console.log("[Info] onRightClick Event - version selected : " + packageVersion.version);
            $scope.newVersion = packageVersion.version;
            if (packageVersion.isRelease == true) {
                console.log("[Info] onRightClick Event - release version");
                $scope.isSnapshot = false;
            } else {
                $scope.isSnapshot = true;
                console.log("[Info] onRightClick Event - snapshot version");
                var existingVersions = _.where($scope.packageVersionsList, {version: $scope.newVersion, isRelease: $scope.isSnapshot});
                if (!_.isEmpty(existingVersions)) {
                    $scope.visibleItem = true;
                } else {
                    $scope.visibleItem = false;
                }
            }
        }

        /* Modal Management */
        //___ Modal for : Create, Rebuild, Release
        $scope.showWizzard = function (typeOfWizzard) {
            if (typeOfWizzard == "create") {
                $scope.newVersion = undefined;
                //___ Show only the button needed
                $scope.isCreateButtonEnabled = true;
            } else if (typeOfWizzard == "rebuild") {
                //___ Show only the button needed
                $scope.isRebuildButtonEnabled = true;
                //___ version field disabled
                $scope.isVersionFieldDisabled = true;
                //___ snapshot checkbox checked
                $scope.isChecked = true;
                $scope.isCheckboxFieldDisabled = true;
            } else {
                //___ Show only the button needed
                $scope.isReleaseButtonEnabled = true;
                //___ version field disabled
                $scope.isVersionFieldDisabled = true;
                //___ Filters
                $scope.isASFieldDisabled = true;
                $scope.filters = ["PROD"];
                //___ snapshot checkbox
                $scope.isChecked = false;
                $scope.isCheckboxFieldDisabled = true;
            }
            //___ Then show the modal window
            $('#Wizzard').modal('show');
        };
        $scope.closeWizzard = function () {
            /* Reinit fields and boolean */
            //___ Values
            $scope.newVersion = '0.0.0';
            $scope.filters = undefined;
            //___ Buttons
            $scope.isCreateButtonEnabled = false;
            $scope.isRebuildButtonEnabled = false;
            $scope.isReleaseButtonEnabled = false;
            //___ Fields
            $scope.isVersionFieldDisabled = false;
            $scope.isASFieldDisabled = false;
            //___ Checkbox
            $scope.isChecked = false;
            $scope.isCheckboxFieldDisabled = false;
            //___ Class
            $scope.iptNewVersion = "form-group";
            $scope.slctAssetStatus = "form-group";
            //___ Version
            $scope.alreadyExist = false;
            //___ Then hide the modal
            $('#Wizzard').modal('hide');
        };
        //___ Modal for : Deploy
        $scope.showDeploy = function (type) {

            var packageSelected = $scope.package;
            //___ Get the list according to the params chosen
            $http.get('./server/runtime/' + packageSelected)
                    .success(function (data) {
                        $scope.activeRuntimeList = [];

                        for (var runtime in data){
                            var aRuntime =data[runtime];
                            if (type=="SNAPSHOT" && aRuntime.environment =="DEV"){
                                $scope.activeRuntimeList.push(aRuntime);
                            }
                            if (type != "SNAPSHOT" && aRuntime.environment !="DEV" ){
                                $scope.activeRuntimeList.push(aRuntime);
                            }
                        }
                        $scope.activeRuntimeList.sort(sortByEnvAndRuleBaseID);
                        console.log("[Success] Active runtime list got successfully");
                    })
                    .error(function (error, status) {
                        $scope.status = status;
                        console.log(error);
                    })


            $('#Deployment').modal('show');
        };

        function sortByEnvAndRuleBaseID (a, b) {
            if (a.environment > b.environment){
                return 1;
            } else if (a.environment < b.environment){
                return -1;
            }else {

                if (a.ruleBaseId > b.ruleBaseId)
                    return 1;
                if (a.ruleBaseId < b.ruleBaseId)
                    return -1;
                // a doit être égale à b
                return 0;
            }
        }
        $scope.closeDeploy = function () {
            _.each($scope.activeRuntimeList, function (runtime) {
                runtime.isSelected = false;
            });
            $scope.allChecked = false;
            $('#Deployment').modal('hide');
        };
        //___ Modal for : Confirm Message
        $scope.showAlertMsg = function () {
            $('#AlertMsg').modal('show');
        };
        $scope.closeAlertMsg = function () {
            $('#AlertMsg').modal('hide');
        };

        /* Functions */

        //___ Create
        $scope.createVersion = function () {
            //___ Recovering the values
            var packageSelected = $scope.package;
            var NewVersion = $scope.newVersion;
            var filters = $scope.filters;
            if ($scope.NewVersionPackageForm.newVersion.$invalid) {
                $scope.iptNewVersion = "form-group has-error has-feedback animated shake";
                if ($scope.NewVersionPackageForm.assetStatusSelect.$invalid) {
                    $scope.slctAssetStatus = "form-group has-error has-feedback animated shake";
                }
                allowToCreate = false;
            } else if ($scope.NewVersionPackageForm.assetStatusSelect.$invalid) {
                $scope.slctAssetStatus = "form-group has-error has-feedback animated shake";
                if ($scope.NewVersionPackageForm.newVersion.$invalid) {
                    $scope.iptNewVersion = "form-group has-error has-feedback animated shake";
                }
                allowToCreate = false;

            } else {
                //__ Last test : does the version already exist ?

                //____ Rechercher dans le tableau si le couple version/snapshot(ou release) existe
                var allExistingVersions = _.where($scope.packageVersionsList, {version: NewVersion, isRelease: !$scope.isChecked});
                var versionsRelease = _.where($scope.packageVersionsList, {version: NewVersion, isRelease: $scope.isChecked});
                //____ Si oui
                if (!_.isEmpty(allExistingVersions)) {
                    //______  --> On pete une exception
                    $scope.iptNewVersion = "form-group has-error has-feedback animated shake";
                    allowToCreate = false;
                    $scope.alreadyExist = true;
                    console.log("[Error] Version already created");
                } else {
                    //___ Si la release existe déjà et la checkbox SNAPSHOT cochée
                    if (!_.isEmpty(versionsRelease) && $scope.isChecked) {
                        //______  --> On pete une exception
                        $scope.iptNewVersion = "form-group has-error has-feedback animated shake";
                        allowToCreate = false;
                        $scope.alreadyExist = true;
                        console.log("[Error] Version already created");
                    } else {
                        //____ Si non, et bien on peut continuer
                        console.log("[Success] KOWABUNGA");
                        $scope.iptNewVersion = "form-group";
                        $scope.slctAssetStatus = "form-group";
                        allowToCreate = true;
                        $scope.alreadyExist = false;
                    }
                }
            }

            if (allowToCreate) {
                //__ If checked add the suffix
                if ($scope.isChecked) {
                    NewVersion += "" + "-SNAPSHOT";
                }
                //___ Check the result on the console
                console.log("Package : " + packageSelected + ", Version chosen : " + NewVersion + ", Asset statuses : " + filters);
                //___ Then close the wizzard window
                $scope.closeWizzard();
                //___ Post the values
                $http.post('./server/rules_package/' + packageSelected + '/' + NewVersion, JSON.stringify(filters))
                        .success(function (data) {
                            $scope.showCancelButton = true;
                            console.log("Build successful");
                            $scope.searchPackageByName();
                            growlNotifications.add('Version created', 'success', 2000);
                        })
                        .error(function (error, status) {
                            $scope.showCancelButton = true;
                            $scope.noAssetSent = true;
                            $scope.status = status;
                            console.log("[Error] Error HTTP " + status);
                            console.log(error);
                        })
            }
        };

        //___ Rebuild
        $scope.rebuildVersion = function () {
            //___ Recovering the values
            var packageSelected = $scope.package;
            var NewVersion = $scope.newVersion;
            var filters = $scope.filters;

            if (filters == undefined || filters.length == 0) {
                console.log("filters undefined of no filters given");
                //___ Red halo to show there's a mistake or an omission
                $scope.slctAssetStatus = "form-group has-error has-feedback animated shake";
            } else {
                //__ If checked add the suffix
                if ($scope.isChecked) {
                    NewVersion += "" + "-SNAPSHOT";
                }
                //___ Check the result on the console
                console.log("Package : " + packageSelected + ", Version chosen : " + NewVersion + ", Asset statuses : " + filters);
                //___ Then close the wizzard window
                $scope.closeWizzard();
                //___ Post the values
                $http.post('./server/rules_package/rebuild/' + packageSelected + '/' + NewVersion, JSON.stringify(filters))
                        .success(function (data) {
                            $scope.showCancelButton = true;
                            console.log("Build successful");
                            //___ reload the list
                            $scope.searchPackageByName();
                            $scope.closeWizzard();
                            growlNotifications.add('Version rebuilded', 'success', 2000);
                        })
                        .error(function (error, status) {
                            $scope.showCancelButton = true;
                            $scope.noAssetSent = true;
                            $scope.status = status;
                            console.log(error);
                        })
            }
        };

        $scope.check = function () {
            // TODO
            if ($scope.allChecked == false) {
                _.each($scope.activeRuntimeList, function (runtime) {
                    runtime.isSelected = true;
                });
            } else {
                _.each($scope.activeRuntimeList, function (runtime) {
                    runtime.isSelected = false;
                });
            }

        };


        //___ Deploy
        $scope.deployVersion = function () {
            if ($scope.isSnapshot) {
                $scope.versionToDeploy = $scope.newVersion + "-SNAPSHOT";
            } else {
                $scope.versionToDeploy = $scope.newVersion;
            }
            console.log("[Info] Version to deploy : " + $scope.versionToDeploy);
            _.each(
                    _.where($scope.activeRuntimeList, {isSelected: true}),
                    function (runtime) {
                        StompService.deployRuntime(runtime.ruleBaseId, $scope.versionToDeploy);
                    }
            );
            $scope.closeDeploy();
        };


        //___ Delete
        $scope.deletePackageVersion = function () {
            $scope.closeAlertMsg();
            allowToCreate = false;
            if ($scope.isSnapshot) {
                $scope.newVersion = $scope.newVersion + "-SNAPSHOT";
            }
            //___ Recovering the values
            var packageSelected = $scope.package;
            var NewVersion = $scope.newVersion;

            console.log("[Info] Package selected : " + packageSelected + ", version chosen : " + NewVersion);

            $http.post('./server/rules_package/delete/' + packageSelected + '/' + NewVersion)
                    .success(function (data) {
                        $scope.showCancelButton = true;
                        console.log("Delete process successful");
                        $scope.searchPackageByName();
                        growlNotifications.add('Version deleted', 'info', 2000);
                    })
                    .error(function (error, status) {
                        $scope.showCancelButton = true;
                        $scope.noAssetSent = true;
                        $scope.status = status;
                        console.log(error);
                    })
        };

        //___ Reset all
        $scope.reset = function () {
            initController();
        };

        /* Initialistion */
        function initController() {
            $scope.isCheckedSearch = false;
            $scope.isSnapshot = false;
            //___ Mainly useful for reset
            $scope.filters = undefined;
            $scope.packageVersionsList = undefined;
            $scope.package = undefined;
            $scope.version = "";

            //___ Allow to erase the item chosen in the field
            $scope.selectPackage = {allowClear: true};
            $scope.filtersOptions = {selectOnBlur: true};

            //___ Showing the "Create" button only if there is no error from the server returned
            $scope.isCreateButtonVisible = false;

            $scope.showCancelButton = false;
            $scope.noAssetSent = false;
            $scope.isChecked = false;
            $scope.searchNotSuccessful = true;

            $scope.checkedActiveRuntimeList = {
                item: []
            };

            //___ Deploy modal
            $scope.allChecked = false;

            //___ After resetting the values, get values from the server
            //___ Get the list of the existed package from the server
            $http.get('./server/rules_package/list')
                    .success(function (data) {
                        $scope.packagesList = data;
                    })
                    .error(function (error, status) {
                        console.log("[Error] Error HTTP " + status);
                        console.log(error);
                    });

            //___ Get the list of the asset statuses from the server
            $http.get('./server/rule_status/all')
                    .success(function (data) {
                        $scope.statuses = data;
                    })
                    .error(function (error, status) {
                        console.log("[Error] Error HTTP " + status);
                        console.log(error);
                    });
        };

    }]);
