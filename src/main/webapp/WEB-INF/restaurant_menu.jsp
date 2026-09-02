<%@page import="main.java.entities.Restaurant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" type="ico" href="assets/icon2.ico" />
<%
Restaurant res = (Restaurant) request.getAttribute("currentRestaurant");

%>


<title>Menú de <%=res.getName() %></title>



</head>
<body>

</body>
</html>