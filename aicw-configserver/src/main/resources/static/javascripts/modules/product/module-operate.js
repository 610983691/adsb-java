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
	    	moduleName: {
                validators: {
                    notEmpty: {
                        message: '模块名不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 128,
                        message: '模块名长度必须在1到128位之间'
                    }
                }
            },
            moduleMark: {
                validators: {
                    notEmpty: {
                        message: '模块标识不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 32,
                        message: '模块标识长度必须在1到32位之间'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_-]+$/,
                        message: '产品标识只能包含大写、小写、数字和下划线'
                    }
                }
            }
	    }
	});
	
	//选择的产品数据
	if (parent.selData) {
		$('input[name="productName"]').val(parent.selData.productName);
		$('input[name="productId"]').val(parent.selData.id);
	}
	
	//选择的模块数据
	var selData = parent.selModuleData;
	if (selData) {
		selData.isExtends = selData.isExtends + "";
		$('.form-horizontal').formEdit(selData);
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
    	layer.msg('加载中', {
    		icon : 16,
    		shade : 0.01
    	});
    	var values = $('.form-horizontal').serializeArray();
		var valuesObj = {};
		$.each(values, function() {
			valuesObj[this.name] = this.value;
		});
		var url = '/product/add/module';
		if (valuesObj.id) {
			url = '/product/update/module';
		}
		$.ajax({
			type : "POST",
			url : url,
			cache : false,
			dataType : 'json',
			contentType: 'application/json',
			data : JSON.stringify(valuesObj),
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
    });

	/**
	 * 取消按钮
	 */
	$('.btn-info').click(function() {
		closeWindowInIFrame();
	});
});

