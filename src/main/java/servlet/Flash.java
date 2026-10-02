package main.java.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Patrón Post/Redirect/Get del panel admin: después de un POST (crear, editar, borrar) en vez de mostrar el listado
 * directamente se redirige a la pantalla con un GET. Así F5 o "reenviar formulario" no repite el alta (no duplica restaurantes ni tipos).
 * El mensaje del resultado ("Restaurante creado correctamente.") viaja en la sesión solo hasta el próximo GET.
 */
class Flash {

	private static final String MESSAGE_KEY = "flashMessage";
	private static final String REOPEN_CREATE_MODAL_KEY = "flashReopenCreateModal";

	// si el request es un POST guarda el mensaje del resultado (atributo "message") y redirige a la pantalla indicada; devuelve true si redirigió.
	static boolean redirectAfterPost(HttpServletRequest request, HttpServletResponse response, String target) throws IOException {
		if (!"POST".equalsIgnoreCase(request.getMethod())) {
			return false;
		}

		HttpSession session = request.getSession();
		Object message = request.getAttribute("message");
		if (message != null) {
			session.setAttribute(MESSAGE_KEY, message);
		}
		if (request.getAttribute("reopenCreateModal") != null) {
			session.setAttribute(REOPEN_CREATE_MODAL_KEY, Boolean.TRUE);
		}

		response.sendRedirect(target);
		return true;
	}

	// recupera (y borra) el mensaje guardado por el POST anterior para que la pantalla lo muestre una sola vez.
	static void restore(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}

		Object message = session.getAttribute(MESSAGE_KEY);
		if (message != null) {
			request.setAttribute("message", message);
			session.removeAttribute(MESSAGE_KEY);
		}
		if (session.getAttribute(REOPEN_CREATE_MODAL_KEY) != null) {
			request.setAttribute("reopenCreateModal", true);
			session.removeAttribute(REOPEN_CREATE_MODAL_KEY);
		}
	}

}
