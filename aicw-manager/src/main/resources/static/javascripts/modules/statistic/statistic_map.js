var beginTime, endTime, mobileNumber,mobileProvince;
$(function() {
    var myChart = echarts.init(document.getElementById('statistic2'), 'dark');
    var option = {
        tooltip : {
            trigger : 'item'
        },
        // 左侧小导航图标
        visualMap : {
            min : 0,
            max : 1000,
            text : [ 'High', 'Low' ],
            realtime : false,
            calculable : true,
            inRange : {
                color : [ 'lightskyblue', 'yellow', 'orangered' ]
            }
        },
        series : [ {
            name : '数据',
            type : 'map',
            mapType : 'china',
            roam : true,
            label : {
                emphasis : {
                    show : false
                }
            },
            data : []
        } ]
    };

    myChart.setOption(option);
    myChart.on('mouseover', function(params) {
        var dataIndex = params.dataIndex;
    });

    function loadChinaMapData() {
        $.ajax({
            url : 'statistic/loadMapData',
            type : 'POST',
            contentType : 'application/json',
            success : function(res) {
                var msg = JSON.parse(res.msg);
                myChart.setOption({
                    tooltip : {
                        trigger : 'item'
                    },
                    // 左侧小导航图标
                    visualMap : {
                        min : 0,
                        max : 1000,
                        text : [ 'High', 'Low' ],
                        realtime : false,
                        calculable : true,
                        inRange : {
                            color : [ 'lightskyblue', 'yellow', 'orangered' ]
                        }
                    },
                    series : [ {
                        name : '数据',
                        type : 'map',
                        mapType : 'china',
                        roam : true,
                        label : {
                            emphasis : {
                                show : false
                            }
                        },
                        data : msg
                    } ]
                });
            }
        });
    }

    loadChinaMapData();
    myChart.on('click', function(params) {
        mobileProvince =params.name;
        showDetail('告警列表', '/modules/statistic/detailTables/alarm/alarmTable.html', '1200px', '660px', function(){
            parent.mobileProvince='';
        }, true);
    });
})