$(function() {
	
	/**
	 * 表单验证
	 */
	$('.form-horizontal').bootstrapValidator({
		live: 'enabled',
		feedbackIcons: {
	        valid: 'glyphicon glyphicon-ok',
	        invalid: 'glyphicon glyphicon-remove',
	        validating: 'glyphicon glyphicon-refresh'
	    },
	    message: '该字段不能为空',
	    submitButtons: '.btn-success',
	    fields: {
	    	configType: {
                validators: {
                    notEmpty: {
                        message: '配置分类不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 64,
                        message: '配置分类长度必须在1到64位之间'
                    }
                }
            },
	    	itemDesc1: {
                validators: {
                    notEmpty: {
                        message: '配置项说明不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 64,
                        message: '配置项说明长度必须在1到64位之间'
                    }
                }
            },
            itemKey1: {
                validators: {
                    notEmpty: {
                        message: '配置项KEY不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 64,
                        message: '配置项KEY长度必须在1到64位之间'
                    },
            		regexp: {
            			regexp: /^[a-zA-Z0-9.-]+$/,
            			message: '配置项KEY只能包含数字、大写、小写、英文句号'
            		}
                }
            }
	    }
	});
	
	//选择的产品模块数据
	if (parent.selRecord) {
		$('.form-group input[name="productId"]').val(parent.selRecord.productId);
		$('.form-group input[name="moduleId"]').val(parent.selRecord.moduleId);
		$('.form-group input[name="productName"]').val(parent.selRecord.productName);
		$('.form-group input[name="moduleName"]').val(parent.selRecord.moduleName);
	}
	
	/**
	 * 取消按钮
	 */
	$('.btn-info').click(function() {
		closeWindowInIFrame();
	});
	
	/**
	 * 保存按钮
	 */
	$('.btn-success').click(function() {
		var bootstrapValidator = $('.form-horizontal').data('bootstrapValidator');
    	bootstrapValidator.validate();
		if (!bootstrapValidator.isValid()) {
			return;
		}
		var trs = $('.table tr');
		var isValid = true;
		var records = new Array();
		$.each(trs, function() {
			var itemConfig =  $(this).find('input');
			var record = {};
			record.itemDesc = $(this).find('input[name^="itemDesc"]').val();
			record.itemKey = $(this).find('input[name^="itemKey"]').val();
			if (!record.itemDesc || !record.itemKey || checkIsContains(records, 'itemKey', record.itemKey)
					|| checkIsContains(records, 'itemDesc', record.itemDesc)) {
				showFailureMsgs('信息填写不完整或存在相同的配置项KEY，请检查！');
				isValid = false;
				return false;
			}
			record.itemValue = $(this).find('input[name^="itemValue"]').val();
			record.itemIsCrypt = $(this).find('input[type="radio"]:checked').val();
			records.push(record);
		});
		var data = new Object();
		data.productId = $('.form-group input[name="productId"]').val();
		data.moduleId = $('.form-group input[name="moduleId"]').val();
		data.configType = $('.form-group input[name="configType"]').val();
		data.items = records;
		if (isValid) {
			layer.msg('加载中', {
				icon : 16,
				shade : 0.01
			});
			$.ajax({
				type : "POST",
				url : '/configitem/additems',
				cache : false,
				dataType : 'json',
				contentType : 'application/json',
				data : JSON.stringify(data),
				success : function(data) {
					layer.closeAll('loading');
					if (data.success) {
						showSuccessMsgs(data.msg, function(){
							closeWindowInIFrame();
						});
					} else {
						showFailureMsgs(data.msg);
					}
				}
			});
		}
	});
})

/**
 * 不同方式变换div内容
 * @param radio
 * @returns
 */
function changeAddType(radio) {
	var type = $(radio).val();
	var label = $('#changeLabel');
	var div = $('#changeDiv');
	var addDiv = $('#addDiv');
	if (type == '1') { //新增
		resetItemTable();
		label.html('填写配置类别：');
		div.empty();
		div.append('<input class="form-control" name="configType" type="text" placeholder="" onblur="checkConfigType(this)"/>');
		addDiv.empty();
	} else if (type == '2') { //模板
		resetItemTable();
		label.html('选择配置模板：');
		div.empty();
		var addHtml = '<label class="control-label col-sm-2" style="padding-left: 0px;">填写配置类别：</label>' +
					  '<div class="col-sm-4">' +
						'<input class="form-control" name="configType" type="text" placeholder="" onblur="checkConfigType(this)"/>' +
					  '</div>';
		addDiv.append(addHtml);
		$.ajax({
			type : "POST",
			url : '/template/listcombo',
			cache : false,
			dataType : 'json',
			contentType : 'application/json',
			success : function(data) {
				var typeSel = '<select id="templateId" class="form-control" onchange="fillDataByTemplate()">';
				typeSel += '<option value="" hidden="true">请选择模板...</option>';
				if (data.length > 0) {
					$.each(data, function() {
						typeSel += '<option value="' + this.id + '" data-configtype="' + this.configType + '">' + this.templateName + '</option>';
					});
				}
				typeSel += '</select>';
				div.append(typeSel);
			}
		});
	}
	$('.form-horizontal').bootstrapValidator('addField', 'configType', {
        validators: {
            notEmpty: {
                message: '配置分类不能为空'
            },
            stringLength: {
                min: 1,
                max: 64,
                message: '配置分类长度必须在1到64位之间'
            }
        }
    });
}

/**
 * 检验输入的配置项类型是否存在
 * @param configType_input
 * @returns
 */
function checkConfigType(configType_input) {
	var configType = $(configType_input).val();
	if (configType) {
		var productId = $('.form-group input[name="productId"]').val();
		var moduleId = $('.form-group input[name="moduleId"]').val();
		$.ajax({
			type : "POST",
			url : '/configitem/checkconfigtype',
			cache : false,
			dataType : 'json',
			contentType : 'application/json',
			data : JSON.stringify({
				productId : productId,
				moduleId : moduleId,
				configType : configType
			}),
			success : function(data) {
				if (!data.success) {
					showFailureMsgs(data.msg, function(){
						$(configType_input).select();
					});
				}
			}
		});
	}
}

/**
 * 根据选择的模板动态填充数据
 * @param template_combo
 * @returns
 */
function fillDataByTemplate() {
	var templateCombo = $('#templateId option:selected');
	var templateId = templateCombo.val();
	var configType = templateCombo.attr('data-configtype');
	$('#addDiv').find('input[name="configType"]').val(configType);
	checkConfigType($('#addDiv').find('input[name="configType"]'));
	$.ajax({
		type : "POST",
		url : '/template/listitems',
		cache : false,
		dataType : 'json',
		data : {
			templateId : templateId
		},
		success : function(data) {
			$(".table").empty();
			if (data.length > 0) {
				addItemToTable(data);
			} else {
				showFailureMsgs('该模板下无配置项数据！');
			}
		}
	});
}


/**
 * 根据选择是否加密动态改变文本框属性
 * @param radio
 * @returns
 */
function isCryptChange(radio) {
	var isCrypt = $(radio).val();
	var itemValueInput = $(radio).parents("td").find('input[name^="itemValue"]');
	if (isCrypt == '1') {
		itemValueInput.attr('type','password');
	} else {
		itemValueInput.attr('type','text');
	}
}

/**
 * 配置项起始值
 */
var itemCount = 1;

/**
 * 动态向配置项表内增加信息
 * @param data
 * @returns
 */
function addItemToTable(data) {
	itemCount = 0;
	$.each(data, function() {
		itemCount++;
		var newRow = '<tr><td>' +
					'<div class="row form-group">' +
						'<label class="control-label col-sm-2"><b>配置项' + itemCount + '</b></label>' +
						'<div class="col-sm-10">';
		if (itemCount == 1) {
			newRow += 		'<a href="#" onclick="addItem()"><i class="fa fa-plus-square-o pull-right" style="font-size: 25px" title="增加一项"></i></a>';
		} else {
			newRow += 		'<a href="#" onclick="delItem(this)"><i class="fa fa-minus-square-o pull-right" style="font-size: 25px" title="删除此项"></i></a>';
		}
			newRow +=	'</div>' +
					'</div>' +
					'<div class="row form-group">' +
						'<label class="control-label col-sm-2">配置项说明：</label>' +
						'<div class="col-sm-4">' +
							'<input type="text" class="form-control" name="itemDesc' + itemCount + '" placeholder="" value="' + this.itemDesc + '"/>' +
						'</div>' +
						'<label class="control-label col-sm-2">是否加密:</label>' +
						'<div class="col-sm-4">' +
							'<label class="radio-inline">' +
								'<input type="radio" name="' + itemCount + '" value="1" onchange="isCryptChange(this)" ' + (this.itemIsCrypt == 1 ? 'checked="checked"' : '') + '/>是' +
							'</label>' +
							'<label class="radio-inline">' +
								'<input type="radio" name="' + itemCount + '" value="0" onchange="isCryptChange(this)" ' + (this.itemIsCrypt == 0 ? 'checked="checked"' : '') + '/>否' +
							'</label>' +
						'</div>' +
					'</div>' +
					'<div class="row form-group">' +
						'<label class="control-label col-sm-2">配置项KEY:</label>' +
						'<div class="col-sm-4">' +
							'<input type="text" class="form-control" name="itemKey' + itemCount + '" value="' + this.itemKey + '"/>' +
						'</div>' +
						'<label class="control-label col-sm-2" style="padding-left: 0px;">配置项VALUE:</label>' +
						'<div class="col-sm-4">';
		if (this.itemIsCrypt == 1) {
			newRow += 		'<input type="password" class="form-control" name="itemValue' + itemCount + '" value="' + this.itemValue + '"/>';
		} else {
			newRow += 		'<input type="text" class="form-control" name="itemValue' + itemCount + '" value="' + this.itemValue + '"/>';
		}
						'</div>' +
					'</div>' +
				'</td></tr>';
		$(".table").append(newRow);
		addItemValidator(itemCount);
	});
}

/**
 * 增加一个配置项配置区域
 * @returns
 */
function addItem() {
	itemCount++;
	var newRow = '<tr><td>' +
					'<div class="row form-group">' +
						'<label class="control-label col-sm-2"><b>配置项' + itemCount + '</b></label>' +
						'<div class="col-sm-10">';
	if (itemCount == 1) {
		newRow += 			'<a href="#" onclick="addItem()"><i class="fa fa-plus-square-o pull-right" style="font-size: 25px" title="增加一项"></i></a>';
	} else {
		newRow +=			'<a href="#" onclick="delItem(this)"><i class="fa fa-minus-square-o pull-right" style="font-size: 25px" title="删除此项"></i></a>';
	}
	newRow +=			'</div>' +
					'</div>' +
					'<div class="row form-group">' +
						'<label class="control-label col-sm-2">配置项说明：</label>' +
						'<div class="col-sm-4">' +
							'<input type="text" class="form-control" name="itemDesc' + itemCount + '" placeholder="" />' +
						'</div>' +
						'<label class="control-label col-sm-2">是否加密：</label>' +
						'<div class="col-sm-4">' +
							'<label class="radio-inline">' +
								'<input type="radio" name="' + itemCount + '" value="1" onchange="isCryptChange(this)"/>是' +
							'</label>' +
							'<label class="radio-inline">' +
								'<input type="radio" name="' + itemCount + '" value="0" checked="checked" onchange="isCryptChange(this)"/>否' +
							'</label>' +
						'</div>' +
					'</div>' +
					'<div class="row form-group">' +
						'<label class="control-label col-sm-2">配置项KEY：</label>' +
						'<div class="col-sm-4">' +
							'<input type="text" class="form-control" name="itemKey' + itemCount + '"/>' +
						'</div>' +
						'<label class="control-label col-sm-2" style="padding-left: 0px;">配置项VALUE：</label>' +
						'<div class="col-sm-4">' +
							'<input type="text" class="form-control" name="itemValue' + itemCount + '"/>' +
						'</div>' +
					'</div>' +
				'</td></tr>';
	$(".table").append(newRow);
	addItemValidator(itemCount);
}

/**
 * 删除一个配置项配置区域
 * @param click_a
 * @returns
 */
function delItem(click_a) {
	var tr = $(click_a).parents('tr');
	tr.remove();
	itemCount--;
	var bs = $('.table tr td div label b');
	if (bs.length > 1) {
		for (var i = 1; i < bs.length; i++) {
			$(bs[i]).html('配置项' + (i + 1));
		}
	}
	var radios = $('.table tr td div label input[type="radio"]');
	if (radios.length > 2) {
		var j = 1;
		for (var i = 2; i < radios.length; i++) {
			if (i % 2 == 0) {
				j++;
			}
			$(radios[i]).attr('name', j);
		}
	}
}

/**
 * 清空配置项表，只保留第一项
 * @returns
 */
function resetItemTable() {
	$(".table").empty();
	itemCount = 0;
	addItem();
}

/**
 * 判断数组内对象是否已存在某属性值
 * @param arr
 * @param srckey
 * @param destvalue
 * @returns
 */
function checkIsContains(arr, srckey, destvalue) {
	for (var i = 0; i < arr.length; i++) {
		if (arr[i][srckey] == destvalue) {
			return true;
		}
	}
}

/**
 * 为一行配置项增加验证
 * @param index
 * @returns
 */
function addItemValidator(index) {
	$('.form-horizontal').bootstrapValidator('addField', 'itemDesc' + index, {
        validators: {
            notEmpty: {
                message: '配置项说明不能为空'
            },
            stringLength: {
                min: 1,
                max: 64,
                message: '配置项说明长度必须在1到64位之间'
            }
        }
    });
	$('.form-horizontal').bootstrapValidator('addField', 'itemKey' + index, {
        validators: {
            notEmpty: {
                message: '配置项KEY不能为空'
            },
            stringLength: {
                min: 1,
                max: 64,
                message: '配置项KEY长度必须在1到64位之间'
            },
    		regexp: {
    			regexp: /^[a-zA-Z0-9.-]+$/,
    			message: '配置项KEY只能包含数字、大写、小写、英文句号'
    		}
        }
    });
}

