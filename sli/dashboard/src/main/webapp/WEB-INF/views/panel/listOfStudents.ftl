<#--
  Copyright 2012 Shared Learning Collaborative, LLC

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
 <@includePanelModel panelId="listOfStudents"/>
 <#assign id = getDivId(panelConfig.id)>
  <div class="ui-widget-no-border">
    <table id="${id}"></table>
    <div id="losError" class="hidden"></div>
  </div>
<#if minifyJs?? && minifyJs= false>
    <script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/ListOfStudent.js"></script>
    <script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/populationWidget.js"></script>
</#if>
<script type="text/javascript">
    function getTableId() {
        return '${id}';
    }
    var instHierarchy=SLC.dataProxy.getData('populationWidget')['root'];
    var courseSectionData;
    populateInstHierarchy();
</script>
