<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>JSP</title>
</head>
<body>
	<%
		String fpath = (String)request.getAttribute("path");
		out.print(fpath);
	%>
	out.print("<br/>" + "http://imagestorage-projectphase.rhcloud.com<%= fpath %>/webapps/iogs.png");
	<img src="http://imagestorage-projectphase.rhcloud.com<%= fpath %>/webapps/iogs.png" />
</body>