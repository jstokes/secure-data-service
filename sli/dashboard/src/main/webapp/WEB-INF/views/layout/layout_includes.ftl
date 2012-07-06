<#assign NUL = {"NUL":true}>
<#macro includePanelModel panelId>
  <#assign panelConfig = viewConfigs[panelId]>
  <#assign panelData = data[panelConfig.data.cacheKey]!NUL>
</#macro>

<#macro includePanelContent panel>
  <#if panel.type == "PANEL">
    <@includePanelModel panelId=panel.id/>
    <#if !panelData.NUL?? >
      <#include "../panel/" + panel.id + ".ftl">
    </#if>
  </#if> 
  <#if panel.type == "GRID">
    <@includeGrid gridId=panel.id/>
  </#if>   
</#macro>

<#function getDivId panelId>
  <#return panelId + "-" + random.nextInt(99999)?string("#####")>
</#function>

<#function getContextPath>
    <#return contextRootPath>
</#function> 

<#macro includeGrid gridId>
  
  <#assign id = getDivId(gridId)>
  </br>
<div class="ui-widget-no-border">
    <table id="${id}"></table>
</div>
    <script type="text/javascript">
      <#-- make grid -->
      SLC.grid.tablegrid.create('${id}', SLC.dataProxy.getConfig("${gridId}"), SLC.dataProxy.getData("${gridId}"));

    </script>

</#macro>

<#noescape>


<script>
	var contextRootPath = '${CONTEXT_ROOT_PATH}',
		pageTitle;
		
	SLC.dataProxy.loadAll(${viewDataConfig});

	if ($.browser.msie && parseInt($.browser.version, 10) < 9) {
		document.createElement("footer");
	}
	pageTitle = SLC.dataProxy.getLayoutName();
	document.title = pageTitle;
	
	setTimeout(SLC.util.placeholderFix, 500);
</script>
</#noescape>
<#include "../panel/studentSearch.ftl">
