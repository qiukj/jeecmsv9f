//弹出组件表单项初始化（需完善）
function initContentDialogForm(){
	//初始化dialog参数值
	$(".dialogForm input").each(function(){
		var inputName=$(this).attr("name");
		var inputVal=$.cookie(clickBorder+"_"+inputName);
		$(this).filter(":radio[value='"+inputVal+"']").prop('checked', true);
		$(this).filter(":checkbox[value='"+inputVal+"']").prop('checked', true);
		$(this).filter(":text[name='"+inputName+"']").val(inputVal);
	});
	$("#"+openDialog+" select").each(function(){
		var inputName=$(this).attr("name");
		var inputVal=$.cookie(clickBorder+"_"+inputName);
		if(inputName!=null&&inputVal!=null){
			$(this).val(inputVal);
		}
	});
	//栏目form需要初始化执行方法
	if(openDialog=="channel"){
		listSelect();
	}else if(openDialog=="content"){
		typeSelect();
		sysSelect();
		tplSelect();
		tpl1Select();
		tpl2Select();
		singleTypeSelect();
		disableDesc();
	}else if(openDialog=="picture"){
		selectPositon();
		focusTypeSelect();
	}else if(openDialog=="html"){
		editor.setContent($("#"+clickBorder+" .directive").val());
		editor.focus(true);
	}else if(openDialog=="pic"){
		$("#preImg1").attr("src",$("#"+clickBorder+" .content img").attr("src"));
		$("#picForm input[name='picUrl']").val($("#"+clickBorder+" .content a").attr("href"));
		$("#picForm input[name='width']").val($("#"+clickBorder+" .content img").css("width"));
		$("#picForm input[name='height']").val($("#"+clickBorder+" .content img").css("height"));
	}else if(openDialog=="media"){
		$("#mediaPath").val($("#"+clickBorder+" video").attr("src"));
		$("#mediaForm input[name='width']").val($("#"+clickBorder+" video").attr("width"));
		$("#mediaForm input[name='height']").val($("#"+clickBorder+" video").attr("height"));
	}else if(openDialog=="vote"){
		votelistSelect();
	}
}

//dialog点击ok调用（获取标签并记录参数写入cookie）
//调用标签展示数据
function dialogSuccessCall(){
	$.ajax({
	    cache: true,
	    type: "POST",
	    url:"../directive/v_getcode.do",
	    data:$('#'+clickFrom).serialize(),
	    dataType: "text",
	    async: false,
	    error: function(request) {
	        alert("Connection error");
	    },
	    success: function(data) {
	    	//添加隐藏标签（后续需要处理）
	    	//限定宽高
	    	if(openDialog=="picture"){
	    		var firstDivIndex=data.indexOf("<div>");
	    		var width=$('#pictureWidth').val();
		    	var height=$('#pictureHeight').val();
		    	data=data.substring(0,firstDivIndex)+"<div style='width:"+width+"px;height:"+height+"px'>"+data.substr(firstDivIndex+5);
		    }
	    	$("#"+clickBorder+" .directive").val(data);
	    	var dialogParams=$('#'+clickFrom).serialize().split("&");
	    	for(var i=0;i<dialogParams.length;i++){
	    		var param=dialogParams[i].split("=");
	    		$.cookie(clickBorder+"_"+param[0],param[1]);
	    	}
	    	ajaxCreateTempPage(data);
	    }
	});
	$("#"+openDialog).dialog("close");
}
function htmlDialogClick(){
	clickFrom="htmlForm";
	//记录所选择参数
	var content = editor.getContent();
	$("#"+clickBorder+" .content").html(content);
	$("#"+clickBorder+" .directive").val(content);
	$("#"+openDialog).dialog("close");
}
function picDialogClick(){
	clickFrom="picForm";
	$("#picContainer img").attr("src",$("#preImg1").attr("src"));
	var picUrl=$("#picForm input[name='picUrl']").val();
	if(picUrl!=""){
		$("#picContainer a").attr("href",picUrl);
	}
	$("#picContainer img").css("width",$("#picForm input[name='width']").val());
	$("#picContainer img").css("height",$("#picForm input[name='height']").val());
	var content=$("#picContainer").html();
	$("#"+clickBorder+" .content").html(content);
	$("#"+clickBorder+" .directive").val(content);
	$("#"+openDialog).dialog("close");
}
function mediaDialogClick(){
	clickFrom="mediaForm";
	var mediaPath=$("#mediaPath").val();
	if(mediaPath!=""){
		$("#mediaContainer video").attr("src",mediaPath);
	}
	$("#mediaContainer video").attr("width",$("#mediaForm input[name='width']").val());
	$("#mediaContainer video").attr("height",$("#mediaForm input[name='height']").val());
	var content=$("#mediaContainer").html();
	$("#"+clickBorder+" .content").html(content);
	$("#"+clickBorder+" .directive").val(content);
	$("#"+openDialog).dialog("close");
}