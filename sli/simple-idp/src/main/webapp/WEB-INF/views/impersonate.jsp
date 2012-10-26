<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="org.slc.sli.sandbox.idp.service.DefaultUsersService.Dataset" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Script-Type" content="text/javascript"/>
<title>Sandbox User Impersonation</title>
<link rel="icon" type="image/x-icon" href="resources/favicon.ico"/>
<script type="text/javascript" src="resources/jquery-1.7.2.min.js"></script>

<style type="text/css">
.tenant {
	/* color: #438746 */
}

.realm-name {
	padding: 30px;
	background-color: #EEE;
	border: thick;
	-webkit-border-radius: 6px;
	-mox-border-radius: 6px;
	border-radius: 6px;
	margin-top: 30px;
}

.form-container {
	margin: 10px;
	margin-top: 30px;
}

.tool-tip-link {
	margin-left:140px;
	color:rgb(0, 102, 153);
	font-size:11px; 
}

.custom-role {
    margin-left: 140px;
    margin-top: 20px;
}

.top-gap {
    margin-top: 10px;
}


</style>
<link href="resources/bootstrap.css" rel="stylesheet"/>
<script type="text/javascript">
$(document).ready(function() {
	$("#manualUserDiv").hide();
	datasetChanged();
	
	$("[rel=tooltip]").tooltip();
	$('body').popover({
    	selector: '[rel=popover]'
	});
});

  function disableTextbox() {
	  select = document.getElementById("selected_roles")
	  textbox = document.getElementById("customRoles")
	  if (select.selectedIndex > 0) {
		  textbox.value = ""
	  } 
  }
  
  function disableSelect() {
	   select = document.getElementById("selected_roles")
	   textbox = document.getElementById("customRoles")
	   if (textbox.value.length > 0) {
		   select.selectedIndex = 0
	   } 
	  
  }
  
  function datasetChanged(){
	  $(".userListDiv").hide();
	  $(".userList").prop("disabled", true);
	  dataset = $("#datasets").val();
	  $("#" + dataset + "-usersDiv").show();
	  $("#" + dataset).prop("disabled", false);
  }
  function showSampleUsers(){
	  $("#manualUserDiv").hide();
	  $("#manualUserBtn").removeClass("active");
	  $("#sampleUserDiv").show();
	  $("#sampleUserBtn").addClass("active");
  }  
  function showManualConfig(){
	  $("#manualUserDiv").show();
	  $("#manualUserBtn").removeClass("active");
	  $("#sampleUserDiv").hide();
	  $("#sampleUserBtn").addClass("active");
  }
</script>
</head>

<body onload="document.impersonate_form.datasets.focus();">
    <div class="container">
      <div class="hero-unit">
      	<div class="row">
      		<div class="span2">
      			<img src="resources/default.png" alt="SLC IDP Logo"/>
      		</div><!-- end span2 -->
      		<div class="span8">
      			<h1>Shared Learning Collaborative</h1>
      			<h2>Application Developer Sandbox</h2>
      		</div><!-- end span7 -->
      	</div><!-- end row -->
      </div><!-- end hero-unit -->	
      <h3>Test your application</h3>
      
      	<form id="logout_form" name="logout_form" action="logout" method="post" class="form-horizontal">
      		<input type="hidden" name="realm" value="${fn:escapeXml(realm)}"/>      
      		<div class="alert alert-success">You are currently logged into your developer account as: 
      			<strong id="">${sessionScope.user_session_key.userId}</strong>
				<input type="hidden" name="SAMLRequest" value="${fn:escapeXml(SAMLRequest)}"/><a class="pull-right" href="#" onclick="javascript:document.forms['logout_form'].submit();">Logout</a>
      		</div>
		</form>
		
		<c:if test="${errorMsg!=null}">
				<div class="error-message"><c:out value="${errorMsg}"/></div>
		</c:if>

      <div class="row">
      	<div class="span8 offset4">
      		<div class="btn-group" data-toggle="buttons-radio">
      			<button type="button" class="btn btn-primary active" id="sampleUserBtn" onclick="showSampleUsers()">Use a Sample User</button>
      			<button type="button" class="btn btn-primary" id="manualUserBtn" onclick="showManualConfig()">Manually Specify a User</button>
      		</div>
      	</div><!-- end span8 offset4 -->
      </div> <!-- end row -->
      

	<div id="sampleUserDiv">
		<form id="impersonate_form" name="impersonate_form" action="impersonate" method="post" class="form-horizontal">
			<input type="hidden" name="realm" value="${fn:escapeXml(realm)}"/>
			<input type="hidden" name="SAMLRequest" value="${fn:escapeXml(SAMLRequest)}"/>
			<input type="hidden" name="manualConfig" value="false"/>
			<div class="control-group">
				<label for="datasets" class="control-label">Select your sample dataset  <i class="icon-question-sign" rel="tooltip" data-placement="bottom" data-original-title="Select the sample dataset you ingested into the Sandbox"></i></label>
				<div class="controls">
				<select id="datasets" name="datasets" class="input-xlarge " onchange="datasetChanged()">
                             <option> </option>
					<c:forEach items="${datasets}" var="dataset">
						<option value="${dataset.key}">${dataset.displayName}</option>
					</c:forEach>
				</select>
				</div>
			</div>
			<c:forEach items="${datasets}" var="dataset">
				<div class="control-group userListDiv" id="${dataset.key}-usersDiv">
					<label for="${dataset.key}" class="control-label">Select a test user</label>
					<div class="controls">
					<select id="${dataset.key}" name="userList" class="input-xlarge userList" onchange="">
						<c:forEach items="<%=request.getAttribute(((Dataset)pageContext.getAttribute("dataset")).getKey())%>" var="user">
                              	 <option value="${user.userId}">${user.name} - ${user.role} at ${user.association}</option>
						</c:forEach>
					</select>
					</div>
				</div>
			</c:forEach>
			<div class="control-group">
				<div class="controls">
					<input id="login_button" name="commit" type="submit" value="Test as this User" class="btn btn-primary" />
				</div>
			</div>
		</form>
	</div>
	
	<div id="manualUserDiv">
		<form id="impersonate_form" name="impersonate_form" action="impersonate" method="post" class="form-horizontal">
			<input type="hidden" name="realm" value="${fn:escapeXml(realm)}"/>
			<input type="hidden" name="SAMLRequest" value="${fn:escapeXml(SAMLRequest)}"/>
			<input type="hidden" name="manualConfig" value="true"/>
			<div class="control-group">
				<label for="impersonate_user" class="control-label">Login as User</label>
				<div class="controls">
					<input type="text" id="impersonate_user" name="impersonate_user" value="${impersonate_user}"/>
				</div>
			</div>
			<div class="control-group">
				<label for="selected_roles" class="control-label">Test as this Role</label>
				<div class="controls">
					<select id="selected_roles" name="selected_roles" class="input-xlarge " onchange="disableTextbox();">
                              <option> </option>
						<c:forEach items="${roles}" var="role">
							<option value="${role.id}">${role.name}</option>
						</c:forEach>
					</select>
                         </div>
                         <div class="control-group top-gap">
                             <label for='customRoles' class="control-label">Or Custom Role</label>
                             <div class="controls"><input type="text" id="customRoles" name="customRoles" onchange="disableSelect();" /></div>
                         </div>
			</div>
			<div class="control-group">
				<div class="controls">
					<input id="login_button" name="commit" type="submit" value="Test as this User" class="btn btn-primary" />
				</div>
			</div>
		</form>
	</div>
</body>
</html>
