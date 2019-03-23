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
	    	appName: {
                validators: {
                    notEmpty: {
                        message: '应用名称不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 200,
                        message: '模块名长度必须在1到200位之间'
                    }
                }
            },
            appAlias: {
                validators: {
                    notEmpty: {
                        message: '应用简称不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 50,
                        message: '用户名长度必须在1到50位之间'
                    }
                }
            },
            fwId: {
                validators: {
                    notEmpty: {
                        message: '出口防火墙不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 50,
                        message: '用户名长度必须在1到50位之间'
                    }
                }
            },
            approveUser: {
                validators: {
                    notEmpty: {
                        message: '审批人不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 50,
                        message: '用户名长度必须在1到50位之间'
                    }
                }
            } ,
            conIp: {
                validators: {
                    stringLength: {
                        min: 1,
                        max: 120,
                        message: '长度必须在1到120位之间'
                    },
                    ip: {
            			message: '请输入正确的IP地址'
            		}
                }
            },
            appIpUrl: {
                validators: {
                	uri: {
            			message: '请输入正确的域名'
            		}
                }
            }
            
	    }
	});
	$('#chooseFw').click(function() {
        showDetail('选择防火墙', '/modules/fwInfo/fw_select.html', '800px', '550px', function() {
            // 通过回调填写下拉框的值
            if (chooseFw != null && chooseFw[0] != null) {
                $('#chooseFw').val(chooseFw[0].fwName);
                $('#fwId').val(chooseFw[0].id);
            }
        },true);
    });
	 /*
     * 清空文本框
     */
    $('#removeFw').click(function() {
        $('#chooseFw').val('');
        $('#fwId').val('');
    });
    
    //审批人 选择
    $('#chooseApproveUser').click(function() {
        showDetail('选择审批人', '/modules/user/user_select.html', '800px', '550px', function() {
            // 通过回调填写下拉框的值
            if (chooseUser != null && chooseUser[0] != null) {
                $('#chooseApproveUser').val(chooseUser[0].userName);
                $('#approveUserId').val(chooseUser[0].userAccount);
            }
        },true);
    });
    /*
     * 清空文本框
     */
    $('#removeApproveUser').click(function() {
        $('#chooseApproveUser').val('');
        $('#approveUserId').val('');
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
			url : '/applicationManage/add',
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

var chooseFw = null;
var chooseUser = null;