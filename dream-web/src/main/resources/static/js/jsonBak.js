$(function () {
  doSomething();
});

var C = {};
C.LEFT_BRACE = "{";             //左大括号
C.RIGHT_BRACE = "}";             //右大括号
C.LEFT_BRACKET = "[";             //左中括号
C.RIGHT_BRACKET = "]";             //右中括号
C.COLON = ":";             //冒号
C.COMMA = ",";             //逗号
C.QUOTES = "'";             //单引号
C.DOUBLE_QUOTES = "\"";            //双引号
C.NULL_DOUBLE_QUOTES = "n";             //空双引号
C.LEFT_DOUBLE_QUOTES = "l";             //左双引号
C.RIGHT_DOUBLE_QUOTES = "r";             //右双引号


C.BACK_SLASH = "\\";
C.FORWARD_SLASH = "\/";
C.BACKSPACE = "\b";
C.FORM_FEED = "\f";
C.NEWLINE = "\n";
C.CARRIAGE_RETURN = "\r";
C.TAB = "\t";
C.BLANK = " ";

C.TRUE = "TRUE";
C.FALSE = "FALSE";
C.NULL = "NULL";
C.STRING = "STRING";
C.NUMBER = "NUMBER";
C.OBJECT = "OBJECT";
C.ARRAY = "ARRAY";

function content() {
  var state;
  var stateColon = 0;
  var stateComma = 0;
  var stateBracket = null;
  var stateDoubleQuote = null;//空,左双引号,右双引号
}

function doSomething() {
  console.log("start");
  var arr = json.split("");
  console.log(arr.length);
  
  var text = parse(json);
  console.log(text);
  console.log("----result---");
  
  try{
  var obj = eval('('+json+')');
  console.log(obj);
  }catch(err){
    console.log(err);
  }

}

function parse(str) {
  var text = "";
  var ct = null;
  var deep = 0;
  for (var i = 0; i < str.length;) {

    var ele = str.charAt(i);
    if (ele == C.DOUBLE_QUOTES) {//######双引号
      if (ct.stateDoubleQuote == null) {
        ct.stateDoubleQuote = C.LEFT_DOUBLE_QUOTES;
      } else if (ct.stateDoubleQuote == C.LEFT_DOUBLE_QUOTES) {
        ct.stateDoubleQuote = C.RIGHT_DOUBLE_QUOTES;
      } else {
        ct.stateDoubleQuote = null;
      }
      text = text + C.DOUBLE_QUOTES;
    }else if (ele == C.LEFT_BRACE) {//######左大括号
      deep++;
      ct = new content();
      ct.state = C.LEFT_BRACE;
      text = text + C.LEFT_BRACE + C.NEWLINE + getBlank(deep);
    } else if (ele == C.RIGHT_BRACE) {//######右大括号
      deep--;
      ct.state = C.RIGHT_BRACE;
      text = text + C.NEWLINE + getBlank(deep) + C.RIGHT_BRACE;
    } else if(ele == C.LEFT_BRACKET){ //######左中括号
      deep++;
      ct.stateBracket = C.LEFT_BRACKET;
      text = text + C.LEFT_BRACKET + C.NEWLINE + getBlank(deep);
    } else if(ele == C.RIGHT_BRACKET){//######右中括号
      deep--;
      ct.stateBracket = C.RIGHT_BRACKET;
      text = text + C.NEWLINE + getBlank(deep) + C.RIGHT_BRACKET;
    } else if(ele == C.COLON) {//######冒号
      if(ct.stateDoubleQuote ==  C.LEFT_DOUBLE_QUOTES){
        //如果遇到冒号,且当前是左双引号状态,那么这个冒号是key的一部分
        text = text + C.COLON;
      }else  if (ct.stateDoubleQuote == C.RIGHT_DOUBLE_QUOTES) {
        //如果遇到冒号,且当前是右双引号状态,那么应该LEFT_DOUBLE_QUOTES,RIGHT_DOUBLE_QUOTES应该清零状态
        ct.stateDoubleQuote = null;
        ct.stateColon = C.COLON;
        text = text + C.COLON;
      }else{
        //当前stateDoubleQuote为null
        ct.stateColon = C.COLON;
        text = text + C.COLON;
      }
    } else if (ele == C.COMMA) {//######逗号
      if(ct.stateDoubleQuote ==  C.LEFT_DOUBLE_QUOTES){
        //如果遇到逗号,且当前是左双引号状态,那么这个逗号是key的一部分
        text = text + C.COMMA;
      }else if (ct.stateDoubleQuote == C.RIGHT_DOUBLE_QUOTES) {
        //如果遇到逗号,且当前是右双引号状态,那么应该
        ct.stateDoubleQuote = null;
        ct.stateComma = C.COMMA;
        text = text + C.COMMA + C.NEWLINE + getBlank(deep);
      }else{
        //当前逗号状态,且不属于左双引号状态,那么表示既不是key({"keynam),也不是value(:"valuename)
        //那么应该是一行key:value的结尾,同时状态state和stateColon和stateDoubleQuote应该清零
        ct.stateComma = C.COMMA;
        text = text + C.COMMA + C.NEWLINE + getBlank(deep);
      }
    }else if (ele == C.NEWLINE) {//######换行
      i++;
      continue;
    } else {
      if (ele == C.TAB || ele == C.BLANK) {
        if(ct.state == C.LEFT_BRACE){//左大括号状态
          //如果双引号状态为null或者是右双引号,表示不是双引号内部数据,则跳过
          if(ct.stateDoubleQuote == null || ct.stateDoubleQuote == C.RIGHT_DOUBLE_QUOTES) {
            i++;
            continue;
          }
        }
        if(ct.stateComma == C.COMMA){//逗号状态
          //如果双引号状态为null或者是右双引号,表示不是双引号内部数据,则跳过
          if(ct.stateDoubleQuote == null || ct.stateDoubleQuote == C.RIGHT_DOUBLE_QUOTES) {
            i++;
            continue;
          }
        }
          //如果当前已经是冒号状态,检查是否处于双引号状态,如果不是LEFT_DOUBLE_QUOTES,那么遇到的空格都应该忽略
        if (ct.stateColon == C.COLON) {//冒号状态
          if(ct.stateDoubleQuote == null || ct.stateDoubleQuote == C.RIGHT_DOUBLE_QUOTES) {
              i++;
              continue;
          }
        }
        if(ct.stateBracket == C.LEFT_BRACKET){
          i++;
          continue;
        }
      }
      text = text + ele;
    }
    i++;
  }

  return text;
}

function getBlank(deep) {
  var blank = "";
  var stand = "    ";
  for (var i = 0; i < deep; i++) {
    blank += stand;
  }
  return blank;
}

var json = "{\"obj\":{\"name\":\"xia:oming\",\"age\":12,\"isBeijing\":false,\"isTianjin\":false,\"data\":null,\"salary\":12.434},\"pName\":\"hebei\",\"add\":123,\"objA\":{\"name\":\"xmA\",\"ageA\":234,\"bjA\":false,\"tjA\":true,\"dataA\":null,\"salaA\":434.222},\"oA\":\"xian\",\"oData\":null,\"oo\":323}";
json = "{\"  na,me\":\" xmA\",\"ageA\":234,\"ob:jA\":\"va:lA\",\"bjA\":false,\"tjA\":true,\"dataA\":null,\"salaA\":434.222}";
json = "{   \"  na,me\"   :\"  xm,,A :   \",\"ageA\":234,\"ob:jA\":\"  : va:lA \"   ,\"bjA\":false  ,  \"tjA\":true,\"dataA\":null,\"salaA\":434.222}";
json = "{\"name\":\"xmA\",\"ageA\":234,\"bjA\":false,\"tjA\":true,\"objB\":{\"nameB\":\"xiaomB\",\"ageB\":12.33,\"addrB\":\"bj\"},\"dataA\":null,\"salaA\":434.222}";

json = "{\"name\":\"xmA\",\"ageA\":234,\"bjA\":false,\"tjA\":true,\" ob,jB \": { \"na me,B\":\"x iao;mB\",  \"ag,eB\":  12.33,\"addrB\":\"bj\" }, \"dataA\":null,\"salaA\":434.222}";
json = "{\"name\":\"xmA\",\"ageA\":234,\"bjA\":false,\"tjA\":true,\"objB\":{\"nameB\":\"xiaomB\",\"ageB\":12.33,\"addrB\":\"bj\"},\"dataA\":null,\"salaA\":434.222}";

json = "{\"name\":\"xmA\",\"ageA\":234,\"bjA\":false,\"tjA\":true,\"objB\":{\"nameB\":\"xiaomB\",\"ageB\":12.33,\"addrB\":\"bj\"},\"dataA\":null,\"objc\":{\"nameC\":\"xC\",\"ageC\":12,\"addrC\":\"tj\"},\"salaA\":434.222}";

json = "{\"na,me\":\"xmA\",\"ageA\":234,\"bjA\":false,\"tjA\":true,\"objB\":{\"nameB\":\"xiaomB\",\"ageB\":12.33,\"addrB\":\"bj\",\"dataA\":null},\"objc\":{\"nameC\":\"xC\",\"ageC\":12,\"addrC\":\"tj\"},\"name\":[{\"name\":\"xx\",\"age\":12.3,\"addr\":\"beij\"},{\"name\":\"ar\",\"age\":4433.22,\"addr\":\"fsd\"}],\"salaA\":434.222}";
json = "{\"na,me\":\"xmA\",\"ageA\":234,\"bjA\":false,\"tjA\":true,\"objB\"{\"nameB\":\"xiaomB\",\"ageB\":12.33,\"addrB\":\"bj\",\"dataA\":null},\"objc\":{\"nameC\":\"xC\",\"ageC\":12,\"addrC\":\"tj\"},\"name\":[{\"name\":\"xx\",\"arr\":[{\"name\":\"nn\",\"age\":23}],\"age\":12.3,\"addr\":\"beij\"},{\"name\":\"ar\",\"age\":4433.22,\"addr\":\"fsd\"}],\"salaA\":434.222}"; 


/**
*符号数据处理,冒号之前的都是key数据,key都在双括号之内,遍历数据时首先定位key中的符号
首先判断特殊字符,双引号,冒号,逗号,特殊字符的判断还要注意是否是转义的正常数据而不是特殊字符
然后根据当前字符的状态判断内容改怎么处理,key中可以存在空格,结果值中只有字符串值中可以存在空格





**/