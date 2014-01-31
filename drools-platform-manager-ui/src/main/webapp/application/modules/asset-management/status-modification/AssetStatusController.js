DroolsPlatformControllers.controller('assetStatusController', function ($rootScope, $scope, $http) {
    $scope.filters = undefined;
    $scope.assets = undefined;
    // TODO get that from the server
    $scope.statuses = [
        { id: "DEV", name: "Development" },
        { id: "INT", name: "Integration" },
        { id: "PROD", name: "Production" }
    ];

    $scope.filtersOptions = {
        width: '600px',
        selectOnBlur: true
    };

    $scope.search = function () {
        var filters = $scope.filters;

        if (filters == undefined || filters.length == 0)
            return;
        $http.get('./server/rule_status')
            .success(function (data) {
                $scope.assets = data;
            })
            .error(function () {
                // TODO show error panel
            });
        /*if (filters[0] == 'PROD' && filters.length == 1) {
            $scope.assets = [];
            return;
        }
        $scope.assets = [
            {name: 'Asset 1', type: 'DRL', status: 'Development', selected: false},
            {name: 'Asset 2', type: 'DRL', status: 'Development', selected: false},
            {name: 'Asset 3', type: 'DRL', status: 'Development', selected: false},
            {name: 'Asset 4', type: 'DRL', status: 'Integration', selected: false},
            {name: 'Asset 5', type: 'DRL', status: 'Development', selected: false},
            {name: 'Asset 6', type: 'DRL', status: 'Integration', selected: false},
            {name: 'Asset 7', type: 'DRL', status: 'Production', selected: false},
            {name: 'Asset 8', type: 'DRL', status: 'Integration', selected: false},
            {name: 'Asset 9', type: 'DRL', status: 'Production', selected: false},
            {name: 'Asset 10', type: 'DRL', status: 'Production', selected: false},
            {name: 'Asset 11', type: 'DRL', status: 'Integration', selected: false},
            {name: 'Asset 12', type: 'DRL', status: 'Production', selected: false},
            {name: 'Asset 13', type: 'DRL', status: 'Production', selected: false},
            {name: 'Asset 14', type: 'DRL', status: 'Development', selected: false},
            {name: 'Asset 15', type: 'DRL', status: 'Production', selected: false},
            {name: 'Asset 16', type: 'DRL', status: 'Production', selected: false},
            {name: 'Asset 17', type: 'DRL', status: 'Development', selected: false}
        ]; */
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
