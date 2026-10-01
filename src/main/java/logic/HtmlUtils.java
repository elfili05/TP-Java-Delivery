package main.java.logic;

public class HtmlUtils {

	// escapa los caracteres especiales de HTML para imprimir texto dinámico en un JSP sin que se interprete como código (evita XSS).
	public static String escape(String text) {
		if (text == null) {
			return "";
		}

		StringBuilder escaped = new StringBuilder(text.length());
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			switch (c) {
				case '&':
					escaped.append("&amp;");
					break;
				case '<':
					escaped.append("&lt;");
					break;
				case '>':
					escaped.append("&gt;");
					break;
				case '"':
					escaped.append("&quot;");
					break;
				case '\'':
					escaped.append("&#39;");
					break;
				default:
					escaped.append(c);
			}
		}
		return escaped.toString();
	}

	// devuelve la URL de imagen lista para usar dentro de un atributo HTML o un url("...") de CSS inline:
	// si está vacía o trae caracteres que podrían cerrar el atributo/CSS (comillas, paréntesis, espacios, < >), usa la imagen por defecto.
	public static String safeImageUrl(String imageUrl, String fallback) {
		if (imageUrl == null || !imageUrl.trim().matches("[A-Za-z0-9_./:%?=&+~-]+")) {
			return fallback;
		}
		return imageUrl.trim();
	}

}
