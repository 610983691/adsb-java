/**
 * 
 */
$(function() {
	/**
	 * 上传文本框
	 */
	var fileInput = $('#exlFile');
	
	/**
	 * 初始化上传控件的样式
	 */
	fileInput.fileinput({
		language : 'zh', // 设置语言
		uploadUrl : '/fwInfo/upload', // 上传的地址
		allowedFileExtensions: ["xls", "xlsx"], //接收的文件后缀
		// uploadExtraData:{"id": 1, "fileName":'123.mp3'},
		uploadAsync : true, // 默认异步上传
		showUpload : true, // 是否显示上传按钮
		showRemove : false, // 显示移除按钮
		showPreview : false, // 是否显示预览
		showCaption : true,// 是否显示标题
		browseClass : 'btn btn-default', // 按钮样式
		dropZoneEnabled : false,// 是否显示拖拽区域
		// minImageWidth: 50, //图片的最小宽度
		// minImageHeight: 50,//图片的最小高度
		// maxImageWidth: 1000,//图片的最大宽度
		// maxImageHeight: 1000,//图片的最大高度
		// maxFileSize:0,//单位为kb，如果为0表示不限制文件大小
		// minFileCount: 0,
		maxFileCount : 1, // 表示允许同时上传的最大文件个数
		enctype : 'multipart/form-data',
		validateInitialCount : true,
//		previewFileIcon : '<iclass='glyphicon glyphicon-king'></i>',
		msgFilesTooMany : '选择上传的文件数量({n}) 超过允许的最大数值{m}！',
		msgInvalidFileExtension : '不正确的类型 "{name}"',
		msgValidationError: '不正确的文件类型'
	});
	
	/**
	 * 文件选择后的回调
	 */
	fileInput.on("filebatchselected", function(event, files) {
		if (files.length == 0) {
			showFailureMsgs('不正确的文件类型，请重新选择！');
		}
	});
	
	/**
	 * 文件上传成功后的回调
	 */
	fileInput.on("fileuploaded", function (event, data, previewId, index){
		if (data.response.success) {
			showSuccessMsgs(data.response.msg, function(){
				fileInput.fileinput('reset');
				closeWindowInIFrame();
			});
		} else {
			fileInput.fileinput('reset');
			showFailureMsgs(data.response.msg);
			setTimeout('myrefresh()',2000); //指定2秒刷新一次
		}
	});
	 /**
     * 重置按钮
     */
    $('.btn-default').click(function(){
    	fileInput.fileinput('reset');
    });
	 /**
     * 取消按钮
     */
    $('.btn-info').click(function(){
    	closeWindowInIFrame();
    });
});
function myrefresh()
{
   window.location.reload();
}
