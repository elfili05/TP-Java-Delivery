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

/**
 * Filtro anti-CSRF: rechaza los POST que no vienen de una página de esta misma app.
 * Un sitio externo que arme un formulario hacia (por ejemplo) OrderDeliver o RestaurantDelete hace que el navegador
 * mande la cookie de sesión del admin, pero el encabezado Origin (o Referer) lleva el host del sitio externo.
 * Si el request no trae ninguno de los dos (curl, herramientas) se deja pasar: no lo manda un navegador.
 */
@WebFilter("/*")
public class CsrfOriginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		if ("POST".equalsIgnoreCase(httpRequest.getMethod()) && !comesFromSameHost(httpRequest)) {
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "Solicitud rechazada: origen no permitido.");
			return;
		}

		chain.doFilter(request, response);
	}

	// compara el host (con puerto) de Origin/Referer contra el encabezado Host de este request.
	private boolean comesFromSameHost(HttpServletRequest request) {
		String source = request.getHeader("Origin");
		if (source == null) {
			source = request.getHeader("Referer");
		}
		if (source == null) {
			return true;
		}

		try {
			String sourceAuthority = new URI(source).getAuthority();
			return sourceAuthority != null && sourceAuthority.equalsIgnoreCase(request.getHeader("Host"));
		} catch (Exception e) {
			// Origin "null" o una URL mal formada: no se puede verificar, se rechaza.
			return false;
		}
	}

}
