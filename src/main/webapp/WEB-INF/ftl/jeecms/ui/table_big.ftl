<#--
表格标签：用于显示列表数据。
	value：列表数据，可以是Pagination也可以是List。
	class：table的class样式。默认"pn-ltable"。
	queryParams:查询条件
	sytle：table的style样式。默认""。
	width：表格的宽度。默认100%。
-->
<#macro bigTable value listAction="v_list.do" pageAction="getPage.do" queryParams="" class="pn-ltable" style="" theadClass="pn-lthead" tbodyClass="pn-ltbody" width="100%" cellspacing="0">
<table class="${class}" style="${style}" width="${width}" cellspacing="${cellspacing}" cellpadding="0" border="0">
<#if value?is_sequence><#local pageList=value/><#else><#local pageList=value.list/></#if>
<#list pageList as row>
<#if row_index==0>
<#assign i=-1/>
<thead class="${theadClass}"><tr><#nested row,i,true/></tr></thead>
</#if>
<#assign i=row_index has_next=row_has_next/>
<#if row_index==0><tbody  class="${tbodyClass}"><tr ><#else><tr ></#if><#nested row,row_index,row_has_next/>
<#if !row_has_next>
</tr></tbody>
<#else>
</tr>
</#if>
</#list>
</table>
<#if !value?is_sequence>
<table  id="pageTable" width="100%"  border="0" cellpadding="0" cellspacing="0"><tr><td align="center" class="pn-sp">
	共 <span id="totalCount"></span>条&nbsp;
	每页<input type="text" id="pageSize"  maxlength="3" style="width:30px" onfocus="this.select();" onblur="refresh(this.value)" onkeypress="if(event.keyCode==13){$(this).blur();return false;}"/>条&nbsp;
	<input class="first-page" type="button"  id="firstPage" value="首 页" onclick="_gotoPage('1');"/>
	<input class="pre-page" type="button" id="prePage" value="上一页" />
	<input class="next-page" type="button" id="nextPage" value="下一页" />
	<input class="last-page" type="button" id="totalPage" value="尾 页" />&nbsp;
	当前  <span id="pageTotal">${value.pageNo}/${value.totalPage} </span>页 &nbsp;转到第<input type="text" id="_goPs" style="width:50px" onfocus="this.select();" onkeypress="if(event.keyCode==13){$('#_goPage').click();return false;}"/>页
	<input class="go" id="_goPage" type="button" value="转" onclick="_gotoPage($('#_goPs').val());"<#if value.totalPage==1> disabled="disabled"</#if>/>
</td></tr></table>
<script type="text/javascript">
$.post("${pageAction}", {
		${queryParams!}
	}, function(data) {
	     $("#totalCount").html(data.totalCount);
	     $("#pageSize").val(data.pageSize);
		 $("#pageTotal").html(data.pageNo+"/"+data.totalPage);
	     if(data.firstPage){
	     	 $("#prePage").attr("disabled","disabled");
	     	 $("#firstPage").attr("disabled","disabled");
	     	 $("#nextPage").removeAttr("disabled");
	     	 $("#totalPage").removeAttr("disabled");
	     }else{
	     	$("#prePage").removeAttr("disabled");
	     	$("#firstPage").removeAttr("disabled");
	     }
		 if(data.lastPage){
	     	 $("#nextPage").attr("disabled","disabled");
	     	 $("#totalPage").attr("disabled","disabled");
	     	 $("#prePage").removeAttr("disabled");
	     	 $("#firstPage").removeAttr("disabled");
	     }else{
	      	 $("#nextPage").removeAttr("disabled");
	     	 $("#totalPage").removeAttr("disabled");
	     }
	     $("#prePage").unbind();
	     $("#prePage").bind("click", function(){
		  	_gotoPage(data.prePage);
		 });
		 $("#nextPage").unbind();
	     $("#nextPage").bind("click", function(){
		  	_gotoPage(data.nextPage);
		 });
		 $("#totalPage").unbind();
	     $("#totalPage").bind("click", function(){
		  	_gotoPage(data.totalPage);
		 });
	}, "json");
function _gotoPage(pageNo) {
	try{
		var tableForm = getTableForm();
		$("input[name=pageNo]").val(pageNo);
		tableForm.action="${listAction}";
		tableForm.onsubmit=null;
		tableForm.submit();
	} catch(e) {
		alert('_gotoPage(pageNo)方法出错');
	}
}
function refresh(value){
	$.cookie('_cookie_page_size',value,{expires:3650});
	_gotoPage($("input[name=pageNo]").val());
}
</script>
</#if>
</#macro>