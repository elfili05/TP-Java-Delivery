<%@page import="main.java.entities.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%  
	User u = (User) session.getAttribute("user");
%>
</head>
<body>
	<h1> HOLA <%=(u != null) ? u.getName() : "Invitado" %> </h1>
	<p>lorem ipsum dolor sit amet.</p>
</body>
</html>