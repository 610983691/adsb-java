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
	
	//选择的产品数据
	if (parent.selRecord) {
		$('.form-group input[name="productName"]').val(parent.selRecord.productName);
		$('.form-group input[name="moduleName"]').val(parent.selRecord.moduleName);
		$('.form-group input[name="configType"]').val(parent.selRecord.configType);
	}
	
	/**
	 * 取消按钮
	 */
	$('.btn-info').click(function() {
		parent.addItemsRecord = new Array();
		closeWindowInIFrame();
	});
	
	/**
	 * 增加按钮
	 */
	$('.fa-save').click(function() {
		var bootstrapValidator = $('.form-horizontal').data('bootstrapValidator');
    	bootstrapValidator.validate();
		if (!bootstrapValidator.isValid()) {
			return;
		}
		var trs = $('.table tr');
		var isValid = true;
		parent.addItemsRecord = new Array();
		$.each(trs, function() {
			var itemConfig =  $(this).find('input');
			var record = {};
			record.id = 0;
			record.isExtends = 0;
			record.itemDesc = $(this).find('input[name^="itemDesc"]').val();
			record.itemKey = $(this).find('input[name^="itemKey"]').val();
			if (!record.itemDesc || !record.itemKey || checkIsContains(parent.addItemsRecord, 'itemKey', record.itemKey)
					|| checkIsContains(parent.addItemsRecord, 'itemDesc', record.itemDesc)) {
				showFailureMsgs('信息不完整或存在相同的配置项说明和KEY，请检查！');
				isValid = false;
				return false;
			}
			record.itemValue = $(this).find('input[name^="itemValue"]').val();
			record.itemIsCrypt = $(this).find('input[type="radio"]:checked').val();
			parent.addItemsRecord.push(record);
		});
		if (isValid) {
			showSuccessMsgs('已添加，请保存！', function() {
				closeWindowInIFrame();
			});
		}
	});
})

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
 * 增加一个配置项配置区域
 * @returns
 */
function addItem() {
	itemCount++;
	var newRow = '<tr><td>' +
					'<div class="row form-group">' +
						'<label class="control-label col-sm-2"><b>配置项' + itemCount + '</b></label>' +
						'<div class="col-sm-10">' +
							'<a href="#" onclick="delItem(this)"><i class="fa fa-minus-square-o pull-right" style="font-size: 25px" title="删除此项"></i></a>' +
						'</div>' +
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
	//重置“配置项x”号码
	if (bs.length > 1) {
		for (var i = 1; i < bs.length; i++) {
			$(bs[i]).html('配置项' + (i + 1));
		}
	}
	//重置是否加密radio的name
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

