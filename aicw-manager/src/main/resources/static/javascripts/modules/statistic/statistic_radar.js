$(function() {
    /***************************************************************************
     * logStastic1 是画雷达图，默认一开始坐标轴无数据。异步从后端加载，加载完成后使用setOption填充数据，实现数据更新。
     */
    // 基于准备好的dom，初始化echarts实例
    var logStastic1 = echarts.init(document.getElementById('statistic1'), 'dark');
    // 指定图表的配置项和数据
    var option1 = {
        tooltip : {
            axisPointer : {
                type : 'line'
            },
            axis : 'radius'
        },
        toolbox : {
            orient : 'vertical',
            itemSize : 30
        },
        radar : {
            shape:'circle',
            splitNumber:4,
            nameGap :1,
            name : {
                textStyle : {
                    color : '#fff',
                    backgroundColor : '#999',
                    borderRadius : 3,
                    padding : [ 3, 5 ]
                }
            },
            triggerEvent:true ,
            indicator : [ {
                name : '成功',
                max : 6500,
                color:"#8dc1a9"
            }, {
                name : '失败',
                max : 16000,
                color:"#dd6b66"
            }, {
                name : '等待管理员审批',
                max : 38000,
                color:"#e69d87"
            }, {
                name : '管理员未同意',
                max:100,
                color : "#87cefa"
            }, {
                name : '策略告警',
                max : 60000,
                color:"#ffd600"
            } ]
        },
        series : [ {
            name : '告警/日志',
            type : 'radar',
            data : [ {
                value : [ 4300, 10000, 28000, 35000, 50000 ],
                name : '告警/日志统计'
            } ]
        } ]
    };
    logStastic1.setOption(option1);
    logStastic1.showLoading();
    loadRadarDatas(null);
    function loadRadarDatas(param) {
        $.ajax({
            url : '/statistic/loadRadarDatas',
            data : param,
            type : 'POST',
            dataType : 'json',
            contentType : 'application/json'
        }).done(function(res) {
            logStastic1.hideLoading();
            res = JSON.parse(res.msg);
            if (res == null) {
                return;
            }
            var values = new Array();
            for (var i = 0; i < res.length; i++) {
                values.push(res[i].value);
            }
            option1.series[0].data[0].value = values;
            for(var i =0;i<5;i++){
                option1.radar.indicator[i].max=values[i]*(Math.random()+1);//坐标轴的最大值动态调整
            }
            logStastic1.setOption(option1);
        });
    }
    logStastic1.on('click', function (params) {
        if(params.name=="告警/日志统计"){
            return;
        }
        if(params.name=="策略告警"){
            showDetail('告警列表', '/modules/statistic/detailTables/alarm/alarmTable.html', '1200px', '660px', null, true);
        }else{
            status = params.name;
            showDetail('策略日志列表', 'modules/statistic/detailtables/statisticControlLogTable.html', '1200px', '660px', function() {
                parent.status = '';
            }, true);
        }
    });
    
});
var status;