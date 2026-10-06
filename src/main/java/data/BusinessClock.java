package main.java.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

// la hora "de negocio" (cuándo abre un restaurante, qué día es hoy) se calcula acá en Java con una zona fija,
// no con NOW()/CURDATE() de MySQL: así no depende de la zona horaria del servidor de base de datos ni de la de la JVM.
// por defecto es la de Argentina; se puede cambiar con la variable de entorno BUSINESS_TIMEZONE.
public class BusinessClock {

	private static final ZoneId ZONE = ZoneId.of(valueOf("BUSINESS_TIMEZONE", "America/Argentina/Buenos_Aires"));
	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

	// fecha de hoy en la zona de negocio (la usa Order para mostrar la misma fecha que se guarda en la base).
	public static LocalDate today() {
		return LocalDate.now(ZONE);
	}

	// nombre del día en inglés y minúsculas ("monday"), como se guarda en schedule.day_of_week.
	public static String todayName() {
		return LocalDate.now(ZONE).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();
	}

	// hora actual como texto "HH:mm:ss" para compararla con una columna TIME.
	public static String nowTime() {
		return LocalTime.now(ZONE).format(TIME_FORMAT);
	}

	// fecha de hoy como texto "yyyy-MM-dd" para guardarla en una columna de fecha.
	public static String todayDate() {
		return today().toString();
	}

	private static String valueOf(String variableName, String defaultValue) {
		String value = System.getenv(variableName);
		return (value != null && !value.trim().isEmpty()) ? value.trim() : defaultValue;
	}

}
