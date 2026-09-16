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

<<<<<<< HEAD
<<<<<<< HEAD
SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'd94a4558-335b-11f1-b286-0a002700000c:1-882';
=======
SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'd94a4558-335b-11f1-b286-0a002700000c:1-966';
>>>>>>> 8ea60ac (feat: orders can now be fully processed in the application. database dump updated with data samples for testing.)
=======
SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'd94a4558-335b-11f1-b286-0a002700000c:1-1114';
>>>>>>> 3fce8c4 (fix: solved a bug where logging out didn't delete the user from the session.)

--
-- Table structure for table `discount`
--

DROP TABLE IF EXISTS `discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discount` (
  `discount_id` int NOT NULL AUTO_INCREMENT,
  `minimum_amount` float unsigned DEFAULT NULL,
  `discount_percentage` float DEFAULT NULL,
  PRIMARY KEY (`discount_id`),
  UNIQUE KEY `minimum_amount_UNIQUE` (`minimum_amount`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discount`
--

LOCK TABLES `discount` WRITE;
/*!40000 ALTER TABLE `discount` DISABLE KEYS */;
INSERT INTO `discount` VALUES (1,30000,0.1),(2,50000,0.2),(3,100000,0.35);
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
  `subtotal` float DEFAULT '0',
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
INSERT INTO `order_detail` VALUES (1,1,4,2,24584),(1,2,5,2,15786.4),(2,1,1,3,3870.96),(3,1,2,6,114121),(3,2,5,3,23679.6),(4,1,2,2,38040.4),(4,2,3,1,16059.3),(4,3,4,2,24584),(5,1,1,1,1290.32),(5,2,3,1,16059.3),(5,3,5,2,15786.4),(6,1,1,3,3870.96),(6,2,3,2,32118.6),(6,3,4,2,24584),(7,1,1,1,1290.32),(7,2,3,2,32118.6),(7,3,4,3,36876),(8,1,5,5,39466),(9,1,2,5,95101),(9,2,4,2,24584),(10,1,1,1,1290.32),(10,2,2,1,19020.2),(10,3,3,1,16059.3),(10,4,4,1,12292),(10,5,5,1,7893.2),(11,1,2,2,38040.4),(11,2,3,1,16059.3),(11,3,4,1,12292),(12,1,1,3,3870.96),(12,2,2,2,38040.4),(13,1,3,1,16059.3),(13,2,4,1,12292),(14,1,3,2,32118.6),(14,2,4,5,61460);
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
  `price` float DEFAULT NULL,
  `product_type_id` int NOT NULL,
  `restaurant_id` int NOT NULL,
  PRIMARY KEY (`product_id`),
  KEY `product_type_fk_idx` (`product_type_id`),
  KEY `restaurant_product_fkey_idx` (`restaurant_id`),
  CONSTRAINT `product_type_fk` FOREIGN KEY (`product_type_id`) REFERENCES `product_type` (`product_type_id`),
  CONSTRAINT `restaurant_product_fkey` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Jamón crudo, 300g',1290.32,1,1),(2,'Roast beef, 200g',19020.2,3,1),(3,'Fideos a la carbonara',16059.3,2,1),(4,'Tiramisu',12292,4,1),(5,'Milanesa Napolitana',7893.2,3,1);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_restaurant`
--

DROP TABLE IF EXISTS `product_restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_restaurant` (
  `restaurant_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`restaurant_id`,`product_id`),
  KEY `product_id_fk_idx` (`product_id`),
  CONSTRAINT `product_fk_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `restaurant_fk_restaurant` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_restaurant`
--

LOCK TABLES `product_restaurant` WRITE;
/*!40000 ALTER TABLE `product_restaurant` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_restaurant` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_type`
--

LOCK TABLES `product_type` WRITE;
/*!40000 ALTER TABLE `product_type` DISABLE KEYS */;
INSERT INTO `product_type` VALUES (1,'Embutidos'),(2,'Pastas'),(3,'Carnes'),(4,'Postres');
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
<<<<<<< HEAD
  `image_url` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
=======
  `image_url` varchar(25) COLLATE utf8mb3_bin DEFAULT NULL,
>>>>>>> 8ea60ac (feat: orders can now be fully processed in the application. database dump updated with data samples for testing.)
  PRIMARY KEY (`restaurant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant`
--

LOCK TABLES `restaurant` WRITE;
/*!40000 ALTER TABLE `restaurant` DISABLE KEYS */;
INSERT INTO `restaurant` VALUES (1,'La Esquina','Av. Pellegrini 1234','uploads/restaurant1.jpg'),(2,'El Buen Sabor','San Lorenzo 850','uploads/restaurant2.jpg'),(3,'Don Giuseppe','Italia 456','uploads/restaurant3.jpg'),(4,'Sabores del Sur','Bv. Oroño 1720','uploads/restaurant4.jpg'),(5,'La Terraza','Córdoba 2100','uploads/restaurant5.jpg'),(6,'Parrilla El Fogón','Mendoza 1450','uploads/restaurant6.jpg'),(7,'Green Garden','Santa Fe 980','uploads/restaurant7.jpg'),(8,'Mar y Tierra','Entre Ríos 620','uploads/restaurant8.jpg');
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
INSERT INTO `schedule` VALUES (1,1,'12:00:00','16:00:00','monday'),(1,2,'11:30:00','18:00:00','monday'),(1,3,'12:00:00','15:00:00','tuesday'),(1,4,'20:00:00','23:00:00','friday'),(1,5,'12:00:00','16:00:00','monday'),(1,6,'20:00:00','23:30:00','saturday'),(1,7,'12:00:00','15:00:00','monday'),(1,8,'12:00:00','16:00:00','tuesday'),(2,1,'12:00:00','16:00:00','tuesday'),(2,2,'11:30:00','15:30:00','wednesday'),(2,3,'12:00:00','15:00:00','thursday'),(2,4,'20:00:00','23:00:00','saturday'),(2,5,'12:00:00','16:00:00','tuesday'),(2,7,'12:00:00','15:00:00','thursday'),(2,8,'12:00:00','16:00:00','thursday'),(3,1,'12:00:00','16:00:00','wednesday'),(3,2,'20:00:00','23:30:00','friday'),(3,3,'20:00:00','23:30:00','friday'),(3,4,'17:00:00','21:00:00','tuesday'),(3,5,'12:00:00','16:00:00','wednesday'),(3,7,'12:00:00','15:00:00','friday'),(3,8,'20:00:00','23:30:00','friday'),(4,1,'20:00:00','00:00:00','friday'),(4,3,'20:00:00','23:30:00','saturday'),(4,5,'20:00:00','00:00:00','friday'),(4,7,'16:00:00','19:30:00','tuesday'),(4,8,'20:00:00','23:30:00','sunday'),(5,1,'20:00:00','00:00:00','saturday'),(5,5,'20:00:00','00:00:00','saturday');
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
<<<<<<< HEAD
<<<<<<< HEAD
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
=======
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
>>>>>>> 8ea60ac (feat: orders can now be fully processed in the application. database dump updated with data samples for testing.)
=======
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
>>>>>>> 3fce8c4 (fix: solved a bug where logging out didn't delete the user from the session.)
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
<<<<<<< HEAD
<<<<<<< HEAD
INSERT INTO `user` VALUES (1,'a@a','a','a','a','a','a','a','client'),(2,'b@b','b','b','b','b','b','b','client'),(3,'c@c','c','c','c','c','c','c','client'),(4,'Hola@hola','Hola','Chau','hola','asdasdl','oasd','JEJE','client'),(5,'pepe@gmail.com','pepe','sand','pepe','2390239203','20202020','Villa 1-11-14','client');
=======
INSERT INTO `user` VALUES (1,'a@a','a','a','a','a','a','a','client'),(2,'b@b','b','b','b','b','b','b','client'),(3,'pepe@gmail.com','Pepe','Sech','pepe','1209301923','46464646','Rivadavia 1121','client');
>>>>>>> 8ea60ac (feat: orders can now be fully processed in the application. database dump updated with data samples for testing.)
=======
INSERT INTO `user` VALUES (1,'a@a','a','a','a','a','a','a','client'),(2,'b@b','b','b','b','b','b','b','client'),(3,'pepe@gmail.com','Pepe','Sech','pepe','1209301923','46464646','Rivadavia 1121','client'),(4,'santifilippini2005@gmail.com','Santiago','Filippini','123','3464440885','20202020','Rivadavia 1010','client');
>>>>>>> 3fce8c4 (fix: solved a bug where logging out didn't delete the user from the session.)
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
  `total_amount` float DEFAULT '0',
  `discount_id` int DEFAULT NULL,
  `user_id` int NOT NULL,
  `restaurant_id` int NOT NULL,
  PRIMARY KEY (`order_id`),
<<<<<<< HEAD
  KEY `discount_fk_idx` (`discount_id`),
  KEY `restaurant_fk_idx` (`restaurant_id`),
  CONSTRAINT `discount_fk` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`discount_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `restaurant_id_fk` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
=======
  KEY `restaurant_fk_idx` (`restaurant_id`),
  KEY `user_fk_idx` (`user_id`),
  KEY `discount_fk_idx` (`discount_id`),
  CONSTRAINT `discount_fk` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`discount_id`),
  CONSTRAINT `restaurant_id_fk` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
<<<<<<< HEAD
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
>>>>>>> 8ea60ac (feat: orders can now be fully processed in the application. database dump updated with data samples for testing.)
=======
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
>>>>>>> 3fce8c4 (fix: solved a bug where logging out didn't delete the user from the session.)
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order`
--

LOCK TABLES `user_order` WRITE;
/*!40000 ALTER TABLE `user_order` DISABLE KEYS */;
INSERT INTO `user_order` VALUES (1,'2026-09-06 00:00:00',40370.4,1,3,1),(2,'2026-09-06 00:00:00',3870.96,NULL,1,1),(3,'2026-09-06 00:00:00',137801,3,3,1),(4,'2026-09-06 00:00:00',78683.7,2,3,1),(5,'2026-09-06 00:00:00',33136,1,4,1),(6,'2026-09-07 00:00:00',60573.6,2,4,1),(7,'2026-09-07 00:00:00',70284.9,2,3,1),(8,'2026-09-07 00:00:00',39466,1,3,1),(9,'2026-09-08 00:00:00',119685,3,4,1),(10,'2026-09-08 00:00:00',56555,2,4,1),(11,'2026-09-14 00:00:00',66391.7,2,4,1),(12,'2026-09-14 00:00:00',41911.4,1,3,1),(13,'2026-09-14 00:00:00',28351.3,NULL,3,1),(14,'2026-09-15 00:00:00',93578.6,2,4,1);
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

<<<<<<< HEAD
<<<<<<< HEAD
-- Dump completed on 2026-09-03 23:30:39
=======
-- Dump completed on 2026-09-06  0:32:40
>>>>>>> 8ea60ac (feat: orders can now be fully processed in the application. database dump updated with data samples for testing.)
=======
-- Dump completed on 2026-09-15 21:59:20
>>>>>>> 3fce8c4 (fix: solved a bug where logging out didn't delete the user from the session.)
