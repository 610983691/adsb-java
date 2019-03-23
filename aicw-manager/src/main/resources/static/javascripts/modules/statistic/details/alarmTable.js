
$(function() {
    /**
     * 列表查询
     */
    $('#tb_alarmDetail').bootstrapTable({
        url : "/alarm/listDetail",
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
        pageList : [ 10, 20,  50 ], // 可供选择的每页的行数（*）
        strictSearch : true,
        showColumns : false, // 是否显示所有的列
        showRefresh : false, // 是否显示刷新按钮
        minimumCountColumns : 2, // 最少允许的列数
        clickToSelect : true, // 是否启用点击选中行
        uniqueId : "ID", // 每一行的唯一标识，一般为主键列
        showToggle : false, // 是否显示详细视图和列表视图的切换按钮
        cardView : false, // 是否显示详细视图
        detailView : false, // 是否显示父子表
        queryParams : function(params) {// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数
            var valuesObj = {
                beginTime : parent.beginTime,
                endTime : parent.endTime,
                mobileNumber:parent.mobileNumber,
                mobileProvince:parent.mobileProvince
            };
            valuesObj.limit = params.limit;
            valuesObj.offset = params.offset;
            return valuesObj;
        },
        onClickCell : function(field, value, row) { // 单击操作列显示详情
            if (field !== 'reciveMsg') {
                return;
            }
            
            layer.open({
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['420px', '240px'], //宽高
                title : '短信内容',
                content : '<div class=""><textarea  style="width:400px;display: block;height: 160px;padding: 6px 12px;font-size: 14px;line-height: 1.42857143;color: #555;background-color: #fff; background-image: none;border: 1px solid #ccc;border-radius: 4px;">' + value + '</textarea></div>',
                icon : -1,
                yes : function(index) {
                        layer.close(index);
                }
            });
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
            field : 'receiveDate',
            title : '拦截时间'
        }, {
            field : 'reciveMsg',
            title : '短信内容',
            width : '300'
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
