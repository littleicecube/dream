
function closeDialog() {
        $("#myModal").css("visibility", "hidden")
}
function showInDialog(data){
        $("#consoleId").html(data);
        $("#myModal").css("visibility", "visible");
}
//添加语句
function insertSql() {
        var text = "";
        $.each($("#linesId").find("[data-select='select']"), function (i, ele) {
                text += $(ele).find("span").text();
        });
        text = text.replace(/\s+/g, "");
        if (text == "") {
                return;
        }
        text = _delEndWith(text, ",");
        if ("{" != text.substring(0, 1)) {
                var index = 0;
                var arrIndex = text.indexOf(":[");
                if(arrIndex <= 0){
                        var objIndex = text.indexOf(":{");
                        if(objIndex <=0){
                                console.log("错误的开始符号");
                                return;
                        }
                        index = objIndex;
                }else{
                        var objIndex = text.indexOf(":{");
                        if(objIndex > 0 && objIndex < arrIndex){
                                index = objIndex;
                        }else{
                                index = arrIndex;
                        }
                }
                if (index > 0) {
                        text = text.substring(index + 1, text.length);
                }
        }
        var object = JSON.parse(text, (key, value) => {
                if (typeof value !== 'string') {
                        return value;
                }
                if (value.startsWith('jsondotcnprefix')) {
                        value = value.slice('jsondotcnprefix'.length);
                        return Number(value);
                }
                return value;
        });

        var sqlHtml = insert(object);
        //console.log(sqlHtml);
        showInDialog(sqlHtml);
}

function insert(object) {
        var typeValue = this._typeof(object);
        var result = "";
        if ("Object" == typeValue) {
                result += simpleInsertSql(object);
        } else if ("Array" == typeValue) {
                var sql = "<div class='sqlText' style='color: #a11;font-weight:bold;'>INSERT INTO TBNAME</div>";
                sql += "<div class='sqlText'>" + insertKey(object[0]) + "</div>";
                sql += "<div class='sqlText' style='color: #a11;font-weight:bold;'>VALUES</div>";
                for (var i = 0, size = object.length; i < size; i++) {
                        var obj = object[i];
                        if (i == object.length - 1) {
                                sql += "<div class='sqlText'>" + insertValue(obj) + "</div>";
                        } else {
                                sql += "<div class='sqlText'>" + insertValue(obj) + ",</div>";
                        }
                }
                result = sql;
        }
        return result;
}
function simpleInsertSql(object) {
        var sql = "<div class='sqlText' style='color: #a11;font-weight:bold;'>INSERT INTO TBNAME</div>";
        sql += "<div class='sqlText'>" + insertKey(object) + "</div>";
        sql += "<div class='sqlText' style='color: #a11;font-weight:bold;' >VALUES</div>";
        sql += "<div class='sqlText'>" + insertValue(object) + "</div>";
        return sql;
}
function insertKey(object) {
        var sqlKey = "(";
        for (var key in object) {
                sqlKey = sqlKey + "`" + key + "`,";
        }
        sqlKey = sqlKey.substring(0, sqlKey.length - 1) + ") ";
        return sqlKey;
}
function insertValue(object) {
        var sqlValue = "(";
        for (var key in object) {
                var objectValue = object[key];
                var typeValue = this._typeof(objectValue);
                if ("Null" == typeValue) {
                        sqlValue = sqlValue + " null,";
                } else if ("Boolean" == typeValue) {
                        sqlValue = sqlValue + objectValue + ",";
                } else if ("Number" == typeValue) {
                        sqlValue = sqlValue + objectValue + ",";
                } else if ("String" == typeValue) {
                        if (objectValue == "电热饭盒") {
                                console.log("aa");
                        }
                        sqlValue = sqlValue + "'" + objectValue + "',";
                } else if ("Array" == typeValue) {
                        throw new Error("包含数组内容");
                }
        }
        sqlValue = sqlValue.substring(0, sqlValue.length - 1) + ")";
        return sqlValue;
}

function _delEndWith(data, cmp) {
        var len = data.length;
        var endString = data.substring(len - cmp.length, len);
        if (endString == cmp) {
                return data.substring(0, len - cmp.length);
        }
        return data;
}


function _typeof(object) {
        var tf = typeof object,
                ts = new Object().toString.call(object);
        return null === object ? 'Null' :
                'undefined' == tf ? 'Undefined' :
                        'boolean' == tf ? 'Boolean' :
                                'number' == tf ? 'Number' :
                                        'string' == tf ? 'String' :
                                                '[object Function]' == ts ? 'Function' :
                                                        '[object Array]' == ts ? 'Array' :
                                                                '[object Date]' == ts ? 'Date' : 'Object';
}