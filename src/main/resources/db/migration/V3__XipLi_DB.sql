-- Create database xipli if not exists;
CREATE DATABASE IF NOT EXISTS xipli;

CREATE TABLE `xipli.Addon` (
  `AddonID` int(11) NOT NULL,
  `Name` varchar(500) NOT NULL,
  `Price` decimal(20,2) NOT NULL,
  `ShopID` int(11) NOT NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`AddonID`)
);



CREATE TABLE `xipli.Ingredient` (
  `IngredientID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(500) NOT NULL,
  `Price` decimal(20,2) NOT NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`IngredientID`,`Name`)
) ;
CREATE TABLE `xipli.Item` (
  `ItemID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(500) NOT NULL,
  `Description` varchar(500) DEFAULT NULL,
  `ShopID` int(11) NOT NULL,
  `Price` decimal(20,2) DEFAULT NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`ItemID`)
) ;

CREATE TABLE `xipli.ItemAddon` (
  `ItemID` int(11) NOT NULL,
  `AddonID` int(11) NOT NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`ItemID`,`AddonID`)
) ;

CREATE TABLE `xipli.ItemIngredient` (
  `ItemID` int(11) NOT NULL,
  `IngredientID` int(11) NOT NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`ItemID`,`IngredientID`)
) ;

CREATE TABLE `xipli.ItemOption` (
  `itemOptionID` int(11) NOT NULL AUTO_INCREMENT,
  `ItemOption` varchar(45) NOT NULL,
  `Price` decimal(20,2) NOT NULL,
  `ItemID` int(11) NOT NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`itemOptionID`)
);

CREATE TABLE `xipli.Order` (
  `OrderID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `ShopID` int(11) NOT NULL,
  `TotalPrice` decimal(20,2) NOT NULL,
  `Status` varchar(45) NOT NULL,
  `OrderTime` datetime NOT NULL,
  `AcknowledgeTime` datetime DEFAULT NULL,
  `CompleteTIme` datetime DEFAULT NULL,
  `PickupTIme` datetime DEFAULT NULL,
  `OrderBarCode` datetime DEFAULT NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`OrderID`)
) ;
CREATE TABLE `xipli.OrderAddon` (
  `OrderLineID` int(11) NOT NULL,
  `AddOnID` varchar(45) NOT NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`OrderLineID`,`AddOnID`)
) ;
CREATE TABLE `xipli.OrderLine` (
  `OrderLineID` int(11) NOT NULL AUTO_INCREMENT,
  `OrderID` int(11) NOT NULL,
  `ItemID` int(11) NOT NULL,
  `OptionID` int(11) NOT NULL,
  `Price` decimal(20,2) NOT NULL,
  `Status` varchar(45) NOT NULL,
  `StartTime` datetime DEFAULT NULL,
  `EndTime` datetime DEFAULT NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`OrderLineID`)
) ;
CREATE TABLE `xipli.Shop` (
  `ShopID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Address1` varchar(45) NOT NULL,
  `Address2` varchar(45) DEFAULT NULL,
  `City` varchar(45) NOT NULL,
  `State` varchar(45) NOT NULL,
  `Zip` varchar(45) NOT NULL,
  `Phone` varchar(45) NOT NULL,
  `latitude` decimal(20,4) NULL,
  `Longitute` decimal(20,4)  NULL,
  `Rating` int(20)  NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`ShopID`)
) ;
CREATE TABLE `xipli.ShopUser` (
  `UserID` int(11) NOT NULL,
  `ShopID` int(11) NOT NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`UserID`,`ShopID`)
) ;
CREATE TABLE `xipli.User` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(500) NOT NULL,
  `LastName` varchar(500) DEFAULT NULL,
  `EmailAddress` varchar(100) DEFAULT NULL,
  `PhoneNo` varchar(45) DEFAULT NULL,
  `Password` varchar(45) NOT NULL,
  `Provider` varchar(45) DEFAULT NULL,
  `ProviderUserID` varchar(45) DEFAULT NULL,in
  `Usercol` varchar(45) NOT NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`UserID`,`Usercol`)
) ;
CREATE TABLE `xipli.UserRole` (
  `UserRoleID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `Role` varchar(45) NOT NULL,
  `Active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`UserRoleID`)
)
