$(function () {
	//防火墙类型下拉框
	$('#fwTypeDic').selectpicker({
        style: 'btn-default',
        size: 5,
        noneResultsText: '无符合检索要求的信息',
        width: '100%'
    });
	var fwTypeDicHtml = parent.fwTypeDicHtml;
	$("#fwTypeDic").html(fwTypeDicHtml);
    $('#fwTypeDic').selectpicker('refresh');
    //IP类型 下拉框
    $('#ipTypeDic').selectpicker({
        style: 'btn-default',
        size: 5,
        noneResultsText: '无符合检索要求的信息',
        width: '100%'
    });
	var ipTypeDicHtml = parent.ipTypeDicHtml;
	$("#ipTypeDic").html(ipTypeDicHtml);
    $('#ipTypeDic').selectpicker('refresh');
    //连接协议下拉框
    $('#fwProtocolDic').selectpicker({
        style: 'btn-default',
        size: 5,
        noneResultsText: '无符合检索要求的信息',
        width: '100%'
    });
	var fwProtocolDicHtml = parent.fwProtocolDicHtml;
	$("#fwProtocolDic").html(fwProtocolDicHtml);
    $('#fwProtocolDic').selectpicker('refresh');
    
    //设备编码 下拉框
    $('#encodeDic').selectpicker({
        style: 'btn-default',
        size: 5,
        noneResultsText: '无符合检索要求的信息',
        width: '100%'
    });
	var encodeDicHtml = parent.encodeDicHtml;
	$("#encodeDic").html(encodeDicHtml);
    $('#encodeDic').selectpicker('refresh');
    
    //设备状态 下拉框
    $('#fwStatusDic').selectpicker({
        style: 'btn-default',
        size: 5,
        noneResultsText: '无符合检索要求的信息',
        width: '100%'
    });
	var fwStatusDicHtml = parent.fwStatusDicHtml;
	$("#fwStatusDic").html(fwStatusDicHtml);
    $('#fwStatusDic').selectpicker('refresh');
     
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
	    	fwName: {
                validators: {
                    notEmpty: {
                        message: '设备名称不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 120,
                        message: '长度必须在1到120位之间'
                    }
                }
            },
            fwType: {
                validators: {
                    notEmpty: {
                        message: '设备类型不能为空'
                    }
                }
            },
            ipType: {
                validators: {
                    notEmpty: {
                        message: 'IP类型不能为空'
                    }
                }
            },
            fwIp: {
                validators: {
                    notEmpty: {
                        message: '设备IP不能为空'
                    },
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
            fwIpv6: {
                validators: {
                    notEmpty: {
                        message: '设备IP不能为空'
                    },
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
            fwProtocol: {
                validators: {
                    notEmpty: {
                        message: '连接协议不能为空'
                    }
                }
            },
            fwPort: {
                validators: {
                    notEmpty: {
                        message: '端口不能为空'
                    } 
                }
            },
            encode: {
                validators: {
                    notEmpty: {
                        message: '设备编码不能为空'
                    } 
                }
            },
            fwStatus: {
                validators: {
                    notEmpty: {
                        message: '设备状态不能为空'
                    } 
                }
            },
            adminAcount: {
                validators: {
                    notEmpty: {
                        message: '管理员账号不能为空'
                    } ,
                    stringLength: {
                        min: 1,
                        max: 120,
                        message: '长度必须在1到120位之间'
                    }
                }
            },
            adminPwd: {
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
                        field: 'adminPwd_confirm',
                        message: '两次输入的密码不相符'
                    }
            	}
            },
            adminPwd_confirm: {
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
                        field: 'adminPwd',
                        message: '两次输入的密码不相符'
                    }
            	}
            },
            adminPrompt: {
                validators: {
                    notEmpty: {
                        message: '管理员提示符不能为空'
                    } ,
                    stringLength: {
                        min: 1,
                        max: 20,
                        message: '长度必须在1到20位之间'
                    }
                }
            },
            rootPwd: {
            	validators: {
            		notEmpty: {
            			message: '特权密码不能为空'
            		},
            		stringLength: {
            			min: 1,
            			max: 32,
            			message: '密码长度必须在1到32位之间'
            		},
            		regexp: {
            			regexp: /^[a-zA-Z0-9_.]+$/,
            			message: '密码只能包含大写、小写、数字和下划线'
            		}
            	}
            },
            rootPrompt: {
                validators: {
                    notEmpty: {
                        message: '特权提示符不能为空'
                    } ,
                    stringLength: {
                        min: 1,
                        max: 20,
                        message: '长度必须在1到20位之间'
                    }
                }
            }
            
	    }
	});
	/**
	 * 表单值初始化
	 */
	var selData = parent.selData;
	selData.adminPwd_confirm = selData.adminPwd ;
	 
	var ipType = selData.ipType;
	var fwIpInput = document.getElementById("fwIp");
	var fwIpv6Input = document.getElementById("fwIpv6");
	/*
	 * 1 是ipv6
	 */
	if(1 == ipType){
		fwIpInput.style.display= "none";
		$('.form-horizontal').data('bootstrapValidator').enableFieldValidators('fwIp', false);
		fwIpInput.value="";
		
		fwIpv6Input.style.display= "inline";
		$('.form-horizontal').data('bootstrapValidator').enableFieldValidators('fwIpv6', true);
    	
	}else{
		document.getElementById("fwIp").style.display= "inline"; 
		$('.form-horizontal').data('bootstrapValidator').enableFieldValidators('fwIp', true);
    	document.getElementById("fwIpv6").style.display= "none"; 
		$('.form-horizontal').data('bootstrapValidator').enableFieldValidators('fwIpv6', false);
		fwIpv6Input.value="";
	}
	$('.form-horizontal').formEdit(selData);
	/*
	 * 下拉框 赋值,上面的默认方法不好使 start
	 */
//	$('#fwTypeDic.selectpicker').selectpicker('val', selData.fwType);
//
//	$('#ipTypeDic.selectpicker').selectpicker('val', selData.ipType);
//
//	$('#fwProtocolDic.selectpicker').selectpicker('val', selData.fwProtocol);
//
//	$('#encodeDic.selectpicker').selectpicker('val', selData.encode);
//
//	$('#fwStatusDic.selectpicker').selectpicker('val', selData.fwStatus);
	/*
	 * 下拉框 赋值,上面的默认方法不好使 end
	 */
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
			url : '/fwInfo/update',
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
    /*
     * 下拉框值改变事件
     */
    $("#ipTypeDic").on('changed.bs.select',function(e){
    	var selectValue = e.target.value;
//    	alert(selectValue);
    	var fwIpInput = document.getElementById("fwIp");
    	var fwIpv6Input = document.getElementById("fwIpv6");
    	var formHor = $('.form-horizontal');
    	/*
    	 * 1 是ipv6
    	 */
    	if(1 == selectValue){
    		fwIpInput.style.display= "none";
    		fwIpInput.value="";
    		fwIpv6Input.style.display= "inline";
    		if(formHor != null){
    			$('.form-horizontal').data('bootstrapValidator').enableFieldValidators('fwIp', false);
    			$('.form-horizontal').data('bootstrapValidator').enableFieldValidators('fwIpv6', true);
    		}
        	
    	}else{
    		document.getElementById("fwIp").style.display= "inline"; 
        	document.getElementById("fwIpv6").style.display= "none"; 
    		fwIpv6Input.value="";
    		if(formHor != null){
    			formHor.data('bootstrapValidator').enableFieldValidators('fwIp', true);
    			formHor.data('bootstrapValidator').enableFieldValidators('fwIpv6', true);
    		}
    	}
    })
})
