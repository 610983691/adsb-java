var beginTime, endTime, mobileNumber,mobileProvince;
$(function() {
    // 基于准备好的dom，初始化echarts实例
    var logStastic5 = echarts.init(document.getElementById('statistic5'), 'dark');
    option = {
        tooltip : {
            trigger : 'axis',
            axisPointer : { // 坐标轴指示器，坐标轴触发有效
                type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend : {
            data : [ '本月', '上月', '半年', '一年' ]
        },
        grid : {
            left : '3%',
            right : '4%',
            bottom : '3%',
            containLabel : true
        },
        xAxis : {
            type : 'value'
        },
        yAxis : {
            type : 'category',
            data : []
        },
        series : [ {
            name : '本月',
            type : 'bar',
            stack : '总量',
            label : {
                normal : {
                    show : true,
                    position : 'insideRight'
                }
            },
            data : []
        }, {
            name : '上月',
            type : 'bar',
            stack : '总量',
            label : {
                normal : {
                    show : true,
                    position : 'insideRight'
                }
            },
            data : []
        }, {
            name : '半年',
            type : 'bar',
            stack : '总量',
            label : {
                normal : {
                    show : true,
                    position : 'insideRight'
                }
            },
            data : []
        }, {
            name : '一年',
            type : 'bar',
            stack : '总量',
            label : {
                normal : {
                    show : true,
                    position : 'insideRight'
                }
            },
            data : []
        } ]
    };

    logStastic5.setOption(option);
    logStastic5.showLoading();
    loadTop10Phone(null);
    function loadTop10Phone(param) {
        $.ajax({
            url : '/statistic/loadTop10Phone',
            data : param,
            type : 'POST',
            dataType : 'json',
            contentType : 'application/json'
        }).done(function(res) {
            logStastic5.hideLoading();
            res = JSON.parse(res.msg);
            if (res == null) {
                return;
            }
            option.yAxis.data=getNameArrayFromNameValueArr(res.month);//必须取本月的数据为基准
            option.series[0].data=getValueArrayFromNameValueArr(option.yAxis.data,res.month);//本月数据
            option.series[1].data=getValueArrayFromNameValueArr(option.yAxis.data,res.month1);//上月数据
            option.series[2].data=getValueArrayFromNameValueArr(option.yAxis.data,res.month6);//半年数据
            option.series[3].data=getValueArrayFromNameValueArr(option.yAxis.data,res.history);//历史数据
            logStastic5.setOption(option);
        });
    }
    
    
    function getNameArrayFromNameValueArr(origin){
        var names = new Array();
        for (var i = 0; i < origin.length; i++) {
            names.push(origin[i].name);
        }
        return names;
    }
    
    function getValueArrayFromNameValueArr(legend,origin){
        var values = new Array();
        for (var i = 0; i < legend.length; i++) {
            var phone = legend[i];
            var count ='';
            for(var j=0;j<origin.length;j++){
                if(origin[j].name ===phone){
                    count = origin[j].value;
                    break;
                }
            }
            values.push(count);
        }
        return values;
    }
    
    logStastic5.on('click', function (params) {
        timeCaculator(params.seriesName);
        mobileNumber = params.name;
        showDetail('告警列表', '/modules/statistic/detailTables/alarm/alarmTable.html', '1200px', '660px', function(){
            parent.mobileNumber='';
        }, true);
    });
    
    
    /***************************************************************************
     * 时间范围选择事件监听
     */
    function timeCaculator(seriesName) {
        if(seriesName=="本月"){
            var now = new Date();
            endTime = now.toString('yyyy-MM-dd HH:mm:ss');
            now.setDate(1);
            now.setHours(0);
            now.setMinutes(0);
            now.setSeconds(0);
            now.setMilliseconds(0);
            beginTime = now.toString('yyyy-MM-dd HH:mm:ss');
        }
        if(seriesName=="上月"){
            var now = new Date();
            var lastMonth= now.addMonths(-1);
            lastMonth.setDate(1);
            lastMonth.setHours(0);
            lastMonth.setMinutes(0);
            lastMonth.setSeconds(0);
            lastMonth.setMilliseconds(0);
            beginTime = lastMonth.toString('yyyy-MM-dd HH:mm:ss');
            lastMonth = lastMonth.moveToLastDayOfMonth();
            lastMonth.setHours(23);
            lastMonth.setMinutes(59);
            lastMonth.setSeconds(59);
            lastMonth.setMilliseconds(999);
            endTime = lastMonth.toString('yyyy-MM-dd HH:mm:ss');
        }
        if(seriesName=="半年"){
            var now = new Date();
            endTime = now.toString('yyyy-MM-dd HH:mm:ss');
            beginTime = now.addMonths(-6).toString('yyyy-MM-dd HH:mm:ss');
        }
        if(seriesName=="一年"){
            var now = new Date();
            endTime = now.toString('yyyy-MM-dd HH:mm:ss');
            beginTime = now.addMonths(-12).toString('yyyy-MM-dd HH:mm:ss');
        }
    }
});