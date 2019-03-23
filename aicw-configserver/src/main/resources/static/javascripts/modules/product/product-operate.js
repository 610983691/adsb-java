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
	    	productName: {
                validators: {
                    notEmpty: {
                        message: '产品名不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 128,
                        message: '用户名长度必须在1到128位之间'
                    }
                }
            },
            productMark: {
                validators: {
                    notEmpty: {
                        message: '产品标识不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 32,
                        message: '产品标识长度必须在1到32位之间'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_-]+$/,
                        message: '产品标识只能包含大写、小写、数字和下划线'
                    }
                }
            },
            cryptKey: {
                validators: {
                    notEmpty: {
                        message: '密钥不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 32,
                        message: '密钥长度必须在1到32位之间'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: '密钥只能包含大写、小写、数字和下划线'
                    }
                }
            }
	    }
	});
	
	/**
	 * 表单值初始化
	 */
	var selData = parent.selData;
	if (selData) {
		$('.form-horizontal').formEdit(selData);
		$('select[name="cryptType"]').attr("disabled", true);
		$('input[name="cryptKey"]').attr("disabled", true);
	}
	
	/**
	 * 保存按钮
	 */
    $('.btn-success').click(function(){
    	var bootstrapValidator = $('.form-horizontal').data('bootstrapValidator');
    	bootstrapValidator.validate();
		if (!bootstrapValidator.isValid()) {
			return;
		}
    	var values = $('.form-horizontal').serializeArray();
		var valuesObj = {};
		$.each(values, function() {
			valuesObj[this.name] = this.value;
		});
		if (valuesObj.id) {
			addOrUpdateProduct('/product/update/product', valuesObj);
		} else {
			confirmAlert('加解密方式及密钥设置后将无法更改，是否继续？', '确定', '取消', function() {
				layer.msg('加载中', {
					icon : 16,
					shade : 0.01
				});
				addOrUpdateProduct('/product/add/product', valuesObj);
			});
		}
    });

	/**
	 * 取消按钮
	 */
	$('.btn-info').click(function() {
		closeWindowInIFrame();
	});
});

/**
 * 增加或修改产品信息
 * @param url
 * @param data
 * @returns
 */
function addOrUpdateProduct(url, data) {
	layer.msg('加载中', {
		icon : 16,
		shade : 0.01
	});
	$.ajax({
		type : "POST",
		url : url,
		cache : false,
		dataType : 'json',
		contentType: 'application/json',
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
