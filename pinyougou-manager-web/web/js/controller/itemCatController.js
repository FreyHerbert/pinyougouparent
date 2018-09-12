 //控制层 
app.controller('itemCatController' ,function($scope,$controller   ,itemCatService, typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
    //读取列表数据绑定到表单中
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;
				$scope.findType($scope.entity.typeId);
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			$scope.entity.parentId = $scope.parentId;
			serviceObject=itemCatService.add( $scope.entity );//增加
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
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={parentId:0};//定义搜索对象
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	// 根据上级分类查询商品分类列表
	$scope.findByParentId=function (parentId) {
		itemCatService.findByParentId(parentId).success(
			function (response) {
				$scope.list = response;
            }
		);
    }

    // 当前级别
    $scope.grade = 1;
	// 设置级别
	$scope.setGrade=function (value) {
		$scope.grade = value;
    }

    $scope.selectList=function (p_entity) {
        if ($scope.grade == 1) {
        	$scope.entity_1 = null;
        	$scope.entity_2 = null;
        }else if ($scope.grade == 2) {
        	$scope.entity_1 = p_entity;
        	$scope.entity_2 = null;
        } else {
        	$scope.entity_2 = p_entity;
        }
		$scope.searchEntity.parentId = p_entity.id;
        $scope.parentId = p_entity.id;
        $scope.reloadList();
    }

    // 记录上级的id
	$scope.parentId = 0;

	// select2 数据
    $scope.typeList={data:[]};
    $scope.typeInit = function () {
		// 初始化 typeList
		typeTemplateService.findAll().success(
			function (response) {
				var templates = response;
                $scope.typeList={data:[]};
				for (var temp in templates) {
                    var template = {id:templates[temp].id,text:templates[temp].name};
                    $scope.typeList.data.push(template);
				}
        	}
        )
    }

    $scope.template = {};
    $scope.initEntity = function () {
    	// 置空 entity ， template
    	$scope.entity={};
        $scope.template = {};
    };
	$scope.findType = function (typeId) {
		if ($scope.typeList.data != null){
            var tail = $scope.typeList.data.length - 1;
            var head = 0;
            var index = parseInt((head + tail) / 2);
            while (head <= tail) {
            	if ($scope.typeList.data[index].id > typeId){
                    tail = index -1;
                    index = (head + tail) / 2;
				} else if ($scope.typeList.data[index].id < typeId) {
                    head = index + 1;
                    index = (head + tail) / 2;
				} else {
                    $scope.template = {id:$scope.typeList.data[index].id, text:$scope.typeList.data[index].text};
                    break;
				}
            }
		}
    }
});	
