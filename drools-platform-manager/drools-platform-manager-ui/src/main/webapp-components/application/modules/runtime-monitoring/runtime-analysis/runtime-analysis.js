DroolsPlatformControllers.controller('runtimeAnalysisController', function ($rootScope, $scope, $document, $http, $log, $timeout) {

    /** SCOPE DEFINITION **/

    $scope.allRuntimes=[];

    //___ Collapse status for each box from the "Search panel"
    $scope.collapseStatus = {
        "firstBox":false,
        "secondBox":false
    };

    //___ Popover details (when you move the mouse over the rulePackage)
    $scope.popoverDetails={
        'title':'Guvnor URL',
        'content':'<b>Guvnor URL</b><br/><a href="http://192.168.1.26:8080/drools-guvnor" target="_blank">192.168.1.26:8080/drools-guvnor</a>'

    };

    //___ Session Execution Details area
    $scope.sessionExecutionDetails={
        'area':false,
        'panel':false
    };

    //___ Dates
    $scope.format = 'yyyy/MM/dd';
    $scope.dates={
        'initDate':new Date('2016/12/20'),
        'startDate':new Date(),
        'endDate':new Date(),
        'minDate':'1993-07-29',
        'maxDate':'2050-07-29',
        'myTime':new Date()
    };
    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
    };

    $scope.datePickers={
        'firstCalendar':false,
        'secondCalendar':false
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

    $scope.selectedTab=1;

    $scope.test="// test code";
    $scope.code = {
        'input':"// input",
        'output':'// output'
    };
    $scope.editorOptions = {
        lineWrapping : true,
        lineNumbers: true,
        styleActiveLine: true,
        matchBrackets: true,
        readOnly: false,
        mode: {name: "javascript", json: true},
        theme :'ambiance'
    };


    //___ Fetch the list
    $http.get('./server/rules_package/list')
        .success(function (data) {
            $scope.packagesList = data;
        })
        .error(function (error) {
            console.log(error);
        });




    //___ Scrolling to the next panel
    $scope.scrollToPanel = function(ruleBaseID) {
        $scope.selectedRuntimeID=ruleBaseID;
        $scope.sessionExecutionDetails.area=true;
        $timeout(function() {
            $scope.sessionExecutionDetails.panel=true;
            var someElement = angular.element(document.getElementById('detailsPanel'));
            $document.scrollToElement(someElement, 0, 1000);
        }, 3);


    };

    // Toggle to another tab
    $scope.selectedTab = '';
    $scope.toggleTab=function(item){
        $scope.selectedTab = item;
    }

    $scope.closeDetailsPanel = function() {
        var someElement = angular.element(document.getElementById('wrap'));
        $document.scrollToElement(someElement, 0, 1000);
        $timeout(function() {
            $scope.sessionExecutionDetails.area=false;
            $scope.sessionExecutionDetails.panel=false;
        }, 1000);



    };

    /**********************/
    /*  DATE & TIME MGMT  */
    /**********************/
    $scope.today = function(dateGiven) {
        return new Date();
    };
    $scope.today();

    $scope.clear = function () {
        $scope.dates.startDate = new Date();
    };

    // Disable weekend selection
    $scope.disabled = function(date, mode) {
        return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
    };
/*
    $scope.toggleMin = function() {
        $scope.dates.minDate = $scope.dates.minDate ? null : new Date();
    };
    $scope.toggleMin();*/

    $scope.open = function($event, inputConcerned) {
        $event.preventDefault();
        $event.stopPropagation();
         if(inputConcerned=="startDate"){
             $scope.datePickers.firstCalendar=true;

         }else{
             $scope.datePickers.secondCalendar=true;
         }

    };

    $scope.toggleMode = function() {
        $scope.ismeridian = ! $scope.ismeridian;
    };

    $scope.update = function() {
        var d = new Date();
        d.setHours( 14 );
        d.setMinutes( 0 );
        $scope.dates.mytime = d;
    };

    $scope.changed = function () {
        console.log('Time changed to: ' + $scope.dates.myTime);
    };



    /** EVENTS ASSOCIATED WITH BUTTONS **/

    /** SEARCH **/

    //___ Method 1 : filters chosen
    //___ if each fields are empty
    //___ if all but one fields are empty
        //___ First field

    //___ if all fields are filled

    $scope.search = function () {
        $scope.allRuntimes= [{
            ruleBaseID:'0',
            runtimeURL:'http://192.168.1.26:8080/runtime-1',
            rulePackage:'alex.test.package',
            sessionId:'1',
            status:'INITMODE',
            startDate:'2014-04-20 1:48:23 AM',
            endDate:'2014-04-20 1:48:42 AM'
            },
            {
                ruleBaseID:'1',
                runtimeURL:'http://192.168.1.26:8080/runtime-2',
                rulePackage:'alex.test.package',
                sessionId:'2',
                status:'STARTED',
                startDate:'2014-04-20 1:48:23 AM',
                endDate:'2014-04-20 1:48:42 AM'
            },
            {
                ruleBaseID:'2',
                runtimeURL:'http://192.168.1.26:8080/runtime-2',
                rulePackage:'alex.test.package',
                sessionId:'3',
                status:'NOT_JOIGNABLE',
                startDate:'2014-04-20 1:48:23 AM',
                endDate:'2014-04-20 1:48:42 AM'
            },
            {
                ruleBaseID:'3',
                runtimeURL:'http://192.168.1.26:8080/runtime-2',
                rulePackage:'alex.test.package',
                sessionId:'4',
                status:'STOPPED',
                startDate:'2014-04-20 1:48:23 AM',
                endDate:'2014-04-20 1:48:42 AM'
            },
            {
                ruleBaseID:'5',
                runtimeURL:'http://192.168.1.26:8080/runtime-2',
                rulePackage:'alex.test.package',
                sessionId:'5',
                status:'CRASHED',
                startDate:'2014-04-20 1:48:23 AM',
                endDate:'2014-04-20 1:48:42 AM'
            },
            {
                ruleBaseID:'6',
                runtimeURL:'http://192.168.1.26:8080/runtime-2',
                rulePackage:'alex.test.package',
                sessionId:'6',
                status:'STOPPED',
                startDate:'2014-04-20 1:48:23 AM',
                endDate:'2014-04-20 1:48:42 AM'
            }
        ];
        /*
        $http.get('.server/runtime_resource/activePlatformRuntimes/'+packageSelected)
            .success(function (data) {
                $scope.packageList = data;
                console.log($scope.packageList);
            })
            .error(function (error, status) {
                console.log(error);
            });

        */

    };
    $scope.reset = function(){
        $scope.allRuntimes= [];

    };


});


