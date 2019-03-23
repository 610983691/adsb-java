$(function() {
	$('#txt_search_fwStatus').selectpicker({
        style: 'btn-default',
        size: 5,
        width : 80,
        noneResultsText: '无符合检索要求的信息'
//        width: '100%'
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
	
	$.ajax({
		type : "POST",
		url : '/dict/queryDicList',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		data : '{"dicTypeCode":"DIC_FW_TYPE"}',
		success : function(data) {
			var optionstring = "";
			optionstring += "<option  value='' >请选择</option>";
			for ( var i in data) {
				var jsonObj = data[i];
				optionstring += "<option  value='" + jsonObj.dicValue + "' >" + jsonObj.dicName + "</option>";
				// alert("optionstring=="+optionstring);
			}
			fwTypeDicHtml = optionstring;

		}
	}); 
	 
	$.ajax({
		type : "POST",
		url : '/dict/queryDicList',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		data : '{"dicTypeCode":"DIC_IP_TYPE"}',
		success : function(data) {
			var optionstring = "";
			optionstring += "<option  value='' >请选择</option>";
			for ( var i in data) {
				var jsonObj = data[i];
				optionstring += "<option  value='" + jsonObj.dicValue + "' >" + jsonObj.dicName + "</option>";
				// alert("optionstring=="+optionstring);
			}
			ipTypeDicHtml = optionstring;

		}
	}); 
	$.ajax({
		type : "POST",
		url : '/dict/queryDicList',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		data : '{"dicTypeCode":"DIC_CON_PROTOCOL"}',
		success : function(data) {
			var optionstring = "";
			optionstring += "<option  value='' >请选择</option>";
			for ( var i in data) {
				var jsonObj = data[i];
				optionstring += "<option  value='" + jsonObj.dicValue + "' >" + jsonObj.dicName + "</option>";
				// alert("optionstring=="+optionstring);
			}
			fwProtocolDicHtml = optionstring;
		}
	}); 
	$.ajax({
		type : "POST",
		url : '/dict/queryDicList',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		data : '{"dicTypeCode":"DIC_ASSET_ENCODE"}',
		success : function(data) {
			var optionstring = "";
			optionstring += "<option  value='' >请选择</option>";
			for ( var i in data) {
				var jsonObj = data[i];
				optionstring += "<option  value='" + jsonObj.dicValue + "' >" + jsonObj.dicName + "</option>";
				// alert("optionstring=="+optionstring);
			}
			encodeDicHtml = optionstring;
		}
	});
	 
	/**
	 * 列表查询
	 */
	$('#tb_fwInfo').bootstrapTable({
		url : "/fwInfo/list",
		method : 'post', // 请求方式（*）
		toolbar : '#toolbar', // 工具按钮用哪个容器
		striped : true, // 是否显示行间隔色
		cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		pagination : true, // 是否显示分页（*）
		sortable : false, // 是否启用排序
		sortOrder : "asc", // 排序方式
		sidePagination : "server", // 分页方式：client客户端分页，server服务端分页（*）
//		contentType: "application/x-www-form-urlencoded",
		pageNumber : 1, // 初始化加载第一页，默认第一页
		pageSize : 15, // 每页的记录行数（*）
		pageList : [ 10, 15, 30, 50, 100 ], // 可供选择的每页的行数（*）
		// search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
		// formatSearch: function () {
		// return 'ID';
		// },
		strictSearch : true,
		showColumns : true, // 是否显示所有的列
		showRefresh : true, // 是否显示刷新按钮
		minimumCountColumns : 2, // 最少允许的列数
		clickToSelect : true, // 是否启用点击选中行
		// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		uniqueId : "ID", // 每一行的唯一标识，一般为主键列
		showToggle : true, // 是否显示详细视图和列表视图的切换按钮
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表
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
		columns : [ {
			checkbox : true
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
		$('#tb_fwInfo').bootstrapTable('refresh');
	} );
	$('#btn_add').click(
			function() {
				showDetail('增加防火墙',
						'/modules/fwInfo/fw_add.html',
						'850px', '550px', function() {
							$("#tb_fwInfo").bootstrapTable('refresh');
						});
			});
	$('#btn_edit').click(
			function() {
				var sel = $("#tb_fwInfo").bootstrapTable('getSelections');
				if (sel.length != 1) {
					showFailureMsgs('请选择一条记录！');
					return;
				}
				if (sel[0].isAdmin == 1) {
					showFailureMsgs('超级管理员无法修改！');
					return;
				}
				selData = sel[0];
				showDetail('修改防火墙',
						'/modules/fwInfo/fw_edit.html',
						'850px', '550px', function() {
							$("#tb_fwInfo").bootstrapTable('refresh');
						});
			});
	$('#btn_syn').click(function() {
		confirmAlert('同步会修改信息，确定同步？', '确定', '取消', function() {
//			var selJson = JSON.stringify(sel);
//			layer.msg('加载中', {
//				icon : 16,
//				shade : 0.01
//			});
			var mask = layer.load(1, {shade: [0.8, '#393D49']});

			$.ajax({
				type : "POST",
				url : '/fwInfo/syn',
				cache : false,
				dataType : 'json',
				contentType : 'application/json',
//				data : selJson,
				data : '{"assetType":"FW"}',
				success : function(data) {
//					layer.closeAll('loading');
					layer.close(mask);
					if (data.success) {
						showSuccessMsgs(data.msg);
						$("#tb_fwInfo").bootstrapTable('refresh');
					} else {
						showFailureMsgs(data.msg);
					}
				}
			});
			return false;
		});
	});
	$('#btn_import').click(
			function() {
				showDetail('导入防火墙信息',
						'/modules/fwInfo/fw_import.html',
						'350px', '250px', function() {
							$("#tb_fwInfo").bootstrapTable('refresh');
						},true);
			});


	$('#btn_downDemo').click(function() {
		exportTemplate("/fwInfo/downDemo", "xls");
	});
	
	$('#btn_delete').click(function() {
		var sel = $("#tb_fwInfo").bootstrapTable('getSelections');
		if (sel.length <= 0) {
			showFailureMsgs('请至少选择一条记录！');
			return;
		}
		var canDel = true;
		var isHaveDel = false;
		var delFwName = null;
		$.each(sel, function(val) {
			var itemObj = sel[val];
			var fwStatus = itemObj.fwStatus;
			if("DIC_RES_STATUS_DEL" == fwStatus){
				var fwName = itemObj.fwName;
				if(delFwName != null){
					delFwName = delFwName + ',';
					delFwName = delFwName + fwName;
				}else{
					delFwName = fwName;
				}
				
				return isHaveDel = true;
			}
			 
			if (this.isAdmin == 1) {
				return canDel = false;
			}
		});
		if(isHaveDel){
			showFailureMsgs(delFwName+'已经是删除状态');
			return;
		}
		if (!canDel) {
			showFailureMsgs('超级管理员无法删除！');
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
				url : '/fwInfo/delete',
				cache : false,
				dataType : 'json',
				contentType : 'application/json',
				data : selJson,
				success : function(data) {
					layer.closeAll('loading');
					if (data.success) {
						showSuccessMsgs(data.msg);
						$("#tb_fwInfo").bootstrapTable('refresh');
					} else {
						showFailureMsgs(data.msg);
					}
				}
			});
		});
	});
	
	
//	/**
//	 * 上传文本框
//	 */
//	var file_input = $('#exlFile');
//	
//	/**
//	 * 初始化上传控件的样式
//	 */
//	file_input.fileinput({
//		language : 'zh', // 设置语言
//		uploadUrl : '/fwInfo/upload', // 上传的地址
//		allowedFileExtensions: ["xls", "xlsx"], //接收的文件后缀
//		// uploadExtraData:{"id": 1, "fileName":'123.mp3'},
//		uploadAsync : true, // 默认异步上传
//		showUpload : true, // 是否显示上传按钮
//		showRemove : false, // 显示移除按钮
//		showPreview : false, // 是否显示预览
//		showCaption : true,// 是否显示标题
//		browseClass : 'btn btn-default', // 按钮样式
//		dropZoneEnabled : false,// 是否显示拖拽区域
//		// minImageWidth: 50, //图片的最小宽度
//		// minImageHeight: 50,//图片的最小高度
//		// maxImageWidth: 1000,//图片的最大宽度
//		// maxImageHeight: 1000,//图片的最大高度
//		// maxFileSize:0,//单位为kb，如果为0表示不限制文件大小
//		// minFileCount: 0,
//		maxFileCount : 1, // 表示允许同时上传的最大文件个数
//		enctype : 'multipart/form-data',
//		validateInitialCount : true,
////		previewFileIcon : '<iclass='glyphicon glyphicon-king'></i>',
//		msgFilesTooMany : '选择上传的文件数量({n}) 超过允许的最大数值{m}！',
//		msgInvalidFileExtension : '不正确的类型 "{name}"',
//		msgValidationError: '不正确的文件类型'
//	});
//	
//	/**
//	 * 文件选择后的回调
//	 */
//	file_input.on("filebatchselected", function(event, files) {
//		if (files.length == 0) {
//			showFailureMsgs('不正确的文件类型，请重新选择！');
//		}
//	});
//	
//	/**
//	 * 文件上传成功后的回调
//	 */
//	file_input.on("fileuploaded", function (event, data, previewId, index){
//		//清空已有的下拉框数据
//		 
//		if (data.response.success) {
//			showSuccessMsgs(data.response.msg, function(){
//				file_input.fileinput('reset');
//				initData();
//			});
//		} else {
//			showFailureMsgs('上传失败');
//		}
//	});
});
function exportTemplate(url, format){
	document.fileExportForm.action = url;
	document.fileExportForm.fileFormat.value = format;
	document.fileExportForm.submit();
}
/**
 * 修改时选择的数据记录
 */
var selData = null;
var fwStatusDicHtml = null;
var fwTypeDicHtml = null;
var encodeDicHtml = null;
var fwProtocolDicHtml = null;
var ipTypeDicHtml = null;
