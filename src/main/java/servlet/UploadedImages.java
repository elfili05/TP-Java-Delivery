package main.java.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.logic.RestaurantImageStorage;

/**
 * Sirve las imágenes de restaurantes (uploads/<archivo>). Las busca primero en la carpeta de subidas configurada
 * (UPLOADS_DIR, que puede estar fuera de la app y sobrevivir a un nuevo despliegue) y, si no están ahí, en la carpeta
 * uploads/ de la aplicación (donde están las imágenes de ejemplo restaurantN.jpg).
 */
@WebServlet("/uploads/*")
public class UploadedImages extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// solo nombres simples con extensión de imagen permitida: nada de rutas ni de otros tipos de archivo.
	private static final String ALLOWED_NAME = "(?i)[A-Za-z0-9_.-]+\\.(jpg|jpeg|png|webp)";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || !pathInfo.startsWith("/") || !pathInfo.substring(1).matches(ALLOWED_NAME)) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String fileName = pathInfo.substring(1);

		Path file = findFile(fileName);
		if (file == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		response.setContentType(contentTypeOf(fileName));
		response.setContentLengthLong(Files.size(file));
		response.setHeader("Cache-Control", "public, max-age=86400");
		response.setHeader("X-Content-Type-Options", "nosniff");
		try (OutputStream out = response.getOutputStream()) {
			Files.copy(file, out);
		}
	}

	private Path findFile(String fileName) {
		// 1) carpeta de subidas configurada; 2) carpeta uploads/ de la aplicación.
		Path uploadsDir = RestaurantImageStorage.uploadsDir(getServletContext());
		Path found = regularFileInside(uploadsDir, fileName);
		if (found != null) {
			return found;
		}

		String appUploads = getServletContext().getRealPath("/uploads");
		return appUploads == null ? null : regularFileInside(Paths.get(appUploads).toAbsolutePath().normalize(), fileName);
	}

	// el archivo solo cuenta si queda dentro de la carpeta indicada y existe.
	private Path regularFileInside(Path directory, String fileName) {
		if (directory == null) {
			return null;
		}
		Path candidate = directory.resolve(fileName).normalize();
		return candidate.startsWith(directory) && Files.isRegularFile(candidate) ? candidate : null;
	}

	private String contentTypeOf(String fileName) {
		String lower = fileName.toLowerCase();
		if (lower.endsWith(".png")) {
			return "image/png";
		}
		if (lower.endsWith(".webp")) {
			return "image/webp";
		}
		return "image/jpeg";
	}

}
