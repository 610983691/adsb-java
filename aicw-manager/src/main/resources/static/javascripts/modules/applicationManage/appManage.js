$(function() {
	/**
	 * 列表查询
	 */
	$('#tb_appManage').bootstrapTable({
		url : "/applicationManage/list",
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
		// search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
		// formatSearch: function () {
		// return 'ID';
		// },
		strictSearch : true,
		showColumns : true, // 是否显示所有的列
		showRefresh : true, // 是否显示刷新按钮
		minimumCountColumns : 2, // 最少允许的列数
		clickToSelect : true, // 是否启用点击选中行
		// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		uniqueId : "ID", // 每一行的唯一标识，一般为主键列
		showToggle : true, // 是否显示详细视图和列表视图的切换按钮
		cardView : false, // 是否显示详细视图
		detailView : false, // 是否显示父子表
		queryParams : function(params) {
			return {
				limit : params.limit,
				offset : params.offset,
				appNameLike : $("#txt_search_app_name").val(),
				appAliasLike : $("#txt_search_app_alias").val(),
				fwId : $("#txt_search_fwId").val(),
				approveUser : $("#txt_search_approveUser").val()
			}
		},
		columns : [ {
			checkbox : true
		}, {
			field : 'id',
			title : 'ID',
			visible : false
		}, {
			field : 'appName',
			title : '应用名称'
		}, {
			field : 'appAlias',
			title : '应用简称'
		}, {
			field : 'fwName',
			title : '出口防火墙名称'
		}, {
			field : 'fwIp',
			title : '出口防火墙IP'
		}, {
			field : 'approveUserName',
			title : '审批人'
		}, {
			field : 'appIpUrl',
			title : '域名'
		}, {
			field : 'conIp',
			title : '访问内网IP'
		}, {
			field : 'conPort',
			title : '访问内网端口'
		}, {
			field : 'isUseUrl',
			title : '是否通过域名访问',
			formatter : function(value, row, index) {
				if (value == 1) {
					return '是';
				} else {
					return '否';
				}
			}
		}, {
			field : 'isApprove',
			title : '是否审批',
			formatter : function(value, row, index) {
				if (value == 1) {
					return '是';
				} else {
					return '否';
				}
			}
		} ]
	});
	$('#chooseFw').click(function() {
        showDetail('选择防火墙', '/modules/fwInfo/fw_select.html', '800px', '550px', function() {
            // 通过回调填写下拉框的值
            if (chooseFw != null && chooseFw[0] != null) {
                $('#chooseFw').val(chooseFw[0].fwName);
                $('#txt_search_fwId').val(chooseFw[0].id);
            }
        },true);
    });
	 /*
     * 清空文本框
     */
    $('#removeFw').click(function() {
        $('#chooseFw').val('');
        $('#txt_search_fwId').val('');
    });
	 //审批人 选择
    $('#chooseApproveUser').click(function() {
        showDetail('选择审批人', '/modules/user/user_select.html', '800px', '550px', function() {
            // 通过回调填写下拉框的值
            if (chooseUser != null && chooseUser[0] != null) {
                $('#chooseApproveUser').val(chooseUser[0].userName);
                $('#txt_search_approveUser').val(chooseUser[0].userAccount);
            }
        },true);
    });
    /*
     * 清空文本框
     */
    $('#removeApproveUser').click(function() {
        $('#chooseApproveUser').val('');
        $('#txt_search_approveUser').val('');
    });
	// 查询
	$('#btn_query').click(function() {
		$('#tb_appManage').bootstrapTable('refresh');
	} );
	$('#btn_add').click(
			function() {
				showDetail('增加应用',
						'/modules/applicationManage/appManage_add.html',
						'800px', '350px', function() {
							$("#tb_appManage").bootstrapTable('refresh');
						});
			});

	$('#btn_edit').click(
			function() {
				var sel = $("#tb_appManage").bootstrapTable('getSelections');
				if (sel.length != 1) {
					showFailureMsgs('请选择一条记录！');
					return;
				}
				if (sel[0].isAdmin == 1) {
					showFailureMsgs('超级管理员无法修改！');
					return;
				}
				selData = sel[0];
				showDetail('修改应用',
						'/modules/applicationManage/appManage_edit.html',
						'800px', '350px', function() {
							$("#tb_appManage").bootstrapTable('refresh');
						});
			});

	$('#btn_delete').click(function() {
		var sel = $("#tb_appManage").bootstrapTable('getSelections');
		if (sel.length <= 0) {
			showFailureMsgs('请至少选择一条记录！');
			return;
		}
		var canDel = true;
		$.each(sel, function(val) {
			if (this.isAdmin == 1) {
				return canDel = false;
			}
		});
		if (!canDel) {
			showFailureMsgs('超级管理员无法删除！');
			return;
		}
		confirmAlert('确定删除？', '确定', '取消', function() {
			var selJson = JSON.stringify(sel);
			layer.msg('加载中', {
				icon : 16,
				shade : 0.01
			});
			$.ajax({
				type : "POST",
				url : '/applicationManage/delete',
				cache : false,
				dataType : 'json',
				contentType : 'application/json',
				data : selJson,
				success : function(data) {
					layer.closeAll('loading');
					if (data.success) {
						showSuccessMsgs(data.msg);
						$("#tb_appManage").bootstrapTable('refresh');
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
var selData = null;