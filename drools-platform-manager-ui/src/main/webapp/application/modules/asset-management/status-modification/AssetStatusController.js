DroolsPlatformControllers.controller('assetStatusController', function ($rootScope, $scope) {
    $scope.filters = undefined;
    $scope.assets = undefined;
    // TODO get that from the server
    $scope.statuses = [
        { id: "DEV", name: "Development" },
        { id: "INT", name: "Integration" },
        { id: "PROD", name: "Production" }
    ];
    var mockedAssets =

        $scope.filtersOptions = {
            width: '600px',
            selectOnBlur: true
        };

    $scope.search = function () {
        var filters = $scope.filters;

        if (filters == undefined || filters.length == 0)
            return;
        if (filters[0] == 'PROD' && filters.length == 1) {
            $scope.assets = [];
            return;
        }
        $scope.assets = [
            {name: 'Asset 1', currentStatus: 'Development', nextStatus: 'Integration', toPromote: false},
            {name: 'Asset 2', currentStatus: 'Development', nextStatus: 'Integration', toPromote: false},
            {name: 'Asset 3', currentStatus: 'Development', nextStatus: 'Integration', toPromote: false},
            {name: 'Asset 4', currentStatus: 'Integration', nextStatus: 'Production', toPromote: false},
            {name: 'Asset 5', currentStatus: 'Development', nextStatus: 'Integration', toPromote: false},
            {name: 'Asset 6', currentStatus: 'Integration', nextStatus: 'Production', toPromote: false},
            {name: 'Asset 7', currentStatus: 'Production', nextStatus: 'n/a', toPromote: false},
            {name: 'Asset 8', currentStatus: 'Integration', nextStatus: 'Production', toPromote: false},
            {name: 'Asset 9', currentStatus: 'Production', nextStatus: 'n/a', toPromote: false},
            {name: 'Asset 10', currentStatus: 'Production', nextStatus: 'n/a', toPromote: false},
            {name: 'Asset 11', currentStatus: 'Integration', nextStatus: 'Production', toPromote: false},
            {name: 'Asset 12', currentStatus: 'Production', nextStatus: 'n/a', toPromote: false},
            {name: 'Asset 13', currentStatus: 'Production', nextStatus: 'n/a', toPromote: false},
            {name: 'Asset 14', currentStatus: 'Development', nextStatus: 'Integration', toPromote: false},
            {name: 'Asset 15', currentStatus: 'Production', nextStatus: 'n/a', toPromote: false},
            {name: 'Asset 16', currentStatus: 'Production', nextStatus: 'n/a', toPromote: false},
            {name: 'Asset 17', currentStatus: 'Development', nextStatus: 'Integration', toPromote: false}
        ];
    };

    $scope.promoteAssetsStatus = function () {

    };

    $scope.demoteAssetsStatus = function () {

    };

});
