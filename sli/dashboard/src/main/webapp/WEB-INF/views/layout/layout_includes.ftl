<#macro includePanelModel panelId>
  <#assign panelConfig = viewConfigs[panelId]>
  <#assign panelData = data[panelConfig.data.alias]>
</#macro>

<#function getDivId panelId>
  <#return panelId + "-" + random.nextInt(99999)?string("#####")>
</#function>

<#macro includeGrid gridId>
  
  <#assign id = getDivId(panelConfig.id)>
  </br>
<div class="ui-widget-no-border">
    <table id="${id}"></table>
</div>
    <script type="text/javascript">


    var tableId = '${id}';
    var panelConfig = config["${gridId}"];
    var data = dataModel[panelConfig.data.alias];
    if (panelConfig.root) {
      data = data[panelConfig.root];
    }

      <#-- make grid -->
      DashboardUtil.makeGrid(tableId, panelConfig, data);

    </script>

</#macro>

<script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/3p/jquery-1.7.1.js"></script>
<script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/3p/jquery-ui/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/3p/jqGrid/js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/3p/raphael-min.js"></script>
<script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/dashboardUtil.js"></script>
<script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/lozengeWidget.js"></script>
<link rel="stylesheet" type="text/css" href="${CONTEXT_ROOT_PATH}/static/js/3p/jquery-ui/css/custom/jquery-ui-1.8.18.custom.css" media="screen" />
<link rel="stylesheet" type="text/css" href="${CONTEXT_ROOT_PATH}/static/js/3p/jqGrid/css/ui.jqgrid.css" media="screen" />
<link rel="stylesheet" type="text/css" href="${CONTEXT_ROOT_PATH}/static/css/common.css" media="screen" />
<script>
  var dataModel = ${dataJson};
  var config = ${viewConfigsJson};
  var widgetConfig = ${widgetConfig};
</script>