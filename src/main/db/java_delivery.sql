-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: javadelivery
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'd94a4558-335b-11f1-b286-0a002700000c:1-1809';

--
-- Table structure for table `discount`
--

DROP TABLE IF EXISTS `discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discount` (
  `discount_id` int NOT NULL AUTO_INCREMENT,
  `minimum_amount` decimal(12,2) unsigned DEFAULT NULL,
  `discount_percentage` decimal(5,4) DEFAULT NULL,
  PRIMARY KEY (`discount_id`),
  UNIQUE KEY `minimum_amount_UNIQUE` (`minimum_amount`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discount`
--

LOCK TABLES `discount` WRITE;
/*!40000 ALTER TABLE `discount` DISABLE KEYS */;
INSERT INTO `discount` VALUES (1,30000.00,0.1000),(2,50000.00,0.2000),(3,100000.00,0.3500),(4,150000.00,0.5000);
/*!40000 ALTER TABLE `discount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `order_id` int NOT NULL,
  `detail_number` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int DEFAULT NULL,
  `subtotal` decimal(12,2) DEFAULT '0.00',
  PRIMARY KEY (`order_id`,`detail_number`),
  KEY `order_fk_idx` (`order_id`),
  KEY `product_id_fk, restaurant_id_fk_idx` (`product_id`),
  KEY `product_restaurant_fk` (`product_id`),
  CONSTRAINT `order_fk` FOREIGN KEY (`order_id`) REFERENCES `user_order` (`order_id`),
  CONSTRAINT `product_fk_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (1,1,4,2,24584.00),(1,2,5,2,15786.40),(2,1,1,3,3870.96),(3,1,2,6,114121.20),(3,2,5,3,23679.60),(4,1,2,2,38040.40),(4,2,3,1,16059.30),(4,3,4,2,24584.00),(5,1,1,1,1290.32),(5,2,3,1,16059.30),(5,3,5,2,15786.40),(6,1,1,3,3870.96),(6,2,3,2,32118.60),(6,3,4,2,24584.00),(7,1,1,1,1290.32),(7,2,3,2,32118.60),(7,3,4,3,36876.00),(8,1,5,5,39466.00),(9,1,2,5,95101.00),(9,2,4,2,24584.00),(10,1,1,1,1290.32),(10,2,2,1,19020.20),(10,3,3,1,16059.30),(10,4,4,1,12292.00),(10,5,5,1,7893.20),(11,1,2,2,38040.40),(11,2,3,1,16059.30),(11,3,4,1,12292.00),(12,1,1,3,3870.96),(12,2,2,2,38040.40),(13,1,3,1,16059.30),(13,2,4,1,12292.00),(14,1,3,2,32118.60),(14,2,4,5,61460.00),(15,1,6,1,8000.00),(15,2,7,3,45000.00),(16,1,10,2,40000.00),(17,1,2,10,190202.00),(18,1,11,4,60000.00),(19,1,1,4,5161.28),(19,2,2,2,38040.40),(19,3,3,1,16059.29),(19,4,4,1,12292.00),(19,5,5,2,15786.40);
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(100) COLLATE utf8mb3_bin DEFAULT NULL,
  `price` decimal(12,2) DEFAULT NULL,
  `product_type_id` int NOT NULL,
  `restaurant_id` int NOT NULL,
  PRIMARY KEY (`product_id`),
  KEY `product_type_fk_idx` (`product_type_id`),
  KEY `restaurant_product_fkey_idx` (`restaurant_id`),
  CONSTRAINT `product_type_fk` FOREIGN KEY (`product_type_id`) REFERENCES `product_type` (`product_type_id`),
  CONSTRAINT `restaurant_product_fkey` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Jamón crudo, 300g',1290.32,1,1),(2,'Roast beef, 200g',19020.20,3,1),(3,'Fideos a la carbonara',16059.29,2,1),(4,'Tiramisu',12292.00,4,1),(5,'Milanesa Napolitana',7893.20,3,1),(6,'Flan con Dulce de Leche',8000.00,4,3),(7,'Sandwich de Lomo',15000.00,3,3),(8,'Flan con Dulce de Leche',15000.00,4,4),(9,'Tarta de Pollo',10000.00,5,4),(10,'Tarta de Pollo y Verdura',20000.00,5,10),(11,'Tarta de Pollo',15000.00,5,8),(12,'Vino Tinto',11150.00,6,2);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_type`
--

DROP TABLE IF EXISTS `product_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_type` (
  `product_type_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(80) COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`product_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_type`
--

LOCK TABLES `product_type` WRITE;
/*!40000 ALTER TABLE `product_type` DISABLE KEYS */;
INSERT INTO `product_type` VALUES (1,'Embutidos'),(2,'Pastas'),(3,'Carnes'),(4,'Postres'),(5,'Tartas'),(6,'Bebidas');
/*!40000 ALTER TABLE `product_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant`
--

DROP TABLE IF EXISTS `restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant` (
  `restaurant_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(80) COLLATE utf8mb3_bin DEFAULT NULL,
  `address` varchar(80) COLLATE utf8mb3_bin DEFAULT NULL,
  `image_url` varchar(80) COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`restaurant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant`
--

LOCK TABLES `restaurant` WRITE;
/*!40000 ALTER TABLE `restaurant` DISABLE KEYS */;
INSERT INTO `restaurant` VALUES (1,'La Esquina','Av. Pellegrini 1234','uploads/restaurant1.jpg'),(2,'El Buen Sabor','San Lorenzo 850','uploads/restaurant2.jpg'),(3,'Don Giuseppe','Italia 456','uploads/restaurant3.jpg'),(4,'Sabores del Sur','Bv. Oroño 1720','uploads/restaurant4.jpg'),(5,'La Terraza','Córdoba 2100','uploads/restaurant5.jpg'),(6,'Parrilla El Fogón','Mendoza 1450','uploads/restaurant6.jpg'),(7,'Green Garden','Santa Fe 980','uploads/restaurant7.jpg'),(8,'Mar y Tierra','Entre Ríos 620','uploads/restaurant8.jpg'),(10,'Lo de Pepe','Rivadavia 4540','uploads/71bee175-585d-48f1-a796-9a74fc0feb68.jpg'),(12,'El Cairo','Santa Fe 1102','uploads/b9b21456-1996-4b72-8228-f3763cd2c75c.png');
/*!40000 ALTER TABLE `restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `schedule_number` int NOT NULL,
  `restaurant_id` int NOT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `day_of_week` enum('monday','tuesday','wednesday','thursday','friday','saturday','sunday') COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`schedule_number`,`restaurant_id`),
  KEY `restaurant_fk_idx` (`restaurant_id`),
  CONSTRAINT `restaurant_fk_id` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (1,1,'08:00:00','20:00:00','monday'),(1,2,'07:30:00','16:00:00','monday'),(1,3,'12:00:00','15:00:00','tuesday'),(1,4,'20:00:00','23:00:00','friday'),(1,5,'12:00:00','16:00:00','monday'),(1,6,'20:00:00','23:30:00','saturday'),(1,7,'12:00:00','15:00:00','monday'),(1,8,'12:00:00','13:30:00','tuesday'),(1,10,'12:30:00','19:30:00','monday'),(1,12,'14:00:00','00:00:00','monday'),(2,1,'12:00:00','16:00:00','tuesday'),(2,2,'11:30:00','15:30:00','wednesday'),(2,3,'12:00:00','15:00:00','thursday'),(2,4,'20:00:00','23:00:00','saturday'),(2,5,'12:00:00','16:00:00','tuesday'),(2,6,'13:00:00','21:00:00','tuesday'),(2,7,'12:00:00','15:00:00','thursday'),(2,8,'12:00:00','16:00:00','thursday'),(2,10,'10:30:00','13:25:00','tuesday'),(3,1,'12:00:00','16:00:00','wednesday'),(3,2,'20:00:00','23:30:00','friday'),(3,3,'20:00:00','23:30:00','friday'),(3,4,'17:00:00','21:00:00','tuesday'),(3,5,'12:00:00','16:00:00','wednesday'),(3,7,'12:00:00','15:00:00','friday'),(3,8,'20:00:00','23:30:00','friday'),(3,12,'00:00:00','01:00:00','friday'),(4,1,'20:00:00','00:00:00','friday'),(4,3,'20:00:00','23:30:00','saturday'),(4,5,'20:00:00','00:00:00','friday'),(4,7,'16:00:00','19:30:00','tuesday'),(4,8,'20:00:00','23:30:00','sunday'),(4,12,'12:00:00','23:59:00','thursday'),(5,1,'20:00:00','00:00:00','saturday'),(5,5,'20:00:00','00:00:00','saturday'),(6,1,'20:00:00','22:00:00','sunday');
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(80) COLLATE utf8mb3_bin NOT NULL,
  `name` varchar(80) COLLATE utf8mb3_bin DEFAULT NULL,
  `surname` varchar(80) COLLATE utf8mb3_bin DEFAULT NULL,
  `password` varchar(200) COLLATE utf8mb3_bin DEFAULT NULL,
  `phone_number` varchar(20) COLLATE utf8mb3_bin DEFAULT NULL,
  `dni` varchar(10) COLLATE utf8mb3_bin DEFAULT NULL,
  `address` varchar(80) COLLATE utf8mb3_bin DEFAULT NULL,
  `role` enum('admin','client') COLLATE utf8mb3_bin NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'a@a','a','a','a','a','a','a','client'),(3,'kiki@gmail.com','Juan Ignacio','De Giorgi','123','1209301923','46464646','Rivadavia 1121','client'),(4,'santifilippini2005@gmail.com','Santiago','Filippini','fili1234','3464440885','46653844','Rivadavia 6060','client'),(6,'admin@gmail.com','Administrador','adddddmin','admin',NULL,NULL,NULL,'admin'),(7,'juanp@hotmail.com','Juan','Perez','123','3464009010','30303030','Salta 4310','client');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_order`
--

DROP TABLE IF EXISTS `user_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_order` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `total_amount` decimal(12,2) DEFAULT '0.00',
  `discount_id` int DEFAULT NULL,
  `user_id` int NOT NULL,
  `restaurant_id` int NOT NULL,
  `status` enum('pending','delivered') COLLATE utf8mb3_bin NOT NULL DEFAULT 'pending',
  PRIMARY KEY (`order_id`),
  KEY `restaurant_fk_idx` (`restaurant_id`),
  KEY `user_fk_idx` (`user_id`),
  KEY `discount_fk_idx` (`discount_id`),
  CONSTRAINT `discount_fk` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`discount_id`),
  CONSTRAINT `restaurant_id_fk` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order`
--

LOCK TABLES `user_order` WRITE;
/*!40000 ALTER TABLE `user_order` DISABLE KEYS */;
INSERT INTO `user_order` VALUES (1,'2026-09-06 00:00:00',40370.40,1,3,1,'delivered'),(2,'2026-09-06 00:00:00',3870.96,NULL,1,1,'delivered'),(3,'2026-09-06 00:00:00',137800.80,3,3,1,'delivered'),(4,'2026-09-06 00:00:00',78683.70,2,3,1,'delivered'),(5,'2026-09-06 00:00:00',33136.02,1,4,1,'delivered'),(6,'2026-09-07 00:00:00',60573.56,2,4,1,'delivered'),(7,'2026-09-07 00:00:00',70284.92,2,3,1,'delivered'),(8,'2026-09-07 00:00:00',39466.00,1,3,1,'delivered'),(9,'2026-09-08 00:00:00',119685.00,3,4,1,'pending'),(10,'2026-09-08 00:00:00',56555.02,2,4,1,'pending'),(11,'2026-09-14 00:00:00',66391.70,2,4,1,'pending'),(12,'2026-09-14 00:00:00',41911.36,1,3,1,'delivered'),(13,'2026-09-14 00:00:00',28351.30,NULL,3,1,'pending'),(14,'2026-09-15 00:00:00',93578.60,2,4,1,'pending'),(15,'2026-09-22 00:00:00',53000.00,2,4,3,'delivered'),(16,'2026-10-05 00:00:00',40000.00,1,4,10,'delivered'),(17,'2026-10-06 00:00:00',190202.00,4,7,1,'delivered'),(18,'2026-10-11 00:00:00',60000.00,2,3,8,'pending'),(19,'2026-10-12 00:00:00',87339.37,2,7,1,'pending');
/*!40000 ALTER TABLE `user_order` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-15 20:17:38
