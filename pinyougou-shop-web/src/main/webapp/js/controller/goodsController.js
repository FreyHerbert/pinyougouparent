 //控制层 
app.controller('goodsController' ,function($scope,$controller   ,$location ,goodsService, uploadService, itemCatService, typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(){
		var id = $location.search()['id'];
		if (id == null) {
			return;
		}else {
            goodsService.findOne(id).success(
                function (response) {
                    $scope.entity = response;
                    // 商品介绍
                    editor.html($scope.entity.goodsDesc.introduction)
					// 商品图片
					$scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
                    // 扩展属性
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                    // 规格选择
                    $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
                    // 转换 SKU 列表中的规格对象
                    for (var i = 0; i < $scope.entity.itemList.length; i++) {
                        $scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
                    }
                }
            );
        }
	}
	
	//保存 
	$scope.save=function(){
        $scope.entity.goodsDesc.introduction = editor.html();
		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改
		}else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    // 提示消息
                    alert("保存成功");
                    // 清空数据
                    $scope.entity = {};
                    // 清空富文本编辑器
                    editor.html("");
                } else {
                    alert(response.message);
                }
            }
        );
	};
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    $scope.image_entity = {url:{}, color:{}};
	// 上传图片
	$scope.uploadFile = function () {
		uploadService.uploadFile().success(
			function (response) {
                if (response.success) {
					$scope.image_entity.url = response.message;
                } else {
					alert(response.message);
                }
            }
		)
    }
	// 定义entity
	$scope.entity = {goods:{}, goodsDesc:{itemImages:[], specificationItems:[]}};

    // 将当前上传的图片保存到图片列表
    $scope.add_image_entity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    //移除图片
    $scope.remove_image_entity=function(index){
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

    // 查询一级商品分类列表
    $scope.selectItemCat1List = function () {
		itemCatService.findByParentId(0).success(
			function (response) {
				$scope.itemCat1List = response;
			}
        );
    }

    // 查询二级商品分类列表
    $scope.$watch('entity.goods.category1Id', function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat2List = response;
            }
        );
    });

    // 查询三级商品分类列表
    $scope.$watch('entity.goods.category2Id', function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat3List = response;
            }
        );
    });

    // 读取模板ID
    $scope.$watch('entity.goods.category3Id', function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(
            function (response) {
                $scope.entity.goods.typeTemplateId = response.typeId;
            }
        );
    });

    // 读取模板ID 后，读取品牌列表，扩展属性，规格列表
    $scope.$watch('entity.goods.typeTemplateId', function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(
            function (response) {
                $scope.typeTemplate = response;
                // 品牌列表字符串
                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
				// 扩展属性
				if ($location.search()['id'] == null) {
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
                }
            }
        );
        typeTemplateService.findSpecList(newValue).success(
        	function (response) {
				$scope.specList = response;
            }
		);

    });

    $scope.updateSpecAttribute = function ($event,name, value) {
    	var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,"attributeName", name);
        if (object != null) {
            if ($event.target.checked) {
				// 选上
                object.attributeValue.push(value);
            } else {
            	// 取消勾选
                object.attributeValue.splice(object.attributeValue.indexOf(value), 1);
                // 如果选项都取消了，将此记录移除
                if (object.attributeValue.length == 0) {
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object), 1);
                }
            }
        } else {
			$scope.entity.goodsDesc.specificationItems.push({"attributeName":name, "attributeValue":[value]});
		}
    }

    var tempList = [];

    $scope.createItemList = function () {
    	// 列表初始化
    	$scope.entity.itemList=[{spec:{}, price:0, num:99999, status:0, isDefault:0}];
    	// 用户选择的值
		var items = $scope.entity.goodsDesc.specificationItems;
		for (var i = 0; i < items.length; i++) {
            $scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
		}

		if (tempList.length == 0) {
			tempList = $scope.entity.itemList;
		} else {
			for (var i = 0; i < $scope.entity.itemList.length; i++) {
				var entityRow = $scope.entity.itemList[i];
				var specS = JSON.stringify(entityRow.spec);;
				for (var j = 0; j < tempList.length; j++) {
					var tempS = JSON.stringify(tempList[j].spec);
					if (specS == tempS) {
                        $scope.entity.itemList[i] = tempList[j];
                        tempList.splice(j, 1);
                        break;
					}
				}
			}
            tempList = $scope.entity.itemList;
		}
    }

    addColumn = function (list, columnName,columnValues) {
		var newList = [];
		
		for (var i = 0; i < list.length; i++) {
			var oldRow = list[i];
			for (var j = 0; j < columnValues.length; j++) {
				// 深克隆
				var newRow = JSON.parse(JSON.stringify(oldRow));
				newRow.spec[columnName] =  columnValues[j];
				newList.push(newRow);
			}
		}
		
		return newList;
    }

    $scope.status = ['未审核', '已审核', '审核未通过', '已关闭'];

    // 商品分类列表
    $scope.itemCatList = [];
    $scope.findItemCatList = function () {
		itemCatService.findAll().success(
			function (response) {
				for (var i = 0; i < response.length; i++){
					$scope.itemCatList[response[i].id] = response[i].name;
				}
        	}
        );
    }

    // 判断规格与规格选项是否应该被勾选
    $scope.checkAttributeValue = function (specName, optionName) {
    	var items = $scope.entity.goodsDesc.specificationItems;
    	var object = $scope.searchObjectByKey(items, 'attributeName', specName)

        if (object != null) {
    		if (object.attributeValue.indexOf(optionName) >= 0) {
    			// 如果能查询到规格选项
                return true;
			}else {
    			return false;
			}
        } else{
    		return false;
		}

    }
});	
