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
-- Table structure for table `tb_link`
--

DROP TABLE IF EXISTS `tb_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_link` (
  `link_id` int NOT NULL,
  `link_owner` varchar(32) NOT NULL,
  `link_type` tinyint NOT NULL,
  `link_name` varchar(30) NOT NULL,
  `link_url` varchar(50) NOT NULL,
  `link_description` varchar(100) DEFAULT NULL,
  `link_rank` int NOT NULL,
  `is_deleted` tinyint NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`link_id`),
  KEY `link_owner` (`link_owner`),
  CONSTRAINT `tb_link_ibfk_1` FOREIGN KEY (`link_owner`) REFERENCES `tb_admin` (`email`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_link`
--

LOCK TABLES `tb_link` WRITE;
/*!40000 ALTER TABLE `tb_link` DISABLE KEYS */;
INSERT INTO `tb_link` VALUES (19008772,'bli725@uwo.ca',1,'maildev','https://github.com/maildev/maildev','Useful tool to assist developers in conducting email testing.',2,0,'2023-09-09 16:11:21'),(36649932,'bli725@uwo.ca',2,'myBlog Frontend Repository','https://github.com/BruceLee0212/My_Blog_frontend','Frontend implemented with React.js',2,0,'2023-09-09 14:59:33'),(39321949,'bli725@uwo.ca',1,'Material UI','https://mui.com/','Streamlined user interface components for web development.',1,0,'2023-09-09 16:16:03'),(59891127,'bli725@uwo.ca',2,'myBlog Backend Repository','https://github.com/BruceLee0212/My_Blog_backend','Backend implemented with Spring Boot',1,0,'2023-09-09 14:41:55'),(80726333,'bli725@uwo.ca',0,'1','1','1',1,1,'2023-09-20 01:46:47');
/*!40000 ALTER TABLE `tb_link` ENABLE KEYS */;
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

-- Dump completed on 2023-10-13 21:05:03
