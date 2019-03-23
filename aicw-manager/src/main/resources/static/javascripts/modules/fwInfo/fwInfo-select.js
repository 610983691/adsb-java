$(function () {
	$('#txt_search_fwStatus').selectpicker({
        style: 'btn-default',
        size: 5,
        noneResultsText: '无符合检索要求的信息',
        width: '100%'
    });
	$.ajax({
		type : "POST",
		url : '/dict/queryDicList',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		data : '{"dicTypeCode":"DIC_ASSET_STATUS"}',
		success : function(data) {
			var optionstring = "";
			optionstring += "<option  value='' >请选择</option>";
			for ( var i in data) {
				var jsonObj = data[i];
				optionstring += "<option  value='" + jsonObj.dicValue + "' >" + jsonObj.dicName + "</option>";
				// alert("optionstring=="+optionstring);
			}
			fwStatusDicHtml = optionstring;
            $("#txt_search_fwStatus").html(optionstring);
            $('#txt_search_fwStatus').selectpicker('refresh');
		}
	}); 
	$('#tb_fwSelect').bootstrapTable({
        url : "/fwInfo/list",
        method: 'post',                     //请求方式（*）
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: false,                    //是否启用排序
        sortOrder: "asc",                   //排序方式
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber:1,                       //初始化加载第一页，默认第一页
        pageSize: 15,                       //每页的记录行数（*）
        pageList: [10, 15, 30, 50, 100],    //可供选择的每页的行数（*）
//        search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
//        formatSearch: function () {
//            return '类型名称';
//        },
        strictSearch: true,
        showColumns: false,                  //是否显示内容列下拉框。
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
//        height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
        showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        queryParams : function(params) {
			return {
				limit : params.limit,
				offset : params.offset,
				fwNameLike : $("#txt_search_fwName").val(),
				fwTypeLike : $("#txt_search_fwType").val(),
				fwIpLike : $("#txt_search_fwIp").val(),
				fwStatus : $("#txt_search_fwStatus").val()
			}
		},
        columns: [{
        	radio: true
        }, {
			field : 'id',
			title : 'ID',
			visible : false
		}, {
			field : 'fwName',
			title : '防火墙名称'
		}, {
			field : 'fwIp',
			title : '防火墙IP'
		}, {
			field : 'fwTypeDes',
			title : '防火墙类型'
		}, {
			field : 'fwStatusDes',
			title : '防火墙状态'
		}  ]
    });
	// 查询
	$('#btn_query').click(function() {
		$('#tb_fwSelect').bootstrapTable('refresh');
	} );
	/**
	 * 确认按钮
	 */
    $('.btn-warning').click(function(){
    	var choosed = $('#tb_fwSelect').bootstrapTable('getSelections');
    	parent.chooseFw =choosed;
    	closeWindowInIFrame();
    });
    
    /**
     * 取消按钮
     */
    $('.btn-info').click(function(){
    	var choosed = $('#tb_fwSelect').bootstrapTable('getSelections');
    	parent.chooseFw =choosed;
    	closeWindowInIFrame();
    });

})
