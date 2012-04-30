<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <#include "layout/layout_header.ftl">
        

        <script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/3p/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/3p/jquery-ui/js/jquery-ui-1.8.18.custom.min.js"></script>
        <script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/3p/jqGrid/js/jquery.jqGrid.min.js"></script>
        <script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/3p/raphael-min.js"></script>
        <script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/dashboardUtil.js"></script>
        <script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/lozengeWidget.js"></script>
        <script type="text/javascript" src="${CONTEXT_ROOT_PATH}/static/js/fuelGaugeWidget.js"></script>
        <link rel="stylesheet" type="text/css" href="${CONTEXT_ROOT_PATH}/static/js/3p/jquery-ui/css/custom/jquery-ui-1.8.18.custom.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="${CONTEXT_ROOT_PATH}/static/js/3p/jqGrid/css/ui.jqgrid.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="${CONTEXT_ROOT_PATH}/static/css/common.css" media="screen" />
        <style>
            .selector {
                width:10%;
                height:80%;
                float:left;
                padding:10px;
                margin:10px;
            }
            
            .selector select {
                width:85%;
                height:80%;
            }
            
            .display {
                width:85%;
                height:80%;
                float:right;
                padding:10px;
                margin:10px;
            }
            
            .display textarea {
                width:85%;
                height:200px;
                padding:10px;
                margin:10px;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#saveButton').click(function() {
                    var contextRootPath = "${CONTEXT_ROOT_PATH}";
                    $.ajax({
                        url:  contextRootPath + '/service/config/ajaxSave',
                        scope: this,
                        type: 'POST',
                        data: "customConfigJSON=" + $('#jsonText').html(),
                        success: function(status){
                            if(status == "Success") {
                                alert("Successfully saved the config. Please logout and log back in to see the changes.");
                            } else {
                                alert("Unable to save the config. Please try again");
                            }
                        },
                        error: $("body").ajaxError( function(event, request, settings) {
                            if (request.responseText == "") {
                                $(location).attr('href',$(location).attr('href'));
                            } else {
                                $(location).attr('href', contextRootPath + "/exception");
                            }
                        }),
                    });
                });
            });
        </script>

    </head>
    <body>
        <div id="fileSelector" class="selector">
            <button id="saveButton" value="Save Config" style="width:150px; height:40px; font-size:13px;margin:10px;margin-top:50px;">  Save Config </Button>
        </div>
        <div id="fileDisplay" class="display">
            
            <#if configJSON != "error">
                <h4> In order to modify the current connfig for your Ed. Org., please replace the current config in the text area below with the updated config and click the "Save Config" button</h3>
    
                <textarea id="jsonText" class="foo" width="100%">${configJSON}</textarea>
            </#if>
        </div> <br>
        
        
    </body>
</html>

<#include "layout/layout_footer.ftl">