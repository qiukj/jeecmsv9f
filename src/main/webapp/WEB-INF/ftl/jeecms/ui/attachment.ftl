<#--
<input type="attachment"/>
-->
<#macro attachment
	maxlength="" readonly="" value=""
	label="" noHeight="false" required="false" colspan="" width="100" help="" helpPosition="2" colon=":" hasColon="true"
	id="" name="" class="" style="" size="" title="" disabled="" tabindex="" accesskey=""
	vld="" equalTo="" maxlength="" minlength="" max="" min="" rname="" rvalue=""
	onclick="" ondblclick="" onmousedown="" onmouseup="" onmouseover="" onmousemove="" onmouseout="" onfocus="" onblur="" onkeypress="" onkeydown="" onkeyup="" onselect="" onchange=""
	>
<#include "control.ftl"/><#rt/>
<table border="0">
<tr>
	<td align="center"></td>
	<td align="center"><@s.m "content.attachmentPath"/></td>
	<td align="center"><@s.m "content.fileUpload"/></td></tr>
	<tr id="attachTr${name}">
		<td align="center"><input type="hidden" id="attachmentNames${name}" /></td>
		<td align="center"><input type="text" id="attachmentPaths${name}" name="${name!}" value="${value!}" <#if required=="true">required="true" class="required"</#if>/></td>
		<td align="center">
			<span id="afc${name}" style="position:relative;display:block;width:300px;*width:300px;">
			<input type='text' id='attachmentText${name}'/>  
			<input class="browse" type='button' value='<@s.m "global.browse"/>'/>
			<input onchange="$('#attachmentText${name}').val(this.value)" size="19" type="file" name="attachmentFile" id="attachmentFile${name}" class="file-button-other"/>
			<input type="button" value="<@s.m "content.fileUpload"/>" onclick="uploadAttachment('${name}');" class="upload-button"/>
			</span>
			<input type="hidden" id="attachmentFilenames${name}" name="attachmentFilenames" value="${name!}"/>
		</td>
	</tr>
</table>
<#include "scripting-events.ftl"/><#rt/>
<#include "control-close.ftl"/><#rt/>
</#macro>
