<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Java Delivery | Restaurante no disponible</title>
<link rel="stylesheet" href="styles/main.css" />
<link rel="stylesheet" href="styles/main_page.css" />
</head>
<body class="home-page">
	<div class="home-layout">
		<main class="restaurant-section" style="display:flex;align-items:center;justify-content:center;">
			<div class="restaurant-empty" role="status" aria-live="polite">
				<h2>El restaurante no está disponible</h2>
				<p>Parece que usted trató de seleccionar un restaurante, o confirmar un pedido en el mismo justo después de su clausura. Vuelva más tarde.</p>

				<form action="signin" method="post" style="margin-top:1rem;">
					<button type="submit" class="signup-modal__button">Volver al menú principal</button>
				</form>
			</div>
		</main>
	</div>
</body>
</html>