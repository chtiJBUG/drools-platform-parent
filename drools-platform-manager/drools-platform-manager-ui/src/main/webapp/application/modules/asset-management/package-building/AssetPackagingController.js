DroolsPlatformControllers.controller('assetPackagingController', function ($rootScope, $scope, $http, $log) {
    $scope.filters = {};
    $scope.isRelease = false;
    $scope.version = undefined;

    $http.get('./server/rule_status/all')
        .success(function (data) {
            $scope.statuses = data;
        })
        .error(function (error) {
            console.log(error);
        });


    $scope.createVersion = function() {
        var packageSnapshotRequest = {
            assetStatuses: $scope.filters,
            version: $scope.version,
            isRelease: $scope.isRelease
        };

        $http.post('./server/rules_package/build', packageSnapshotRequest)
            .success(function (data) {
                $scope.assets = data;
            })
            .error(function (error) {
                console.log(error);
            });
    };
});
