$(function() {
    /**
     * 列表查询
     */
    $('#tb_fwInfo').bootstrapTable({
        url : "/fwInfo/list",
        method : 'post', // 请求方式（*）
        toolbar : '#toolbar', // 工具按钮用哪个容器
        striped : true, // 是否显示行间隔色
        cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination : true, // 是否显示分页（*）
        sortable : false, // 是否启用排序
        sortOrder : "asc", // 排序方式
        sidePagination : "server", // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber : 1, // 初始化加载第一页，默认第一页
        pageSize : 10, // 每页的记录行数（*）
        pageList : [ 10, 20, 50 ], // 可供选择的每页的行数（*）
        strictSearch : true,
        minimumCountColumns : 2, // 最少允许的列数
        clickToSelect : true, // 是否启用点击选中行
        uniqueId : "ID", // 每一行的唯一标识，一般为主键列
        detailView : false, // 是否显示父子表
        queryParams : function(params) {
            var valuesObj = {
                beginTime : parent.beginTime,
                endTime : parent.endTime,
                testLoginStatus : parent.testLoginStatus
            };
            valuesObj.limit = params.limit;
            valuesObj.offset = params.offset;
            return valuesObj;
        },
        columns : [ {
            field : 'id',
            title : 'ID',
            visible : false
        }, {
            field : 'fwName',
            title : '防火墙名称'
        }, {
            field : 'fwIp',
            title : '防火墙IP'
        }, {
            field : 'fwTypeDes',
            title : '防火墙类型'
        }, {
            field : 'testLoginStatus',
            title : '网络状态',
            formatter : function(value, row, index) {
                return value == 0 ? "失败" : "成功";
            }
        }, {
            field : 'testLoginLog',
            title : '检测日志'
        }, {
            field : 'testLoginDate',
            title : '检测时间'
        } ]
    });

});
