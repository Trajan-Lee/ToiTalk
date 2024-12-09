CREATE DATABASE  IF NOT EXISTS `toitalk` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `toitalk`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: toitalk
-- ------------------------------------------------------
-- Server version	8.0.40

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

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `booking_id` int NOT NULL AUTO_INCREMENT,
  `tutor_id` int NOT NULL,
  `student_id` int NOT NULL,
  `date` datetime DEFAULT NULL,
  `status` enum('scheduled','completed','canceled') NOT NULL DEFAULT 'scheduled',
  PRIMARY KEY (`booking_id`),
  KEY `tutor_id` (`tutor_id`),
  KEY `fk_bookings_students1_idx` (`student_id`),
  CONSTRAINT `fk_bookings_students1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
  CONSTRAINT `sessions_ibfk_1` FOREIGN KEY (`tutor_id`) REFERENCES `tutors` (`tutor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (1,4,5,'2024-12-01 10:00:00','completed'),(2,5,6,'2024-12-02 11:00:00','scheduled'),(3,4,6,'2024-12-03 12:00:00','canceled'),(4,6,5,'2024-12-04 13:00:00','completed'),(5,5,7,'2024-12-05 14:00:00','scheduled'),(6,6,6,'2024-12-06 15:00:00','completed'),(7,4,7,'2024-12-07 16:00:00','canceled'),(8,5,5,'2024-12-08 17:00:00','completed'),(9,6,7,'2024-12-09 18:00:00','scheduled'),(10,4,6,'2024-12-10 19:00:00','completed'),(12,4,5,'2024-12-05 00:30:00','completed'),(13,4,5,'2024-12-02 07:30:00','scheduled'),(14,4,5,'2024-12-02 01:30:00','scheduled'),(15,4,5,'2024-12-03 01:30:00','scheduled'),(16,4,5,'2024-12-04 01:30:00','scheduled');
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `feedback_id` int NOT NULL AUTO_INCREMENT,
  `booking_id` int NOT NULL,
  `rating` int NOT NULL,
  `comment` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `tutor_id` int NOT NULL,
  PRIMARY KEY (`feedback_id`),
  KEY `session_id` (`booking_id`),
  KEY `fk_feedback_tutors1_idx` (`tutor_id`),
  CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`),
  CONSTRAINT `fk_feedback_tutors1` FOREIGN KEY (`tutor_id`) REFERENCES `tutors` (`tutor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES (1,1,5,'Great session!','2024-12-06 19:38:02',4),(2,4,4,'Very helpful.','2024-12-06 19:38:02',6),(3,6,3,'It was okay.','2024-12-06 19:38:02',6),(4,8,4,'Not as Excellent tutor!','2024-12-06 19:38:02',5),(5,10,2,'Not satisfied.','2024-12-06 19:38:02',4);
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `languages`
--

DROP TABLE IF EXISTS `languages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `languages` (
  `language_id` int NOT NULL AUTO_INCREMENT,
  `language_name` varchar(50) NOT NULL,
  PRIMARY KEY (`language_id`),
  UNIQUE KEY `language_name` (`language_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `languages`
--

LOCK TABLES `languages` WRITE;
/*!40000 ALTER TABLE `languages` DISABLE KEYS */;
INSERT INTO `languages` VALUES (1,'French'),(3,'German'),(2,'Spanish');
/*!40000 ALTER TABLE `languages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `student_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  PRIMARY KEY (`student_id`,`user_id`),
  KEY `fk_students_users1_idx` (`user_id`),
  CONSTRAINT `fk_students_users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (5,8),(6,9),(7,10);
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time_slots`
--

DROP TABLE IF EXISTS `time_slots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `time_slots` (
  `slot_id` int NOT NULL AUTO_INCREMENT,
  `day` enum('Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday') NOT NULL,
  `hour` int NOT NULL,
  PRIMARY KEY (`slot_id`)
) ENGINE=InnoDB AUTO_INCREMENT=169 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_slots`
--

LOCK TABLES `time_slots` WRITE;
/*!40000 ALTER TABLE `time_slots` DISABLE KEYS */;
INSERT INTO `time_slots` VALUES (1,'Monday',0),(2,'Monday',1),(3,'Monday',2),(4,'Monday',3),(5,'Monday',4),(6,'Monday',5),(7,'Monday',6),(8,'Monday',7),(9,'Monday',8),(10,'Monday',9),(11,'Monday',10),(12,'Monday',11),(13,'Monday',12),(14,'Monday',13),(15,'Monday',14),(16,'Monday',15),(17,'Monday',16),(18,'Monday',17),(19,'Monday',18),(20,'Monday',19),(21,'Monday',20),(22,'Monday',21),(23,'Monday',22),(24,'Monday',23),(25,'Tuesday',0),(26,'Tuesday',1),(27,'Tuesday',2),(28,'Tuesday',3),(29,'Tuesday',4),(30,'Tuesday',5),(31,'Tuesday',6),(32,'Tuesday',7),(33,'Tuesday',8),(34,'Tuesday',9),(35,'Tuesday',10),(36,'Tuesday',11),(37,'Tuesday',12),(38,'Tuesday',13),(39,'Tuesday',14),(40,'Tuesday',15),(41,'Tuesday',16),(42,'Tuesday',17),(43,'Tuesday',18),(44,'Tuesday',19),(45,'Tuesday',20),(46,'Tuesday',21),(47,'Tuesday',22),(48,'Tuesday',23),(49,'Wednesday',0),(50,'Wednesday',1),(51,'Wednesday',2),(52,'Wednesday',3),(53,'Wednesday',4),(54,'Wednesday',5),(55,'Wednesday',6),(56,'Wednesday',7),(57,'Wednesday',8),(58,'Wednesday',9),(59,'Wednesday',10),(60,'Wednesday',11),(61,'Wednesday',12),(62,'Wednesday',13),(63,'Wednesday',14),(64,'Wednesday',15),(65,'Wednesday',16),(66,'Wednesday',17),(67,'Wednesday',18),(68,'Wednesday',19),(69,'Wednesday',20),(70,'Wednesday',21),(71,'Wednesday',22),(72,'Wednesday',23),(73,'Thursday',0),(74,'Thursday',1),(75,'Thursday',2),(76,'Thursday',3),(77,'Thursday',4),(78,'Thursday',5),(79,'Thursday',6),(80,'Thursday',7),(81,'Thursday',8),(82,'Thursday',9),(83,'Thursday',10),(84,'Thursday',11),(85,'Thursday',12),(86,'Thursday',13),(87,'Thursday',14),(88,'Thursday',15),(89,'Thursday',16),(90,'Thursday',17),(91,'Thursday',18),(92,'Thursday',19),(93,'Thursday',20),(94,'Thursday',21),(95,'Thursday',22),(96,'Thursday',23),(97,'Friday',0),(98,'Friday',1),(99,'Friday',2),(100,'Friday',3),(101,'Friday',4),(102,'Friday',5),(103,'Friday',6),(104,'Friday',7),(105,'Friday',8),(106,'Friday',9),(107,'Friday',10),(108,'Friday',11),(109,'Friday',12),(110,'Friday',13),(111,'Friday',14),(112,'Friday',15),(113,'Friday',16),(114,'Friday',17),(115,'Friday',18),(116,'Friday',19),(117,'Friday',20),(118,'Friday',21),(119,'Friday',22),(120,'Friday',23),(121,'Saturday',0),(122,'Saturday',1),(123,'Saturday',2),(124,'Saturday',3),(125,'Saturday',4),(126,'Saturday',5),(127,'Saturday',6),(128,'Saturday',7),(129,'Saturday',8),(130,'Saturday',9),(131,'Saturday',10),(132,'Saturday',11),(133,'Saturday',12),(134,'Saturday',13),(135,'Saturday',14),(136,'Saturday',15),(137,'Saturday',16),(138,'Saturday',17),(139,'Saturday',18),(140,'Saturday',19),(141,'Saturday',20),(142,'Saturday',21),(143,'Saturday',22),(144,'Saturday',23),(145,'Sunday',0),(146,'Sunday',1),(147,'Sunday',2),(148,'Sunday',3),(149,'Sunday',4),(150,'Sunday',5),(151,'Sunday',6),(152,'Sunday',7),(153,'Sunday',8),(154,'Sunday',9),(155,'Sunday',10),(156,'Sunday',11),(157,'Sunday',12),(158,'Sunday',13),(159,'Sunday',14),(160,'Sunday',15),(161,'Sunday',16),(162,'Sunday',17),(163,'Sunday',18),(164,'Sunday',19),(165,'Sunday',20),(166,'Sunday',21),(167,'Sunday',22),(168,'Sunday',23);
/*!40000 ALTER TABLE `time_slots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tutor_languages`
--

DROP TABLE IF EXISTS `tutor_languages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tutor_languages` (
  `tutor_id` int NOT NULL,
  `language_id` int NOT NULL,
  PRIMARY KEY (`tutor_id`,`language_id`),
  KEY `language_id` (`language_id`),
  CONSTRAINT `tutor_languages_ibfk_1` FOREIGN KEY (`tutor_id`) REFERENCES `tutors` (`tutor_id`),
  CONSTRAINT `tutor_languages_ibfk_2` FOREIGN KEY (`language_id`) REFERENCES `languages` (`language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutor_languages`
--

LOCK TABLES `tutor_languages` WRITE;
/*!40000 ALTER TABLE `tutor_languages` DISABLE KEYS */;
INSERT INTO `tutor_languages` VALUES (4,1),(5,1),(10,1),(4,2),(5,2),(6,2),(4,3),(6,3);
/*!40000 ALTER TABLE `tutor_languages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tutor_schedule`
--

DROP TABLE IF EXISTS `tutor_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tutor_schedule` (
  `tutor_schedule_id` int NOT NULL AUTO_INCREMENT,
  `slot_id` int NOT NULL,
  `tutor_id` int NOT NULL,
  PRIMARY KEY (`tutor_schedule_id`),
  KEY `fk_tutor_schedule_time_slots1_idx` (`slot_id`),
  KEY `fk_tutor_schedule_tutors1_idx` (`tutor_id`),
  CONSTRAINT `fk_tutor_schedule_time_slots1` FOREIGN KEY (`slot_id`) REFERENCES `time_slots` (`slot_id`),
  CONSTRAINT `fk_tutor_schedule_tutors1` FOREIGN KEY (`tutor_id`) REFERENCES `tutors` (`tutor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutor_schedule`
--

LOCK TABLES `tutor_schedule` WRITE;
/*!40000 ALTER TABLE `tutor_schedule` DISABLE KEYS */;
INSERT INTO `tutor_schedule` VALUES (132,1,4),(133,2,4),(134,3,4),(135,4,4),(136,5,4),(137,6,4),(138,7,4),(139,8,4),(140,25,4),(141,26,4),(142,27,4),(143,28,4),(144,29,4),(145,30,4),(146,31,4),(147,32,4),(148,49,4),(149,50,4),(150,51,4),(151,73,4),(152,74,4),(153,75,4),(154,145,4);
/*!40000 ALTER TABLE `tutor_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tutors`
--

DROP TABLE IF EXISTS `tutors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tutors` (
  `tutor_id` int NOT NULL AUTO_INCREMENT,
  `years` int DEFAULT NULL,
  `bio` varchar(500) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`tutor_id`,`user_id`),
  KEY `fk_tutors_users_idx` (`user_id`),
  CONSTRAINT `fk_tutors_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutors`
--

LOCK TABLES `tutors` WRITE;
/*!40000 ALTER TABLE `tutors` DISABLE KEYS */;
INSERT INTO `tutors` VALUES (4,5,'I am a tutor named John',3.5,20),(5,3,'I am a tutor named Alice',4,21),(6,2,'I am a tutor named Bob',4.5,22),(10,5,'this is a bio',5,26);
/*!40000 ALTER TABLE `tutors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(60) NOT NULL,
  `user_type` enum('student','tutor') NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (8,'jane_student','jane@example.com','$2a$10$7xrxDf3XC6IJ8OwcCYjAR.LgPBy4bY2ckw8cuJEZ/UaQgC1.Zr6dG','student','2024-11-28 18:38:29'),(9,'jane2_student','jane2@example.com','$2a$10$.eoHtxS/LI0EGSa8f3P8CeRWgS/NePgw0kSP5QkZkV8lBb47U/Opm','student','2024-11-28 18:38:56'),(10,'jane3_student','jane3@example.com','$2a$10$RE0YqJqF60cKXB3aY0xc5O5oLLDirpRLCgessseOPCjeLqrdYWDMm','student','2024-11-28 18:39:11'),(20,'john_tutor','john@example.com','$2a$10$GdfosPoomXif80Cz3UMDUuQMkViWQIyvawU0H7AzytWEGCzKxsO6S','tutor','2024-12-06 19:13:43'),(21,'alice_tutor','alice@example.com','$2a$10$DeaYQczpXubWQrrqee8Nx.xFSUMiAMO9nyicWNal/P9A3WiLIW2YG','tutor','2024-12-06 19:13:43'),(22,'bob_tutor','bob@example.com','$2a$10$J9kcNIcHoPo7R.ezAVjc0e2y5mnQTSiSK0seqMncw305uPSDHPBRC','tutor','2024-12-06 19:13:43'),(26,'jane4_tutor','jane4@example.com','$2a$10$.oqh7/94i6cn.u70iaYKh.SWHszKDv8kamhbPtba34QKiTRZI4mEm','tutor','2024-12-07 21:16:21');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-07 16:35:17
