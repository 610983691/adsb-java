$(function () {
	
	/**
	 * 产品列表查询
	 */
    $('#tb_products').bootstrapTable({
        url : "/product/listproducts",
        method: 'post',                     //请求方式（*）
        toolbar: '#toolbar',                //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false,                   //是否显示分页（*）
        sortable: false,                    //是否启用排序
        sortOrder: "asc",                   //排序方式
        search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: true,
        showColumns: false,                  //是否显示所有的列
        showRefresh: false,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        singleSelect:true,					//是否单选
        uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
        showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        columns: [{
            checkbox: true
        }, {
            field: 'id',
            title: 'ID',
            visible: false
        }, {
            field: 'productName',
            title: '产品名'
        }, {
            field: 'productMark',
            title: '产品标识'
        }],
		onLoadSuccess: function(data) { // 加载成功时执行
			if (data.length > 0) {
				var productId = data[0].id;
				$("#tb_products").bootstrapTable('check', 0);
				initModulesTable(productId);
			} else {
				initModulesTable(0);
			}
		},
		onClickRow: function(row) { //点击某行
			$("#tb_modules").bootstrapTable('refresh', {
				query:{
					productId: row.id
				}
			});
		}
    });
    
    /**
     * 增加产品
     */
    $('#btn_add_product').click(function() {
    	selData = null;
    	showDetail('增加产品', '/modules/product/product-operate.html', '800px', '350px', function(){
    		$("#tb_products").bootstrapTable('refresh');
    	});
    });
    
    /**
     * 修改产品
     */
    $('#btn_edit_product').click(function() {
    	var sel = $("#tb_products").bootstrapTable('getSelections');
    	if (sel.length != 1) {
    		showFailureMsgs('请选择一条记录！');
    		return;
    	}
    	selData = sel[0];
    	showDetail('修改产品', '/modules/product/product-operate.html', '800px', '350px', function(){
    		$("#tb_products").bootstrapTable('refresh');
    	});
    });
    
    /**
     * 删除产品
     */
    $('#btn_delete_product').click(function(){
    	var sel = $("#tb_products").bootstrapTable('getSelections');
    	if (sel.length <= 0) {
    		showFailureMsgs('请选择一条记录！');
    		return;
    	}
    	confirmAlert('此操作将产品及其模块下所有配置信息一并删除，是否继续？', '确定', '取消', function() {
        	layer.msg('加载中', {
    			icon : 16,
    			shade : 0.01
    		});
        	$.ajax({
    			type : "POST",
    			url : '/product/delete/product',
    			cache : false,
    			dataType : 'json',
    			contentType: 'application/json',
    			data : JSON.stringify(sel),
    			success : function(data) {
    				layer.closeAll('loading');
    				if (data.success) {
    					showSuccessMsgs(data.msg);
    					$("#tb_products").bootstrapTable('refresh');
    				} else {
    					showFailureMsgs(data.msg);
    				}
    			}
    		});
    	});
    });
    
    /**
     * 增加模块
     */
    $('#btn_add_module').click(function() {
    	selModuleData = null;
    	var sel = $("#tb_products").bootstrapTable('getSelections');
    	if (sel.length != 1) {
    		showFailureMsgs('请选择模块所属产品！');
    		return;
    	}
    	selData = sel[0];
    	showDetail('增加模块', '/modules/product/module-operate.html', '800px', '350px', function(){
    		$("#tb_modules").bootstrapTable('refresh', {
    			query : {
    				productId: selData.id
    			}
    		});
    	});
    });
    
    
    /**
     * 修改模块
     */
    $('#btn_edit_module').click(function() {
    	var sel = $("#tb_modules").bootstrapTable('getSelections');
    	if (sel.length != 1) {
    		showFailureMsgs('请选择一条记录！');
    		return;
    	}
    	selModuleData = sel[0];
    	showDetail('修改模块', '/modules/product/module-operate.html', '800px', '350px', function(){
    		$("#tb_modules").bootstrapTable('refresh', {
    			query : {
    				productId: selModuleData.productId
    			}
    		});
    	});
    });
    
    
    /**
     * 删除模块
     */
    $('#btn_delete_module').click(function(){
    	var sel = $("#tb_modules").bootstrapTable('getSelections');
    	if (sel.length <= 0) {
    		showFailureMsgs('请至少选择一条记录！');
    		return;
    	}
    	var productId = sel[0].productId;
    	confirmAlert('此操作将该模块下所有配置信息一并删除，是否继续？', '确定', '取消', function() {
        	layer.msg('加载中', {
    			icon : 16,
    			shade : 0.01
    		});
        	$.ajax({
    			type : "POST",
    			url : '/product/delete/module',
    			cache : false,
    			dataType : 'json',
    			contentType: 'application/json',
    			data : JSON.stringify(sel),
    			success : function(data) {
    				layer.closeAll('loading');
    				if (data.success) {
    					showSuccessMsgs(data.msg);
    					$("#tb_modules").bootstrapTable('refresh', {
    		    			query : {
    		    				productId: productId
    		    			}
    		    		});
    				} else {
    					showFailureMsgs(data.msg);
    				}
    			}
    		});
    	});
    });
    
});

/**
 * 修改时选择的产品数据记录
 */
var selData = null;

/**
 * 修改时选择的模块数据记录
 */
var selModuleData = null;

/**
 * 加载产品的模块列表
 * @param productId
 * @returns
 */
function initModulesTable(productId) {
	/**
	 * 模块列表查询
	 */
    $('#tb_modules').bootstrapTable({
        url : "/product/listmodules",
        method: 'post',                     //请求方式（*）
        toolbar: '#toolbar2',                //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        sortable: false,                    //是否启用排序
        sortOrder: "asc",                   //排序方式
        search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: true,
        showColumns: false,                  //是否显示所有的列
        showRefresh: false,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
        showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        queryParams: function(params) {
        	return {
        		productId: productId
        	}
        },
        columns: [{
            checkbox: true
        }, {
            field: 'id',
            title: 'ID',
            visible: false
        }, {
            field: 'moduleName',
            title: '模块名'
        }, {
            field: 'moduleMark',
            title: '模块标识'
        }, {
            field: 'isExtends',
            title: '是否继承产品配置',
            formatter:function (value,row,index) {
            	if (value == 1) {
            		return '是';
            	} else {
            		return '否';
            	}
            }
        }]
    });
}
