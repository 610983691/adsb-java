$(function() {
	
	$('.sidebar-nav a[data-toggle="collapse"]').click(function(e) {
		if ($(this).hasClass('collapsed')) {
			$(this).removeClass('collapsed');
		} else {
			$(this).addClass('collapsed');
		}
	});

	//响应式布局菜单
	var uls = $('.sidebar-nav > ul > *').clone();
	uls.addClass('visible-xs');
	$('#main-menu').append(uls.clone());

	//进入首页视图
	$('#pagecontent').load("/modules/configitem/config-item.html");

	/**
	 * 首页-个人信息菜单点击事件
	 */
	$(function() {
		$('.dropdown-menu li a').click(function() {
			var url = $(this).attr("data-url");
			if (url) {
				$('#pagecontent').load(url);
			}
		});
	});
	
	/**
	 * 首页-菜单点击事件
	 */
	$(function() {
		$('.sidebar-nav ul li a').click(function() {
			var url = $(this).attr("data-url");
			if (url) {
				$('#pagecontent').load(url);
			}
		});
	});
	
	if (isAdmin != 1) {
		$('.sidebar-nav ul li a[data-target=".systemmgr-menu"]').parent('li').next().remove();
		$('.sidebar-nav ul li a[data-target=".systemmgr-menu"]').parent('li').remove();
	}
});

