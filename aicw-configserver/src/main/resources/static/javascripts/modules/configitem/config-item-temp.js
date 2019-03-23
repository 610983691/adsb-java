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
				buildModuleSelect(data[0].id);
			}
		}
	});
	
	/**
	 * 保存按钮
	 */
	$('.btn-success').click(function(){
		var values = $('.form-horizontal').serializeArray();
		var valuesObj = {};
		$.each(values, function() {
			valuesObj[this.name] = this.value;
		});
		layer.msg('加载中', {
			icon : 16,
			shade : 0.01,
			time : 10000
		});
		$.ajax({
			type : "POST",
			url : '/configitem/savetemp',
			cache : false,
			dataType : 'json',
			contentType: 'application/json',
			data : JSON.stringify(valuesObj),
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
	
	/**
	 * 重置按钮
	 */
	$('.btn-info').click(function(){
		$('#pagecontent').load("/modules/configitem/config-item-temp.html");
	});

});

/**
 * 选择产品联动构造模块下拉框
 * @returns
 */
function changeModuleSelect() {
	var productId = $('#productId option:selected').val();
	$('#moduleId option').remove();
	buildModuleSelect(productId);
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
			if (data.length > 0) {
				$('#moduleId').append('<option value=""></option>');
				$.each(data, function() {
					$('#moduleId').append('<option value="' + this.id + '">' + this.moduleName + '</option>');
				});
			}
		}
	});
}