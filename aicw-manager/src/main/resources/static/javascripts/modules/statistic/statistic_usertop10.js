var userName,status;
$(function() {
    // 基于准备好的dom，初始化echarts实例
    var logStastic4 = echarts.init(document.getElementById('statistic4'), 'dark');

    option = {
        tooltip : {},
        xAxis : {
            type : 'category',
            data : [  ]
        },
        yAxis : {
            type : 'value'
        },
        series : [ {
            data : [  ],
            type : 'bar'
        } ]
    };

    // 指定图表的初始配置项和数据
    logStastic4.setOption(option);

    logStastic4.showLoading();

    loadUserTop10(null);
    function loadUserTop10(param) {
        $.ajax({
            url : '/statistic/loadUserLogTop10',
            data : param,
            type : 'POST',
            dataType : 'json',
            contentType : 'application/json'
        }).done(function(res) {
            logStastic4.hideLoading();
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
            logStastic4.setOption({
                title : {
                    text : '日志用户'
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

    logStastic4.on('click', function(params) {
        userName = params.name;
        showDetail('策略日志列表' , 'modules/statistic/detailtables/statisticControlLogTable.html', '1200px', '660px', function() {
            parent.userName = '';
        }, true);
    });
})