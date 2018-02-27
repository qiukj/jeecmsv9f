Pn = {
	version : '1.0'
};
/**
 * get url parameter
 */
Pn.getParam = function(key) {
	var params = location.search.substr(1).split('&');
	var kv;
	for (var i = 0;i < params.length; i++) {
		kv = params[i].split('=');
		if (kv[0] == key) {
			return kv[1];
		}
	}
};
/**
 * check checkbox.
 * 
 * @param name
 *            string of checkbox name
 * @param checked
 *            boolean of checked
 */
Pn.checkbox = function(name, checked) {
	$("input[type=checkbox][name=" + name + "]").each(function() {
		$(this).prop("checked", checked);
	});
	if(checked){
		$("tbody tr").each(function(){
			$(this).prop("bgColor","#f3f3f3");
			$(this).unbind();
		});
	}else{
		$("tbody tr").each(function(){
			$(this).prop("bgColor","#ffffff");
			$(this).bind('mouseover', function() {
				$(this).prop("bgColor","#f3f3f3");
			});
			$(this).bind('mouseout', function() {
				$(this).prop("bgColor","#ffffff");
			});
		});
	}
}

Pn.selectCheckBox = function(value, checked) {
	var checkbox=$("input[type=checkbox][value=" + value + "]");
	var tr=checkbox.parent().parent();
	checkbox.prop("checked", checked);
	if(checked){
		tr.prop("bgColor","#f3f3f3");
		tr.unbind("mouseout");
		tr.unbind("mouseover");
	}else{
		tr.prop("bgColor","#ffffff");
		tr.bind('mouseout', function() {
			tr.prop("bgColor","#ffffff");
		});
		tr.bind('mouseover', function() {
			tr.attr("bgColor","#f3f3f3");
		});
	}
}

/**
 * 复选框选中的个数
 * 
 * @param name
 *            string of checkbox name
 */
Pn.checkedCount = function(name) {
	var batchChecks = document.getElementsByName(name);
	var count = 0;
	for (var i = 0;i < batchChecks.length; i++) {
		if (batchChecks[i].checked) {
			count++;
		}
	}
	return count;
}

/**
 * 颜色选择器
 */
Pn.ColorPicker = function(input) {
	var obj = this;
	this.isShow = false;
	this.target = $(input);
	this.button = $("<input type='text' tabindex='10000'"
			+ " readonly='readonly' style='background-color:#fff;width:30px;"
			+ "border:1px solid #ccc;margin-left:2px;cursor:pointer'/>");
	this.target.after(this.button);
	this.over = function() {
		$(this).css("border", "1px solid #000");
	}
	this.out = function() {
		$(this).css("border", "1px solid #fff");
	}
	//title获取不到改成获取id属性
	this.click = function() {
		var color = $(this).attr("id");
		obj.setColor(color);
		this.isShow = false;
		obj.picker.hide();
	}
	this.createPicker = function() {
		var c = ["#FF8080", "#FFFF80", "#80FF80", "#00FF80", "#80FFFF",
				"#0080FF", "#FF80C0", "#FF80FF", "#FF0000", "#FFFF00",
				"#80FF00", "#00FF40", "#00FFFF", "#0080C0", "#8080C0",
				"#FF00FF", "#804040", "#FF8040", "#00FF00", "#008080",
				"#004080", "#8080FF", "#800040", "#FF0080", "#800000",
				"#FF8000", "#008000", "#008040", "#0000FF", "#0000A0",
				"#800080", "#8000FF", "#400000", "#804000", "#004000",
				"#004040", "#000080", "#000040", "#400040", "#400080",
				"#000000", "#808000", "#808040", "#808080", "#408080",
				"#C0C0C0", "#400040", "#FFFFFF"];
		var s = "<table border='0' cellpadding='0' cellspacing='5' "
				+ "style='display:none;position:absolute;margin-top:0px;border:1px solid #ccc;background-color:#fff;z-index: 102'>"
				+ "<tr height='15'>";
		// 列数
		var col = 8;
		for (var i = 0, len = c.length;i < len; i++) {
			/*
			s += "<td width='15'><div class='c' style='width:13px;height:13px;"
					+ "border:1px solid #fff;cursor:pointer;background-color:"
					+ c[i] + "' title='" + c[i] + "'>&nbsp;</div></td>";
					*/
			s += "<td width='15'><div class='c' id='"+c[i]+"' title='"+c[i]+"' style='width:13px;height:13px;"
				+ "border:1px solid #fff;cursor:pointer;background-color:"
				+ c[i] + "'>&nbsp;</div></td>";
			if ((i + 1) != c.length && (i + 1) % col == 0) {
				s += "</tr><tr height='15'>";
			}
		}
		// s += "</tr><tr><td colspan=" + col
		// + "><div class='c' style='width:153px;height:13px;text-align:center;"
		// + "border:1px solid #fff;cursor:pointer;background-color:#fff'"
		// + " title=''>clear<div></td>";
		s += "</tr></table>";
		var picker = $(s);
		picker.find(".c").each(function() {
			$(this).bind("mouseover", obj.over);
			$(this).bind("mouseout", obj.out);
			$(this).bind("click", obj.click);
		});
		$(document.body).append(picker);
		return picker;
	}
	this.setColor = function(color) {
		obj.target.val(color);
		if (color == "") {
			color = "#fff";
		}
		try {
			obj.button.css( {
				backgroundColor : color
			});
		} catch (e) {
			alert("color error: " + color);
			obj.target.focus().select();
		}
	}
	this.picker = this.createPicker();
	this.showPicker = function() {
		if (!obj.isShow) {
			obj.isShow = true;
			obj.picker.showBy(obj.target);
			$(document).bind("mousedown", function() {
				$(document).unbind("mousedown");
				setTimeout(function() {
					obj.isShow = false;
					obj.picker.hide();
					var val = obj.target.val();
					obj.setColor(obj.target.val());
				}, 200);
			});
		}
	}
	this.button.bind("click", obj.showPicker);
	this.target.bind("blur", function() {
		obj.setColor(obj.target.val());
	});
	var v = this.target.val();
	if (v != "") {
		this.setColor(v);
	}
};
Pn.loginSSO=function(base){
	var username=$.cookie('username');
	var sessionId=$.cookie('JSESSIONID');
	var ssoLogout=$.cookie('sso_logout');
	if(username!=null){
		if(sessionId!=null||(ssoLogout!=null&&ssoLogout=="true")){
			$.post(base+"/sso/login.jspx", {
				 username:username,
				 sessionId:sessionId,
				 ssoLogout:ssoLogout
			}, function(data) {
					if(data.result=="login"||data.result=="logout"){
						location.reload();
					}
			}, "json");
		}
	}
};
Pn.getCookie=function getCookie(c_name){
	if (document.cookie.length>0)
	  {
	  	c_start=document.cookie.lastIndexOf(c_name + "=")
		  if (c_start!=-1)
		    { 
			    c_start=c_start + c_name.length+1;
			    c_end=document.cookie.indexOf(";",c_start);
			    if (c_end==-1){
			    	c_end=document.cookie.length;
			    } 
			    return unescape(document.cookie.substring(c_start,c_end))
		    } 
		  }
	return ""
};
Pn.isInt=function isInt(a) {     var reg = /^[0-9]*$/;     return reg.test(a); };
/*
** randomWord 产生任意长度随机字母数字组合
** randomFlag-是否任意长度 min-任意长度最小位[固定位数] max-任意长度最大位
*  numFlag 产生随机数字组合
*/
Pn.randomWord=function randomWord(randomFlag,numFlag, min, max){
    var str = "",
        range = min,
        arr = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
 
    // 随机产生
    if(randomFlag){
        range = Math.round(Math.random() * (max-min)) + min;
    }
    //纯数字组合
    if(numFlag){
    	arr = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
    }
    for(var i=0; i<range; i++){
        pos = Math.round(Math.random() * (arr.length-1));
        str += arr[pos];
    }
    return str;
}
$.fn.extend( {
	colorPicker : function() {
		new Pn.ColorPicker(this);
	}
});