 <@includePanelModel panelId="listOfStudents"/>
 <#assign id = getDivId(panelConfig.id)>
  <div class="ui-widget-no-border">
    <table id="${id}"></table>
    <div id="losError" style="display:none"></div>
  </div>
<script type="text/javascript" src="/dashboard/static/js/ListOfStudent.js"></script>
<script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/populationWidget.js"></script>
<script type="text/javascript">
    function getTableId() {
        return '${id}';
    }
    var instHierarchy=DashboardProxy.getData('userEdOrg')['root'];
    populateInstHierarchy();
</script>
