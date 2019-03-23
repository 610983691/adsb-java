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
            dicName : {
                validators : {
                    notEmpty : {
                        message : '字典名称不能为空'
                    },
                    stringLength : {
                        min : 1,
                        max : 32,
                        message : '字典名称长度必须在1到32位之间'
                    }
                }
            },
            dicStatus : {
                validators : {
                    notEmpty : {
                        message : '状态不能为空'
                    }
                }
            },
            dicValue : {
                validators : {
                    notEmpty : {
                        message : '字典值不能为空'
                    },
                    stringLength : {
                        min : 1,
                        max : 32,
                        message : '字典值长度必须在1到32位之间'
                    },
                    regexp : {
                        regexp : /^[a-zA-Z0-9_.]+$/,
                        message : '字典值只能包含大写、小写、数字和下划线'
                    }
                }
            }
        }
	});
	
	/**
	 * 表单值初始化
	 */
	var selData = parent.selData;
	$('.form-horizontal').formEdit(parent.selData);
	
	initSelector("#dicStatusId","DIC_STATUS",selData.dicStatus);
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
			url : '/dict/update',
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