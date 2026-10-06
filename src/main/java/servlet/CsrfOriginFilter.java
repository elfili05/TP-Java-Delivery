package main.java.servlet;

import java.io.IOException;
import java.net.URI;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.data.DbConnector;

/**
 * Filtro general de la app (se ejecuta antes que cualquier servlet):
 * - Anti-CSRF: rechaza los POST que no vienen de una página de esta misma app. Un sitio externo que arme un formulario
 *   hacia (por ejemplo) OrderDeliver o RestaurantDelete hace que el navegador mande la cookie de sesión del admin,
 *   pero el encabezado Origin (o Referer) lleva el host del sitio externo. Si el request no trae ninguno de los dos
 *   (curl, herramientas) se deja pasar: no lo manda un navegador.
 * - Codificación: fija UTF-8 para leer los formularios (sin esto Tomcat decodifica los POST como ISO-8859-1 y los acentos y la ñ se guardan rotos).
 * - Cabeceras de seguridad: X-Frame-Options (anti-clickjacking) y no-store en las páginas dinámicas (que "atrás" no muestre páginas de una sesión cerrada).
 * - Red de seguridad: al terminar el request cierra la conexión de base de datos del hilo si algún repositorio no la liberó.
 */
@WebFilter("/*")
public class CsrfOriginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// tiene que ir antes de que alguien lea un parámetro del request.
		request.setCharacterEncoding("UTF-8");

		httpResponse.setHeader("X-Frame-Options", "DENY");
		if (!isStaticResource(httpRequest)) {
			httpResponse.setHeader("Cache-Control", "no-store");
		}

		if ("POST".equalsIgnoreCase(httpRequest.getMethod()) && !comesFromSameHost(httpRequest)) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Solicitud rechazada: origen no permitido.");
			return;
		}

		try {
			chain.doFilter(request, response);
		} finally {
			DbConnector.getInstance().forceRelease();
		}
	}

	// las imágenes, estilos y subidas pueden cachearse; todo lo demás es dinámico.
	private boolean isStaticResource(HttpServletRequest request) {
		String path = request.getServletPath();
		return path.startsWith("/assets/") || path.startsWith("/styles/") || path.equals("/uploads") || path.startsWith("/uploads/");
	}

	// compara el host (con puerto) de Origin/Referer contra el host con el que se pidió esta página.
	private boolean comesFromSameHost(HttpServletRequest request) {
		// los navegadores modernos avisan solos si el POST sale de la misma página.
		if ("same-origin".equalsIgnoreCase(request.getHeader("Sec-Fetch-Site"))) {
			return true;
		}

		String source = request.getHeader("Origin");
		if (source == null) {
			source = request.getHeader("Referer");
		}
		if (source == null) {
			return true;
		}

		try {
			String sourceAuthority = new URI(source).getAuthority();
			if (sourceAuthority == null) {
				return false;
			}
			// detrás de un proxy inverso el Host puede ser el interno: se acepta también el que reenvía el proxy.
			return sourceAuthority.equalsIgnoreCase(request.getHeader("Host"))
					|| sourceAuthority.equalsIgnoreCase(request.getHeader("X-Forwarded-Host"));
		} catch (Exception e) {
			// Origin "null" o una URL mal formada: no se puede verificar, se rechaza.
			return false;
		}
	}

}
