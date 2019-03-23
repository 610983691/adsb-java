$(function () {
	
	//防火墙类型下拉框
	$('#fwTypeDic').selectpicker({
        style: 'btn-default',
        size: 5,
        noneResultsText: '无符合检索要求的信息',
        width: '100%'
    });
	var fwTypeDicHtml = parent.fwTypeDicHtml;
//	alert(fwTypeDicHtml);
	$("#fwTypeDic").html(fwTypeDicHtml);
    $('#fwTypeDic').selectpicker('refresh');
    //命令类型 下拉框
    $('#orderTypeDic').selectpicker({
        style: 'btn-default',
        size: 5,
        noneResultsText: '无符合检索要求的信息',
        width: '100%'
    });
	var orderTypeDicHtml = parent.orderTypeDicHtml;
	$("#orderTypeDic").html(orderTypeDicHtml);
    $('#orderTypeDic').selectpicker('refresh');
//    alert(orderTypeDicHtml);
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
	    	fwType: {
                validators: {
                    notEmpty: {
                        message: '设备类型不能为空'
                    } 
                }
            },
            orderType: {
                validators: {
                    notEmpty: {
                        message: '命令类型不能为空'
                    }
                }
            },
            fwOrder: {
                validators: {
                    notEmpty: {
                        message: '命令不能为空'
                    }
                }
            },
            
            fwOrderNum: {
                validators: {
                    notEmpty: {
                        message: '命令执行顺序不能为空'
                    }
                }
            } 
	    }
	});
	/**
	 * 表单值初始化
	 */
	var selData = parent.selData;
	$('.form-horizontal').formEdit(selData);
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
			url : '/fwOrder/update',
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
