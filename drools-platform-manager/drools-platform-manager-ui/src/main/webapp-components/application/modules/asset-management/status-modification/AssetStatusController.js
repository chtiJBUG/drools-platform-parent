DroolsPlatformControllers.controller('assetStatusController', function ($rootScope, $scope, $http) {
    /* Select package */
    $scope.selectPackage = {
        allowClear: true
    };
    /*$scope.packagesList = [
     {name:'package 1'},
     {name:'package 2'},
     {name:'package 3'},
     {name:'package 4'}
     ];*/

    $http.get('./server/rules_package/list')
        .success(function (data) {
            $scope.packagesList = data;
        })
        .error(function (error) {
            console.log(error);
        });


    $scope.selectAction = function () {
        alert("You've selected : " + $scope.package.name);
    };

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
        width: '600px',
        selectOnBlur: true
    };

    $scope.search = function () {
        var filters = $scope.filters;
        var packageSelected=$scope.package;
        console.log("Package selected : "+packageSelected);
        console.log("Filter()s selected : "+packageSelected);
        if(packageSelected == "" && filters.length == 0 ) {
            $("#popoverPackage").popover('show');
            $("#popoverBtn").popover('show');
        }else if (filters.length == 0) {
            /* When mistake happens display the tooltip */
            $("#popoverBtn").popover('show');
            return;
        }else if (packageSelected == "") {
            /* When mistake happens display the tooltip */
            $("#popoverPackage").popover('show');
            return;
        }else{
            //$("#popoverBtn").popover('destroy');
            $http.post('./server/rule_status/'+packageSelected, JSON.stringify(filters))
                .success(function (data) {
                    $scope.assets = data;
                })
                .error(function (error) {
                    console.log(error);
                    $scope.noAssetSent = true;
                });
        }
    };

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
