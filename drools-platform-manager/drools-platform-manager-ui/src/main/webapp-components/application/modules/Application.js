var droolsPlatformApp = angular.module('droolsPlatformApp', [
    'ngRoute',
    'http-auth-interceptor',
    'drools-platform.controllers',
    'ui.select2',
    'ui.bootstrap',
    'ng-context-menu',
    'duScroll',
    'growlNotifications',
    'ngSanitize',
    'ngAnimate',
    'hljs',
    'angularSpinner'

]);

droolsPlatformApp.config(function ($locationProvider) {
    //___ Enable Html5 mode
    $locationProvider.html5Mode(true);
    Stomp.WebSocketClass = SockJS;
});

droolsPlatformApp.run(['$animate', function($animate){
    $animate.enabled(true);
    console.log('Animation enabled: ' + $animate.enabled());
}]);

droolsPlatformApp.factory('httpInterceptor', ['$q', '$rootScope', function ($q, $rootScope) {

    function success(response) {
        return response;
    }

    function error(response) {
        var errorId;
        if (response.status != 404 && response.status != 401 && response.status != 403)
            errorId = null;
        if (response.status == 500)
            errorId = response.headers('x-exception-id');
        $rootScope.$broadcast('event:serverError', errorId)
        $q.reject(response)
    }

    return function (promise) {
        return promise.then(success, error);
    };

}]);

droolsPlatformApp.factory('loaderSpinner', ['$rootScope', 'usSpinnerService', function($rootScope, usSpinnerService) {
    return {
        openSpinner: function() {
            $("#main").fadeTo("slow",0.10); //fadeTo(speed, opacity)
            $rootScope.disableButton=true;
            usSpinnerService.spin('spinner-1');
        },
        closeSpinner : function(){
            $("#main").fadeTo("slow",1);
            $rootScope.disableButton=false;
            usSpinnerService.stop('spinner-1');
        }
    };
}]);

droolsPlatformApp.run(function ($rootScope, $log, $location) {

    $rootScope.login = {};
    $rootScope.$on('event:auth-loginRequired', function () {
        $rootScope.loginRequired = true;
        $rootScope.login.focus = new Date().getTime();
        $log.log('Login required')
    });
    $rootScope.$on('event:auth-loginConfirmed', function () {
        $rootScope.loginRequired = false;
        /* When login confirmed : redirect to homepage */
        $location.path('/home');
        $log.log('Login confirmed');
    });

});

var DroolsPlatformControllers = angular.module('drools-platform.controllers', []);

//___ STOMP Service
droolsPlatformApp.service('StompService', function(growlNotifications){
    var stompClient = null;
    function connect(){
        var socket = new SockJS('/drools-platform-ui/server/update');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected : ' + frame);
            stompClient.subscribe('/topic/newpackageVersiondeployed', function (message) {
                console.log(JSON.parse(message.body).state);
            });
        });
    }



    this.disconnect = function () {
        stompClient.disconnect();
        //setConnected(false);
        console.log("Disconnected");
    };

    this.deployRuntime = function (ruleBaseID, packageVersion) {
        stompClient.send("/app/update", {},  JSON.stringify({'ruleBaseID': ruleBaseID, 'packageVersion': packageVersion}));
        growlNotifications.add('Deployment launched', 'info', 2000);
    };

    connect();

    //___ TODO Get back infos about the Monitoring ?

});

DroolsPlatformControllers.controller('MainCtrl', ['$rootScope', '$scope', '$window', '$http', 'LoginService','$log', '$location',
    function ($rootScope, $scope, $window, $http, LoginService, $log, $location) {
        $scope.logout = function () {
            var loginRequired = function () {
                $rootScope.loginRequired = true;
            };
            LoginService.logout().then(loginRequired);
        };
        $rootScope.$on('event:serverError', function (event, errorId) {
            var content = "";
            if (errorId != null)
                content = "<p>Error #" + errorId + "</p>";
            $scope.alerts = [
                {
                    type: "error",
                    title: "Oups, server error occurred ",
                    content: content
                }
            ];
        });
        $http.get('./server/username')
            .success(function (data) {
                $rootScope.username = data
            })
            .error(function (cause) {
                $log.log("Error. Caused by : " + cause);
                $rootScope.username = "default";
            });
        $scope.version = "0.0.0";
        console.log("Details about $location :");
        console.log($location); //complete location
        console.log($location.absUrl()); //complete URL
        console.log($location.host()); // hostname
        console.log($location.port()); //port
        console.log($location.path()); //path

    }
]);

