DroolsPlatformControllers.controller('assetStatusController', function ($rootScope, $scope, $http) {
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
        if (filters == undefined || filters.length == 0)
            return;
        $http.post('./server/rule_status', JSON.stringify(filters))
            .success(function (data) {
                $scope.assets = data;
            })
            .error(function (error) {
                console.log(error);
            });
    };

    $scope.promoteAssetsStatus = function () {
        var assetsToPromote = _.where($scope.assets, {selected: true});
        var data = {assetsToPromote: assetsToPromote, assetStatuses: $scope.filters};
        $http.post('./server/rule_status/promote', data)
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
        $http.post('./server/rule_status/demote', data)
            .success(function (data) {
                $scope.assets = data;
            })
            .error(function (error) {
                console.log(error);
            });
    };

});
