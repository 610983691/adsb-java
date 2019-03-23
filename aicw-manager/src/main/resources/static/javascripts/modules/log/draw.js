$(function() {
    /***************************************************************************
     * logStastic1 是画柱状图，默认一开始坐标轴无数据。异步从后端加载，加载完成后使用setOption填充数据，实现数据更新。
     */
    // 基于准备好的dom，初始化echarts实例
    var logStastic1 = echarts.init(document.getElementById('logStastic1'));
    // 指定图表的配置项和数据
    var option1 = {
        title : {
            text : '防火墙下发数量统计',
            textStyle : {
                align : 'center'
            }
        },
        xAxis : {},
        yAxis : {
            data : []
        },
        series : [ {
            name : '防火墙策略',
            type : 'bar',
            data : []
        } ]
    };
    logStastic1.showLoading();
    loadIPStatistic(JSON.stringify({
        limit : 10,
        offset : 0
    }));// 默认加载这个
    function loadIPStatistic(param) {
        $.ajax({
            url : '/controlLog/statisticByIP',
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
            var countArr = new Array();
            var fwIpArr = new Array();
            for (var i = 0; i < res.length; i++) {
                fwIpArr.push(res[i].name);
                countArr.push(res[i].value);
            }
            logStastic1.setOption({
                title : {
                    text : '防火墙下发数量统计',
                    textStyle : {
                        align : 'center'
                    }
                },
                tooltip : {
                    trigger : 'axis',
                    formatter : '数量 {c0}<br />防火墙IP:{b0}'
                },
                xAxis : {
                    type : 'value',
                },
                yAxis : {
                    data : fwIpArr,
                    nameRotate : 45,
                    splitNumber : fwIpArr.length,
                    boundaryGap : true,
                    axisTick : {
                        alignWithLabel : true,
                    }
                },
                series : [ {
                    name : '防火墙策略',
                    type : 'bar',
                    data : countArr
                } ]
            });
        });
    }

    /**
     * 柱状图的点击事件
     */
    logStastic1.on('click', function(params) {
        var values = $('.form-inline').serializeArray();
        var valuesObj = {};
        $.each(values, function() {
            valuesObj[this.name] = this.value;
        });
        valuesObj.fwIp = params.name;
        $("#fwIp").val(params.name);
        valuesObj.status = '';
        $("#status").selectpicker('val', '');
        $('#status').selectpicker('refresh');
        $('#tb_controlLog').bootstrapTable('refreshOptions', {
            pageNumber : 1,
            url : "/controlLog/list",
            query : valuesObj
        });
        // $('#tb_controlLog').bootstrapTable('refresh', {
        // url : "/controlLog/list",
        // query : valuesObj
        // });
    });

    /***************************************************************************
     * logStastic2 是画饼图，一开始无数据。异步从后端加载，加载完成后使用setOption填充数据，实现数据更新。
     */
    // 基于准备好的dom，初始化echarts实例
    var logStastic2 = echarts.init(document.getElementById('logStastic2'));

    // 指定图表的初始配置项和数据
    logStastic2.setOption({
        title : {
            text : '防火墙日志统计',
            x : 'center'
        },
        tooltip : {
            trigger : 'item',
            formatter : "{a} <br/>{b} : {c} ({d}%)"
        },
        series : [ {
            name : '统计',
            type : 'pie',
            radius : '55%',
            center : [ '50%', '60%' ],
            data : [],
            itemStyle : {
                emphasis : {
                    shadowBlur : 10,
                    shadowOffsetX : 0,
                    shadowColor : 'rgba(0, 0, 0, 0.5)'
                }
            }
        } ]
    });
    logStastic2.showLoading();
    loadStatusStatistic(JSON.stringify({
        limit : 10,
        offset : 0
    }));// 默认加载这个
    function loadStatusStatistic(params) {
        $.ajax({
            url : '/controlLog/statisticByStatus',
            data : params,
            type : 'POST',
            dataType : 'json',
            contentType : 'application/json'
        }).done(function(res) {
            logStastic2.hideLoading();
            res = JSON.parse(res.msg);
            if (res == null) {
                return;
            }
            logStastic2.setOption({
                title : {
                    text : '防火墙日志统计',
                    x : 'center'
                },
                tooltip : {
                    trigger : 'item',
                    formatter : "{a} <br/>{b} : {c} ({d}%)"
                },
                series : [ {
                    name : '统计',
                    type : 'pie',
                    radius : '55%',
                    center : [ '50%', '60%' ],
                    data : res,
                    itemStyle : {
                        emphasis : {
                            shadowBlur : 10,
                            shadowOffsetX : 0,
                            shadowColor : 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                } ]
            });
        });
    }

    /**
     * 饼图的点击事件
     */
    logStastic2.on('click', function(params) {
        var values = $('.form-inline').serializeArray();
        var valuesObj = {};
        $.each(values, function() {
            valuesObj[this.name] = this.value;
        });
        valuesObj.fwIp = '';
        $("#fwIp").val('');
        valuesObj.status = params.name;
        $("#status").selectpicker('val', params.name);
        $('#status').selectpicker('refresh');
        
        $('#tb_controlLog').bootstrapTable('refreshOptions', {
            pageNumber : 1,
            url : "/controlLog/list",
            query : valuesObj
        });
//        $('#tb_controlLog').bootstrapTable('refresh', {
//            silent : true,
//            url : "/controlLog/list",
//            query : valuesObj
//        });
    });
    /**
     * 刷新表格
     */
    $('#searchBtn').click(function() {
        var values = $('.form-inline').serializeArray();
        var valuesObj = {};
        $.each(values, function() {
            valuesObj[this.name] = this.value;
        });
        loadStatusStatistic(JSON.stringify(valuesObj));
        loadIPStatistic(JSON.stringify(valuesObj));
    });
})