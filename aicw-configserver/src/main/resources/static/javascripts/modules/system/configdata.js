$(function() {

	/**
	 * 上传文本框
	 */
	var file_input = $('#dbFile');
	
	/**
	 * 初始化上传控件的样式
	 */
	file_input.fileinput({
		language : 'zh', // 设置语言
		uploadUrl : '/configdata/upload', // 上传的地址
		allowedFileExtensions : [ 'db' ],// 接收的文件后缀
		// uploadExtraData:{"id": 1, "fileName":'123.mp3'},
		uploadAsync : true, // 默认异步上传
		showUpload : true, // 是否显示上传按钮
		showRemove : false, // 显示移除按钮
		showPreview : false, // 是否显示预览
		showCaption : true,// 是否显示标题
		browseClass : 'btn btn-default', // 按钮样式
		dropZoneEnabled : false,// 是否显示拖拽区域
		// minImageWidth: 50, //图片的最小宽度
		// minImageHeight: 50,//图片的最小高度
		// maxImageWidth: 1000,//图片的最大宽度
		// maxImageHeight: 1000,//图片的最大高度
		// maxFileSize:0,//单位为kb，如果为0表示不限制文件大小
		// minFileCount: 0,
		maxFileCount : 1, // 表示允许同时上传的最大文件个数
		enctype : 'multipart/form-data',
		validateInitialCount : true,
//		previewFileIcon : '<iclass='glyphicon glyphicon-king'></i>',
		msgFilesTooMany : '选择上传的文件数量({n}) 超过允许的最大数值{m}！',
		msgInvalidFileExtension : '不正确的类型 "{name}"',
		msgValidationError: '不正确的文件类型'
	});
	
	/**
	 * 文件选择后的回调
	 */
	file_input.on("filebatchselected", function(event, files) {
		if (files.length == 0) {
			showFailureMsgs('不正确的文件类型，请重新选择！');
		}
	});
	
	/**
	 * 文件上传成功后的回调
	 */
	file_input.on("fileuploaded", function (event, data, previewId, index){
		//清空已有的下拉框数据
		$('#productId option').remove();
		$('#moduleId option').remove();
		if (data.response.success) {
			showSuccessMsgs(data.response.msg, function(){
				file_input.fileinput('reset');
				initData();
			});
		} else {
			showFailureMsgs('上传失败');
		}
	});
	
	/**
	 * 导入分类
	 */
	$('.fa-angle-down').click(function(){
		var productId = $('#productId option:selected').val();
		var productName = $('#productId option:selected').text();
		var moduleId = $('#moduleId option:selected').val();
		var moduleName = moduleId ? $('#moduleId option:selected').text() : null;
		var typeId = $('.list-group div[class~="active"] a:first').attr("data-id");
		var typeName = $('.list-group div[class~="active"] a:first').html();
		if (!typeId) {
			showFailureMsgs('请选择要导入的配置类别');
			return;
		}
		var haveData = $('.col-sm-9 span[class*="label-warning"]');
		if (haveData.length == 0) {
			showFailureMsgs('该分类下不存在<span class="label label-danger">new</span>配置信息，无需导入！');
			return;
		}
		var txt = '此操作将<font color="red"><b>产品：' + productName + '</b></font>';
		if (moduleName) {
			txt += '，<font color="red"><b>模块：' + moduleName + '</b></font>';
		}
		txt += '下的<font color="red"><b>类别：' + typeName + '</b></font>中<span class="label label-danger">new</span>配置信息导入至真实数据中，';
		if (!moduleName) {
			txt += '<b>导入产品级配置将覆盖<span class="label label-info">extends</span>该产品的模块级配置信息，<b>是否继续？';
		} else {
			txt += '是否继续?';
		}
		confirmAlert(txt, '确定', '取消', function() {
			importType();
		});
	});
	
	/**
	 * 导入全部
	 */
	$('.fa-angle-double-down').click(function(){
		var productId = $('#productId option:selected').val();
		var productName = $('#productId option:selected').text();
		var moduleId = $('#moduleId option:selected').val();
		var moduleName = moduleId ? $('#moduleId option:selected').text() : null;
		if (!productId) {
			showFailureMsgs('请选择要导入的产品');
			return;
		}
		$.ajax({
			type : "POST",
			url : '/configdata/checkhaveitems',
			cache : false,
			contentType : 'application/json',
			data : JSON.stringify({
				productId : productId,
				moduleId : moduleId
			}),
			success : function(data) {
				if (data.success) {
					var txt = '此操作将<font color="red"><b>产品：' + productName + '</b></font>';
					if (moduleName) {
						txt += '，<font color="red"><b>模块：' + moduleName + '</b></font>';
					}
					txt += '下所有<span class="label label-danger">new</span>配置信息导入至真实数据中，';
					if (!moduleName) {
						txt += '<b>导入产品级配置将覆盖<span class="label label-info">extends</span>该产品的模块级配置信息<b>，是否继续？';
					} else {
						txt += '是否继续?';
					}
					confirmAlert(txt, '确定', '取消', function() {
						importAll();
					});
				} else {
					showFailureMsgs(data.msg);
				}
			}
		});
	});
	
	/**
	 * 保存按钮事件
	 */
	$('.btn-success').click(function() {
		var typeId = $('.list-group div[class~="active"] a:first').attr("data-id");
		var realTypeId = $('.list-group div[class~="active"] a:first').attr("data-realId");
		var haveData = $('.col-sm-9 span[class*="label-warning"]');
		if (!typeId || haveData.length == 0) {
			showFailureMsgs('不存在新配置项信息，无需保存！');
			return;
		}
		var moduleId = $('#moduleId option:selected').val();
		var txt = '此操作将<span class="label label-danger">new</span>配置信息临时保存，';
		if (!moduleId) {
			txt += '并将继承<span class="label label-info">extends</span>该产品的模块级配置信息临时保存，';
		}
		txt += '导入时将配置值更新至真实数据中，是否继续？';
		confirmAlert(txt, '确定', '取消', function() {
			layer.msg('加载中', {
				icon : 16,
				shade : 0.01
			});
			var trs = $('.table tr');
			var obj = new Object();
			obj.configTypeId = typeId;
			var items = new Array();
			$.each(trs, function() {
				var obj = {};
				obj.id = $(this).find('input[name="id"]').val();
				obj.itemSeq = $(this).find('input[name="itemSeq"]').val();
				obj.isExtends = $(this).find('input[name="isExtends"]').val();
				obj.extendsId = $(this).find('input[name="extendsId"]').val();
				obj.itemValue = $(this).find('input[name="itemValue"]').val();
				obj.itemIsCrypt = $(this).find('input[name="itemIsCrypt"]').val();
				items.push(obj);
			});
			obj.items = items;
			$.ajax({
				type : "POST",
				url : '/configdata/saveitems',
				cache : false,
				dataType : 'json',
				contentType : 'application/json',
				data : JSON.stringify(obj),
				success : function(data) {
					layer.closeAll('loading');
					if (data.success) {
						showSuccessMsgs(data.msg, function(){
							lastTypeId = null;
							buildConfigItemsList(typeId, realTypeId);
						});
					} else {
						showFailureMsgs(data.msg);
					}
				}
			});
		});
	});
	
	/**
	 * 重置按钮事件
	 */
	$('.btn-info').click(function() {
		var typeId = $('.list-group div[class~="active"] a:first').attr("data-id");
		var realTypeId = $('.list-group div[class~="active"] a:first').attr("data-realId");
		if (!typeId) {
			return;
		}
		lastTypeId = null;
		buildConfigItemsList(typeId, realTypeId);
	});
})

/**
 * 加载数据
 * @returns
 */
function initData() {
	/**
	 * 构造产品下拉框
	 */
	$.ajax({
		type : "POST",
		url : '/configdata/listproducts',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		success : function(data) {
			if (data.length > 0) {
				$.each(data, function() {
					$('#productId').append('<option value="' + this.id + '" data-version="' + this.dataVersion + '" data-realId="' + this.realId + '">' + this.productName + '</option>');
				});
				$('#productId option:first').prop('selected', 'selected');//默认加载后选中第一个
				buildModuleSelect(data[0].id, data[0].realId);//根据第一个产品构造模块下拉框数据
				buildConfigTypeList();//默认加载第一个产品的配置类别
			}
		}
	});
}

/**
 * 选择产品联动构造模块下拉框
 * @returns
 */
function changeModuleSelect() {
	var productId = $('#productId option:selected').val();
	var realId = $('#productId option:selected').attr('data-realId');
	$('#moduleId option').remove();
	buildModuleSelect(productId, realId);
	buildConfigTypeList();
}

/**
 * 构造模块下拉框
 * @param productId
 * @param realId
 * @returns
 */
function buildModuleSelect(productId, realId) {
	$.ajax({
		type : "POST",
		url : '/configdata/listmodules',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		data : JSON.stringify({
			productId : productId,
			realId : realId
		}),
		success : function(data) {
			$('#moduleId').append('<option value="" hidden="true">请选择模块...</option>');
			if (data.length > 0) {
				$.each(data, function() {
					$('#moduleId').append('<option value="' + this.id + '" data-version="' + this.dataVersion + '" data-realId="' + this.realId + '">' + this.moduleName + '</option>');
				});
			}
		}
	});
}

/**
 * 构造配置类别列表
 * @returns
 */
function buildConfigTypeList() {
	var productId = $('#productId option:selected').val();
	var moduleId = $('#moduleId option:selected').val();
	var realProductId = $('#productId option:selected').attr('data-realId');
	var realModuleId = $('#moduleId option:selected').attr('data-realId');
	$.ajax({
		type : "POST",
		url : '/configdata/listtype',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		data : JSON.stringify({
			productId : productId,
			moduleId : moduleId,
			realProductId : realProductId,
			realModuleId : realModuleId
		}),
		success : function(data) {
			$('.list-group').empty();
			if (data.length > 0) {
				var typeList = $('.list-group');
				$.each(data, function() {
					var html = '<div class="list-group-item">'+
							    	'<a href="javascript:void(0)" onclick="buildConfigItemsList(' + this.id + ',' + this.realId + ')" data-id="' + this.id + '" data-version="' + this.dataVersion + '" data-realId="' + this.realId + '">' + this.configType + '</a>';
					if (this.dataVersion == 'new') {
						html += 	'<span class="label label-danger pull-down pull-right" title="新增的配置类别">new</span>';
					} else {
						html += 	'<span class="label label-default pull-down pull-right" title="已存在的配置类别">old</span>';
					}
					html +=	   '</div>';
					typeList.append(html);
				});
				$('.list-group div:first').addClass('active');//默认加载后选中第一个
				//配置类别选中样式
				$('.list-group div a').click(function(e) {
					if ($(this).children('i').length <= 0) {
						$(this).parent().addClass('active').siblings().removeClass('active');
					}
				});
			} else {
				$('.list-group').append('<table class="table"><tr style="text-align:center"><td>没有找到匹配的记录</td></tr></table>');
			}
			//构造第一个配置项类型的配置项列表
			buildConfigItemsList();
		}
	});
}


/**
 * 构造配置项列表
 * @returns
 */
var lastTypeId = null;
function buildConfigItemsList(typeId, realTypeId) {
	if (!typeId) { //非点击情况，取第一个类型id
		typeId = $('.list-group div:first a:first').attr("data-id");
		realTypeId = $('.list-group div:first a:first').attr("data-realId");
	}
	if ((typeId == lastTypeId) && typeId) { //防止多次点击加载
		return;
	}
	lastTypeId = typeId;
	$.ajax({
		type : "POST",
		url : '/configdata/listitems',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		data : JSON.stringify({
			configTypeId : typeId ? typeId : 0,
			realConfigTypeId : realTypeId ? realTypeId : 0
		}),
		success : function(data) {
			$(".table").empty();
			if (data.length > 0) {
				addItemToTable(data);
			} else {
				var newRow = '<tr style="text-align:center"><td>没有找到匹配的记录</td></tr>';
				$(".table").append(newRow);
			}
		}
	});
}



/**
 * 动态向配置项表内增加信息
 * @param data
 * @returns
 */
function addItemToTable(data) {
	$.each(data, function() {
		var newRow = '<tr><td>' +
						  '<div class="row">' +
							  '<div class="col-sm-4">' +
							  	'<input type="hidden" name="itemDesc" value="' + this.itemDesc + '"/>' +
							  	'<input type="hidden" name="isExtends" value="' + this.isExtends + '"/>' +
							  	'<input type="hidden" name="extendsId" value="' + this.extendsId + '"/>' +
								'<b>' + this.itemDesc + '</b>';
		if (this.isExtends == 1) {
			newRow +=           '<span class="label label-info pull-down pull-left" title="继承自产品的配置">extends</span>';
		}
			newRow +=		  '</div>' +
							  '<div class="col-sm-8">';
		if (this.dataVersion == 'new') {
			newRow +=			'<span class="label label-danger pull-down pull-right" title="新增的配置">new</span>';
		} else {
			newRow +=			'<span class="label label-default pull-down pull-right" title="已存在的配置">old</span>';
		}
			newRow +=		  '</div>' +
						  '</div>' +
						  '<div  class="row">' +
							'<div class="col-sm-4">' +
								'<input type="hidden" name="itemKey" value="' + this.itemKey + '"/>' +
								'<label class="control-label">' + this.itemKey + '</label>' +
							'</div>' +
							'<div class="col-sm-8">' +
								'<input type="hidden" name="itemIsCrypt" value="' + this.itemIsCrypt + '"/>' +
								'<input type="hidden" name="id" value="' + this.id + '"/>' +
								'<input type="hidden" name="itemSeq" value="' + this.itemSeq + '"/>' +
								'<input type="hidden" name="realId" value="' + this.realId + '"/>';
		if (this.itemIsCrypt == 1) {
			newRow +=			'<input type="password" name="itemValue" class="form-control" value="' + this.itemValue + '" ' + (this.dataVersion == 'new' ? '' : 'readonly="readonly"') + '/>';
		} else {
			newRow +=			'<input type="text" name="itemValue" class="form-control" value="' + this.itemValue + '" ' + (this.dataVersion == 'new' ? '' : 'readonly="readonly"') + '/>';
		}
			newRow +=		'</div>' +
						  '</div>'
					 '</tr></td>';
		$(".table").append(newRow);
	});
}


/**
 * 导入分类
 * @returns
 */
function importType() {
	layer.msg('加载中', {
		icon : 16,
		shade : 0.01
	});
	var obj = new Object();
	obj.productId = $('#productId option:selected').val();
	obj.realProductId = $('#productId option:selected').attr('data-realId');
	obj.moduleId = $('#moduleId option:selected').val();
	obj.realModuleId = $('#moduleId option:selected').attr('data-realId');
	obj.typeId = $('.list-group div[class~="active"] a:first').attr("data-id");
	obj.realTypeId = $('.list-group div[class~="active"] a:first').attr("data-realId");
	$.ajax({
		type : "POST",
		url : '/configdata/importtype',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		data : JSON.stringify(obj),
		success : function(data) {
			layer.closeAll('loading');
			if (data.success) {
				showSuccessMsgs(data.msg, function(){
					$('#productId option').remove();
					$('#moduleId option').remove();
					initData();
				});
			} else {
				showFailureMsgs(data.msg);
			}
		}
	});
}


/**
 * 导入全部
 * @returns
 */
function importAll() {
	layer.msg('加载中', {
		icon : 16,
		shade : 0.01
	});
	var obj = new Object();
	obj.productId = $('#productId option:selected').val();
	obj.realProductId = $('#productId option:selected').attr('data-realId');
	obj.moduleId = $('#moduleId option:selected').val();
	obj.realModuleId = $('#moduleId option:selected').attr('data-realId');
	$.ajax({
		type : "POST",
		url : '/configdata/importall',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		data : JSON.stringify(obj),
		success : function(data) {
			layer.closeAll('loading');
			if (data.success) {
				showSuccessMsgs(data.msg, function() {
					$('#productId option').remove();
					$('#moduleId option').remove();
					initData();
				});
			} else {
				showFailureMsgs(data.msg);
			}
		}
	});
}

