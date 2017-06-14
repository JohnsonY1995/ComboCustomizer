var restEndpoint = "http://localhost:8080/";
var mainApp = angular.module("ComboCustomizer", ['ui.tree']);

mainApp.controller('comboController', function($scope, $http) {
    $scope.availableItems = [];
    $scope.selectedItems = [];
    $scope.allItems = [];

    $scope.getAllItems = function() {
        $http.get(restEndpoint + "items").then(function(response) {
            $scope.availableItems = response.data;
            $scope.allItems = response.data;
        });
    };
    $scope.getAllItems();

    $scope.allCategories = [];
    $scope.getAllCategories = function() {
        $http.get(restEndpoint + "categories").then(function(response) {
            $scope.allCategories = response.data;
        });
    };
    $scope.getAllCategories();


    $scope.addNewItem = function() {
        var data = "name=" + $scope.newItem.name + "&categoryId=" + $scope.newItem.category;
        $http({
            url: restEndpoint + "items",
            method: 'POST',
            data: data,
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "Accept": "application/json"
            }

        }).then(function successCallback(response) {
            $scope.getAllItems();
            $scope.newItem.name = "";
            console.log(response);
        }, function errorCallback(response) {
            console.error(response);
        });

    };

    $scope.addNewCategory = function() {
        var data = "name=" + $scope.newCategory.name;
        $http({
            url: restEndpoint + "categories",
            method: 'POST',
            data: data,
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "Accept": "application/json"
            }

        }).then(function successCallback(response) {
            $scope.getAllCategories();
            $scope.newCategory.name = "";
            console.log(response);
        }, function errorCallback(response) {
            console.error(response);
        });
    };

    $scope.newDependency = {};
    $scope.addNewDependency = function() {
        var data = {
            "depender": $scope.newDependency.depender,
            "dependee": $scope.newDependency.dependee
        };
        $http({
            url: restEndpoint + "itemdependencies",
            method: 'POST',
            data: data,
            headers: {
                "Accept": "application/json"
            }

        }).then(function successCallback(response) {
            console.log(response);
        }, function errorCallback(response) {
            console.error(response);
        });
    };

    $scope.startExecute = function() {
        console.log($scope.selectedItems);
        var executeItemIds = [];
        angular.forEach($scope.selectedItems, function(value, key) {
            executeItemIds.push(value.item_id);
        });
        console.log(executeItemIds);

        $scope.execution = {};
        $scope.execution.done = false;
        $scope.execution.failed = false;
        $scope.execution.processing = true;
        var data = {
            "items": executeItemIds
        };
        $http({
            url: restEndpoint + "execute",
            method: 'POST',
            data: data,
            headers: {
                "Accept": "application/json"
            }

        }).then(function successCallback(response) {
            $scope.execution.elapsedMs = response.data.msElapsed;
            $scope.execution.result = response.data.result;
            $scope.execution.done = true;
            $scope.execution.processing = false;
            console.log(response);
        }, function errorCallback(response) {
            $scope.execution.failed = true;
            $scope.execution.result = response.data.failReason;
            $scope.execution.done = false;
            $scope.execution.processing = false;
            console.error(response);
        });
    };

});