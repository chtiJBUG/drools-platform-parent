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
        endDate: undefined,
        onlyRunningInstances : false
    };

    //___ Collapse status for each box from the "Search panel"
    $scope.collapseStatus = {
        "firstBox": false,
        "secondBox": false
    };

    //___ Popover details (when you move the mouse over the rulePackage)
    $scope.popoverDetails = {
        'title': 'Guvnor URL',
        'content': '<b>Guvnor URL</b><br/><a href=""+{{runtime.hostname}}+"" target="_blank">{{runtime.hostname}}</a>'

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


    /**  DATE & TIME MGMT  **/

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

    //___ Scrolling to the next panel and load Session Execution details
    $scope.scrollToPanel = function (ruleBaseID, sessionId) {

        $scope.selectedRuleBaseID = ruleBaseID; // Temporary var (value displayed in the head of the panel)
        $scope.selectedSessionId =  sessionId; // Idem
        $scope.sessionExecutionDetails.area = true; // Second part of the page made visible
        $timeout(function () {
            $scope.sessionExecutionDetails.panel = true; // Panel visible now
            var someElement = angular.element(document.getElementById('detailsPanel')); // Scroll to the panel
            $document.scrollToElement(someElement, 0, 1000);
        }, 3);

        // DEBUG
        console.log(typeof(sessionId));

        //___ Load Details
        $http.get('./server/runtime/session/'+ruleBaseID+'/'+sessionId)
            .success(function (data) {
                $scope.allSessionExecutionDetails = data;
                //console.log($scope.allSessionExecutionDetails);
            })
            .error(function (error, status) {
                console.log("[Error] Error HTTP " + status);
                console.log(error);
                //____ TODO Send an appropriate message
                growlNotifications.add('Whoops ! Error HTTP ' + status, 'danger', 2000);
            })

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
                });
        }
        $scope.showCancelButton = true;
    };

    $scope.reset = function () {
        $scope.allRuntimes = [];
        $scope.namePackageSelectClass = "form-group";
        $scope.showCancelButton = false;
    };


});


