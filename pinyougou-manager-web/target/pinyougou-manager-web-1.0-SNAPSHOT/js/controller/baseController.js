app.controller('baseController', function ($scope) {
    // 分页
    $scope.paginationConf={
        // 当前页
        currentPage : 1,
        // 数据的总条数
        totalItems :  10,
        // 每页显示多少数据
        itemsPerPage : 10,
        // 分页选项
        perPageOption : [10,20,30,40,50],
        // 当页码改变时触发
        onChange : function () {
            $scope.reloadList();
        }
    };

    // 刷新列表
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage,
            $scope.paginationConf.itemsPerPage);
        $scope.selectA = false;
        $scope.selectIds=[];
        $scope.m = [];
    };

    // 用户勾选的 ID 集合
    $scope.selectIds=[];
    // 用户勾选复选框
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {
            // push 方法向集合添加原属
            $scope.selectIds.push(id);
        } else {
            // 查找值的位置
            var index = $scope.selectIds.indexOf(id);
            // 参数1：移除的位置，参数2：移除的个数
            $scope.selectIds.splice(index, 1);
            $scope.selectA = false;
        }
    }

    // 勾选的全选设置
    $scope.m = [];
    $scope.selectInit = function () {
        // $scope.list 是分页请求返回的数据的list
        angular.forEach($scope.list, function (i, index) {
            $scope.m[i.id] = false;
        });
    };
    $scope.selectAll = function () {
        if ($scope.selectA) {
            // 取消操作
            $scope.selectIds = [];
            angular.forEach($scope.list, function (i, index) {
                $scope.m[i.id] = false;
            });
        } else {
            // 勾选操作
            $scope.selectIds = [];
            angular.forEach($scope.list, function (i, index) {
                $scope.selectIds.push(i.id);
                $scope.m[i.id] = true;
            });
        }
    };

    // json 转 string
    $scope.jsonToString = function (jsonString, key) {
        var json = JSON.parse(jsonString);
        var value ="";

        for (var i = 0; i < json.length; i++){
            if (i > 0) {
                value += ',';
            }
            value += json[i][key];
        }

        return value;
    }

});