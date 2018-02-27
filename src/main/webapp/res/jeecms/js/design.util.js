//获取开始名称开始的cookie 返回var(非数组)
//待完善
function getParamsByCookie(name){
	//js获取cookie 
	var acookie=document.cookie.split(";"); 
	var params = new Array();  
	var j=0;
	for(var i=0;i<acookie.length;i++){ 
		var arr=acookie[i].split("="); 
		if(arr[0].trim()!=""&&arr[0].trim().startWith(name)&&arr[1].trim()!=""){ 
			var one=new Array();
			one[0]=arr[0];
			one[1]=unescape(arr[1]);
			params[j++]=one;
		}
	} 
	params=unique(params);
	var resultStr="";
	for(var i=0;i<params.length;i++){
		resultStr=resultStr+params[i][0].trim()+"="+params[i][1]+";";
	}
	return resultStr;
}
function clearCookieByPrefix(name){
	var cookieNames = new Array(); 
	cookieNames=getCookieNameByPrefix(name);
	for(var i=0;i<cookieNames.length;i++){
		$.cookie(cookieNames[i], null);
	}
}
function getCookieNameByPrefix(name){
	//js获取cookie 
	var acookie=document.cookie.split(";"); 
	var params = new Array();  
	var j=0;
	for(var i=0;i<acookie.length;i++){ 
		var arr=acookie[i].split("="); 
		if(arr[0].trim()!=""&&arr[0].trim().startWith(name)){
			params[j++]=arr[0];
		}
	} 
	params=unique(params);
	return params;
}
//去除数组重复元素
function unique(arr) {
    var result = [], hash = {};
    for (var i = 0, elem; (elem = arr[i]) != null; i++) {
        if (!hash[elem]) {
            result.push(elem);
            hash[elem] = true;
        }
    }
    return result;
}
String.prototype.trim=function(){
    return this.replace(/^\s+|\s+$/g,"");
}
String.prototype.startWith=function(str){  
    if(str==null||str==""||this.length==0||str.length>this.length)  
      return false;  
    if(this.substr(0,str.length)==str)  
      return true;  
    else  
      return false;  
    return true;  
}
String.prototype.endWith=function(str){  
    if(str==null||str==""||this.length==0||str.length>this.length)  
      return false;  
    if(this.substring(this.length-str.length)==str)  
      return true;  
    else  
      return false;  
    return true;  
} 
String.prototype.replaceAll  = function(s1,s2){   
    return this.replace(new RegExp(s1,"gm"),s2);   
} 
function RGBToHex(rgb){ 
	   var regexp = /[0-9]{0,3}/g;  
	   var re = rgb.match(regexp);//利用正则表达式去掉多余的部分，将rgb中的数字提取
	   var hexColor = "#"; var hex = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'];  
	   for (var i = 0; i < re.length; i++) {
	        var r = null, c = re[i], l = c; 
	        var hexAr = [];
	        while (c > 16){  
	              r = c % 16;  
	              c = (c / 16) >> 0; 
	              hexAr.push(hex[r]);  
	         } hexAr.push(hex[c]);
	         if(l < 16&&l != ""){        
	             hexAr.push(0)
	         }
	       hexColor += hexAr.reverse().join(''); 
	    }  
	   //alert(hexColor)  
	   return hexColor;  
}