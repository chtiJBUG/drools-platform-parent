DroolsPlatformControllers.controller('assetStatusController', function ($rootScope, $scope, $http) {
    /* Select package */
    $scope.selectPackage = {
        allowClear: true
    };

    $http.get('./server/rules_package/list')
        .success(function (data) {
            $scope.packagesList = data;
        })
        .error(function (error) {
            console.log(error);
        });

    /* Input select */
    $scope.filters = {};
    $scope.assets = undefined;
    $http.get('./server/rule_status/all')
        .success(function (data) {
            $scope.statuses = data;
        })
        .error(function (error) {
            console.log(error);
        });

    $scope.filtersOptions = {
        selectOnBlur: true
    };

    $scope.search = function () {
        var filters = $scope.filters;
        var packageSelected=$scope.package;
        console.log("Package selected : "+packageSelected);
        console.log("Filter()s selected : "+packageSelected);
        if(packageSelected == "" && filters.length == 0 ) {
            $scope.namePackageSelectClass="form-group has-error has-feedback";
            $scope.assetStatusSelectClass="form-group has-error has-feedback";
        }else if (filters.length == 0) {
            /* When mistake happens display the tooltip */
            $scope.assetStatusSelectClass="form-group has-error has-feedback";
            return;
        }else if (packageSelected == "") {
            /* When mistake happens display the tooltip */
            $scope.namePackageSelectClass="form-group has-error has-feedback";
            $scope.assetStatusSelectClass="form-group";
            return;
        }else{
            $scope.namePackageSelectClass="form-group";
            $scope.assetStatusSelectClass="form-group";
            //$("#popoverBtn").popover('destroy');
            $http.post('./server/rule_status/'+packageSelected, JSON.stringify(filters))
                .success(function (data) {
                    $scope.showCancelButton=true;
                    $scope.assets = data;
                })
                .error(function (error) {
                    console.log(error);
                    $scope.showCancelButton=true;
                    $scope.noAssetSent = true;
                });
        }
    };
    $scope.reset = function () {
        $scope.showCancelButton=false;
        $scope.package=undefined;
        $scope.filters = undefined;
        $scope.assets = undefined;
    }

    $scope.promoteAssetsStatus = function () {
        var packageSelected=$scope.package;
        var assetsToPromote = _.where($scope.assets, {selected: true});
        var data = {assetsToPromote: assetsToPromote, assetStatuses: $scope.filters};
        $http.post('./server/rule_status/'+packageSelected+'/promote', data)
            .success(function (data) {
                $scope.assets = data;
            })
            .error(function (error) {
                console.log(error);
            });
    };

    $scope.demoteAssetsStatus = function () {
        var assetsToDemote = _.where($scope.assets, {selected: true});
        var data = {assetsToDemote: assetsToDemote, assetStatuses: $scope.filters};
        $http.post('./server/rule_status/'+packageSelected+'/demote', data)
            .success(function (data) {
                $scope.assets = data;
            })
            .error(function (error) {
                console.log(error);
            });
    };

});
