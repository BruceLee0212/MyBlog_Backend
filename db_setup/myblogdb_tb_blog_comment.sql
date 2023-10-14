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
-- Table structure for table `tb_blog_comment`
--

DROP TABLE IF EXISTS `tb_blog_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_blog_comment` (
  `comment_id` bigint NOT NULL AUTO_INCREMENT,
  `comment_owner` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `comment_blog_id` bigint NOT NULL DEFAULT '0',
  `commentator` varchar(50) NOT NULL DEFAULT '',
  `email` varchar(32) DEFAULT '',
  `comment_body` varchar(200) NOT NULL DEFAULT '',
  `comment_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `comment_status` tinyint NOT NULL DEFAULT '0',
  `reply_body` varchar(200) DEFAULT NULL,
  `reply_create_time` datetime DEFAULT NULL,
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`comment_id`),
  KEY `comment_blog_id` (`comment_blog_id`),
  KEY `tb_blog_comment_ibfk_1_idx` (`comment_owner`),
  CONSTRAINT `tb_blog_comment_ibfk_1` FOREIGN KEY (`comment_blog_id`) REFERENCES `tb_blog` (`blog_id`) ON DELETE CASCADE,
  CONSTRAINT `tb_blog_comment_ibfk_2` FOREIGN KEY (`comment_owner`) REFERENCES `tb_admin` (`email`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=87441877 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_blog_comment`
--

LOCK TABLES `tb_blog_comment` WRITE;
/*!40000 ALTER TABLE `tb_blog_comment` DISABLE KEYS */;
INSERT INTO `tb_blog_comment` VALUES (3792248,'bli725@uwo.ca',75452900,'Sophia Martinez','234567@test.com','hello','2023-09-18 09:13:00',1,'hi','2023-09-18 09:13:22',0),(5195300,'bli725@uwo.ca',28320450,'test','','comment test','2023-09-15 23:24:30',1,'aaa','2023-09-16 02:11:47',0),(20553296,'bli725@uwo.ca',28320450,'ttaa','123@test.com','sghrd6jh','2023-09-15 23:27:42',1,NULL,NULL,0),(30997418,'bli725@uwo.ca',13018210,'Sophia Martinez','234567@test.com','test test test','2023-09-18 09:11:43',1,'hi','2023-09-18 09:11:58',0),(32049568,'bli725@uwo.ca',28320450,'Sophia Martinez','234567@test.com','good','2023-09-18 09:14:13',1,'thank you','2023-09-18 09:14:26',0),(35199222,'bli725@uwo.ca',28320450,'ttaa','123@test.com','asd','2023-09-15 23:26:03',1,NULL,NULL,0),(39045822,'bli725@uwo.ca',41373132,'Sophia Martinez','234567@test.com','hello','2023-09-18 09:12:23',1,'hi','2023-09-18 09:13:30',0),(57901212,'bli725@uwo.ca',8904667,'Michael Johnson','123456@test.com','comment2 comment2 comment2','2023-09-17 17:39:16',1,'reply2','2023-09-17 17:40:22',0),(61067417,'bli725@uwo.ca',8904667,'Michael Johnson','123456@test.com','comment test comment test comment test','2023-09-17 17:31:16',1,'reply test reply test reply test','2023-09-17 17:32:02',0),(87441876,'bli725@uwo.ca',28320450,'ttaa','123@test.com','sghrd6jh','2023-09-15 23:26:58',0,NULL,NULL,1);
/*!40000 ALTER TABLE `tb_blog_comment` ENABLE KEYS */;
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

-- Dump completed on 2023-10-13 21:04:59
