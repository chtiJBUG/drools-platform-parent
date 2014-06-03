DroolsPlatformControllers.controller('runtimeAnalysisController', function ($rootScope, $scope, $document, $http, $log, $timeout) {
    $scope.isPanelCalled=false;

    //___ Fetch the list
    $http.get('./server/rules_package/list')
        .success(function (data) {
            $scope.packagesList = data;
        })
        .error(function (error) {
            console.log(error);
        });

    //___ Collapse
    $scope.isCollapsed = true;
    $scope.isCollapsed2 = true;

    $scope.dynamicPopover = 'test';
    $scope.dynamicPopoverTitle = 'Guvnor URL';

    //___ Mockup
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
            status:'STOPPED',
            startDate:'2014-04-20 1:48:23 AM',
            endDate:'2014-04-20 1:48:42 AM'
        },
        {
            ruleBaseID:'2',
            runtimeURL:'http://192.168.1.26:8080/runtime-2',
            rulePackage:'alex.test.package',
            sessionId:'3',
            status:'STOPPED',
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
            status:'STOPPED',
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
    $scope.selectedRuntimeID = '32';

    //___ Scrolling to the next panel
    $scope.scrollToPanel = function() {
        $scope.isPanelCalled=true;
        $timeout(function() {
            //alert("nulllll");
            $scope.isPanelShown=true;
            var someElement = angular.element(document.getElementById('detailsPanel'));
            $document.scrollToElement(someElement, 0, 1000);
        }, 3);


    };

    $scope.closeDetailsPanel = function() {
        $scope.isPanelCalled=false;
        var someElement = angular.element(document.getElementById('mainPanel'));
        $document.scrollToElement(someElement, 0, 1000);

    };

    /**********************/
    /*  DATE & TIME MGMT  */
    /**********************/
    $scope.today = function(dateGiven) {
        return new Date();
    };
    $scope.today();

    $scope.clear = function () {
        $scope.startDate = new Date();
    };

    // Disable weekend selection
    $scope.disabled = function(date, mode) {
        return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
    };

    $scope.toggleMin = function() {
        $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function($event, inputConcerned) {
        $event.preventDefault();
        $event.stopPropagation();
         if(inputConcerned=="startDate"){

             $scope.datepickerOpened1=true;
         }else{
             $scope.datepickerOpened2=true;
         }

    };

    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
    };

    $scope.startDate = new Date();
    $scope.endDate = new Date();
    $scope.initDate = new Date('2016/15/20');
    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[1];


    $scope.search = function() {
        alert($scope.startDate);
    }


    $scope.mytime = new Date();

    $scope.hstep = 1;
    $scope.mstep = 15;

    $scope.options = {
        hstep: [1, 2, 3],
        mstep: [1, 5, 10, 15, 25, 30]
    };

    $scope.ismeridian = true;
    $scope.toggleMode = function() {
        $scope.ismeridian = ! $scope.ismeridian;
    };

    $scope.update = function() {
        var d = new Date();
        d.setHours( 14 );
        d.setMinutes( 0 );
        $scope.mytime = d;
    };

    $scope.changed = function () {
        console.log('Time changed to: ' + $scope.mytime);
    };

    $scope.clear = function() {
        $scope.mytime = null;
    };



    /** SEARCH **/

    //___ Method 1 : filters chosen
    //___ if each fields are empty
    //___ if all but one fields are empty
        //___ First field

    //___ if all fields are filled

    $scope.search = function () {
        var filters = $scope.filters;
        var packageSelected=$scope.package;
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


});
