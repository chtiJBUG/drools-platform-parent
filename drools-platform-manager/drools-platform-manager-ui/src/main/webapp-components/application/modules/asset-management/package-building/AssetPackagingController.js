DroolsPlatformControllers.controller('assetPackagingController', function ($rootScope, $scope, $http, $log) {
    initController();
    //___ Quick search after any modification
    $scope.searchPackageByName = function() {
        $scope.noAssetSent = false;
        //___ Recovering the values
        var packageSelected=$scope.package;
        var versionSelected="default";
        //___ Get the list according to the params chosen
        $http.get('./server/rules_package/'+packageSelected+'/'+versionSelected)
            .success(function (data) {
                $scope.showCancelButton=true;
                $scope.isCreateButtonVisible = true;
                $scope.packageVersionsList = data;
                console.log("Get process successful");
            })
            .error(function (error, status) {
                $scope.showCancelButton=true;
                $scope.noAssetSent = true;
                $scope.status=status;
                console.log(error);
            })
    };
    //___ Search all package's versions
    $scope.searchPackage = function() {
        $scope.noAssetSent = false;
        //___ Recovering the values
        var packageSelected=$scope.package;
        var versionSelected=$scope.version;
        console.log(versionSelected);

        //___ Test if the field is not empty
        if(packageSelected == "" || packageSelected==undefined) {
            //___ Red halo to show there's a mistake or an omission
            $scope.namePackageSelectClass="form-group has-error has-feedback";
        }else{
            //___ No red halo
            $scope.namePackageSelectClass="form-group";
            //___ Test if the field is empty
            if(versionSelected == "undefined" || versionSelected == ""){
                console.log("No version chosen");
                //___ if it is, value by default attributed
                versionSelected="default";
                console.log("keyword applied : "+ versionSelected);
            }
            if($scope.isCheckedSearch){
                versionSelected+="-SNAPSHOT";
            }
            //___ Get the list according to the params chosen
            $http.get('./server/rules_package/'+packageSelected+'/'+versionSelected)
                .success(function (data) {
                    $scope.showCancelButton=true;
                    $scope.isCreateButtonVisible = true;
                    $scope.searchNotSuccessful=false;
                    $scope.packageVersionsList = data;
                    console.log("Get process successful");
                })
                .error(function (error, status) {
                    $scope.showCancelButton=true;
                    $scope.noAssetSent = true;
                    $scope.status=status;
                    console.log(error);
                })
        }
    };

    //___ Right click management
    $scope.onRightClick = function(packageVersion) {
        $scope.newVersion=packageVersion.version;
        if(packageVersion.isRelease==true){
            $scope.isSnapshot = false;
        }else{
            $scope.isSnapshot = true;
            var existingVersions =_.where($scope.packageVersionsList, {version: $scope.newVersion, isRelease : $scope.isSnapshot});
            if (!_.isEmpty(existingVersions)) {
                //______  --> On pete une exception
                $scope.visibleItem=true;
            } else  {
                $scope.visibleItem=false;
            }
        }
    }

    $scope.launchWizzard = function(typeOfWizzard){
        if(typeOfWizzard=="create"){
            $scope.newVersion=undefined;
            //___ Show only the button needed
            $scope.isCreateButtonEnabled = true;
        }else if (typeOfWizzard=="rebuild"){
            //___ Show only the button needed
            $scope.isRebuildButtonEnabled = true;
            //___ version field disabled
            $scope.isVersionFieldDisabled = true;
            //___ snapshot checkbox checked
            $scope.isChecked=true;
            $scope.isCheckboxFieldDisabled=true;
        }else {
            //___ Show only the button needed
            $scope.isReleaseButtonEnabled = true;
            //___ version field disabled
            $scope.isVersionFieldDisabled = true;
            //___ Filters
            $scope.isASFieldDisabled=true;
            $scope.filters=["PROD"];
            //___ snapshot checkbox
            $scope.isChecked=false;
            $scope.isCheckboxFieldDisabled=true;
        }
        //___ Then show the modal window
        $('#Wizzard').modal('show');
    };

    $scope.closeWizzard = function() {
        //___ Reinit fields and boolean
        $scope.newVersion='0.0.0';
        $scope.filters = undefined;

        $scope.isCreateButtonEnabled = false;
        $scope.isRebuildButtonEnabled = false;
        $scope.isReleaseButtonEnabled = false;

        $scope.isVersionFieldDisabled = false;
        $scope.isASFieldDisabled=false;

        $scope.isChecked=false;
        $scope.isCheckboxFieldDisabled=false;

        $scope.iptNewVersion = "form-group";
        $scope.slctAssetStatus = "form-group";

        $scope.alreadyExist=false;

        //___ The hide the modal
        $('#Wizzard').modal('hide');
    };
    $scope.launchAlertMsg = function() {
        $('#AlertMsg').modal('show');
    };
    $scope.closeAlertMsg = function() {
        $('#AlertMsg').modal('hide');
    };


    //___ Create
    $scope.createVersion = function() {
        //___ Recovering the values
        var packageSelected=$scope.package;
        var NewVersion=$scope.newVersion;
        var filters = $scope.filters;
        if($scope.NewVersionPackageForm.newVersion.$invalid){
            $scope.iptNewVersion="form-group has-error has-feedback animated shake";
            if($scope.NewVersionPackageForm.assetStatusSelect.$invalid){
                $scope.slctAssetStatus="form-group has-error has-feedback animated shake";
            }
            allowToCreate = false;
        }else if($scope.NewVersionPackageForm.assetStatusSelect.$invalid){
            $scope.slctAssetStatus="form-group has-error has-feedback animated shake";
            if($scope.NewVersionPackageForm.newVersion.$invalid){
                $scope.iptNewVersion="form-group has-error has-feedback animated shake";
            }
            allowToCreate = false;

        }else{
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
            } else {
                //___ Si la release existe déjà et la checkbox SNPASHOT cochée
                if (!_.isEmpty(versionsRelease) && $scope.isChecked) {
                    //______  --> On pete une exception
                    $scope.iptNewVersion = "form-group has-error has-feedback animated shake";
                    allowToCreate = false;
                    $scope.alreadyExist = true;
                } else {
                    //____ Si non, et bien on peut continuer
                    console.log("kowabunga");
                    $scope.iptNewVersion = "form-group";
                    $scope.slctAssetStatus = "form-group";
                    allowToCreate = true;
                    $scope.alreadyExist = false;
                }
            }
        }

        if(allowToCreate){
            //__ If checked add the suffix
            if($scope.isChecked){
                NewVersion+=""+"-SNAPSHOT";
            }
            //___ Check the result on the console
            console.log("Package : "+packageSelected +", Version chosen : "+ NewVersion+", Asset statuses : "+ filters);
            //___ Then close the wizzard window
            $scope.closeWizzard();
            //___ Post the values
            $http.post('./server/rules_package/'+packageSelected+'/'+NewVersion, JSON.stringify(filters))
                .success(function (data) {
                    $scope.showCancelButton=true;
                    console.log("Build successful");
                    $scope.searchPackageByName();
                })
                .error(function (error, status) {
                    $scope.showCancelButton=true;
                    $scope.noAssetSent = true;
                    $scope.status=status;
                    console.log(error);
                })
        }
    };

    //___ Rebuild
    $scope.rebuildVersion = function() {
        //___ Recovering the values
        var packageSelected=$scope.package;
        var NewVersion=$scope.newVersion;
        var filters = $scope.filters;

        if(filters == undefined || filters.length == 0){
            console.log("filters undefined of no filters given");
            //___ Red halo to show there's a mistake or an omission
            $scope.slctAssetStatus="form-group has-error has-feedback animated shake";
        }else {
            //__ If checked add the suffix
            if($scope.isChecked){
                NewVersion+=""+"-SNAPSHOT";
            }
            //___ Check the result on the console
            console.log("Package : "+packageSelected +", Version chosen : "+ NewVersion+", Asset statuses : "+ filters);
            //___ Then close the wizzard window
            $scope.closeWizzard();
            //___ Post the values
            $http.post('./server/rules_package/rebuild/'+packageSelected+'/'+NewVersion, JSON.stringify(filters))
                .success(function (data) {
                    $scope.showCancelButton=true;
                    console.log("Build successful");
                    //___ reload the list
                    $scope.searchPackageByName();
                       $scope.closeWizzard();
                })
                .error(function (error, status) {
                    $scope.showCancelButton=true;
                    $scope.noAssetSent = true;
                    $scope.status=status;
                    console.log(error);
                })
        }
    };

    //___ Delete
    $scope.deletePackageVersion = function() {
        $scope.closeAlertMsg();
        allowToCreate = false;
        if($scope.isSnapshot){
            $scope.newVersion=$scope.newVersion + "-SNAPSHOT";
        }
        //___ Recovering the values
        var packageSelected=$scope.package;
        var NewVersion=$scope.newVersion;

        console.log("Package : "+packageSelected +", Version chosen : "+ NewVersion);

        $http.post('./server/rules_package/delete/'+packageSelected+'/'+NewVersion)
            .success(function (data) {
                $scope.showCancelButton=true;
                console.log("Delete process successful");
                $scope.searchPackageByName();
            })
            .error(function (error, status) {
                $scope.showCancelButton=true;
                $scope.noAssetSent = true;
                $scope.status=status;
                console.log(error);
            })
    };

    //___ Reset all
    $scope.reset = function () { initController();};

    function initController() {
        $scope.isCheckedSearch = false;
        $scope.isSnapshot = false;
        //___ Mainly useful for reset
        $scope.filters = undefined;
        $scope.packageVersionsList=undefined;
        $scope.package=undefined;
        $scope.version="";

        //___ Allow to erase the item chosen in the field
        $scope.selectPackage = {allowClear: true};
        $scope.filtersOptions = {selectOnBlur: true};

        //___ Showing the "Create" button only if there is no error from the server returned
        $scope.isCreateButtonVisible = false;

        $scope.showCancelButton=false;
        $scope.noAssetSent = false;
        $scope.isChecked=false;
        $scope.searchNotSuccessful=true;

        //___ After resetting the values, get values from the server
        //___ Get the list of the existed package from the server
        $http.get('./server/rules_package/list')
            .success(function (data) {
                $scope.packagesList = data;
            })
            .error(function (error) {
                console.log(error);
            });

        //___ Get the list of the asset statuses from the server
        $http.get('./server/rule_status/all')
            .success(function (data) {
                $scope.statuses = data;
            })
            .error(function (error) {
                console.log(error);
            });
    };

});
