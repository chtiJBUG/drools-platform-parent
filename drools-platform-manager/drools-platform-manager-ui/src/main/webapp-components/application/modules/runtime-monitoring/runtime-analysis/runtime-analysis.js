DroolsPlatformControllers.controller('runtimeAnalysisController', function ($rootScope, $scope, $document, $http, $log, $timeout, growlNotifications) {

    /** SCOPE DEFINITION **/

    $scope.allRuntimes = [];

    //___ Fetch the list
    $http.get('./server/rules_package/list')
        .success(function (data) {
            $scope.packagesList = data;
        })
        .error(function (error) {
            console.log(error);
        });


    $http.get('./server/runtime/all/statuses')
        .success(function (data) {
            $scope.statusesList = data;
        })
        .error(function (error) {
            console.log(error);
        });

    $scope.filters = {
        packageName: undefined,
        status: undefined,
        hostname: undefined,
        startDate: undefined,
        endDate: undefined
    };

    //___ Collapse status for each box from the "Search panel"
    $scope.collapseStatus = {
        "firstBox": false,
        "secondBox": false
    };

    //___ Popover details (when you move the mouse over the rulePackage)
    $scope.popoverDetails = {
        'title': 'Guvnor URL',
        'content': '<b>Guvnor URL</b><br/><a href="http://192.168.1.26:8080/drools-guvnor" target="_blank">192.168.1.26:8080/drools-guvnor</a>'

    };

    //___ Session Execution Details area
    $scope.sessionExecutionDetails = {
        'area': false,
        'panel': false
    };

    //___ Dates
    $scope.format = 'dd/MM/yyyy';
    $scope.dates = {
        'initDate': new Date('2016/12/20'),
        'minDate': '1993-07-29',
        'maxDate': '2050-07-29',
        'myTime': new Date()
    };
    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
    };

    $scope.datePickers = {
        'firstCalendar': false,
        'secondCalendar': false
    };
    $scope.hstep = 1;
    $scope.mstep = 15;

    $scope.options = {
        hstep: [1, 2, 3],
        mstep: [1, 5, 10, 15, 25, 30]
    };

    $scope.ismeridian = true;

    //___ ID of the runtime selected
    $scope.selectedRuntimeID = undefined;

    //___ Tab mgmt
    $scope.selectedTab = 1;

    //___ Code JSON
    $scope.code = {
        'input': "// input",
        'output': '// output'
    };

    /**********************/
    /*  DATE & TIME MGMT  */
    /**********************/
    $scope.today = function (dateGiven) {
        return new Date();
    };
    $scope.today();

    $scope.clear = function () {
        $scope.filters.startDate = new Date();
    };

    // Disable weekend selection
    $scope.disabled = function (date, mode) {
        return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
    };
    /*
     $scope.toggleMin = function() {
     $scope.dates.minDate = $scope.dates.minDate ? null : new Date();
     };
     $scope.toggleMin();*/

    $scope.open = function ($event, inputConcerned) {
        $event.preventDefault();
        $event.stopPropagation();
        if (inputConcerned == "startDate") {
            $scope.datePickers.firstCalendar = true;

        } else {
            $scope.datePickers.secondCalendar = true;
        }

    };

    $scope.toggleMode = function () {
        $scope.ismeridian = !$scope.ismeridian;
    };

    $scope.update = function () {
        var d = new Date();
        d.setHours(14);
        d.setMinutes(0);
        $scope.dates.mytime = d;
    };

    $scope.changed = function () {
        console.log('Time changed to: ' + $scope.dates.myTime);
    };

    /** SESSION EXECUTION DETAILS **/

        //___ Scrolling to the next panel
    $scope.scrollToPanel = function (ruleBaseID) {
        $scope.selectedRuntimeID = ruleBaseID;
        $scope.sessionExecutionDetails.area = true;
        $timeout(function () {
            $scope.sessionExecutionDetails.panel = true;
            var someElement = angular.element(document.getElementById('detailsPanel'));
            $document.scrollToElement(someElement, 0, 1000);
        }, 3);
    };

    // Toggle to another tab
    $scope.selectedTab = 'output';
    $scope.toggleTab = function (item) {
        $scope.selectedTab = item;
    }

    $scope.closeDetailsPanel = function () {
        var someElement = angular.element(document.getElementById('wrap'));
        $document.scrollToElement(someElement, 0, 1000);
        $timeout(function () {
            $scope.sessionExecutionDetails.area = false;
            $scope.sessionExecutionDetails.panel = false;
        }, 1000);
    };

    /** EVENTS ASSOCIATED WITH BUTTONS **/


    /** SEARCH **/
        //___ Method :
        //___ Retrieve filters
        //___ Then launch the http get request
    $scope.search = function () {

        //___ TODO date filters >> USE MOMENT.JS
        var str = '' + $scope.filters.startDate;
        var suffix = "(CEST)";

        if (str.indexOf(suffix, str.length - suffix.length) !== -1) {
            $scope.filters.startDate = '' + $scope.filters.startDate.getFullYear() + '-' + $scope.filters.startDate.getMonth() + '-' + $scope.filters.startDate.getDate();
        }
        str = '' + $scope.filters.endDate;
        if (str.indexOf(suffix, str.length - suffix.length) !== -1) {
            $scope.filters.endDate = '' + $scope.filters.endDate.getFullYear() + '-' + $scope.filters.endDate.getMonth() + '-' + $scope.filters.endDate.getDate();
        }

        console.log($scope.filters.startDate);
        console.log($scope.filters.endDate);


        //___ You must chose the package
        if ($scope.filters.packageName == null || $scope.filters.packageName == "") {
            console.log("[Error] Package name is mandatory for filtering");
            $scope.namePackageSelectClass = "form-group has-error has-feedback";

        } else {
            if ($scope.filters.status == "") {
                $scope.filters.status = undefined;
            }
            console.log('[Info] Filters chosen : ');
            //___ Put $scope into var
            var filters = $scope.filters;
            console.log(filters);
            $scope.namePackageSelectClass = "form-group";
            $http.post('./server/runtime/filter', filters)
                .success(function (data) {
                    $scope.allRuntimes = data;
                })
                .error(function (error, status) {
                    console.log("[Error] Error HTTP " + status);
                    console.log(error);
                    //____ TODO Send an appropriate message
                    growlNotifications.add('Whoops ! Error HTTP ' + status, 'danger', 2000);
                    mockValues();
                });
        }
        $scope.showCancelButton = true;
    };

    $scope.reset = function () {
        $scope.allRuntimes = [];
        $scope.namePackageSelectClass = "form-group";
        $scope.showCancelButton = false;
    };

    //___ Temporary function to mock values because search function is not working
    function mockValues() {
        $scope.allRuntimes = [
            {
                ruleBaseID: '0',
                runtimeURL: 'http://192.168.1.26:8080/runtime-1',
                rulePackage: 'loyalty',
                sessionId: '1',
                status: 'INITMODE',
                startDate: '2014-04-20 1:48:23 AM',
                endDate: '2014-04-20 1:48:42 AM'
            },
            {
                ruleBaseID: '1',
                runtimeURL: 'http://192.168.1.26:8080/runtime-2',
                rulePackage: 'loyalty',
                sessionId: '2',
                status: 'STARTED',
                startDate: '2014-04-20 1:48:23 AM',
                endDate: '2014-04-20 1:48:42 AM'
            },
            {
                ruleBaseID: '2',
                runtimeURL: 'http://192.168.1.26:8080/runtime-2',
                rulePackage: 'loyalty',
                sessionId: '3',
                status: 'NOT_JOIGNABLE',
                startDate: '2014-04-20 1:48:23 AM',
                endDate: '2014-04-20 1:48:42 AM'
            },
            {
                ruleBaseID: '3',
                runtimeURL: 'http://192.168.1.26:8080/runtime-2',
                rulePackage: 'loyalty',
                sessionId: '4',
                status: 'STOPPED',
                startDate: '2014-04-20 1:48:23 AM',
                endDate: '2014-04-20 1:48:42 AM'
            },
            {
                ruleBaseID: '5',
                runtimeURL: 'http://192.168.1.26:8080/runtime-2',
                rulePackage: 'loyalty',
                sessionId: '5',
                status: 'CRASHED',
                startDate: '2014-04-20 1:48:23 AM',
                endDate: '2014-04-20 1:48:42 AM'
            },
            {
                ruleBaseID: '6',
                runtimeURL: 'http://192.168.1.26:8080/runtime-2',
                rulePackage: 'loyalty',
                sessionId: '6',
                status: 'STOPPED',
                startDate: '2014-04-20 1:48:23 AM',
                endDate: '2014-04-20 1:48:42 AM'
            }
        ];
    }

});


