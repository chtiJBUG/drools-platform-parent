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

    //___ Filters
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

    //___ Date management
    /** WARNING : I had to custom lang('en') to fit with the pattern of Bootstrap-UI's datepicker... **/
    moment.lang(navigator.language); //define local date
    $scope.format = moment.langData(navigator.language).longDateFormat('L'); //define local format date

    $scope.dates = {
        'minDate': moment("07-29-1995", "MM-DD-YYYY"),
        'maxDate': moment("07-29-2095", "MM-DD-YYYY")
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

    $scope.today = function (dateGiven) {
        return moment();
    };
    $scope.today();

    $scope.clear = function () {
        $scope.filters.startDate = moment().format('L');
        $scope.filters.endDate = moment().format('L');
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

    // Open the popup
    $scope.open = function ($event, inputConcerned) {
        $event.preventDefault();
        $event.stopPropagation();
        if (inputConcerned == "startDate") {
            $scope.datePickers.firstCalendar = true;

        } else {
            $scope.datePickers.secondCalendar = true;
        }

    };

    //___ Var needed for the 'Session Execution Details' Panel :

    // Session Execution Details area
    $scope.sessionExecutionDetails = {
        'area': false,
        'panel': false
    };

    // ID of the runtime selected
    $scope.selectedRuntimeID = undefined;

    // Tab
    $scope.selectedTab = 1;

    // JSON Data Viewer
    $scope.code = {
        'input': "// input",
        'output': '// output'
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
                console.log('[Success] Data retrieved successfully');

                $scope.code.input=JSON.stringify(JSON.parse($scope.allSessionExecutionDetails.inputObject), null, 3);
                $scope.code.output=JSON.stringify(JSON.parse($scope.allSessionExecutionDetails.outputObject), null, 3);

                //How to get the rule executions in alist in AngularJS :
                //ruleExcecutionList =_.map(_.map(_.map($scope.allSessionExecutionDetails.allRuleFlowGroupDetails, function(item){ return item; }), function(ruleflowGroupItem){return ruleflowGroupItem.allRuleExecutionDetails;}), function(ruleExecutionGroupItem){ return ruleExecutionGroupItem[0]; });

            })
            .error(function (error, status) {
                console.log("[Error] Error HTTP " + status);
                console.log(error);
                //____ TODO Send an appropriate message
                growlNotifications.add('Whoops ! Error HTTP ' + status, 'danger', 2000);
            })

    };


    // Indent JSON code in Details panel Event list section
    $scope.indentJSON = function (fact) {
        fact.jsonFact = JSON.stringify(JSON.parse(fact.jsonFact),null,3);
    }


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

    /** SEARCH **/
    $scope.search = function () {
        console.log("[Info] Start date : "+$scope.filters.startDate);
        console.log("[Info] End date : "+$scope.filters.endDate);
        //___ You must chose the package
        if ($scope.filters.packageName == null || $scope.filters.packageName == "") {
            console.log("[Error] Package name is mandatory for filtering");
            $scope.namePackageSelectClass = "form-group has-error has-feedback";

        } else {
            if ($scope.filters.status == '') {
                $scope.filters.status = undefined;
            }
            if ($scope.filters.hostname == '') {
                $scope.filters.hostname = undefined;
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
        $scope.filters=undefined;
        $scope.showCancelButton = false;
    };

});


