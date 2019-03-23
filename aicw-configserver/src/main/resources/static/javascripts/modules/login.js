$(function() {
	$("#username").focus();
	$("#username").keydown(function(event) {
		if ($.trim($("#username").val()) == '') {
			layer.tips('请输入用户名', '#username');
			$("#username").focus();
		} else {
			layer.closeAll('tips');
			if (event.which == "13") {
				$("#password").focus();
			}
		}
	});
	$("#password").keydown(function(event) {
		if ($.trim($("#password").val()) == '') {

			layer.tips('请输入密码', '#password');
			$("#password").focus();
		} else {
			layer.closeAll('tips');
			if (event.which == "13") {
				$("#loginButton").trigger("click");
			}
		}
	});
	$('#loginButton').click(
			function() {
				var username = $('#username').val();
				var password = $('#password').val();

				if (username == '' || password == '') {
					layer.msg('用户名或密码不能为空', {
						offset : 't',
						anim : 6
					});
				} else {
					if ($.trim($("#username").val()) != ''
							&& $.trim($("#password").val()) != '') {
						var userName = $.trim($("#username").val());
						var password = $.trim($("#password").val());
						layer.msg('加载中', {
							icon : 16,
							shade : 0.01
						});
						$.ajax({
							type : "POST",
							url : '/login',
							cache : false,
							contentType : 'application/json',
							data : JSON.stringify({
                                username : userName,
                                password : password
                            }),
							success : function(data) {
								layer.closeAll('loading');
								if (data.code == 1) {
									window.location.href = "/index";
								} else {
									layer.msg(data.msg, {
										offset : 't',
										anim : 6
									});
								}
							}
						});
					} else {
						layer.msg('服务器错误', {
							offset : 't',
							anim : 6
						});
					}

				}
			});

});