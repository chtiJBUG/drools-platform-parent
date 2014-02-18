DroolsPlatformControllers.controller('assetStatusController', function ($rootScope, $scope, $http) {
    $scope.filters = {};
    $scope.assets = undefined;
    $http.get('./server/rule_status/all')
        .success(function (data) {
            $scope.statuses = data;
        })
        .error(function () {
            // TODO show error panel
        });

    $scope.filtersOptions = {
        width: '600px',
        selectOnBlur: true
    };

    $scope.search = function () {
        var filters = $scope.filters;
        if (filters == undefined || filters.length == 0)
            return;
        $http.post('./server/rule_status', JSON.stringify(filters), {headers: {'Content-Type': 'application/json', 'Accept':'application/json'}})
            .success(function (data) {
                $scope.assets = data;
            })
            .error(function () {
                // TODO show error panel
            });
    };

    $scope.promoteAssetsStatus = function () {
        var assetsToPromote = _.where($scope.assets, {selected: true});
        // TODO Call REST Resource for the list of selected assets
    };

    $scope.demoteAssetsStatus = function () {
        var assetsToDemote = _.where($scope.assets, {selected: false});
        // TODO Call REST Resource for the list of selected assets
    };

    $scope.changeAssetStatus = function(asset) {
        asset.selected = !asset.selected;
    };

});
