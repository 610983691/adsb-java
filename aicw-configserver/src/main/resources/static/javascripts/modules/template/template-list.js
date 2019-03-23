$(function () {
	
    $('#template_records').bootstrapTable({
        url : "/template/list",
        method: 'post',                     //请求方式（*）
        toolbar: '#toolbar',                //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: false,                    //是否启用排序
        sortOrder: "asc",                   //排序方式
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber:1,                       //初始化加载第一页，默认第一页
        pageSize: 15,                       //每页的记录行数（*）
        pageList: [10, 15, 30, 50, 100],    //可供选择的每页的行数（*）
        search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        formatSearch: function () {
            return '模板名称';
        },
        strictSearch: true,
        showColumns: true,                  //是否显示所有的列
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
//        height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
        showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        queryParams: function(params) {
        	return {
        		limit: params.limit,
        		offset: params.offset,
        		templateName: params.search
        	}
        },
        columns: [{
            checkbox: true
        }, {
            field: 'id',
            title: 'ID',
            visible: false
        }, {
            field: 'templateName',
            title: '模板名称'
        }, {
            field: 'configType',
            title: '配置类别'
        }, {
            field: 'templateDesc',
            title: '模板描述'
        }, {
            field: 'createUuid',
            title: '创建人'
        }, {
            field: 'isInner',
            title: '是否内置',
            formatter:function (value, row, index) {
            	if (value == 1) {
            		return '是';
            	} else {
            		return '否';
            	}
            }
        }]
    });

    
    $('#btn_add').click(function(){
    	showDetail('增加模板', '/modules/template/template-add.html', '800px', '450px', function(){
    		$("#template_records").bootstrapTable('refresh');
    	});
    });
    
    $('#btn_edit').click(function(){
    	var sel = $("#template_records").bootstrapTable('getSelections');
    	if (sel.length != 1) {
    		showFailureMsgs('请选择一条记录！');
    		return;
    	}
		if (isAdmin != 1 && sel[0].isInner == 1) {
    		showFailureMsgs('内置模板无法修改！');
			return;
		}
    	selData = sel[0];
    	showDetail('修改模板', '/modules/template/template-edit.html', '800px', '450px', function(){
    		$("#template_records").bootstrapTable('refresh');
    	});
    });
    
    $('#btn_delete').click(function(){
    	var sel = $("#template_records").bootstrapTable('getSelections');
    	if (sel.length <= 0) {
    		showFailureMsgs('请至少选择一条记录！');
    		return;
    	}
    	var canDel = true;
    	$.each(sel, function(val) {
    		if (isAdmin != 1) {
    			if (this.isInner == 1) {
    				return canDel = false;
    			}	
    		}
		});
    	if (!canDel) {
			showFailureMsgs('内置模板无法删除！');
			return;
    	}
    	confirmAlert('确定删除？', '确定', '取消', function() {
    		var selJson = JSON.stringify(sel);
        	layer.msg('加载中', {
    			icon : 16,
    			shade : 0.01
    		});
        	$.ajax({
    			type : "POST",
    			url : '/template/delete',
    			cache : false,
    			dataType : 'json',
    			contentType: 'application/json',
    			data : selJson,
    			success : function(data) {
    				layer.closeAll('loading');
    				if (data.success) {
    					showSuccessMsgs(data.msg);
    					$("#template_records").bootstrapTable('refresh');
    				} else {
    					showFailureMsgs(data.msg);
    				}
    			}
    		});
    	});
    });
});

var selData = null;