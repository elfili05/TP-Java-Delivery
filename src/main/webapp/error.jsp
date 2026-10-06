<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%-- Página de error general (ver <error-page> en web.xml). Las rutas llevan el context path para que se vea bien aunque el error ocurra en una URL anidada. --%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Algo salió mal</title>
	<link rel="icon" type="ico" href="<%= request.getContextPath() %>/assets/icon2.ico" />
	<style>
		body {
			margin: 0;
			min-height: 100vh;
			display: flex;
			align-items: center;
			justify-content: center;
			background: #eeeeee;
			font-family: "Lexend", Arial, sans-serif;
			color: #262626;
			text-align: center;
		}
		main {
			padding: 24px;
			max-width: 420px;
		}
		img {
			width: 120px;
			height: auto;
		}
		a {
			display: inline-block;
			margin-top: 16px;
			padding: 10px 20px;
			border-radius: 8px;
			background: #FF430A;
			color: #ffffff;
			text-decoration: none;
			font-weight: 600;
		}
	</style>
</head>
<body>
	<main>
		<img src="<%= request.getContextPath() %>/assets/error.png" alt="" />
		<h1>Algo salió mal</h1>
		<p>No pudimos completar lo que pediste. Probá de nuevo en unos minutos o volvé al inicio.</p>
		<a href="<%= request.getContextPath() %>/index.html">Volver al inicio</a>
	</main>
</body>
</html>
