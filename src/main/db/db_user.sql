
CREATE USER 'DBAdmin'@'%' IDENTIFIED BY 'admin';
GRANT SELECT, INSERT, UPDATE, DELETE ON `javadelivery`.* TO 'java'@'%';
