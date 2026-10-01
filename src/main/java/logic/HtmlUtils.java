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

}
