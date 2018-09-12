 //控制层 
app.controller('specificationController' ,function($scope,$controller   ,specificationService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		specificationService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		specificationService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){
        $scope.initAddTableRow();
		specificationService.findOne(id).success(
			function(response){
				$scope.entity= response;
			}
		);				
	}
	
	//保存 
	$scope.save=function(){
        //服务层对象
		var serviceObject;
        //如果有ID
		if($scope.entity.specification.id!=null){
            //修改
			serviceObject=specificationService.update( $scope.entity );
		}else{
            //增加
			serviceObject=specificationService.add( $scope.entity  );
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){
		if (($scope.selectIds != null) && ($scope.selectIds.length != 0)) {
            //获取选中的复选框
            specificationService.dele($scope.selectIds).success(
                function (response) {
                    if (response.success) {
                        $scope.reloadList();//刷新列表
                        $scope.selectIds = [];
                    }
                }
            );
        }
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		specificationService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	// 新建时初始化规格添加的容器。
    $scope.initAddTableRow = function () {
        $scope.entity={specificationOptionList:[]};
    };

	//增加规格选项行
	$scope.addTableRow=function(){
		$scope.entity.specificationOptionList.push({});
	}


	//删除规格选项行
	$scope.deleTableRow=function(index){
		$scope.entity.specificationOptionList.splice(index,1);
	}


});	
