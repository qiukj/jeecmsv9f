//改变布局组件大小更改宽、高
function contentBolockCSS(object){
	var width=object.css("width");
	var height=object.css("height");
	object.find("div[type='btDiv']").css({"width":width,"height":height});
	object.find(".content").css({"max-width":width,"max-height":height});
	divWidth=width.substring(0,width.indexOf("px"))-20+"px";
	divHeight=height.substring(0,height.indexOf("px"))-20+"px";
	object.find(".content div:first").css({"width":divWidth,"height":divHeight});
}
//生成布局组件标签块页面
function ajaxCreateDirectivePage(base) {
	$("div[id^='"+contentIdPrefix+"']").each(function(){
		var id=$(this).attr("id");
		var contentDivId=id.substring(contentIdPrefix.length);
		var directive=$(this).find(".content").html();
		$.post("../visual/createPage"+contentDivId+".do", {
			"directive" : directive
		}, function(data) {
			setTimeout("loadDiretivePage('"+base+"',"+contentDivId+")",100*1);  
		}, "json");
	});
}
//加载布局组件标签块页面
function loadDiretivePage(base,bordId){
	var contentBlock=$("#"+contentIdPrefix+bordId+" .content");
	contentBlock.after(directDiv);
	$("#"+contentIdPrefix+bordId+" .directive").val(contentBlock.html());
	contentBlock.before(shieldDiv);
	contentBlock.load(base+"/visual/getPage"+bordId+".jspx");
}
//异步生成整个模板html页面
function ajaxCreateHtml(root){
    //处理多余div
    var resizeCopy=new Array();
    $("div[id^='"+contentIdPrefix+"'],div[id^='"+borderIdPrefix+"']").each(function(i){
    	var uiResize=$(this).find("div[class^='ui-resizable']");
    	resizeCopy[i]=uiResize;
    	uiResize.remove();
    	$(this).find(".shield").remove();
 	});
    //标签和展示数据替换(提交后还原content div数据)
    var contentHtml=new Array();
    var directiveHtml=new Array();
    $("div[id^='"+contentIdPrefix+"']").each(function(i){
       var directiveBlock=$(this).find(".directive");
       var contentBlock=$(this).find(".content");
       contentHtml[i]=contentBlock.html();
       directiveHtml[i]=directiveBlock.val();
       contentBlock.text(directiveBlock.val());
	   directiveBlock.remove();
    });
	var source=$(".rightDiv").html();
	source=source.trim();  
	source=source.replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&amp;","&");
	var filename=$("#filename").val();
	var directiveParams=getParamsByCookie("border");
	directiveParams+=getParamsByCookie("content");
	//生成html
	$.post("createhtml.do", {
		source:source,
		root:root,
		filename:filename,
		params:directiveParams
	}, function(data) {
	}, "json");
	//还原标签
	$("div[id^='"+contentIdPrefix+"']").each(function(i){
	   $(this).find(".content").after(directDiv);
	   $(this).find(".directive").val(directiveHtml[i]);
	   $(this).find(".content").html(contentHtml[i]);
	   $(this).find(".content").before(shieldDiv);
	   $(this).append(resizeCopy[i]);
	});
	$("div[id^='"+contentIdPrefix+"'],div[id^='"+borderIdPrefix+"']").each(function(i){
		   $(this).append(resizeCopy[i]);
   });
}
function preview(root){
	ajaxCreateHtml(root);
	setTimeout("previewSubmit('"+root+"')",500*1);   
}
function previewSubmit(root){
	$("#tplName").val(root+"/"+$("#filename").val()+".html");
	$("#previewForm").submit();
}
//单个布局块参数cookie生成
function initWriteDirectiveParamToCookie(blockId,blockCookiePrefix,blockParamArray,blockValueArray){
	for(var i=0;i<blockParamArray.length;i++){
		$.cookie(blockCookiePrefix+"_"+blockId+"_"+blockParamArray[i],blockValueArray[i]);
	}
}
//依据标签生成临时页面（然后加载）
function ajaxCreateTempPage(directive) {
	$.post("../visual/createTempPage.do", {
		"directive" : directive
	}, function(data) {
		setTimeout("loadTempPage()",500*1);   
	}, "json");
}
//删除当前所选组件cookie以及服务器端数据
function ajaxDeleteSelectedContainer(){
	var bid=clickBorder.split(borderIdPrefix)[1];
	if(bid!=null){
		$.post("block/delete.do", {
			"blockId" : bid
		}, function(data) {  
		}, "json");
	    clearCookieByPrefix(clickBorder);
	}
}
//获取当前存在组件容器的最大ID
function getExistMaxContainerId(){
	  var maxContainerId;
	  if(contentMaxId!=null&&borderMaxId!=null){
		  maxContainerId=contentMaxId-borderMaxId>0?contentMaxId:borderMaxId;
	  }else if(contentMaxId!=null){
		  maxContainerId=contentMaxId;
	  }else if(borderMaxId!=null){
		  maxContainerId=borderMaxId;
	  }else{
		  maxContainerId=borderId;
	  }
	  borderId=parseInt(maxContainerId)+1;
	  return borderId;
}