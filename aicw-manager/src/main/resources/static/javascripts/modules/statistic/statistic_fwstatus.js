var testLoginStatus;
$(function() {

    // 基于准备好的dom，初始化echarts实例
    var logStastic6 = echarts.init(document.getElementById('statistic6'), 'dark');

    option = {
        tooltip : {},
        xAxis : {
            type : 'category',
            data : [ '成功', '失败' ]
        },
        yAxis : {
            type : 'value'
        },
        series : [ {
            data : [ 22, 2 ],
            type : 'bar'
        } ]
    };

    // 指定图表的初始配置项和数据
    logStastic6.setOption(option);

    loadfwStatus(null);
    function loadfwStatus(param) {
        $.ajax({
            url : '/statistic/loadfwTestStatus',
            data : param,
            type : 'POST',
            dataType : 'json',
            contentType : 'application/json'
        }).done(function(res) {
            logStastic6.hideLoading();
            res = JSON.parse(res.msg);
            if (res == null) {
                return;
            }
            var countArr = new Array();
            var userArr = new Array();
            for (var i = 0; i < res.length; i++) {
                userArr.push(res[i].name);
                countArr.push(res[i].value);
            }
            logStastic6.setOption({
                title : {
                    text : '防火墙网络状态'
                },
                xAxis : {
                    type : 'category',
                    data : userArr
                },
                yAxis : {
                    type : 'value'
                },
                series : [ {
                    data : countArr,
                    type : 'bar'
                } ]
            });
        });
    }

    logStastic6.on('click', function(params) {
        testLoginStatus = params.name == "失败" ? 0 : 1;
        showDetail('防火墙列表' , '/modules/statistic/detailTables/fw_detailTable.html', '1200px', '600px', function() {
            parent.testLoginStatus = '';
        }, true);
    });
})