$(function () {
    
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
	    	uuid: {
                validators: {
                    notEmpty: {
                        message: '用户登录ID不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 64,
                        message: '模块名长度必须在1到64位之间'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: '产品标识只能包含大写、小写、数字和下划线'
                    }
                }
            },
            userName: {
                validators: {
                    notEmpty: {
                        message: '用户名不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 64,
                        message: '用户名长度必须在1到64位之间'
                    }
                }
            },
            userPassword: {
            	validators: {
            		notEmpty: {
            			message: '密码不能为空'
            		},
            		stringLength: {
            			min: 1,
            			max: 32,
            			message: '密码长度必须在1到32位之间'
            		},
            		regexp: {
            			regexp: /^[a-zA-Z0-9_.]+$/,
            			message: '密码只能包含大写、小写、数字和下划线'
            		},
            		identical: {
                        field: 'userPassword_confirm',
                        message: '两次输入的密码不相符'
                    }
            	}
            },
            userPassword_confirm: {
            	validators: {
            		notEmpty: {
            			message: '确认密码不能为空'
            		},
            		stringLength: {
            			min: 1,
            			max: 32,
            			message: '确认密码长度必须在1到32位之间'
            		},
            		regexp: {
            			regexp: /^[a-zA-Z0-9_.]+$/,
            			message: '确认密码只能包含大写、小写、数字和下划线'
            		},
            		identical: {
                        field: 'userPassword',
                        message: '两次输入的密码不相符'
                    }
            	}
            }
	    }
	});
	
	/**
	 * 表单值初始化
	 */
	var selData = parent.selData;
	selData.isAdmin = selData.isAdmin + "";
	$('.form-horizontal').formEdit(parent.selData);
	
	/**
	 * 动态启用、禁用密码输入框
	 */
	$('.checkbox label input:checkbox').change(function(){
		var isChecked = $(this).is(':checked');
		var userPassword = $('input[name="userPassword"]');
		var userPasswordConfirm = $('input[name="userPassword_confirm"]');
		if (isChecked) {
			$('.form-horizontal').data('bootstrapValidator').enableFieldValidators('userPassword', true);
			$('.form-horizontal').data('bootstrapValidator').enableFieldValidators('userPassword_confirm', true);
			userPassword.attr("disabled", false);
			userPasswordConfirm.attr("disabled", false);
		} else {
			$('.form-horizontal').data('bootstrapValidator').enableFieldValidators('userPassword', false);
			$('.form-horizontal').data('bootstrapValidator').enableFieldValidators('userPassword_confirm', false);
			userPassword.val("");
			userPasswordConfirm.val("");
			userPassword.attr("disabled", true);
			userPasswordConfirm.attr("disabled", true);
		}
	});
	
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
		layer.msg('加载中', {
			icon : 16,
			shade : 0.01
		});
		$.ajax({
			type : "POST",
			url : '/user/update',
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
    $('.btn-info').click(function(){
    	closeWindowInIFrame();
    });

})