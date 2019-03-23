function showAlerts(title, icon, content, callback) {
	layer.open({
		title : title,
		content : content,
		icon : icon,
		yes : function(index) {
			if (typeof callback == 'function') {
				callback();
				layer.close(index);
			} else {
				layer.close(index);
			}
		}
	});
}

function closeWindowInIFrame() {
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}

function showDetail(title, url, width, height, callback) {
	layer.open({
		type : 2,
		title : title,
		shadeClose : false,
		shade : 0.8,
		maxmin : true, // 开启最大化最小化按钮
		area : [ width != null ? width : '1306px',
				height != null ? height : '95%' ],
		content : url,
		end : function(index) {
			if (typeof callback == 'function') {
				callback();
			} else {
				layer.close(index);
			}
		}
	});
}

function selectPanel(title, url, width, height, callback) {
	layer.open({
		type : 2,
		title : title,
		shadeClose : false,
		shade : 0.8,
		maxmin : false, // 开启最大化最小化按钮
		area : [ width != null ? width : '1306px',
				height != null ? height : '95%' ],
		content : [ url, 'no' ],
		btn : [ "确认", "取消" ],
		yes : function(index, layero) {
			var data = window[layero.find('iframe')[0]['name']].formData();
			callback(data);
			layer.close(index);
		},
		btn1 : function(index) {
			layer.close(index);
		},
		success : function(layero, index) {
			layer.close(loading);
		}

	});
	var loading = layer.load(1);
}

function fixWidth(percent) {
	return (document.body.clientWidth - 5) * percent;
}

function confirmAlert(content, oktext, notext, okcallback, nocallback) {
	layer.confirm(content, {
		btn : [ oktext, notext ]
	}, function() {
		layer.closeAll('dialog');
		if (typeof okcallback == 'function') {
			okcallback();
		}
	}, function() {
		if (typeof nocallback == 'function') {
			nocallback();
		} else {
			return;
		}
	});
}

function showSuccessMsgs(content, callback) {
	if (typeof callback == 'function') {
		layer.msg(content, {
			icon : 1,
			time : 1000
		// 1秒关闭（如果不配置，默认是3秒）
		}, function() {
			callback();
		});
	} else {
		layer.msg(content, {
			icon : 1
		});
	}
}

function showFailureMsgs(content, callback) {
	if (typeof callback == 'function') {
		layer.msg(content, {
			icon : 2,
			time : 1000
		// 1秒关闭（如果不配置，默认是3秒）
		}, function() {
			callback();
		});
	} else {
		layer.msg(content, {
			icon : 2
		});
	}
}