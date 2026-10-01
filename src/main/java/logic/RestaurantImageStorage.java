package main.java.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;

// concentra toda la lógica de validación, guardado y borrado de las imágenes de restaurante
// para que los servlets no tengan que saber cómo se persiste el archivo.
public class RestaurantImageStorage {

	public static final long MAX_IMAGE_BYTES = 2L * 1024 * 1024;

	private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<String>(Arrays.asList("jpg", "jpeg", "png", "webp"));

	private static final String UPLOADS_PREFIX = "uploads/";

	// solo las imágenes que generó la app (uploads/<uuid>.<ext>) pueden borrarse;
	// las semilla restaurantN.jpg y cualquier otra ruta nunca se tocan.
	private static final Pattern GENERATED_IMAGE = Pattern.compile(
			"^" + UPLOADS_PREFIX + "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.(jpg|jpeg|png|webp)$",
			Pattern.CASE_INSENSITIVE);

	// error de validación con un mensaje en español listo para mostrar al admin.
	public static class ImageUploadException extends Exception {
		private static final long serialVersionUID = 1L;

		public ImageUploadException(String message) {
			super(message);
		}
	}

	// true si el usuario eligió un archivo en el input (aunque después resulte inválido).
	public static boolean hasImage(Part part) {
		return part != null && part.getSubmittedFileName() != null && !part.getSubmittedFileName().trim().isEmpty();
	}

	// valida el contenido del archivo y, solo si pasa todo, lo escribe en uploads/ con nombre aleatorio.
	// devuelve la ruta relativa ("uploads/<uuid>.<ext>") que se guarda en la BD.
	public static String store(Part part, ServletContext context) throws ImageUploadException, IOException {
		if (part == null || part.getSize() == 0) {
			throw new ImageUploadException("El archivo está vacío.");
		}
		if (part.getSize() > MAX_IMAGE_BYTES) {
			throw new ImageUploadException("El archivo es demasiado grande (máximo 2 MB).");
		}

		// el nombre del cliente se ignora: solo se usa para sacar la extensión, contra una lista blanca.
		String extension = extensionOf(part.getSubmittedFileName());
		if (extension == null || !ALLOWED_EXTENSIONS.contains(extension)) {
			throw new ImageUploadException("Formato de imagen no permitido (solo JPG, PNG o WEBP).");
		}

		byte[] content;
		try (InputStream in = part.getInputStream()) {
			content = readAllBytes(in);
		}
		if (content.length == 0) {
			throw new ImageUploadException("El archivo está vacío.");
		}
		if (content.length > MAX_IMAGE_BYTES) {
			throw new ImageUploadException("El archivo es demasiado grande (máximo 2 MB).");
		}

		if (!hasExpectedSignature(content, extension)) {
			throw new ImageUploadException("El archivo no es una imagen válida.");
		}
		if (!extension.equals("webp")) {
			// ImageIO no decodifica webp; para jpg/png además se verifica que el contenido sea una imagen real.
			try {
				if (ImageIO.read(new ByteArrayInputStream(content)) == null) {
					throw new ImageUploadException("El archivo no es una imagen válida.");
				}
			} catch (IOException | RuntimeException e) {
				throw new ImageUploadException("El archivo no es una imagen válida.");
			}
		}

		Path uploadsDir = uploadsDir(context);
		if (uploadsDir == null) {
			throw new ImageUploadException("No se pudo guardar la imagen.");
		}
		Files.createDirectories(uploadsDir);

		String fileName = UUID.randomUUID().toString() + "." + extension;
		Path target = uploadsDir.resolve(fileName).normalize();
		if (!target.startsWith(uploadsDir)) {
			throw new ImageUploadException("No se pudo guardar la imagen.");
		}

		Files.write(target, content);
		return UPLOADS_PREFIX + fileName;
	}

	// borra una imagen SOLO si la generó la app y su ruta resuelta queda dentro de uploads/.
	// se usa después de que la BD confirmó el cambio, para no dejar archivos huérfanos.
	public static void deleteIfGenerated(String imageUrl, ServletContext context) {
		if (imageUrl == null || !GENERATED_IMAGE.matcher(imageUrl).matches()) {
			return;
		}

		Path uploadsDir = uploadsDir(context);
		if (uploadsDir == null) {
			return;
		}

		try {
			Path target = uploadsDir.resolve(imageUrl.substring(UPLOADS_PREFIX.length())).normalize();
			if (target.startsWith(uploadsDir)) {
				Files.deleteIfExists(target);
			}
		} catch (IOException | RuntimeException e) {
			e.printStackTrace();
		}
	}

	// resuelve el directorio real de uploads/ dentro de la app desplegada; null si no se puede saber.
	private static Path uploadsDir(ServletContext context) {
		String realPath = context.getRealPath("/uploads");
		if (realPath == null) {
			return null;
		}
		try {
			return Paths.get(realPath).normalize();
		} catch (InvalidPathException e) {
			e.printStackTrace();
			return null;
		}
	}

	// lee el stream completo a memoria; los archivos ya están acotados a 2 MB.
	private static byte[] readAllBytes(InputStream in) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] chunk = new byte[8192];
		int read;
		while ((read = in.read(chunk)) != -1) {
			buffer.write(chunk, 0, read);
		}
		return buffer.toByteArray();
	}

	// saca la extensión (en minúsculas) del nombre que mandó el cliente, limpiando cualquier ruta que venga pegada.
	private static String extensionOf(String submittedFileName) {
		if (submittedFileName == null) {
			return null;
		}
		String fileName;
		try {
			fileName = Paths.get(submittedFileName).getFileName().toString();
		} catch (InvalidPathException e) {
			return null;
		}
		int dot = fileName.lastIndexOf('.');
		if (dot < 0 || dot == fileName.length() - 1) {
			return null;
		}
		return fileName.substring(dot + 1).toLowerCase();
	}

	// chequea los magic bytes del formato declarado por la extensión.
	private static boolean hasExpectedSignature(byte[] content, String extension) {
		switch (extension) {
			case "webp":
				return content.length >= 12
						&& content[0] == 'R' && content[1] == 'I' && content[2] == 'F' && content[3] == 'F'
						&& content[8] == 'W' && content[9] == 'E' && content[10] == 'B' && content[11] == 'P'
						// la cabecera RIFF declara el tamaño del resto del archivo (4 bytes little-endian): debe coincidir.
						&& (content[4] & 0xFFL | (content[5] & 0xFFL) << 8 | (content[6] & 0xFFL) << 16 | (content[7] & 0xFFL) << 24) == content.length - 8;
			case "png":
				return content.length >= 4
						&& (content[0] & 0xFF) == 0x89 && content[1] == 'P' && content[2] == 'N' && content[3] == 'G';
			default:
				// jpg / jpeg arrancan con FF D8 FF.
				return content.length >= 3
						&& (content[0] & 0xFF) == 0xFF && (content[1] & 0xFF) == 0xD8 && (content[2] & 0xFF) == 0xFF;
		}
	}

}
