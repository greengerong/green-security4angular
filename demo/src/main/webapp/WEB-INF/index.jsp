<html ng-app="app">
<head>
    <title>XSRF demo page</title>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular.min.js"></script>
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css"/>
</head>
<body ng-controller="ctr">
<h2>XSRF demo page</h2>

<div class="container">
    <div><input class="input" type="text" ng-model="age"/></div>
    <hr/>
    <div>
        <button class="btn" ng-click="get();">GET</button>
        <button class="btn" ng-click="post();">POST</button>
        <button class="btn" ng-click="delete();">DELETE</button>
        <button class="btn" ng-click="put();">PUT</button>
    </div>
</div>
<script type="text/javascript">
    angular.module('app', []).controller("ctr", function ($scope, $http) {
        $scope.age = 28;
        $scope.get = function () {
            $http.get("process/" + $scope.age)
                    .success(function () {
                        console.log(arguments, "success", "get");
                    }).error(function () {
                        console.log(arguments, "error", "get");
                    });
        };

        $scope.post = function () {
            $http.post("process/" + $scope.age)
                    .success(function () {
                        console.log(arguments, "success", "post");
                    }).error(function () {
                        console.log(arguments, "error", "post");
                    });
        };

        $scope.put = function () {
            $http.put("process/" + $scope.age)
                    .success(function () {
                        console.log(arguments, "success", "put");
                    }).error(function () {
                        console.log(arguments, "error", "put");
                    });
        };

        $scope.delete = function () {
            $http.delete("process/" + $scope.age)
                    .success(function () {
                        console.log(arguments, "success", "delete");
                    }).error(function () {
                        console.log(arguments, "error", "delete");
                    });
        };
    });

</script>
</body>
</html>
