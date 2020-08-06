DROP TABLE IF EXISTS `Article`;
CREATE TABLE `Article` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Status` int(2) NOT NULL,
  `Author` varchar(20) COLLATE utf8_bin NOT NULL,
  `CreateTime` bigint(12) NOT NULL,
  `UpdateTime` bigint(12) NOT NULL,
  `Title` varchar(30) COLLATE utf8_bin NOT NULL,
  `Content` text COLLATE utf8_bin NOT NULL,
  `Markdown` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DROP TABLE IF EXISTS `ArticleCategory`;
CREATE TABLE `ArticleCategory` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ArticleId` int(11) NOT NULL,
  `CategoryId` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `ArticleTag`;
CREATE TABLE `ArticleTag` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ArticleId` int(11) NOT NULL,
  `Tag` varchar(10) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `Category`;
CREATE TABLE `Category` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `CategoryId` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `CategoryName` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `CreateTime` bigint(20) NOT NULL,
  `UpdateTime` bigint(20) NOT NULL,
  `Status` int(11) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

