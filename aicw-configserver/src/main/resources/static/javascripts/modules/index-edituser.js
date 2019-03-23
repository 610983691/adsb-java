$(function() {
	
	/**
	 * 表单验证
	 */
	$('form').bootstrapValidator({
		live: 'enabled',
		feedbackIcons: {
	        valid: 'glyphicon glyphicon-ok',
	        invalid: 'glyphicon glyphicon-remove',
	        validating: 'glyphicon glyphicon-refresh'
	    },
	    message: '该字段不能为空',
	    submitButtons: '.btn-success',
	    fields: {
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
            			regexp: /^[a-zA-Z0-9_]+$/,
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
            			regexp: /^[a-zA-Z0-9_]+$/,
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
	 * 保存个人信息
	 */
	$(function() {
		$('.btn-toolbar .btn-success').click(function() {
			var bootstrapValidator = $('form').data('bootstrapValidator');
	    	bootstrapValidator.validate();
			if (!bootstrapValidator.isValid()) {
				return;
			}
			var values = $('.main-content .col-md-4 form').serializeArray();
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
				url : '/user/selfupdate',
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
	});
	
	/**
	 * 取消修改
	 */
	$(function() {
		$('.btn-toolbar .btn-info').click(function() {
			location.href="index.html";
		});
	});
	
});
