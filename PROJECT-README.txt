Las carpetas del proyecto funcionan de la siguiente manera:

- src: el codigo fuente, acá va todo el código del proyecto, lo demás son archivos de configuración para el IDE
- src/db: acá va el dump de la base de datos, y todo lo relacionado con ella
- src/java/entities: aca van todas las clases java que corresponden a los objetos del negocio, básicamente es el "modelo".
- src/java/data: acá van todos las clases que tienen que ver con el acceso a la base de datos, los "repository"
- src/java/logic: acá van todas las clases que utilizan las entidades y las clases de acceso a base de datos para cumplir determinados casos de uso, como por ejemplo, un login. Son los "controladores".
- src/java/servlet: Son las clases que se van a encargar de manejar todas las peticiones web, se configuran para que te redireccionen a una pagina u otra
- src/webapp: acá va todo lo relacionado con la página web en sí, acá va la carpeta de estilos, y todos los html que sean necesarios.
- src/webapp/WEB-INF: acá van los .JSP: páginas HTML que tienen código java dentro, para poder mostrar cualquier cosa: listas de objetos del negocio, etc. Son los servlets los que nos redirigen a esas paginas .JSP .

Notas de despliegue y desarrollo:

- Requisitos: JDK 11 o superior (los JSP usan String.isBlank) y Tomcat 9 (la app usa javax.servlet.*, no jakarta.*).
- Los archivos fuente están en UTF-8 y tienen tildes: compilar con -encoding UTF-8 (en Eclipse: Window > Preferences > General > Workspace > Text file encoding = UTF-8).
- La base de datos se arma con src/main/db/java_delivery.sql (el dump es la fuente de verdad del esquema). El usuario de la app está en src/main/db/db_user.sql.
- Conexión a la base: por defecto localhost:3306 / usuario y clave de desarrollo (ver DbConnector.java). En un despliegue se pisa con las variables de entorno DB_HOST, DB_PORT, DB_USER, DB_PASSWORD y DB_NAME.
- Zona horaria del negocio (horarios de los restaurantes y fecha de los pedidos): America/Argentina/Buenos_Aires; se puede cambiar con la variable BUSINESS_TIMEZONE. Para que las horas de los horarios no se desplacen, MySQL y la JVM deben compartir zona horaria; si hace falta, arrancar Tomcat con -Duser.timezone=America/Argentina/Buenos_Aires (un id como "GMT+01:00" no lo acepta el driver y se usa UTC).
- Las imágenes que suben los admins se guardan, por defecto, en la carpeta uploads/ de la aplicación desplegada: si se vuelve a desplegar la app desde cero, se pierden (las rutas quedan en la base). Para que sobrevivan, definir la variable de entorno UPLOADS_DIR (o -Duploads.dir=...) con una carpeta fuera de la app, con permiso de escritura para el usuario de Tomcat; el servlet UploadedImages las sirve desde ahí.
- Si la app corre detrás de un proxy inverso, el proxy tiene que reenviar el encabezado Host original (o X-Forwarded-Host); si no, el filtro anti-CSRF rechaza los formularios.
- Al publicar con HTTPS, agregar <secure>true</secure> dentro de <cookie-config> en WEB-INF/web.xml.
