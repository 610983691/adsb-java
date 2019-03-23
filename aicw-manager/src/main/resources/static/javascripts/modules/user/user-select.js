$(function () {
	/**
	 * 列表查询
	 */
    $('#tb_userSelect').bootstrapTable({
        url : "/user/list",
        method: 'post',                     //请求方式（*）
        toolbar: '#toolbar',                //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: false,                    //是否启用排序
        sortOrder: "asc",                   //排序方式
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber:1,                       //初始化加载第一页，默认第一页
        pageSize: 15,                       //每页的记录行数（*）
        pageList: [10, 15, 30, 50, 100],    //可供选择的每页的行数（*）
        search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        formatSearch: function () {
            return '用户名称';
        },
        strictSearch: true,
        showColumns: false,                  //是否显示所有的列
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
//        height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
        showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        queryParams: function(params) {
        	return {
        		limit: params.limit,
        		offset: params.offset,
        		userName: params.search
        	}
        },
        columns: [{
            checkbox: true
        }, {
            field: 'userId',
            title: 'ID',
            visible: false
        },  {
            field: 'userName',
            title: '用户名称'
        }, {
            field: 'userAccount',
            title: '用户账号'
        }, {
            field: 'userGender',
            title: '性别'
        }, {
            field: 'telphone',
            title: '电话号码'
        }, {
            field: 'userStatus',
            title: '用户状态',
            formatter:function (value,row,index) {
                if (value == 1) {
                    return '启用';
                } else {
                    return '禁用';
                }
            }
        }, {
            field: 'isReviceMsg',
            title: '短信权限'
        }]
    });

    /**
	 * 确认按钮
	 */
    $('.btn-warning').click(function(){
    	var choosed = $('#tb_userSelect').bootstrapTable('getSelections');
    	parent.chooseUser = choosed;
    	closeWindowInIFrame();
    });
    
    /**
     * 取消按钮
     */
    $('.btn-info').click(function(){
    	var choosed = $('#tb_userSelect').bootstrapTable('getSelections');
    	parent.chooseUser = choosed;
    	closeWindowInIFrame();
    });
    
});
