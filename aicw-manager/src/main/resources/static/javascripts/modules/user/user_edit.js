var province = null;
$(function() {
    /**
     * 表单值初始化
     */
    var updateUser = parent.updateUser;
    $('.form-horizontal').formEdit(parent.updateUser);
    $("#validDate_datetimepicker").datetimepicker({
        language:'zh-CN',
        autoclose: true,
        todayBtn: true,
        todayHighlight:true
    });
    $("#invalidDate_datetimepicker").datetimepicker({
        language:'zh-CN',
        autoclose: true,
        todayBtn: true,
        todayHighlight:true
    });
    /***
     * 下拉框初始化填充
     */
    initSelector("#userGender","DIC_SEX",updateUser.userGender);
    initSelector("#userStatus","DIC_USER_STATUS",updateUser.userStatus);
    initSelector("#isReviceMsgId","DIC_RECEIVE_MSG",updateUser.isReviceMsg);
    initSelector("#nation","DIC_NATIONALITY",updateUser.nation);
    
    /***
     * 省区市关联下拉框值填充
     */
    loadDictByType('DIC_PROVINCE', function(data) {
        province = parse(data.rows);
        $("#provinceDic").html(parseOptionHtml(province));
        if(updateUser.provinceDic !=undefined){
            $("#provinceDic").selectpicker('val', updateUser.provinceDic);
        }
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
            if(updateUser.regionDic !=undefined){
                $("#regionDic").selectpicker('val', updateUser.regionDic);
            }
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
                    }
                }
            },
            loginPassword : {
                validators: {
                    stringLength: {
                        min: 6,
                        max: 16,
                        message: '密码长度必须在6到16位之间'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: '密码只能包含大写、小写、数字和下划线'
                    },
                    identical: {
                        field: 'loginPasswordConfirm',
                        message: '两次输入的密码不相符'
                    }
                }
            },
            loginPasswordConfirm: {
                validators: {
                    stringLength: {
                        min: 6,
                        max: 16,
                        message: '确认密码长度必须在6到16位之间'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: '确认密码只能包含大写、小写、数字和下划线'
                    },
                    identical: {
                        field: 'loginPassword',
                        message: '两次输入的密码不相符'
                    }
                }
            },
            userGender:{
                validators : {
                    notEmpty : {
                        message : '性别不能为空'
                    }
                }
            },
            idNumber:{
                validators : {
                    notEmpty : {},
                    regexp: {
                        regexp: /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
                        message: '请输入正确的身份证号码'
                    }
                }
            },
            isReviceMsg:{
                validators : {
                    notEmpty : {
                    }
                }
            },
            userStatus:{
                validators : {
                    notEmpty : {
                    }
                }
            },
            telphone:{
                validators : {
                    notEmpty : {
                    },
                    regexp: {
                        regexp: /^1[0-9]{10}$/,
                        message: '请输入正确的手机号码'
                    }
                }
            },
            nation:{
                validators : {
                    notEmpty : {
                    }
                }
            },
            provinceDic:{
                validators : {
                    notEmpty : {
                    }
                }
            },
            regionDic:{
                validators : {
                    notEmpty : {
                    }
                }
            },
            validDate:{
                validators : {
                    notEmpty : {
                    }
                }
            },
            invalidDate:{
                validators : {
                    notEmpty : {
                    }
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
            url : '/user/update',
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
