var beginTime, endTime, alarmLogId;
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
    timeClickListener();

    /***************************************************************************
     * 时间范围选择事件监听
     */
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
     * 列表查询
     */
    $('#tb_alarm').bootstrapTable({
        url : "/alarm/list",
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
        pageList : [ 10, 15, 30, 50 ], // 可供选择的每页的行数（*）
        strictSearch : true,
        showColumns : false, // 是否显示所有的列
        showRefresh : false, // 是否显示刷新按钮
        minimumCountColumns : 2, // 最少允许的列数
        clickToSelect : true, // 是否启用点击选中行
        uniqueId : "ID", // 每一行的唯一标识，一般为主键列
        showToggle : false, // 是否显示详细视图和列表视图的切换按钮
        cardView : false, // 是否显示详细视图
        detailView : false, // 是否显示父子表
        onDblClickRow : function(row) { // 双击显示详情
            alarmLogId = row.id;
            beginTime = $("#validDate_datetimepicker").val();
            endTime = $("#invalidDate_datetimepicker").val();
            showDetail('短信详情', '/modules/alarm/alarmDetail.html', '1000px', '600px', null, true);
        },
        onClickCell : function(field, value, row) { // 单击操作列显示详情
            if (field !== 'Button') {
                return;
            }
            alarmLogId = row.id;
            beginTime = $("#validDate_datetimepicker").val();
            endTime = $("#invalidDate_datetimepicker").val();
            showDetail('短信详情', '/modules/alarm/alarmDetail.html', '1000px', '600px', null, true);
        },
        columns : [ {
            field : 'id',// alarm_log_msg表的主键ID
            title : 'ID',
            visible : false
        }, {
            field : 'mobileNumber',
            title : '来源号码'
        }, {
            field : 'mobileProvince',
            title : '号码归属省份'
        }, {
            field : 'mobileCity',
            title : '号码归属地'
        }, {
            field : 'reciveCount',
            title : '拦截次数'
        }, {
            field : 'createDate',
            title : '拦截时间'
        }, {
            field : 'Button',
            title : '操作',
            align : 'center',
            formatter : '<button type="button" id="clickToDetail" class="btn btn-default btn-sm">详情</button>'
        } ]
    });

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
        $('#tb_alarm').bootstrapTable('refresh', {
            silent : true,
            url : "/alarm/list",
            query : valuesObj
        });
    });
});
