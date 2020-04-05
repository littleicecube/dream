var JSONFormat = {
    lineNum: 0,
    _bigNums: []
}
JSONFormat.indent_tab = function (indent_count) {
    var tmp = "&nbsp;&nbsp;";
    var ret = "";
    for(var i=1;i<=indent_count;i++){
        ret +=tmp;
    }
   return ret;
}
JSONFormat._typeof = function (object) {
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
JSONFormat._loadCssString = function () {
    var code = '.json_key{color: #a11;font-weight:bold;}';
    code += '.json_null{color: #219;font-weight:bold;}';
    code += '.json_string{ color: #8B1C62;font-weight:bold;}';
    code += '.json_number{ color: #164;font-weight:bold;}';
    code += '.json_boolean{ color: #219;font-weight:bold;}';
    code += '.json_link{ color: #219;font-weight:bold;}';
    code += '.json_array_brackets{}';

    var style = document.createElement('style');
    style.type = 'text/css';
    try {
        style.appendChild(document.createTextNode(code));
    } catch (ex) {
        style.styleSheet.cssText = code;
    }
    document.getElementsByTagName('head')[0].appendChild(style);
}

JSONFormat._parseData = function (origin_data) {
    let stringedJSON = origin_data.replace(/([^\\]\"):\s*(\[)?([-+Ee0-9.]+)/g, '$1: $2"jsondotcnprefix$3"');
    try {
        var temp = JSON.parse(stringedJSON, (key, value) => {
            if (typeof value !== 'string') {
                return value;
            }
            if (value.startsWith('jsondotcnprefix')) {
                value = value.slice('jsondotcnprefix'.length);
                //return new BigNumber(value);
                return Number(value);
            }
            return value;
        });
        return temp;
    } catch (e) {
        return JSON.parse(origin_data);
    }
}
JSONFormat._lineNumHtml = function (lineNum) {
    var lineNumArr = new Array();
    for (var i = 1; i < lineNum; i++) {
        if (i < 10) {
            lineNumArr.push('<div class="j-ln-data"><code>000' + i + '</code></div>');
        } else if(i<100){
            lineNumArr.push('<div class="j-ln-data"><code>00' + i + '</code></div>');
        }else if(i<1000){
            lineNumArr.push('<div class="j-ln-data"><code>0' + i + '</code></div>');
        }else{
            lineNumArr.push('<div class="j-ln-data"><code>' + i + '</code></div>');
        }
    }
    return lineNumArr;
}
JSONFormat._replace = function(result){
    var len = result.length;
    var endStrA = "<span>,</span></div>";
    var lenA = endStrA.length;
    if(len > lenA){
        var endString = result.substring(len-lenA,len);
        if(endString == endStrA){
            return result.substring(0,len-lenA)+"</div>";
        }
            
    }
    var endStrB = "<span>],</span></div>";
    var lenB = endStrB.length;
    if(len > lenB){
        var endString = result.substring(len-lenB,len);
        if(endString == endStrB){
            return result.substring(0,len-lenB)+"<span>]</span></div>";
        }
    }
    var endStrC = "<span>},</span></div>";
    var lenC = endStrC.length;
    if(len > lenC){
        var endString = result.substring(len-lenC,len);
        if(endString == endStrC){
            return result.substring(0,len-lenC)+"<span>}</span></div>";
        }
    }
    return result;
}
JSONFormat._simpleFormat = function (typeValue,objValue) {
    var htmlFragment;
    var typeValue = this._typeof(objValue);
    if( "Null" == typeValue){
        htmlFragment = '<span class="json_null">null</span>';
    }else if("Boolean" == typeValue){
        htmlFragment = '<span class="json_boolean">' + objValue + '</span>';
    }else if("Number" == typeValue){
        htmlFragment = '<span class="json_number">' + objValue + '</span>';
    }else if("String" == typeValue){
        objValue = objValue.replace(/ /g, "&nbsp;");
        objValue = objValue.replace(/\</g, "&lt;");
        objValue = objValue.replace(/\>/g, "&gt;");
        htmlFragment = '<span class="json_string">"' + objValue + '"</span>';
    }
    return htmlFragment;
}


JSONFormat._format_array = function (keyHtml,object,indentCount) {
    var index = this.lineNum;
    var space = this.indent_tab(indentCount);
    var result = "<div data-index='"+ index +"-start'>" + space;
    if(keyHtml == null){
        result +=  + "<span>[</span></div>";
    }else{
        result += keyHtml + "<span>:[</span></div>";
    }
    this.lineNum++;
    for (var i = 0, size = object.length; i < size; i++) {
        this.lineNum++;
        var objectValue = object[i];
        var typeValue = this._typeof(objectValue);
        if("Object" == typeValue){
            result += this._format_object(null,objectValue,indentCount + 2);
        }else if("Array" == typeValue){
            result += tthis._format_array(spanKey,objectValue,indentCount + 2);
        }else{
            this.lineNum++;
            var keyHtml = this.indent_tab(indentCount + 1); 
            var htmlFragment = this._simpleFormat(typeValue,objectValue);
            var item = "<div>" + keyHtml + htmlFragment + "<span>,</span></div>";
            result += item;
        }
    }
    result = this._replace(result);
    var endIndex = index+"-end";
    result += "<div data-index='"+ endIndex +"'>" + space +"<span>],</span></div>";
    this.lineNum++;
    return result;
}
JSONFormat._format_object = function (lastKeyHtml,object,indentCount) {
    var space = this.indent_tab(indentCount-1);
    var index = this.lineNum;
    var startIndex = index+"-start";
    var result = "<div data-index='"+ startIndex +"'>" + space;
    if(lastKeyHtml == null){
        result += "<span>{</span></div>";
    }else{
        result += lastKeyHtml + "<span>:{</span></div>";
    }
    this.lineNum++;
    for (var key in object) {
        var objectValue = object[key];
        var typeValue = this._typeof(objectValue);
        var spanKey = '<span class="json_key">' + key + '</span>' ;
        if("Object" == typeValue){
            result += this._format_object(spanKey,objectValue,indentCount + 2);
        }else if("Array" == typeValue){
            result += this._format_array(spanKey,objectValue,indentCount + 2);
        }else{
            this.lineNum++;
            var keyHtml = this.indent_tab(indentCount + 1) + spanKey; 
            var htmlFragment = this._simpleFormat(typeValue,objectValue);
            var item = "<div>" + keyHtml + "<span>:</span>" +htmlFragment + "<span>,</span></div>";
            result += item;
        }
    }
    result = this._replace(result);
    result += "<div data-index='"+index+"-end'>" + space +"<span>},</span></div>";
    //this.lineNum++;
    return result;
}
JSONFormat._mainFormat = function(data){
    var objectValue = this._parseData(data);
    var typeValue = this._typeof(objectValue);
    var result = "";
    if("Object" == typeValue){
        result += this._format_object(null,objectValue,0);
    }else if("Array" == typeValue){
        result += this._format_array(null,objectValue,0);
    }
    result = this._replace(result);
    return result;
}

JSONFormat.doFormat = function (data) {
    this.lineNum = 0;
    this._loadCssString();
    var html = this._mainFormat(data);
    var lineNumHtml = this._lineNumHtml(this.lineNum);
    var result = {
        "html": html,
        "lineNumHtml": lineNumHtml
    }
    return result;
}
