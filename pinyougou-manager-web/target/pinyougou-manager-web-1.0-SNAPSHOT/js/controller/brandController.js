app.controller("brandController", function ($scope,$controller, brandService) {

    $controller('baseController',{$scope: $scope});

    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };


    $scope.findPage = function (page, size) {
        brandService.findPage(page, size).success(
            function (response) {
                // 当前页的数据
                $scope.list = response.rows;
                // 数据的总条数
                $scope.paginationConf.totalItems = response.total;
            }
        );
    }

    // 新增
    $scope.save = function () {
        var object = null;
        if ($scope.entity.id != null) {
            object = brandService.update($scope.entity);
        } else {
            object = brandService.add($scope.entity);
        }
        object.success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                } else {
                    alert(response.message);
                }
            }
        );
    }

    // 查询实体
    $scope.findOne = function(id) {
        brandService.findOne(id).success(
            function (response) {
                if (response.success) {
                    $scope.entity = response;
                } else {
                    alert(response.message);
                }
            }
        )
    }



    //删除
    $scope.dele = function () {
        brandService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                } else {
                    alert(response.message);
                }
            }
        )
    }

    $scope.searchEntity = {};
    // 条件查询
    $scope.search = function(page, size) {
        brandService.search($scope.searchEntity, page, size).success(
            function (response) {
                // 当前页的数据
                $scope.list=response.rows;
                // 数据的总条数
                $scope.paginationConf.totalItems = response.total;
            }
        );
    }
});