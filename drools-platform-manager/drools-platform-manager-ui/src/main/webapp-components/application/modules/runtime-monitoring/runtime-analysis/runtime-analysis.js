DroolsPlatformControllers.controller('runtimeAnalysisController', function ($rootScope, $scope, $document, $http, $log) {
    $http.get('./server/rules_package/list')
        .success(function (data) {
            $scope.packagesList = data;
        })
        .error(function (error) {
            console.log(error);
        });
    $scope.isCollapsed = true;
    $scope.isCollapsed2 = true;
    $scope.dynamicPopover = 'test';
    $scope.dynamicPopoverTitle = 'Guvnor URL';
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
            sessionId:'3',
            status:'STOPPED',
            startDate:'2014-04-20 1:48:23 AM',
            endDate:'2014-04-20 1:48:42 AM'
        }
    ];
    $scope.selectedRuntimeID = '32';
    //___ Right click management
    $scope.onLeftClick = function(packageVersion) {
        //alert("test");
        var someElement = angular.element(document.getElementById('detailsPanel'));
        $document.scrollToElement(someElement, 0, 1000);
    }

    $scope.collapseForm = function() {
        $scope.isCollapsed=!$scope.isCollapsed;
        if($scope.isCollapsed==true){
            if($scope.isCollapsed2==false){
                $scope.isCollapsed2=true;
            }
        }
    }

    $scope.today = function(dateGiven) {
        return new Date();
       /* $scope.dt = new Date();
        $scope.endDate = new Date();*/
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
});
