DroolsPlatformControllers.controller('assetPackagingController', function ($rootScope, $scope, $http, $log) {
    $scope.filters = {};
    $scope.isRelease = false;
    $scope.version = undefined;
    $scope.selectPackage = {allowClear: true};
    $scope.filtersOptions = {selectOnBlur: true};

    $http.get('./server/rules_package/list')
        .success(function (data) {
            $scope.packagesList = data;
        })
        .error(function (error) {
            console.log(error);
        });

    $http.get('./server/rule_status/all')
        .success(function (data) {
            $scope.statuses = data;
        })
        .error(function (error) {
            console.log(error);
        });
    $scope.searchPackageByName = function() {
        var packageSelected=$scope.package;
        var packageSnapshotRequest = {
            assetStatuses: $scope.filters,
            version: $scope.version,
            isRelease: $scope.isRelease
        };
        if(packageSelected == "") {
            $scope.namePackageSelectClass="form-group has-error has-feedback";

        }else{
            $scope.namePackageSelectClass="form-group";
            $http.post('./server/rules_package/build', packageSnapshotRequest)
                .success(function (data) {
                    $scope.showCancelButton=true;
                    $scope.assets = data;
                    console.log(data);
                })
                .error(function (error, status) {
                    $scope.showCancelButton=true;
                    $scope.noAssetSent = true;
                    $scope.status=status;
                    console.log(error);


                })
        }
    };
    $scope.createVersion = function() {
        var packageSelected=$scope.package;
        var packageSnapshotRequest = {
            assetStatuses: $scope.filters,
            version: $scope.version,
            isRelease: $scope.isRelease
        };
        if(packageSelected == "") {
            $scope.namePackageSelectClass="form-group has-error has-feedback";

        }else{
            $scope.namePackageSelectClass="form-group";
            $http.post('./server/rules_package/build', packageSnapshotRequest)
                .success(function (data) {
                    $scope.showCancelButton=true;
                    $scope.assets = data;
                    console.log(data);
                })
                .error(function (error, status) {
                    $scope.showCancelButton=true;
                    $scope.noAssetSent = true;
                    $scope.status=status;
                    console.log(error);


                })
        }
    };
    $scope.reset = function () {
        $scope.showCancelButton=false;
        $scope.noAssetSent = false;
        $scope.filters = undefined;
    }
});
