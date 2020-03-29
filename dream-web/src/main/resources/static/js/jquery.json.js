var JSONFormat = {
    lineNum: 0,
    _bigNums: []
}
JSONFormat.format = function (object, indent_count) {
    var html_fragment = '';
    switch (this._typeof(object)) {
        case 'Null':
            html_fragment = this._format_null(object);
            break;
        case 'Boolean':
            html_fragment = this._format_boolean(object);
            break;
        case 'Number':
            html_fragment = this._format_number(object);
            break;
        case 'String':
            //replace blank to html blank to display in html.
            object = object.replace(/ /g, "&nbsp;");
            html_fragment = this._format_string(object);
            break;
        case 'Array':
            html_fragment = this._format_array(object, indent_count);
            break;
        case 'Object':
            if (object instanceof BigNumber) {
                html_fragment = this._format_number(object.toFixed());
            } else {
                html_fragment = this._format_object(object, indent_count);
            }
            break;
    }
    return html_fragment;
};

JSONFormat._format_null = function (object) {
    return '<span class="json_null">null</span>';
}

JSONFormat._format_boolean = function (object) {
    return '<span class="json_boolean">' + object + '</span>';
}

JSONFormat._format_number = function (object) {
    return '<span class="json_number">' + object + '</span>';
}

JSONFormat._format_string = function (object) {
    object = object.replace(/\</g, "&lt;");
    object = object.replace(/\>/g, "&gt;");
    if (0 <= object.search(/^http/)) {
        object = '<a href="' + object + '" target="_blank" class="json_link">' + object + '</a>'
    }
    return '<span class="json_string">"' + object + '"</span>';
}

JSONFormat._format_array = function (object, indent_count) {
    var tmp_array = [];
    for (var i = 0, size = object.length; i < size; ++i) {
        this.lineNum++;
        tmp_array.push(this.indent_tab(indent_count) + this.format(object[i], indent_count + 1));
    }
    this.lineNum += 2;
    return '<span data-type="array">[<br/>'
        + tmp_array.join(',<br/>')
        + '<br/>' + this.indent_tab(indent_count - 1) + ']</span>';
}
JSONFormat._format_object = function (object, indent_count) {
    var tmp_array = [];
    for (var key in object) {
        this.lineNum++;
        tmp_array.push(this.indent_tab(indent_count) + '<span class="json_key">"' + key + '"</span>:' + this.format(object[key], indent_count + 1));
    }
    this.lineNum += 2;
    return '<span  data-type="object">{<br/>'
        + tmp_array.join(',<br/>')
        + '<br/>' + this.indent_tab(indent_count - 1) + '}</span>';
}

JSONFormat.indent_tab = function (indent_count) {
    return (new Array(indent_count + 1)).join('&nbsp;&nbsp;&nbsp;&nbsp;');
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
};

JSONFormat.loadCssString = function () {
    var code = '.json_key{color: #a11;font-weight:bold;}';
    code += '.json_null{color: #219;font-weight:bold;}';
    code += '.json_string{ color: #a11;font-weight:bold;}';
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

JSONFormat.parseData = function (origin_data) {
    let stringedJSON = origin_data.replace(/([^\\]\"):\s*(\[)?([-+Ee0-9.]+)/g, '$1: $2"jsondotcnprefix$3"');
    try {
        var temp = JSON.parse(stringedJSON, (key, value) => {
            if (typeof value !== 'string') {
                return value;
            }
            if (value.startsWith('jsondotcnprefix')) {
                value = value.slice('jsondotcnprefix'.length);
                return new BigNumber(value);
            }
            return value;
        });
        return temp;
    } catch (e) {
        return JSON.parse(origin_data);
    }
}
JSONFormat.lineNumHtml = function (lineNum) {
    var lineNumArr = new Array();
    for (var i = 1; i < lineNum; i++) {
        if (i < 10) {
            lineNumArr.push('<div style="">00' + i + '</div>');
        } else if(i<100){
            lineNumArr.push('<div style="">0' + i + '</div>');
        }else{
            lineNumArr.push('<div style="">' + i + '</div>');
        }
    }
    return lineNumArr;
}



JSONFormat.doFormat = function (data) {
    this.lineNum = 0;
    this.loadCssString();
    var objData = this.parseData(data);
    var html = this.format(objData, 1);
    var lineNumHtml = this.lineNumHtml(this.lineNum);
    var result = {
        "html": html,
        "lineNumHtml": lineNumHtml
    }
    return result;
}




