<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Choose your realm</title>
</head>
<style>
body {
  background-color: #fff;
  color: #333;
  font-family: verdana, arial, helvetica, sans-serif;
  font-size: 13px;
  line-height: 18px;
}
</style>
<body>
	<div style="color: red;width: 35%;">${errorMsg}</div>
	<form:form action="/api/oauth/sso" method="GET" commandName="dummy">
		<input type="hidden" name="RelayState" value="${relayState}" />
		<input type="hidden" name="clientId" value="${clientId}" />
		<input type="hidden" name="state" value="${state}" />
		Please choose your State/District:
		<form:select path="" name="realmId" items="${realms}" />
		<input type="submit" value="Go" id="go" />

	</form:form>
</body>
</html>