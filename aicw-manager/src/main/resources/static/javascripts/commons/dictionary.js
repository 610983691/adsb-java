/*******************************************************************************
 * 从后台获取数据字典
 * 
 * @param typeCode
 * @param 回调
 * @returns
 */
function loadDictByType(typeCode, func, parentDicId) {
    $.ajax({
        type : "POST",
        url : '/dict/list',
        cache : false,
        dataType : 'json',
        contentType : 'application/json',
        data : JSON.stringify({
            'dicTypeCode' : typeCode,
            'parentDicId' : parentDicId
        }),
        success : func
    });
}

function parse(arr) {
    var dict = new Array();
    for ( var i in arr) {
        dict.push({
            'id' : arr[i].id,
            'dicName' : arr[i].dicName,
            'dicValue' : arr[i].dicValue
        });
    }
    return dict;
}

function parseOptionHtml(json) {
    var optionstring = "";
    optionstring += "<option  value='' >请选择</option>";
    for (var j = 0; j < json.length; j++) {
        optionstring += "<option  value='" + json[j].dicValue + "' >" + json[j].dicName + "</option>";
    }
    return optionstring;
}

function getIdByVal(val, arr) {
    if(arr ==undefined){
        return '';
    }
    for (var i = 0; i < arr.length; i++) {
        if (arr[i].dicValue == val) {
            return arr[i].id;
        }
    }
}
/***
 * 根据字典值获取字典名称
 * @param val
 * @param arr
 * @returns
 */
function getNameByVal(val,arr){
    if(arr ==undefined){
        return '';
    }
    for (var i = 0; i < arr.length; i++) {
        if (arr[i].dicValue == val) {
            return arr[i].dicName;
        }
    }
}

/***
 * 初始化select下拉框，并赋值初始选中的值。
 * @param selector 下拉框的jquery选择器
 * @param dictId 下拉框需要填充的数据字典ID
 * @param defaultVal 下拉框的初始值
 */
function initSelector(selector,dictId,defaultVal){
    loadDictByType(dictId, function(data) {
        var json = parse(data.rows);
        $(selector).html(parseOptionHtml(json));
        if(defaultVal !=undefined){
            $(selector).selectpicker('val', defaultVal);
        }
        $(selector).selectpicker('refresh');
    });
}