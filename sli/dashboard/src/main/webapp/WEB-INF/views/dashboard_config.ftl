<style>
    .selector {
        width:100px;
        padding:10px;
        margin-left: 20px;
        align: left;
    }
    
    .display {
        width:650px;
        height: 400px;
        padding:10px;
        margin-left:20px;
    }
    
    .display textarea {
        width:100%;
        height: 100%;
    }
</style>
<script type="text/javascript">
    $(document).ready(function() {
        $('#saveButton').click(function() {
            var contextRootPath = "${CONTEXT_ROOT_PATH}";
            $.ajax({
                url: contextRootPath + '/service/config/ajaxSave',
                scope: this,
                type: 'POST',
                processData : false,
                data: $('#jsonText').val(),
                contentType: 'application/json',
                success: function(status){
                    if(status == "Success") {
                        alert("Successfully saved the config.");
                    } else if(status == "Permission Denied") {
                        alert("Permission denied, you are not allowed to do this operation.");
                    } else if(status == "Invalid Input") {
                        alert("The input should be a valid JSON string.");
                    }
                },
                error: function(event, request, settings) {
                    alert("Your request could not be completed.");
                }
            });
        });

		document.title = SLC.dataProxy.getLayoutName();
		
		setTimeout(SLC.util.placeholderFix, 500);
    });
</script>

    
    <#if configJSON == "nonLocalEducationAgency" >
        <div id="fileDisplay" class="display">
            <h4> This page is only available for district level IT Administrator.</h4>
        </div>
    <#elseif configJSON != "error">
        <div id="fileSelector" class="selector">
            <button id="saveButton" value="Save Config" >  Save Config </Button>
        </div>
        <div id="fileDisplay" class="display">
            <h4> In order to modify the current config for your Ed. Org., please replace the current config in the text area below with the updated config and click the "Save Config" button</h4>

            <textarea id="jsonText" >${configJSON}</textarea>
        </div>
    </#if>
<br>
