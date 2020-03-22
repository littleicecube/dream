
var JSONFormat = (function(){
        var _toString = Object.prototype.toString;
        var _bigNums = [];
        function format(object, indent_count){
            var html_fragment = '';
            switch(_typeof(object)){
                case 'Null' :
                    html_fragment = _format_null(object);
                    break;
                case 'Boolean' :
                    html_fragment = _format_boolean(object);
                    break;
                case 'Number' :
                    html_fragment = _format_number(object);
                    break;
                case 'String' :
                    //replace blank to html blank to display in html.
                    object  = object.replace(/ /g,"&nbsp;");
                    html_fragment = _format_string(object);
                    break;
                case 'Array' :
                    html_fragment = _format_array(object, indent_count);
                    break;
                case 'Object' :
                    if(object instanceof BigNumber){
                      html_fragment = _format_number(object.toFixed());
                    }else{
                      html_fragment = _format_object(object, indent_count);
                    }
                    break;
            }
            return html_fragment;
        };
    
        function _format_null(object){
            return '<span class="json_null">null</span>';
        }
    
        function _format_boolean(object){
            return '<span class="json_boolean">' + object + '</span>';
        }
    
        function _format_number(object){
            return '<span class="json_number">' + object + '</span>';
        }
    
        function _format_string(object){
            object = object.replace(/\</g,"&lt;");
            object = object.replace(/\>/g,"&gt;");
            if(0 <= object.search(/^http/)){
                object = '<a href="' + object + '" target="_blank" class="json_link">' + object + '</a>'
            }
            return '<span class="json_string">"' + object + '"</span>';
        }
    
        function _format_array(object, indent_count){
            var tmp_array = [];
            for(var i = 0, size = object.length; i < size; ++i){
                lineNum++;
                tmp_array.push(indent_tab(indent_count) + format(object[i], indent_count + 1));
            }
            return '<span data-type="array">[<br/>'
                + tmp_array.join(',<br/>')
                + '<br/>' + indent_tab(indent_count - 1) + ']</span>';
        }
        
        function _format_object(object, indent_count){
            var tmp_array = [];
            for(var key in object){
                lineNum++;
                tmp_array.push( indent_tab(indent_count) + '<span class="json_key">"' + key + '"</span>:' +  format(object[key], indent_count + 1));
            }
            return '<span  data-type="object">{<br/>'
                + tmp_array.join(',<br/>')
                + '<br/>' + indent_tab(indent_count - 1) + '}</span>';
        }
    
        function indent_tab(indent_count){
            return (new Array(indent_count + 1)).join('&nbsp;&nbsp;&nbsp;&nbsp;');
        }
    
        function _typeof(object){
            var tf = typeof object,
                ts = _toString.call(object);
            return null === object ? 'Null' :
                'undefined' == tf ? 'Undefined'   :
                    'boolean' == tf ? 'Boolean'   :
                        'number' == tf ? 'Number'   :
                            'string' == tf ? 'String'   :
                                '[object Function]' == ts ? 'Function' :
                                    '[object Array]' == ts ? 'Array' :
                                        '[object Date]' == ts ? 'Date' : 'Object';
        };
    
        function loadCssString(){
            var style = document.createElement('style');
            style.type = 'text/css';
            var code = Array.prototype.slice.apply(arguments).join('');
            try{
                style.appendChild(document.createTextNode(code));
            }catch(ex){
                style.styleSheet.cssText = code;
            }
            document.getElementsByTagName('head')[0].appendChild(style);
        }
    
        loadCssString(
            '.json_key{color: #a11;font-weight:bold;}',
            '.json_null{color: #219;font-weight:bold;}',
            '.json_string{ color: #a11;font-weight:bold;}',
            '.json_number{ color: #164;font-weight:bold;}',
            '.json_boolean{ color: #219;font-weight:bold;}',
            '.json_link{ color: #219;font-weight:bold;}',
            '.json_array_brackets{}');
    
        var _JSONFormat = function(origin_data){
            let stringedJSON = origin_data.replace(/([^\\]\"):\s*(\[)?([-+Ee0-9.]+)/g, '$1: $2"jsondotcnprefix$3"');
            try {
              var temp = JSON.parse(stringedJSON, (key, value) => {
                if (typeof value !== 'string'){
                    return value;
                }
                if (value.startsWith('jsondotcnprefix')){
                    value = value.slice('jsondotcnprefix'.length);
                    return new BigNumber(value);
                } 
                return value;
              });
              this.data = temp;
            } catch (e) {
              this.data = JSON.parse(origin_data);
            } 
        };
        var lineNum = 0;
        _JSONFormat.prototype = {
            constructor : JSONFormat,
            parse : function(){
                lineNum = 0;
                var obj = {
                    "html": format(this.data, 1),
                    "lineNum":lineNum
                }
                return obj;
            }
        }
        return _JSONFormat;
    })();


    var last_html = '';
    function hide(obj){
        var data_type = obj.parentNode.getAttribute('data-type');
        var data_size = obj.parentNode.getAttribute('data-size');
        obj.parentNode.setAttribute('data-inner',obj.parentNode.innerHTML);
        if (data_type === 'array') {
            obj.parentNode.innerHTML = '<i  style="cursor:pointer;" class="fa fa-plus-square-o" onclick="show(this)"></i>Array[<span class="json_number">' + data_size + '</span>]';
        }else{
            obj.parentNode.innerHTML = '<i  style="cursor:pointer;" class="fa fa-plus-square-o" onclick="show(this)"></i>Object{...}';
        }
    
    }
    
    function show(obj){
        var innerHtml = obj.parentNode.getAttribute('data-inner');
        obj.parentNode.innerHTML = innerHtml;
    }
    