droolsPlatformApp.service('LoginService', ['$http', '$q', function ($http, $q) {
    this.login = function (username, password) {
        var deferred = $q.defer();
        var data = $.param({username: username, password: password});
        $http.post('./server/login', data, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
                deferred.resolve(data);
            })
            .error(function () {
                deferred.reject();
            });
        return deferred.promise;
    };

    this.logout = function () {
        var deferred = $q.defer();
        $http.get('./server/logout')
            .success(function () {
                deferred.resolve();
            })
            .error(function () {
                deferred.reject();
            });
        return deferred.promise;
    };
}
]);