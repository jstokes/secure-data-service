<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="org.slc.sli.sandbox.idp.service.DefaultUsersService.Dataset" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Script-Type" content="text/javascript"/>
<title>Sandbox User Impersonation</title>
<link rel="icon" type="image/x-icon" href="resources/favicon.ico"/>
<link href="resources/bootstrap.css" rel="stylesheet"/>
<script type="text/javascript" src="resources/jquery-1.7.2.min.js"></script>

<style type="text/css">

.form-container {
	margin: 10px;
	margin-top: 30px;
}

.top-gap {
    margin-top: 10px;
}

.whitespacesm {
	min-height: 30px;
}
.brandContainer {
    background-color: #F6F3EA;
    border-color: #ECE7D8;
    border-left: 3px solid #ECE7D8;
    border-radius: 6px 6px 6px 6px;
    border-style: solid;
    border-width: 3px;
    color: #007096;
    margin-top: 60px;
    margin-bottom: 30px;
    padding: 30px;
}
.brandContainerTop {
    background-color: #F6F3EA;
    border-left: 3px solid #ECE7D8;
    border-radius: 6px 6px 0 0;
    border-right: 3px solid #ECE7D8;
    border-top: 3px solid #ECE7D8;
    padding: 30px;
}
.brandContainerBottom {
    background-color: #FFFFFF;
    border: 3px solid #ECE7D8;
    border-radius: 0 0 6px 6px;
    padding: 30px;
}

.brandContainer h1 { font-size: 36px; color: #512B73; }

</style>

<link href="resources/bootstrap.css" rel="stylesheet"/>
<link href="resources/globalStyles.css" rel="stylesheet"/>

<script type="text/javascript">
$(document).ready(function() {
	$("#manualUserDiv").hide();
	//$("#sampleUserDiv").hide();
	//$("#impersonationModeDiv").hide();
	datasetChanged();
});

  function disableTextbox() {
	  select = document.getElementById("selected_roles");
	  textbox = document.getElementById("customRoles");
	  if (select.selectedIndex > 0) {
		  textbox.value = "";
	  } 
  }
  
  function disableSelect() {
	   select = document.getElementById("selected_roles");
	   textbox = document.getElementById("customRoles");
	   if (textbox.value.length > 0) {
		   select.selectedIndex = 0;
	   } 
	  
  }
  function showImpersonation(){
	  showSampleUsers();
	  $("#impersonationModeDiv").show();
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
	  $("#manualUserBtn").toggleClass("active");
	  $("#sampleUserDiv").show();
	  $("#sampleUserBtn").toggleClass("active");
  }  
  function showManualConfig(){
	  $("#manualUserDiv").show();
	  $("#manualUserBtn").toggleClass("active");
	  $("#sampleUserDiv").hide();
	  $("#sampleUserBtn").toggleClass("active");
	  return false;
  }
</script>
</head>

<body>
<div class="container">
	<div class="row">
		<div class="span12">
			<div class="brandContainer sandBanner">
				<div class="row">
					<div class="span2"> <img src="resources/SLC-Logo-text.png"> </div>
					<div class="span8">
						<h1>Developer Sandbox</h1>
					</div>
				</div>
			</div>
		</div>
	</div>
      
      	<form id="logout_form" name="logout_form" action="logout" method="post" class="form-horizontal">
      		<input type="hidden" name="realm" value="${fn:escapeXml(realm)}"/>
      		<input type="hidden" name="SAMLRequest" value="${fn:escapeXml(SAMLRequest)}"/>      
      		<div class="alert alert-success">You are currently logged into your developer account as: 
      			<strong id="">${sessionScope.user_session_key.userId}</strong>
				<a  id="logoutLink" class="pull-right" href="#" onclick="javascript:document.forms['logout_form'].submit();">Logout</a>
      		</div>
		</form>
		
		<c:if test="${errorMsg!=null}">
				<div class="error-message"><c:out value="${errorMsg}"/></div>
		</c:if>

      <div class="row">
		<div class="span6">
			<h3>Administer my Sandbox</h3>
			<p>Administering your sandbox allows you to ingest test data, register applications  and manage accounts on your sandbox.</p>
			<div class="whitespacesm"></div>
			<a id="adminLink" href="admin" class="btn btn-primary">Next</a>
		</div>
		<!-- end span6 -->
		<div class="span6">
			<div class="row">
				<div class="span6">
					<h3>Test Applications in my Sandbox</h3>
					<p>The sandbox gives you a safe place to test your applications while you're developing them.</p>
					<!--<div class="whitespacesm"></div>
					 <a id="testNext" href="#" class="btn btn-primary" onclick="showImpersonation()">Next</a>
					 -->
				</div>
			</div>
			<div class="whitespacesm impersonate"></div>
			<div id="impersonationModeDiv" class="row impersonate">
				<div class="span5 offset1">
	      		<div class="btn-group" data-toggle="buttons-radio">
	      			<button type="button" class="btn btn-primary active" id="sampleUserBtn" onclick="showSampleUsers()">Use a Sample User</button>
	      			<button type="button" class="btn btn-primary" id="manualUserBtn" onclick="showManualConfig()">Manually Specify a User</button>
	      		</div>
	      	</div><!-- end span8 offset4 -->
	      </div> <!-- end row -->
	      <div class="whitespacesm"></div>      
	
		  <div id="sampleUserDiv">
			<form id="impersonate_form" name="impersonate_form" action="impersonate" method="post" class="form-horizontal">
				<input type="hidden" name="manualConfig" value="false"/>
				<input type="hidden" name="realm" value="${fn:escapeXml(realm)}"/>
      			<input type="hidden" name="SAMLRequest" value="${fn:escapeXml(SAMLRequest)}"/>
				<div class="control-group">
					<label for="datasets" class="control-label">Select your sample dataset</label>
					<div class="controls">
					<select id="datasets" name="datasets" class="input-xlarge " onchange="datasetChanged()">
	                             <option> </option>
						<c:forEach items="${datasets}" var="dataset" varStatus="status">
							<option value="${dataset.key}" ${status.index == 0 ? "selected='true'" : ""}>${dataset.displayName}</option>
						</c:forEach>
					</select>
					</div>
				</div>
				<c:forEach items="${datasets}" var="dataset">
					<div class="control-group userListDiv" id="${dataset.key}-usersDiv">
						<label for="${dataset.key}" class="control-label">Select a test user</label>
						<div class="controls">
						<select id="${dataset.key}" name="userList" class="input-xlarge userList" onchange="">
							<c:forEach items='<%=request.getAttribute(((Dataset)pageContext.getAttribute("dataset")).getKey())%>' var="user">
	                              	 <option value="${user.userId}">${user.name} - ${user.role} at ${user.association}</option>
							</c:forEach>
						</select>
						</div>
					</div>
				</c:forEach>
				<div class="control-group">
					<div class="controls">
						<input id="sampleUserLoginButton" name="commit" type="submit" value="Test as this User" class="btn btn-primary" />
					</div>
				</div>
			</form>
		  </div>
		
		  <div id="manualUserDiv">
			<form id="impersonate_form" name="impersonate_form" action="impersonate" method="post" class="form-horizontal">
				<input type="hidden" name="manualConfig" value="true"/>
				<input type="hidden" name="realm" value="${fn:escapeXml(realm)}"/>
      			<input type="hidden" name="SAMLRequest" value="${fn:escapeXml(SAMLRequest)}"/>
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
						<input id="manualUserLoginButton" name="commit" type="submit" value="Test as this User" class="btn btn-primary" />
					</div>
				</div>
			</form>
		  </div>
	  </div>
	</div>
</body>
</html>
