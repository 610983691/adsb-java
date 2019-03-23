$(function() {

    /***************************************************************************
     * 先这样，这个方式不好,后面再改。
     */
    function Listeners() {
        $("#alarmGroupLi").click(function() {// 切换到告警
            $("#errLogGroup").attr('hidden', 'hidden');
            $("#errLogGroupLi").removeClass('active');
            $("#alarmGroupLi").addClass('active');
            $("#alarmGroup").removeAttr('hidden');
        });
        $("#errLogGroupLi").click(function() {// 切换到失败
            $("#alarmGroup").attr('hidden', 'hidden');
            $("#alarmGroupLi").removeClass('active');
            $("#errLogGroupLi").addClass('active');
            $("#errLogGroup").removeAttr('hidden');
        });
    }
    Listeners();
    
    /**
     * 列表查询
     */
    $('#table1').bootstrapTable({
        url : "/statistic/LoadLatestAlarm10",
        method: 'post',                     // 请求方式（*）
        striped: true,                      // 是否显示行间隔色
        showHeader:false,
        cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        sortable: false,                    // 是否启用排序
        sortOrder: "asc",                   // 排序方式
        sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
        showColumns: false,                  // 是否显示所有的列
        minimumCountColumns: 2,             // 最少允许的列数
        clickToSelect: true,                // 是否启用点击选中行
        uniqueId: "ID",                     // 每一行的唯一标识，一般为主键列
        showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
        cardView: false,                    // 是否显示详细视图
        detailView: false,                  // 是否显示父子表
        queryParams: function(params) {
            return {
                limit: params.limit,
                offset: params.offset,
                dicName: params.search
            }
        },
        columns: [{
            field: 'id',
            title: 'ID',
            visible: false
        },{
            title: '序号',
            width:'5%',
            align:'center',
            formatter:function(value,row,index){
                return index+1;
            }
        },{
            field: 'reciveMsg',
            width: '90%',
            formatter:function(value, row, index) {
                var nameString = "";
                if (value.length > 50) {
                    nameString = value.substring(0,50) + '...';
                } else{
                    nameString = value;
                }
                return '<a data-toggle="tooltip" style="color:#8a6d3b;" title="'+ value +'">' + nameString +'</a>';
            }
        }]
    });
    
    
    /**
     * 列表查询
     */
    $('#table2').bootstrapTable({
        url : "/statistic/loadLatestLog10",
        method: 'post',                     // 请求方式（*）
        striped: true,                      // 是否显示行间隔色
        showHeader:false,
        cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        sortable: false,                    // 是否启用排序
        sortOrder: "asc",                   // 排序方式
        sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
        showColumns: false,                  // 是否显示所有的列
        minimumCountColumns: 2,             // 最少允许的列数
        clickToSelect: true,                // 是否启用点击选中行
        uniqueId: "ID",                     // 每一行的唯一标识，一般为主键列
        showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
        cardView: false,                    // 是否显示详细视图
        detailView: false,                  // 是否显示父子表
        queryParams: function(params) {
            return {
                status : "失败"
            }
        },
        columns: [{
            field: 'id',
            title: 'ID',
            visible: false
        },{
            title: '序号',
            width:'5%',
            align:'center',
            formatter:function(value,row,index){
                return index+1;
            }
        },{
            field: 'mobileMsg',
            title: '日志内容',
            width: '90%',
            formatter:function(value, row, index) {
                var nameString = "";
                if (value.length > 50) {
                    nameString = value.substring(0,50) + '...';
                } else{
                    nameString = value;
                }
                return '<a  data-toggle="tooltip" style="color:#a94442;backgroud-corlor:#f2dede;" title="'+ value +'">' + nameString +'</a>';
            }

        }]
    });
})