app.controller('indexController', function ($scope, loginService) {

    $scope.username = "";
    $scope.nickName = "";

    $scope.loginProfile=function () {
        loginService.loginProfile().success(function (response) {
            $scope.username = response.username;
            $scope.nickName = response.nickName;
        });
    }
});