<#--
<textarea name="textarea"></textarea>
-->
<#macro editor
	name value="" height="230"
	fullPage="false" toolbarSet="My"
	label="" noHeight="false" required="false" colspan="" width="100" help="" helpPosition="2" colon=":" hasColon="true"
	maxlength="65535"
	onclick="" ondblclick="" onmousedown="" onmouseup="" onmouseover="" onmousemove="" onmouseout="" onfocus="" onblur="" onkeypress="" onkeydown="" onkeyup="" onselect="" onchange=""
	>
<#include "control.ftl"/><#rt/>
<#--
<textarea id="${name}" name="${name}">${value}</textarea>  
-->
<textarea id="${name}" name="${name}">${value}</textarea>  
<#--
<script id="${name}" name="${name}" type="text/plain">${value}</script>
-->
<script type="text/javascript">
  $(document).ready(function(){
   var editor= UE.getEditor('${name}');
   //截图快捷键ctrl+shift+A
   editor.addshortcutkey({
        "snapscreen" : "ctrl+shift+65"
   });
  });
</script>

<#include "control-close.ftl"/><#rt/>
</#macro>