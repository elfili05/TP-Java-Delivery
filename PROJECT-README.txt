Las carpetas del proyecto funcionan de la siguiente manera:

- src: el codigo fuente, acá va todo el código del proyecto, lo demás son archivos de configuración para el IDE
- src/db: acá va el dump de la base de datos, y todo lo relacionado con ella
- src/java/entities: aca van todas las clases java que corresponden a los objetos del negocio, básicamente es el "modelo".
- src/java/data: acá van todos las clases que tienen que ver con el acceso a la base de datos, los "repository"
- src/java/logic: acá van todas las clases que utilizan las entidades y las clases de acceso a base de datos para cumplir determinados casos de uso, como por ejemplo, un login. Son los "controladores".
- src/java/servlet: Son las clases que se van a encargar de manejar todas las peticiones web, se configuran para que te redireccionen a una pagina u otra
- src/webapp: acá va todo lo relacionado con la página web en sí, acá va la carpeta de estilos, y todos los html que sean necesarios.
- src/webapp/WEB-INF: acá van los .JSP: páginas HTML que tienen código java dentro, para poder mostrar cualquier cosa: listas de objetos del negocio, etc. Son los servlets los que nos redirigen a esas paginas .JSP .