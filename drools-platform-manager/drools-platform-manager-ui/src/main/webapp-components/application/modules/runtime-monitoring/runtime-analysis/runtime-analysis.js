DroolsPlatformControllers.controller('runtimeAnalysisController', function ($rootScope, $scope, $document, $http, $log, $timeout, growlNotifications) {

    /** SCOPE DEFINITION **/

    initRuntimeAnalysisController();

    /** SESSION EXECUTION DETAILS **/

    $scope.loadDetails = function (ruleBaseID, sessionId){
        $http.get('./server/runtime/session/'+ruleBaseID+'/'+sessionId)
            .success(function (data) {
                $scope.allSessionExecutionDetails = data;
                $scope.code.input=JSON.stringify(JSON.parse($scope.allSessionExecutionDetails.inputObject), null, 3);
                $scope.code.output=JSON.stringify(JSON.parse($scope.allSessionExecutionDetails.outputObject), null, 3);
                /*
                    From allSessionExecution, we will extract the useful informations.
                    We use undescoreJS, otherwise it will be complexed to write the for loops
                */
                // RuleExecution list
                ruleExcecutionList =_.map(
                    _.map(
                        _.map(
                            $scope.allSessionExecutionDetails.allRuleFlowGroupDetails,
                            function(item){
                                console.log(item);
                                return item;
                            }
                        ),
                        function(ruleflowGroupItem){
                            return ruleflowGroupItem.allRuleExecutionDetails;
                        }
                    ),
                    function(ruleExecutionGroupItem){
                        // The list is in an array [{...},{...}]
                        // We need the first item of the array
                        return ruleExecutionGroupItem[0];
                    }
                );

                console.log(ruleExcecutionList);

                // ruleName List extracted from the ruleExecutionList
                $scope.ruleNameList=_.map(
                    ruleExcecutionList,
                    function(ruleExecutionItem){
                        return ruleExecutionItem.ruleName;
                    }
                )

                // ruleAssetCategory List
                $scope.ruleAssetCategoryList=_.map(
                    _.map(
                        _.map(
                            ruleExcecutionList,
                            function(ruleExecutnItem){
                                return ruleExecutnItem.ruleAsset;
                            }
                        ),
                        function(ruleAssetItem){
                            return ruleAssetItem.ruleAssetCategory;
                        }
                    ),
                    function(item){
                        return item[0];
                    }
                );

                whenFactsFullClasseNameList = _.map(
                    _.map(
                        _.map(
                            ruleExcecutionList,
                            function(ruleExecutionItem){
                                return ruleExecutionItem.whenFacts;
                            }
                        ),
                        function(whenFactsList){
                            return whenFactsList[0];
                        }
                    ),
                    function(whenFactItem){
                        return whenFactItem.fullClassName;
                    }
                );

                thenFactsFullClasseNameList = _.map(
                    _.map(
                        _.map(
                            ruleExcecutionList,
                            function(ruleExecutionItem){
                                return ruleExecutionItem.thenFacts;
                            }
                        ),
                        function(thenFactsList){
                            return thenFactsList[0];
                        }
                    ),
                    function(thenFactItem){
                        return thenFactItem.fullClassName;
                    }
                );

                // We add each items from whenFact fullClassName list and thenFact fullClassName list to the fact type list which already contains FactType.

                _.each(whenFactsFullClasseNameList, function(item){
                    $scope.facttypesList[$scope.facttypesList.length] = item;
                });

                _.each(thenFactsFullClasseNameList, function(item){
                    $scope.facttypesList[$scope.facttypesList.length] = item;
                });

                // If we wanna delete duplicates, we have to do this :
                $scope.facttypesList=_.uniq($scope.facttypesList);

                // When details have been loaded : Scrolling to the 'details' panel
                $scope.scrollToPanel(ruleBaseID, sessionId);
            })
            .error(function (error, status) {
                console.log("[Error] Error HTTP " + status);
                console.log(error);
                //____ TODO Send an appropriate message
                growlNotifications.add('Whoops ! Error HTTP ' + status, 'danger', 2000);
            })
    }

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

    $scope.collapseSidebar = function(){
        $scope.sidebarContracted=!$scope.sidebarContracted;
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
            $scope.namePackageSelectClass = "form-group";

            $http.post('./server/runtime/count', filters)
                .success(function (data) {
                    $scope.count = data;
                    console.log("[Info] Total count : "+$scope.count);
                })
                .error(function (error, status) {
                    console.log("[Error] Error HTTP " + status);
                    console.log(error);
                    //____ TODO Send an appropriate message
                    growlNotifications.add('Whoops ! Error HTTP ' + status, 'danger', 2000);
                });


            $http.post('./server/runtime/filter', filters)
                .success(function (data) {
                    $scope.allRuntimes = data;
                    console.log("[Info] Number of items displayed per page : "+$scope.allRuntimes.length);
                })
                .error(function (error, status) {
                    console.log("[Error] Error HTTP " + status);
                    console.log(error);
                    //____ TODO Send an appropriate message
                    growlNotifications.add('Whoops ! Error HTTP ' + status, 'danger', 2000);
                });
        }
        $scope.currentPage=1;
        $scope.showCancelButton = true;
    };

    $scope.searchDetails = function(){
        //Apply filters [Temporary (?)]
        $scope.detailsFilters.ruleName=$scope.detailsFilters.ruleNameSelect;
        $scope.detailsFilters.ruleCategory=$scope.detailsFilters.ruleCategorySelect;
        $scope.detailsFilters.factType=$scope.detailsFilters.factTypeSelect;
        //Reinitialize values
        $scope.detailsFilters.ruleNameSelect=undefined;
        $scope.detailsFilters.ruleCategorySelect=undefined;
        $scope.detailsFilters.factTypeSelect=undefined;
        //Hide sidebar
        $scope.collapseSidebar();
    }

    $scope.pageChanged = function() {
        console.log('Page changed to: ' + $scope.currentPage);
        var filters=$scope.filters;
        filters.page.currentIndex=$scope.currentPage;
        $http.post('./server/runtime/filter', filters)
            .success(function (data) {
                $scope.allRuntimes = data;
                console.log("[Info] Number of items displayed per page : "+$scope.allRuntimes.length);
            })
            .error(function (error, status) {
                console.log("[Error] Error HTTP " + status);
                console.log(error);
                //____ TODO Send an appropriate message
                growlNotifications.add('Whoops ! Error HTTP ' + status, 'danger', 2000);
            });
    };

    $scope.reset = function () {
        initRuntimeAnalysisController();
        //___ Close filters collapsible boxes from "Filter execution by Runtime"
        $scope.collapseStatus.firstBox=false;
        $scope.collapseStatus.seconddBox=false;
        $scope.showCancelButton = false;
    };

    /** INIT THE SCOPE VALUES **/
    function initRuntimeAnalysisController() {
        $scope.allRuntimes = [];

        //___ Fetch the rule package list
        $http.get('./server/rules_package/list')
            .success(function (data) {
                $scope.packagesList = data;
            })
            .error(function (error) {
                console.log(error);
            });

        //___ Fecth the statuses
        $http.get('./server/runtime/all/statuses')
            .success(function (data) {
                $scope.statusesList = data;
            })
            .error(function (error) {
                console.log(error);
            });

        //___ Fetch the list of FactType
        $http.get('./server/runtime/all/facttypes')
            .success(function (data) {
                $scope.facttypesList = data;
            })
            .error(function (error) {
                console.log(error);
            });

        // Allow the user to cancel his choice when selecting an item
        $scope.selectPackage = {allowClear: true};
        $scope.selectStatus = {allowClear: true};
        $scope.selectCategory = {allowClear: true};
        $scope.selectRuleName = {allowClear: true};
        $scope.selectFactType = {allowClear: true};

        //___ Pagination options
        $scope.page = {
            currentIndex : undefined,
            totalCount: undefined,
            maxItemPerPage : undefined

        }

        //___ Filters
        $scope.filters = {
            packageName: undefined,
            status: undefined,
            hostname: undefined,
            startDate: undefined,
            endDate: undefined,
            onlyRunningInstances : false,
            page : $scope.page
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

        //___ Status of the sidebar from the 'Details' panel
        $scope.sidebarContracted=true;

        //___ Filters for the 'Details' panel
        $scope.detailsFilters = {
            ruleCategorySelect: undefined, //Temporary (?)
            ruleNameSelect: undefined, //Temporary (?)
            factTypeSelect: undefined, //Temporary (?)
            ruleCategory: undefined,
            ruleName: undefined,
            factType: undefined
        };
    };

});


