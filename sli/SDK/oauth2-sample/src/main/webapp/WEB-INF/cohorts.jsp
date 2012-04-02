<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.stream.JsonWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cohorts</title>
</head>
<body>
	<table>
		<tr>
			<th id="header.Student">Cohort</th>
		</tr>
		<c:forEach var="cohort" items="${idDesc}">
			<tr>
				<td id="name.${cohort.key}">${cohort.key}</td><td>${cohort.value}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>