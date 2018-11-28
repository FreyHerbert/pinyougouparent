app.service('loginService', function ($http) {
    this.loginProfile=function () {
        return $http.get('../login/getProfile.do');
    }
});