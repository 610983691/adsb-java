$(function() {
    // 初始化的时候渲染查询条件等输入框
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
    $('#time').selectpicker('refresh');
    $('#status').selectpicker('refresh');
    timeClickListener();
    /**
     * 列表查询
     */
    $('#tb_controlLog').bootstrapTable({
        url : "/controlLog/list",
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
        pageList : [ 10, 15, 30, 50 ], // 可供选择的每页的行数（*）
        strictSearch : true,
        showColumns : false, // 是否显示所有的列
        showRefresh : false, // 是否显示刷新按钮
        minimumCountColumns : 2, // 最少允许的列数
        clickToSelect : true, // 是否启用点击选中行
        // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId : "ID", // 每一行的唯一标识，一般为主键列
        showToggle : false, // 是否显示详细视图和列表视图的切换按钮
        cardView : false, // 是否显示详细视图
        detailView : false, // 是否显示父子表
        queryParams : function(params) {// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数
            var values = $('.form-inline').serializeArray();
            var valuesObj = {};
            $.each(values, function() {
                valuesObj[this.name] = this.value;
            });
            valuesObj.limit = params.limit;
            valuesObj.offset = params.offset;
            return valuesObj;
        },
        columns : [ {
            field : 'id',
            title : 'ID',
            visible : false
        }, {
            field : 'fwIp',
            title : '防火墙IP'
        }, {
            field : 'fwName',
            title : '防火墙名称'
        }, {
            field : 'sendOrder',
            title : '下发策略'
        }, {
            field : 'applyUserId',
            title : '申请人'
        }, {
            field : 'status',
            title : '状态'
        }, {
            field : 'validTime',
            title : '生效时间'
        }, {
            field : 'invalidTime',
            title : '失效时间'
        } ]
    });

    /***************************************************************************
     * 时间范围选择事件监听
     */
    var beginTime, endTime;
    function timeClickListener() {
        $('#month1').click(function() {
            var now = new Date();
            endTime = now.toString('yyyy-MM-dd HH:mm:ss');
            beginTime = now.addMonths(-1).toString('yyyy-MM-dd HH:mm:ss');
            $("#validDate_datetimepicker").val(beginTime);
            $("#invalidDate_datetimepicker").val(endTime);
        });
        $('#month3').click(function() {
            var now = new Date();
            endTime = now.toString('yyyy-MM-dd HH:mm:ss');
            beginTime = now.addMonths(-3).toString('yyyy-MM-dd HH:mm:ss');
            $("#validDate_datetimepicker").val(beginTime);
            $("#invalidDate_datetimepicker").val(endTime);
        });
        $('#month6').click(function() {
            var now = new Date();
            endTime = now.toString('yyyy-MM-dd HH:mm:ss');
            beginTime = now.addMonths(-6).toString('yyyy-MM-dd HH:mm:ss');
            $("#validDate_datetimepicker").val(beginTime);
            $("#invalidDate_datetimepicker").val(endTime);
        });
        $('#month12').click(function() {
            var now = new Date();
            endTime = now.toString('yyyy-MM-dd HH:mm:ss');
            beginTime = now.addMonths(-12).toString('yyyy-MM-dd HH:mm:ss');
            $("#validDate_datetimepicker").val(beginTime);
            $("#invalidDate_datetimepicker").val(endTime);
        });
    }

    /**
     * 查询按钮
     */
    $('#searchBtn').click(function() {
        var values = $('.form-inline').serializeArray();
        var valuesObj = {};
        $.each(values, function() {
            valuesObj[this.name] = this.value;
        });
        valuesObj.limit = 10;
        valuesObj.offset = 0;
        $('#tb_controlLog').bootstrapTable('refreshOptions', {
            pageNumber : 1,
            url : "/controlLog/list",
            query : valuesObj
        });
    });

});
