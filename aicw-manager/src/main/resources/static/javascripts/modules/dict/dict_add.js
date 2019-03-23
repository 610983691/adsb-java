$(function() {
    loadDictByType('DIC_STATUS', function(data) {
        var json = parse(data.rows);
        $("#dicStatusId").html(parseOptionHtml(json));
        $('#dicStatusId').selectpicker('refresh');

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

    $('#chooseDictType').click(function() {
        showDetail('选择字典类型', '/modules/dict/dictType.html', '800px', '350px', function() {
            // 通过回调填写下拉框的值
            if (chooseType[0] != null) {
                $('#chooseDictType').val(chooseType[0].typeName);
                $('#dictTypeCodeVal').val(chooseType[0].typeCode);
            }
        });
    });

    /***************************************************************************
     * 清空文本框
     */
    $('#removeDictTye').click(function() {
        $('#chooseDictType').val('');
        $('#dictTypeCodeVal').val('');
    });

    $('#parentDicTypeName').click(function() {
        showDetail('选择字典类型', '/modules/dict/dictType.html', '800px', '350px', function() {
            // 通过回调填写下拉框的值
            if (chooseType[0] != null) {
                $('#parentDicTypeName').val(chooseType[0].typeName);
                $('#parentDicTypeId').val(chooseType[0].typeCode);
            }

        });
    });

    /***************************************************************************
     * 清空文本框
     */
    $('#removeParentDictTye').click(function() {
        $('#parentDicTypeName').val('');
        $('#parentDicTypeId').val('');
    });

    function sameDict(){
        var parentDictId = $('#parentDicTypeId').val();
        if (parentDictId === null || parentDictId.length === 0) {
            return false;
        }
        if(parentDictId===$('#dictTypeCodeVal').val()){
            return true;
        }
        return false;
    }
    /**
     * 保存按钮
     */
    $('.btn-success').click(function() {
        if(sameDict()){
            showFailureMsgs("父子字典值不能相同！");
            return;
        }
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
            url : '/dict/add',
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
var chooseType = null;