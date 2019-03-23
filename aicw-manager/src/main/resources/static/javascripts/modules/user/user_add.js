var province = null;
$(function() {
    $("#validDate_datetimepicker").datetimepicker({
        language : 'zh-CN',
        autoclose : true,
        todayBtn : true,
        todayHighlight : true
    });
    $("#invalidDate_datetimepicker").datetimepicker({
        language : 'zh-CN',
        autoclose : true,
        todayBtn : true,
        todayHighlight : true
    });
    loadDictByType('DIC_SEX', function(data) {
        var json = parse(data.rows);
        $("#userGender").html(parseOptionHtml(json));
        $('#userGender').selectpicker('refresh');
    });
    loadDictByType('DIC_USER_STATUS', function(data) {
        var json = parse(data.rows);
        $("#userStatus").html(parseOptionHtml(json));
        $('#userStatus').selectpicker('refresh');
    });
    loadDictByType('DIC_RECEIVE_MSG', function(data) {
        var json = parse(data.rows);
        $("#isReviceMsgId").html(parseOptionHtml(json));
        $('#isReviceMsgId').selectpicker('refresh');
    });

    loadDictByType('DIC_NATIONALITY', function(data) {
        var json = parse(data.rows);
        $("#nation").html(parseOptionHtml(json));
        $('#nation').selectpicker('refresh');
    });

    loadDictByType('DIC_PROVINCE', function(data) {
        province = parse(data.rows);
        $("#provinceDic").html(parseOptionHtml(province));
        $('#provinceDic').selectpicker('refresh');
    });
    $("#provinceDic").change(function() {
        var dic = $("#provinceDic").val();
        if (dic == undefined || dic == '') {
            $("#regionDic").html("<option  value='' >请选择</option>");// 清空
            $('#regionDic').selectpicker('refresh');
            return;
        }
        var parentId = getIdByVal(dic, province);
        loadDictByType('DIC_REGION', function(data) {
            var json = parse(data.rows);
            $("#regionDic").html(parseOptionHtml(json));
            $('#regionDic').selectpicker('refresh');
        }, parentId);
    });

    /**
     * 表单验证
     */
    $('.form-horizontal').bootstrapValidator({
        live : 'enabled',
        feedbackIcons : {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        message : '该字段不能为空',
        submitButtons : '.btn-success',
        fields : {
            userName : {
                validators : {
                    notEmpty : {
                        message : '用户名称不能为空'
                    },
                    stringLength : {
                        min : 1,
                        max : 32,
                        message : '用户名称长度必须在1到32位之间'
                    }
                }
            },
            userAccount : {
                validators : {
                    notEmpty : {
                        message : '用户账号不能为空'
                    },
                    stringLength : {
                        min : 1,
                        max : 32,
                        message : '用户账号长度必须在1到32位之间'
                    },
                    regexp : {
                        regexp : /^[a-zA-Z0-9_.]+$/,
                        message : '用户账号只能包含大写、小写、数字和下划线'
                    },
                    remote : {
                        type : 'POST',// 默认是POST,源码第5955开始的注释往下
                        url : '/user/isExistAccount',
                        message : "用户名已经存在",
                        data : {
                            userAccount : $("#userAccount").val()
                        },
                        delay : 1500,//每输入一个字符，就发ajax请求压力太大，设置1.5秒校验一次
                        dataConvertor : function(data) {
                            if (data.msg === 'false' || data.msg === false) {// 不存在该账号就是有效
                                return {
                                    valid : true
                                };
                            } else {
                                return {
                                    valid : false
                                };
                            }
                        }
                    }
                }
            },
            loginPassword : {
                validators : {
                    notEmpty : {
                        message : '密码不能为空'
                    },
                    stringLength : {
                        min : 6,
                        max : 16,
                        message : '密码长度必须在6到16位之间'
                    },
                    regexp : {
                        regexp : /^[a-zA-Z0-9_]+$/,
                        message : '密码只能包含大写、小写、数字和下划线'
                    },
                    identical : {
                        field : 'loginPasswordConfirm',
                        message : '两次输入的密码不相符'
                    }
                }
            },
            loginPasswordConfirm : {
                validators : {
                    notEmpty : {
                        message : '确认密码不能为空'
                    },
                    stringLength : {
                        min : 6,
                        max : 16,
                        message : '密码长度必须在6到16位之间'
                    },
                    regexp : {
                        regexp : /^[a-zA-Z0-9_]+$/,
                        message : '确认密码只能包含大写、小写、数字和下划线'
                    },
                    identical : {
                        field : 'loginPassword',
                        message : '两次输入的密码不相符'
                    }
                }
            },
            userGender : {
                validators : {
                    notEmpty : {
                        message : '性别不能为空'
                    }
                }
            },
            idNumber : {
                validators : {
                    notEmpty : {},
                    regexp : {
                        regexp : /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
                        message : '请输入正确的身份证号码'
                    }
                }
            },
            isReviceMsg : {
                validators : {
                    notEmpty : {}
                }
            },
            userStatus : {
                validators : {
                    notEmpty : {}
                }
            },
            telphone : {
                validators : {
                    notEmpty : {},
                    regexp : {
                        regexp : /^1[0-9]{10}$/,
                        message : '请输入正确的手机号码'
                    }
                }
            },
            nation : {
                validators : {
                    notEmpty : {}
                }
            },
            provinceDic : {
                validators : {
                    notEmpty : {}
                }
            },
            regionDic : {
                validators : {
                    notEmpty : {}
                }
            },
            validDate : {
                validators : {
                    notEmpty : {}
                }
            },
            invalidDate : {
                validators : {
                    notEmpty : {}
                }
            }
        }
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
            url : '/user/add',
            cache : false,
            dataType : 'json',
            contentType : 'application/json',
            data : JSON.stringify(valuesObj),
            success : function(data) {
                layer.closeAll('loading');
                if (data.success) {
                    showSuccessMsgs(data.msg, function() {
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
