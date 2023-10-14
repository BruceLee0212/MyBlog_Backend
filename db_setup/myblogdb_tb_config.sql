-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: myblogdb.ceehp3ooy3ma.ca-central-1.rds.amazonaws.com    Database: myblogdb
-- ------------------------------------------------------
-- Server version	8.0.33

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

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '';

--
-- Table structure for table `tb_config`
--

DROP TABLE IF EXISTS `tb_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_config` (
  `config_id` int NOT NULL,
  `config_name` varchar(100) NOT NULL DEFAULT '',
  `config_owner` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `config_value` varchar(200) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`config_id`),
  KEY `config_account` (`config_owner`),
  CONSTRAINT `tb_config_ibfk_1` FOREIGN KEY (`config_owner`) REFERENCES `tb_admin` (`email`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_config`
--

LOCK TABLES `tb_config` WRITE;
/*!40000 ALTER TABLE `tb_config` DISABLE KEYS */;
INSERT INTO `tb_config` VALUES (11712703,'github','bli725@uwo.ca','https://github.com/BruceLee0212','2023-09-17 23:56:51'),(13192396,'websiteUrl','234567@test.com','sophiamartinez','2023-09-17 21:38:11'),(37545864,'websiteUrl','bli725@uwo.ca','brucetechhome','2023-09-09 14:15:49'),(39498291,'websiteIcon','bli725@uwo.ca','bli725@uwo.ca/website-icon/35998418','2023-09-17 00:04:24'),(50487634,'profilePic','234567@test.com','234567@test.com/profile-pics/43280012','2023-09-17 21:41:35'),(53585026,'profilePic','bli725@uwo.ca','bli725@uwo.ca/profile-pics/45768435','2023-09-09 14:30:25'),(72636659,'websiteTitle','bli725@uwo.ca','Bruce\'s Blog','2023-09-09 14:34:47'),(72639461,'websiteDescription','bli725@uwo.ca','Keep a record of newly acquired technical skills','2023-09-09 14:34:47'),(74879514,'websiteUrl','123456@test.com','michaeljohnson','2023-09-17 16:51:47'),(85826605,'footerAbout','bli725@uwo.ca','Steps to Becoming an Excellent Software Developer.','2023-09-09 14:15:49'),(86072922,'footerCopyRight','bli725@uwo.ca','Bruce Li All Rights Reserved','2023-09-09 14:15:49'),(86075045,'footerPoweredBy','bli725@uwo.ca','Spring Boot & React.js','2023-09-09 14:15:49'),(86123516,'footerPoweredByUrl','bli725@uwo.ca','https://github.com/BruceLee0212','2023-09-09 14:15:49'),(92394697,'profilePic','123456@test.com','123456@test.com/profile-pics/88713881','2023-09-17 17:29:49');
/*!40000 ALTER TABLE `tb_config` ENABLE KEYS */;
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

-- Dump completed on 2023-10-13 21:05:00
