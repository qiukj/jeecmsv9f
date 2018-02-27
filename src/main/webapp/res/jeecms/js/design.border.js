//弹出布局组件表单项初始化
function initBorderDialogForm(){
	//初始化dialog参数值
	$(".dialogForm input").each(function(){
		$(this).filter("input[name='title']").val($("#"+clickBorder+" span[type='borderTitle']").html());
		$(this).filter("input[name='link']").val($("#"+clickBorder+" .borderLink").attr("href"));
	});
}
//布局块弹出框点击确定事件(写入参数到cookie和更改元素值)
function borderDialogClick(){
	$("#"+openDialog).dialog("close");
	setBorderValue($('#'+clickFrom+" input[name='title']").val(), $('#'+clickFrom+" input[name='link']").val());
}
function setBorderValue(title,link){
	$("#"+clickBorder+" span[type='borderTitle']").html(title);
	$("#"+clickBorder+" .borderLink").attr("href",link);
}