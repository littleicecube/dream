 function _delEndWith(data,cmp){
        var len = data.length;
        var endString = data.substring(len-cmp.length,len);
        if(endString == cmp){
            return data.substring(0,len-cmp.length);
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