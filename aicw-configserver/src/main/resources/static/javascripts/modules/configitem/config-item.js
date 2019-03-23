$(function() {
	
	/**
	 * 构造产品下拉框
	 */
	$.ajax({
		type : "POST",
		url : '/product/listproducts',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		success : function(data) {
			if (data.length > 0) {
				$.each(data, function() {
					$('#productId').append('<option value="' + this.id + '">' + this.productName + '</option>');
				});
				$('#productId option:first').prop('selected', 'selected');//默认加载后选中第一个
				buildModuleSelect(data[0].id);//根据第一个产品构造模块下拉框数据
				buildConfigTypeList();//默认加载第一个产品的配置类别
			}
		}
	});
	
	/**
	 * 增加分类及配置项
	 */
	$('.fa-tasks').click(function(){
		selRecord = {};
		selRecord.productName = $('#productId option:selected').text();
		selRecord.moduleName = $('#moduleId option:selected').val() ? $('#moduleId option:selected').text() : '';
		selRecord.productId = $('#productId option:selected').val();
		selRecord.moduleId = $('#moduleId option:selected').val() ? $('#moduleId option:selected').val() : null;
		showDetail('增加类别及配置项', '/modules/configitem/config-add-all.html', '800px', '450px', function(){
			buildConfigTypeList();
    	});
	});
	
	/**
	 * 增加配置项
	 */
	$('.fa-qrcode').click(function(){
		var typeId = $('.list-group div[class~="active"] a:first').attr("data-id");
		if (!typeId) {
			showFailureMsgs('请选择配置类别！');
			return;
		}
		selRecord = {};
		selRecord.productName = $('#productId option:selected').text();
		selRecord.moduleName = $('#moduleId option:selected').val() ? $('#moduleId option:selected').text() : '';
		selRecord.configType = $('.list-group div[class~="active"] a:first').html();
		showDetail('增加配置项', '/modules/configitem/config-add-item.html', '800px', '450px', function(){
			addItemToTable(addItemsRecord);
    	});
	});
	
	/**
	 * 重写配置
	 */
	$('.fa-refresh').click(function(){
		var productName = $('#productId option:selected').text();
		var moduleName = $('#moduleId option:selected').val() ? $('#moduleId option:selected').text() : null;
		var txt = '此操作将<font color="red"><b>产品：' +　productName + '</b></font>';
		if (moduleName) {
			txt += '，<font color="red"><b>模块：' + moduleName + '</b></font>';
		}
		txt += '的配置信息重新写入<b>zookeeper</b>内，是否继续？';
		confirmAlert(txt, '确定', '取消', function() {
			layer.msg('加载中', {
				icon : 16,
				shade : 0.01,
				time : 10000
			});
			$.ajax({
				type : "POST",
				url : '/configitem/overwrite',
				cache : false,
				dataType : 'json',
				contentType : 'application/json',
				data : JSON.stringify({
					productId : $('#productId option:selected').val(),
					moduleId :$('#moduleId option:selected').val()
				}),
				success : function(data) {
					layer.closeAll('loading');
					if (data.success) {
						showSuccessMsgs(data.msg);
					} else {
						showFailureMsgs(data.msg);
					}
				}
			});
		});
	});
	
	/**
	 * 重置按钮绑定点击事件
	 */
	$('.fa-undo').click(function(){
		var typeId = $('.list-group div[class~="active"] a:first').attr("data-id");
		if (!typeId) {
			return;
		}
		lastTypeId = null;
		buildConfigItemsList(typeId);
	});
	
	/**
	 * 保存按钮绑定点击事件
	 */
	$('.fa-save').click(function(){
		var moduleId = $('#moduleId option:selected').val();
		var haveData = $('.col-sm-9 input[type="hidden"]');
		if (haveData.length == 0) {
			showFailureMsgs('配置项信息不存在，无需修改！');
			return;
		}
		if (!moduleId) {
			confirmAlert('修改产品级配置，将同步修改继承<span class="label label-info">extends</span>该产品的模块级配置信息，确定修改？', '确定', '取消', function() {
				saveConfigItems();
			});
		} else {
			saveConfigItems();
		}
	});
	
})


var selRecord = {};

/**
 * 增加的配置项内容
 */
var addItemsRecord = new Array();

/**
 * 选择产品联动构造模块下拉框
 * @returns
 */
function changeModuleSelect() {
	var productId = $('#productId option:selected').val();
	$('#moduleId option').remove();
	buildModuleSelect(productId);
	buildConfigTypeList();
}

/**
 * 构造模块下拉框
 * @param productId
 * @returns
 */
function buildModuleSelect(productId) {
	$.ajax({
		type : "POST",
		url : '/product/listmodules',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		data : JSON.stringify({productId: productId}),
		success : function(data) {
			$('#moduleId').append('<option value="" hidden="true">请选择模块...</option>');
			if (data.length > 0) {
				$.each(data, function() {
					$('#moduleId').append('<option value="' + this.id + '">' + this.moduleName + '</option>');
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
	$.ajax({
		type : "POST",
		url : '/configitem/listtype',
		cache : false,
		dataType : 'json',
		contentType : 'application/json',
		data : JSON.stringify({
			productId : productId,
			moduleId : moduleId
		}),
		success : function(data) {
			$('.list-group').empty();
			if (data.length > 0) {
				var typeList = $('.list-group');
				$.each(data, function() {
					var html = '<div class="list-group-item">'+
							    	'<a href="javascript:void(0)" onclick="buildConfigItemsList(' + this.id + ')" data-id="' + this.id + '">' + this.configType + '</a>'+
							    	'<a href="javascript:void(0)" onclick="deleteConfigType(' + this.id + ')"><i class="fa fa-minus-square-o pull-right" style="font-size: 25px" title="删除此配置类别"></i></a>'+
							   '</div>';
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
function buildConfigItemsList(typeId) {
	if (!typeId) { //非点击情况，取第一个类型id
		typeId = $('.list-group div:first a:first').attr("data-id");
	}
	if ((typeId == lastTypeId) && typeId) { //防止多次点击加载
		return;
	}
	lastTypeId = typeId;
	$.ajax({
		type : "POST",
		url : '/configitem/listitems',
		cache : false,
		contentType : 'application/json',
		data : JSON.stringify({
			configTypeId : typeId ? typeId : 0
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
								'<b>' + this.itemDesc + '</b>';
		if (this.isExtends == 1) {
			newRow +=           '<span class="label label-info pull-down pull-left" title="继承自产品的配置">extends</span>';
		}
		if (this.dataVersion == 'unsyn') {
			newRow +=           '<span class="label label-warning pull-down pull-left" title="与zookeeper数据不一致">unsyn</span>';
		}
			newRow +=		  '</div>' +
							  '<div class="col-sm-8">' +
								'<a href="javascript:void(0)" onclick="deleteConfigRow(this)"><i class="fa fa-minus-square-o pull-right" style="font-size: 25px" title="移除此配置项"></i></a>' +
							  '</div>' +
						  '</div>' +
						  '<div  class="row">' +
							'<div class="col-sm-4">' +
								'<input type="hidden" name="itemKey" value="' + this.itemKey + '"/>' +
								'<label class="control-label">' + this.itemKey + '</label>' +
							'</div>' +
							'<div class="col-sm-8">' +
								'<input type="hidden" name="itemIsCrypt" value="' + this.itemIsCrypt + '"/>' +
								'<input type="hidden" name="id" value="' + this.id + '"/>';
		if (this.itemIsCrypt == 1) {
			newRow +=			'<input id="' + this.id + '_itemValue" type="password" name="itemValue" class="form-control"/>';
		} else {
			newRow +=			'<input id="' + this.id + '_itemValue" type="text" name="itemValue" class="form-control"/>';
		}
			newRow +=		'</div>' +
						  '</div>'
					 '</tr></td>';
		$(".table").append(newRow);
		//防止itemValue内存在特殊字符，使用jQuery为文本框赋值
		$("#" + this.id + "_itemValue").val(this.itemValue);
	});
}


/**
 * 删除配置项分类及其配置项信息
 * @param typeId
 * @returns
 */
function deleteConfigType(typeId){
	confirmAlert('确定删除该配置分类及其配置项信息？', '确定', '取消', function() {
		layer.msg('加载中', {
			icon : 16,
			shade : 0.01
		});
		$.ajax({
			type : "POST",
			url : '/configitem/delete',
			cache : false,
			contentType : 'application/json',
			data : JSON.stringify({
				configTypeId : typeId
			}),
			success : function(data) {
				layer.closeAll('loading');
				if (data.success) {
					showSuccessMsgs(data.msg, function(){
						lastTypeId = null;
						buildConfigTypeList();
					});
				} else {
					showFailureMsgs(data.msg);
				}
			}
		});
	});
}


/**
 * 动态删除某行配置
 * @param click_a
 * @returns
 */
function deleteConfigRow(click_a) {
	if ($('.table tr').length <= 1) {
		showFailureMsgs('每种配置类型至少存在一条配置项信息！');
		return;
	}
	confirmAlert('确定移除此配置项？', '确定', '取消', function() {
		$(click_a).parents("tr").remove();
		showSuccessMsgs('已移除，请保存！');
	});
}

/**
 * 保存配置信息
 * @returns
 */
function saveConfigItems() {
	layer.msg('加载中', {
		time : 20000,
		icon : 16,
		shade : 0.01
	});
	var typeId = $('.list-group div[class~="active"] a:first').attr("data-id");
	var trs = $('.table tr');
	var items = new Array();
	var isValid = true;
	$.each(trs, function() {
		var itemConfig =  $(this).find('input');
		var obj = {};
		obj.id = $(this).find('input[name="id"]').val();
		obj.configMainId = typeId;
		obj.isExtends = $(this).find('input[name="isExtends"]').val();
		obj.itemDesc = $(this).find('input[name="itemDesc"]').val();
		obj.itemKey = $(this).find('input[name="itemKey"]').val();
		if (checkIsContains(items, obj.itemKey)) {
			showFailureMsgs('存在相同的配置项KEY，请检查！');
			isValid = false;
			return false;
		}
		obj.itemValue = $(this).find('input[name="itemValue"]').val();
		obj.itemIsCrypt = $(this).find('input[name="itemIsCrypt"]').val();
		items.push(obj);
	});
	if (isValid) {
		var data = {};
		data.configMainId = typeId;
		data.records = items;
		$.ajax({
			type : "POST",
			url : '/configitem/saveitems',
			cache : false,
			dataType : 'json',
			contentType : 'application/json',
			data : JSON.stringify(data),
			success : function(data) {
				layer.closeAll('loading');
				if (data.success) {
					showSuccessMsgs(data.msg, function(){
						lastTypeId = null;
						buildConfigItemsList(typeId);
					});
				} else {
					showFailureMsgs(data.msg);
				}
			}
		});
	}
}


/**
 * 判断数组内对象是否已存在itemKey
 * @param arr
 * @param itemKey
 * @returns
 */
function checkIsContains(arr, itemKey) {
	for (var i = 0; i < arr.length; i++) {
		if (arr[i].itemKey == itemKey) {
			return true;
		}
	}
}
