-- Usuario de desarrollo. En un despliegue público: crear el usuario con @'localhost' (no '%') y otra clave,
-- y pasar los datos a la app con las variables de entorno DB_HOST, DB_PORT, DB_USER, DB_PASSWORD y DB_NAME (ver DbConnector.java).

CREATE USER 'DBAdmin'@'%' IDENTIFIED BY 'admin';
GRANT SELECT, INSERT, UPDATE, DELETE ON `javadelivery`.* TO 'DBAdmin'@'%';
