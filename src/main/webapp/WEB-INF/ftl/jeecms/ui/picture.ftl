<#--
<input type="picture"/>
-->
<#macro picture
	maxlength="" readonly="" value=""
	label="" noHeight="false" required="false" colspan="" width="100" help="" helpPosition="2" colon=":" hasColon="true"
	id="" name="" class="" style="" size="" title="" disabled="" tabindex="" accesskey=""
	vld="" equalTo="" maxlength="" minlength="" max="" min="" rname="" rvalue="" imageWidth="100" imageHeight="100"
	onclick="" ondblclick="" onmousedown="" onmouseup="" onmouseover="" onmousemove="" onmouseout="" onfocus="" onblur="" onkeypress="" onkeydown="" onkeyup="" onselect="" onchange=""
	>
<#include "control.ftl"/><#rt/>
<table id="picTable${name}" border="0" style="float:left;">
<tr>
	<td>
		<div>
			<input type="text" id="uploadImgPath${name}" name="${name}" value="${value}"  <#if required=="true">required="true" class="required"</#if> style="width:170px"/>
			<input type="button" value="<@s.m "imageupload.preview"/>" onclick="previewImg('${name}');" class="preview-button"/>
		</div>
		<div>
		<span id="ufc${name}" style="position:relative">
		<input type='text' id='uploadFileText${name}' size="10"/>  <label>
		<input class="browse" type='button' value='<@s.m "global.browse"/>'/>
		<input onchange="$('#uploadFileText${name}').val(this.value)" size="10" type="file" id="uploadFile${name}" class="file-button"/>
		</span> 
		<input type="checkbox" onclick="$('#mark${name}').val(this.checked);"/><@s.m "imageupload.mark"/></label><input type="hidden" id="mark${name}" value="false"/> 
		<input type="button" value="<@s.m "content.fileUpload"/>" onclick="upload('${name}');" class="upload-button"/><br/>
		<@s.m "global.width"/>: <input type="text" id="zoomWidth${name}" value="${imageWidth!}" size="5"/> <@s.m "global.height"/>: <input type="text" id="zoomHeight${name}" value="${imageHeight!}" size="5"/> <input type="button" value="<@s.m "imageupload.cut"/>" onclick="imgCut('${name}');" class="cut-button"/> 
		</div>
	</td>
	<td><img id="preImg${name}" src="${value}" alt="<@s.m "imageupload.preview"/>" noResize="true" style="width:110px;height:110px;background-color:#ccc;border:1px solid #333"/></td>
</tr>
</table>
<#include "scripting-events.ftl"/><#rt/>
<#include "control-close.ftl"/><#rt/>
</#macro>
