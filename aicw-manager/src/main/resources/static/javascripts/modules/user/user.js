$(function() {
    var USER_STATUS_JSON; // 用户状态字典
    loadDictByType('DIC_USER_STATUS', function(data) {
        USER_STATUS_JSON = parse(data.rows);
    });
    var DIC_SEX;// 性别
    loadDictByType('DIC_SEX', function(data) {
        DIC_SEX = parse(data.rows);
    });
    var DIC_RECEIVE_MSG;// 短信权限
    loadDictByType('DIC_RECEIVE_MSG', function(data) {
        DIC_RECEIVE_MSG = parse(data.rows);
    });
    /**
     * 列表查询
     */
    $('#tb_user').bootstrapTable({
        url : "/user/list",
        method : 'post', // 请求方式（*）
        toolbar : '#toolbar', // 工具按钮用哪个容器
        striped : true, // 是否显示行间隔色
        cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination : true, // 是否显示分页（*）
        sortable : false, // 是否启用排序
        sortOrder : "asc", // 排序方式
        sidePagination : "server", // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber : 1, // 初始化加载第一页，默认第一页
        pageSize : 15, // 每页的记录行数（*）
        pageList : [ 10, 15, 30, 50, 100 ], // 可供选择的每页的行数（*）
        search : true, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        formatSearch : function() {
            return '用户名称';
        },
        strictSearch : true,
        showColumns : false, // 是否显示所有的列
        showRefresh : true, // 是否显示刷新按钮
        minimumCountColumns : 2, // 最少允许的列数
        clickToSelect : true, // 是否启用点击选中行
        // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId : "ID", // 每一行的唯一标识，一般为主键列
        showToggle : false, // 是否显示详细视图和列表视图的切换按钮
        cardView : false, // 是否显示详细视图
        detailView : false, // 是否显示父子表
        queryParams : function(params) {
            return {
                limit : params.limit,
                offset : params.offset,
                userName : params.search
            }
        },
        columns : [ {
            radio : true
        }, {
            field : 'userId',
            title : 'ID',
            visible : false
        }, {
            field : 'userName',
            title : '用户名称'
        }, {
            field : 'userAccount',
            title : '用户账号'
        }, {
            field : 'userGender',
            title : '性别',
            formatter : function(value, row, index) {
                return getNameByVal(value, DIC_SEX);
            }
        }, {
            field : 'telphone',
            title : '电话号码'
        }, {
            field : 'userStatus',
            title : '用户状态',
            formatter : function(value, row, index) {
                return getNameByVal(value, USER_STATUS_JSON);
            }
        }, {
            field : 'isReviceMsg',
            title : '短信权限',
            formatter : function(value, row, index) {
                return getNameByVal(value, DIC_RECEIVE_MSG);
            }
        } ]
    });

    $('#btn_add').click(function() {
        showDetail('新增用户', '/modules/user/user_add.html', '800px', '350px', function() {
            $("#tb_user").bootstrapTable('refresh');
        });
    });

    $('#btn_edit').click(function() {
        var sel = $("#tb_user").bootstrapTable('getSelections');
        if (sel.length != 1) {
            showFailureMsgs('请选择一条记录！');
            return;
        }
        updateUser = sel[0];
        showDetail('修改用户', '/modules/user/user_edit.html', '800px', '350px', function() {
            $("#tb_user").bootstrapTable('refresh');
        });
    });

    $('#btn_delete').click(function() {
        var sel = $("#tb_user").bootstrapTable('getSelections');
        if (sel.length != 1) {
            showFailureMsgs('只能选择一条记录！');
            return;
        }
        confirmAlert('确定删除？', '确定', '取消', function() {
            var selJson = JSON.stringify(sel[0]);
            layer.msg('加载中', {
                icon : 16,
                shade : 0.01
            });
            $.ajax({
                type : "POST",
                url : '/user/delete',
                cache : false,
                dataType : 'json',
                contentType : 'application/json',
                data : selJson,
                success : function(data) {
                    layer.closeAll('loading');
                    if (data.success) {
                        showSuccessMsgs(data.msg);
                        $("#tb_user").bootstrapTable('refresh');
                    } else {
                        showFailureMsgs(data.msg);
                    }
                }
            });
        });
    });
});

/**
 * 修改时选择的数据记录
 */
var updateUser = null;