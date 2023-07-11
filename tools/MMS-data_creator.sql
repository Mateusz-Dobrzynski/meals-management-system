CREATE DATABASE IF NOT EXISTS `mms_database`;

USE `mms_database`;

CREATE TABLE `users` (
  `email` varchar(50) NOT NULL,
  `password` varchar(30) NOT NULL,
  `userId` INT AUTO_INCREMENT NOT NULL,
  PRIMARY KEY (`userId`)
);

CREATE TABLE `fridges` (
    `fridgeId` INT AUTO_INCREMENT NOT NULL,
    `userId` INT NOT NULL,
    PRIMARY KEY (`fridgeId`),
    FOREIGN KEY (userId) REFERENCES users(userId)
);

CREATE TABLE `MeasurmentUnits` (
  `measurmentUnitId` INT AUTO_INCREMENT NOT NULL,
  `measurmentUnitName` varchar(50) NOT NULL,
  PRIMARY KEY (`measurmentUnitId`)
);

CREATE TABLE `ProductType` (
  `productTypeId` INT AUTO_INCREMENT NOT NULL,
  `productTypeName` varchar(50) NOT NULL,
  `measurmentUnitId` INT,
  PRIMARY KEY (`productTypeId`)
  FOREIGN KEY (measurmentUnitId) REFERENCES MeasurmentUnits(measurmentUnitId)
);

CREATE TABLE `ProductInstance`(
    `instanceId` INT AUTO_INCREMENT NOT NULL,
    `productName` varchar(100) NOT NULL,
    `fridgeId` INT NOT NULL,
    `productTypeId` INT,
    `amount` INT,
    `exporationDate` date,
    `entryDate` date NOT NULL,
    `barcode` varchar(13),
    PRIMARY KEY (`instanceId`),
    FOREIGN KEY (fridgeId) REFERENCES fridges(fridgeId),
    FOREIGN KEY (productTypeId) REFERENCES ProductType(productTypeId)
);