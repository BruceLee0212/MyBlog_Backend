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
-- Table structure for table `tb_blog_tag`
--

DROP TABLE IF EXISTS `tb_blog_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_blog_tag` (
  `tag_id` int NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(100) NOT NULL,
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tag_owner` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`tag_id`),
  KEY `tag_owner` (`tag_owner`),
  CONSTRAINT `tb_blog_tag_ibfk_1` FOREIGN KEY (`tag_owner`) REFERENCES `tb_admin` (`email`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=97556238 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_blog_tag`
--

LOCK TABLES `tb_blog_tag` WRITE;
/*!40000 ALTER TABLE `tb_blog_tag` DISABLE KEYS */;
INSERT INTO `tb_blog_tag` VALUES (2343495,'axios',0,'2023-09-10 22:20:10','bli725@uwo.ca'),(5487627,'tag3',1,'2023-09-16 22:59:31','bli725@uwo.ca'),(15446476,'Framework',0,'2023-09-10 17:29:02','bli725@uwo.ca'),(15454176,'UI design',0,'2023-09-10 17:29:02','bli725@uwo.ca'),(15811503,'Cloud Service',0,'2023-09-10 19:37:42','bli725@uwo.ca'),(18003321,'useEffect',0,'2023-09-11 11:46:22','bli725@uwo.ca'),(19932279,'JDBC',0,'2023-09-10 21:55:02','bli725@uwo.ca'),(19933221,'Java',0,'2023-09-10 21:55:02','bli725@uwo.ca'),(25801388,'Console',0,'2023-09-10 18:28:43','bli725@uwo.ca'),(29913673,'Markdown',0,'2023-09-09 16:36:02','bli725@uwo.ca'),(31509827,'text editor',0,'2023-09-09 16:48:23','bli725@uwo.ca'),(37979274,'tag2',1,'2023-09-16 22:41:03','bli725@uwo.ca'),(44543952,'useMemo',0,'2023-09-11 12:02:14','bli725@uwo.ca'),(60746742,'test',1,'2023-09-20 01:11:05','bli725@uwo.ca'),(61000359,'maildev',0,'2023-09-09 16:18:36','bli725@uwo.ca'),(78564925,'Spring Boot',0,'2023-09-09 16:16:57','bli725@uwo.ca'),(79907811,'MySql',0,'2023-09-10 17:55:58','bli725@uwo.ca'),(79921111,'MyBatis',0,'2023-09-10 17:55:58','bli725@uwo.ca'),(84260812,'Amazon S3',0,'2023-09-09 16:18:18','bli725@uwo.ca'),(96520700,'tag1',1,'2023-09-16 22:39:49','bli725@uwo.ca'),(97556237,'React.js',0,'2023-09-09 16:17:09','bli725@uwo.ca');
/*!40000 ALTER TABLE `tb_blog_tag` ENABLE KEYS */;
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

-- Dump completed on 2023-10-13 21:04:55
