DroolsPlatformControllers.controller('assetPackagingController', function ($rootScope, $scope, $http, $log) {
    $scope.filters = {};

    $scope.isRelease = false;
    $scope.isSnapshot = false;

    $scope.version = undefined;

    //___ The version you'll chose for the wizzard,
    //___ the default input's placeholder value is "x.y.z"
    $scope.newVersion = "x.y.z";

    //___ Allow to erase the item chosen in the field
    $scope.selectPackage = {allowClear: true};
    $scope.filtersOptions = {selectOnBlur: true};

    //___ According to the wizzard you have opened some field are disabled
    $scope.isVersionFieldEnabled = false;
    $scope.isCreateButtonEnabled = false;
    $scope.isRebuildButtonEnabled = false;

    //___ Showing the "Create" button only if there is no error from the server returned
    $scope.isCreateButtonVisible = false;

    //___ Allowing the creation or not of the version
    $scope.allowToCreate=true;

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

    //___ Search all package's versions
    $scope.searchPackageByName = function() {
        //___ Recovering the values
        var packageSelected=$scope.package;
        var versionSelected=$scope.version;
        //___ Test if the field is not empty
        if(packageSelected == "") {
            //___ Red halo to show there's a mistake or an omission
            $scope.namePackageSelectClass="form-group has-error has-feedback";
        }else{
            //___ No red halo
            $scope.namePackageSelectClass="form-group";
            //___ Test if the field is empty
            if(typeof(versionSelected) == "undefined" || versionSelected == ""){
                console.log("No version chosen");
                //___ if it is, value by default attributed
                versionSelected="default";
                console.log("keyword applied : "+ versionSelected);
            }
            //___ Get the list according to the params chosen
            $http.get('./server/rules_package/'+packageSelected+'/'+versionSelected)
                .success(function (data) {
                    $scope.showCancelButton=true;
                    $scope.isCreateButtonVisible = true;
                    $scope.packageVersionsList = data;
                    console.log("Get process successful");
                    console.log($scope.packageVersionsList[0].version);
                    console.log($scope.packageVersionsList.length);
                    for(var i=0; i<$scope.packageVersionsList.length;i++){
                        console.log($scope.packageVersionsList[i].version);
                        console.log($scope.packageVersionsList[i].isRelease);
                    }

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
        console.log("Right click");
        console.log(packageVersion);
        $scope.newVersion=packageVersion.version;
        if(packageVersion.isRelease==true){
            $scope.isSnapshot = false;
            console.log($scope.isSnapshot);
        }else{
            $scope.isSnapshot = true;
            console.log($scope.isSnapshot);
        }
    }

    //___ Launching wizzard according to the type of wizzard needed
    $scope.launchCreationWizzard = function() {
        //___ Show only the button needed
        $scope.isCreateButtonEnabled = true;
        $scope.isRebuildButtonEnabled = false;
        $scope.isReleaseButtonEnabled = false;
        //___ version field enabled
        $scope.isVersionFieldEnabled = false;
        //___ snapshot checkbox unchecked
        $scope.snapshot=false;
        $scope.isCheckboxFieldEnabled=false;
        //___ Show the modal window
        $('#Wizzard').modal('show');
    }
    $scope.launchRebuildWizzard = function() {
        //___ Show only the button needed
        $scope.isRebuildButtonEnabled = true;
        $scope.isCreateButtonEnabled = false;
        $scope.isReleaseButtonEnabled = false;
        //___ version field disabled
        $scope.isVersionFieldEnabled = true;
        //___ snapshot checkbox checked
        $scope.snapshot=true;
        $scope.isCheckboxFieldEnabled=true;

        //$scope.newVersion="0.0.0";
        //___ Show the modal window
        $('#Wizzard').modal('show');
    }
    $scope.launchReleaseWizzard = function() {
        //___ Show only the button needed
        $scope.isReleaseButtonEnabled = true;
        $scope.isCreateButtonEnabled = false;
        $scope.isRebuildButtonEnabled = false;
        //___ version field disabled
        $scope.isVersionFieldEnabled = true;
        //___ snapshot checkbox unchecked
        $scope.snapshot=false;
        $scope.isCheckboxFieldEnabled=true;

        //___ Show the modal window
        $('#Wizzard').modal('show');
    }
    $scope.closeWizzard = function() {
        $scope.newVersion = "x.y.z";
        $scope.filters = {};
        $scope.NewVersionClass="form-group";
        $scope.AssetStatusClass="form-group";
        $('#Wizzard').modal('hide');
    }

    //___ Wizzard events
    //___ When you click on Version Field
    $scope.changeVersionField = function() {
        if($scope.newVersion=="x.y.z" || $scope.NewVersionClass=="form-group has-error has-feedback"){
            $scope.newVersion="";
            $scope.NewVersionClass="form-group";
        }
    }
    //___ When you click on AssetStatus Field while you've done a mistake
    $scope.changeASField = function() {
        if($scope.AssetStatusClass=="form-group has-error has-feedback"){
            $scope.AssetStatusClass="form-group";
        }
    }

    //___ Create the new package
    $scope.createVersion = function() {
        //___ Regex to filter the version field
        var searchPattern  = new RegExp("^[0-9]+.[0-9]+.[0-9]+$");

        //___ Recovering the values
        var packageSelected=$scope.package;
        var NewVersion=$scope.newVersion;
        var filters = $scope.filters;
        console.log(filters);
        console.log(filters.length);
        //___ test if the field is not empty or respects the pattern x.y.z.
        if(NewVersion == undefined || searchPattern.test($scope.newVersion) == false) {
            console.log("new version undefined of no new version given did not respect the pattern");
            //___ Red halo to show there's a mistake or an omission
            $scope.NewVersionClass="form-group has-error has-feedback";
            if(filters == undefined || filters.length == 0) {
                //___ Red halo to show there's a mistake or an omission
                $scope.AssetStatusClass="form-group has-error has-feedback";
            }
            $scope.allowToCreate=false;
        }else if(filters == undefined || filters.length == 0){
            console.log("filters undefined of no filters given");
            //___ Red halo to show there's a mistake or an omission
            $scope.AssetStatusClass="form-group has-error has-feedback";
            if(NewVersion == undefined || searchPattern.test($scope.newVersion) == false) {
                //___ Red halo to show there's a mistake or an omission
                $scope.NewVersionClass="form-group has-error has-feedback";
            }
            $scope.allowToCreate=false;
        }else {
            //___ No red halo
            $scope.NewVersionClass = "form-group";
            $scope.AssetStatusClass = "form-group";
            //___ Last test : does the version already exist ?
            for (var i = 0; i < $scope.packageVersionsList.length; i++) {
                console.log($scope.packageVersionsList[i].version);
                if (NewVersion == $scope.packageVersionsList[i].version) {
                    //alert("pb");
                    if ($scope.snapshot == !$scope.packageVersionsList[i].isRealease) {
                        $scope.NewVersionClass = "form-group has-error has-feedback";
                        $scope.allowToCreate = false;
                    }

                }else{
                    $scope.allowToCreate = true;
                }
            }
        }

        if($scope.allowToCreate){
            //__ If checked add the suffix
            if($scope.snapshot){
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
                    $scope.newVersion = "x.y.z";
                    //___ reload the list
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

    $scope.rebuildVersion = function() {
        console.log("Ahem...not yet done");
        //___ Then close the wizzard window
        $scope.closeWizzard();

    }

    $scope.makeRelease = function() {
        console.log("Ahem...not yet done");
        //___ Then close the wizzard window
        $scope.closeWizzard();
    }

    $scope.deletePackageVersion = function() {
        console.log("Ahem...not yet done");
    }

    //___ Reset all
    $scope.reset = function () {
        $scope.package = undefined;
        $scope.filters = undefined;
        $scope.packageVersionsList=undefined;
        $scope.newVersion = "x.y.z";
        $scope.isRelease = false;
        $scope.isSnapshot = false;
        $scope.showCancelButton=false;
        $scope.noAssetSent = false;
        $scope.isVersionFieldEnabled = false;
        $scope.isCreateButtonEnabled = false;
        $scope.isRebuildButtonEnabled = false;
        $scope.isCreateButtonVisible = false;
        $scope.allowToCreate=true;
    }
});
