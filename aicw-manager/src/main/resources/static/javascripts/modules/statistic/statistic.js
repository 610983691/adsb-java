$(function() {
    /***************************************************************************
     * 用于查询用户数、防火墙数量、应用数量
     */
    var userCount, appCount, fwCount;
    function showCenterArea(){
        $.ajax({
            url:'/statistic/loadFwCount',
            type:'POST',
            success:function(res){
                $("#fwCount").html(res.msg);
            }
        });
        $.ajax({
            url:'/statistic/loadAppCount',
            type:'POST',
            success:function(res){
                $("#appCount").html(res.msg);
            }
        });
        $.ajax({
            url:'/statistic/loadUserCount',
            type:'POST',
            success:function(res){
                $("#userCount").html(res.msg);
            }
        });
    }
    showCenterArea();
    
    /***
     * 点击数量跳转到对应的列表页面
     */
    function showDetail(){
        $("#fwCount").click(function(){
            $('#pagecontent').load("/modules/fwInfo/fwInfoList.html");
        });
        $("#userCount").click(function(){
            $('#pagecontent').load("/modules/user/user.html");
        });
        $("#appCount").click(function(){
            $('#pagecontent').load("/modules/applicationManage/appManageList.html");
        });
    }
    showDetail();
});