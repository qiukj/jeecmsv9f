//设置全局样式
function globalStyleSet(){
	$("#globalSet").dialog("open");
	//初始化全局样式dialog
	initGlobalStyleDialog();
}
//全局样式设置弹出框点击确定事件
function globalStyleSetDialogClick(){
	$("#globalSet").dialog("close");
	//普通像素类样式
	$("#"+clickFrom+" .input").each(function(){
		var cssName=$(this).attr("name");
		var cssVal=$(this).val();
		$(".pageContext").css(cssName,cssVal+"px");
	});
	//select 和粗体、斜体
	$("#font-family,#text-align,.customInput").each(function(){
		var cssName=$(this).attr("name");
		var cssVal=$(this).val();
		if($(this).attr("disabled")=="disabled"){
			$(".pageContext").css(cssName,"");
		}else{
			$(".pageContext").css(cssName,cssVal);
		}
	});
	//边线
	$("#"+clickFrom+" input[id$='Px']").each(function(){
		if($(this).val()!=null&&$(this).val().trim()!=""&&$(this).val().trim()!=0){
			var thisid=$(this).attr("id");
			var id=thisid.substring(0,thisid.indexOf("Px"));
			var cssName=$("#"+id).attr("name");
			var cssVal=$(this).val()+"px "+$("#"+id).val()+" "+$("#"+id+"Color").val()+" ";
			$(".pageContext").css(cssName,cssVal);
		}
	});
	//页面背景
	var backgroundImg=$("#"+clickFrom+" input[name='backgroundImg']").val();
	var backgroundRepeat=$("#"+clickFrom+" select[name='background-repeat']").val();
	var backgroundColor=$("#"+clickFrom+" input[name='backgroundColor']").val();
	var repeat=false;
	if(backgroundImg!=null&&backgroundImg.trim()!=""){
		repeat=true;
		$(".bodyPage").css("background","url('"+backgroundImg+"')");
	}else{
		$(".bodyPage").css("background","");
	}
	if(backgroundColor!=null&&backgroundColor.trim()!=""){
		repeat=true;
		$(".bodyPage").css("background-color",backgroundColor);
	}else{
		$(".bodyPage").css("background-color","");
	}
	if(repeat){
		$(".bodyPage").css("background-repeat",backgroundRepeat);
	}else{
		$(".bodyPage").css("background-repeat","");
	}
	//内容区背景
	var contentBackgroundImg=$("#"+clickFrom+" input[name='contentBackgroundImg']").val();
	var contentBackgroundRepeat=$("#"+clickFrom+" select[name='contentBackground-repeat']").val();
	var contentBackgroundColor=$("#"+clickFrom+" input[name='contentBackgroundColor']").val();
	var repeat=false;
	if(contentBackgroundImg!=null&&contentBackgroundImg.trim()!=""){
		repeat=true;
		$(".pageContext").css("background","url('"+contentBackgroundImg+"')");
	}else{
		$(".pageContext").css("background","");
	}
	if(contentBackgroundColor!=null&&contentBackgroundColor.trim()!=""){
		repeat=true;
		$(".pageContext").css("background-color",contentBackgroundColor);
	}else{
		$(".pageContext").css("background-color","");
	}
	if(repeat){
		$(".pageContext").css("background-repeat",contentBackgroundRepeat);
	}else{
		$(".pageContext").css("background-repeat","");
	}
}
function initGlobalStyleDialog(){
	$("#globalSetForm .input").each(function(){
		var cssName=$(this).attr("name");
		var cssVal=$(".pageContext").css(cssName);
		var val;
		if(cssName=="color"||cssName.endWith("Color")){
			val=RGBToHex(cssVal);
			$(this).next().css("background-color",cssVal);
		}else{
			val=cssVal.substr(0,cssVal.length-2);
		}
		$(this).val(val);
	});
	$("#font-family,#text-align").each(function(){
		var cssName=$(this).attr("name");
		var cssVal=$(".pageContext").css(cssName);
		$(this).val(cssVal);
	});
	//加粗斜体
	$("#globalSetForm .customInput").each(function(){
		var cssName=$(this).attr("name");
		var cssVal=$(".pageContext").css(cssName);
		if(cssName=="font-weight"){
			//火狐、ie标识为700
			if(cssVal=="bold"||cssVal=="700"){
				$(this).removeAttr("disabled");
				$("#font-weightCheckBox").prop("checked",true);
			}else{
				$(this).attr("disabled","disabled");
				$("#font-weightCheckBox").prop("checked",false);
			}
		}
		if(cssName=="font-style"){
			if(cssVal=="italic"){
				$(this).removeAttr("disabled");
				$("#font-styleChecxBox").prop("checked",true);
			}else{
				$(this).attr("disabled","disabled");
				$("#font-styleChecxBox").prop("checked",false);
			}
		}
	});
	//边线
	var borderTopWidth=$(".pageContext").css("borderTopWidth");
	var borderTopColor=$(".pageContext").css("borderTopColor");
	borderTopWidth=borderTopWidth.substring(0,borderTopWidth.length-2);
	if(borderTopWidth>0){
		$("#border-top").val($(".pageContext").css("borderTopStyle"));
		$("#border-topColor").val(RGBToHex(borderTopColor));
		$("#border-topColor").next().css("background-color",borderTopColor);
		$("#border-topPx").val(borderTopWidth);
	}
	var borderLeftWidth=$(".pageContext").css("borderLeftWidth");
	var borderLeftColor=$(".pageContext").css("borderLeftColor");
	borderLeftWidth=borderLeftWidth.substring(0,borderLeftWidth.length-2);
	if(borderLeftWidth>0){
		$("#border-left").val($(".pageContext").css("borderLeftStyle"));
		$("#border-leftColor").val(RGBToHex(borderLeftColor));
		$("#border-leftColor").next().css("background-color",borderLeftColor);
		$("#border-leftPx").val(borderLeftWidth);
	}
	var borderRightWidth=$(".pageContext").css("borderRightWidth");
	var borderRightColor=$(".pageContext").css("borderRightColor");
	borderRightWidth=borderRightWidth.substring(0,borderRightWidth.length-2);
	if(borderRightWidth>0){
		$("#border-right").val($(".pageContext").css("borderRightStyle"));
		$("#border-rightColor").val(RGBToHex(borderRightColor));
		$("#border-rightColor").next().css("background-color",borderRightColor);
		$("#border-rightPx").val(borderRightWidth);
	}
	var borderBottomWidth=$(".pageContext").css("borderBottomWidth");
	var borderBottomColor=$(".pageContext").css("borderBottomColor");
	borderBottomWidth=borderBottomWidth.substring(0,borderBottomWidth.length-2);
	if(borderBottomWidth>0){
		$("#border-bottom").val($(".pageContext").css("borderBottomStyle"));
		$("#border-bottomColor").val(RGBToHex(borderBottomColor));
		$("#border-bottomColor").next().css("background-color",borderBottomColor);
		$("#border-bottomPx").val(borderBottomWidth);
	}
	//背景图
	var backgroundRepeat=$(".bodyPage").css("background-repeat");
	var backgroundColor=$(".bodyPage").css("background-color");
	var backgroundURL=$(".bodyPage").css("background-image");
	if(backgroundURL!=null&&backgroundURL.trim()!="none"){
		var chrome=backgroundURL.indexOf("\"");
		var lastIndex=backgroundURL.length-2;
		if(chrome==-1){
			lastIndex=backgroundURL.length-1;
		}
		backgroundURL=backgroundURL.substring(backgroundURL.indexOf("http"),lastIndex);
		$("#globalSetForm input[name='backgroundImg']").val(backgroundURL);
		$("#preImg0").attr("src",backgroundURL);
	}
	$("#globalSetForm select[name='background-repeat']").val(backgroundRepeat);
	$("#globalSetForm input[name='backgroundColor']").val(RGBToHex(backgroundColor));
	$("#globalSetForm input[name='backgroundColor']").next().css("background-color",backgroundColor);
	//内容区背景
	var contentBackgroundRepeat=$(".pageContext").css("background-repeat");
	var contentBackgroundColor=$(".pageContext").css("background-color");
	var contentBackgroundURL=$(".pageContext").css("background-image");
	if(contentBackgroundURL!=null&&contentBackgroundURL.trim()!="none"){
		var chrome=contentBackgroundURL.indexOf("\"");
		var lastIndex=contentBackgroundURL.length-2;
		if(chrome==-1){
			lastIndex=contentBackgroundURL.length-1;
		}
		contentBackgroundURL=contentBackgroundURL.substring(contentBackgroundURL.indexOf("http"),lastIndex);
		$("#globalSetForm input[name='contentBackgroundImg']").val(contentBackgroundURL);
		$("#preImg1").attr("src",contentBackgroundURL);
	}
	$("#globalSetForm select[name='contentBackground-repeat']").val(contentBackgroundRepeat);
	$("#globalSetForm input[name='contentBackgroundColor']").val(RGBToHex(contentBackgroundColor));
	$("#globalSetForm input[name='contentBackgroundColor']").next().css("background-color",contentBackgroundColor);
}