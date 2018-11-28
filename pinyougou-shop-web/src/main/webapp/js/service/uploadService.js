app.service("uploadService", function ($http) {
    this.uploadFile = function () {
        var formdata = new FormData();
        // file 文件上传框的 name
        formdata.append('file', file.files[0]);
        return $http({
            url:'../upload.do',
            method:'post',
            data:formdata,
            headers:{ 'Content-Type':undefined },
            transformRequest: angular.identity
        });
    }
});