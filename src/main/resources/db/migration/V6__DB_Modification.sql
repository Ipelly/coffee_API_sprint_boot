ALTER TABLE `xipli`.`ItemOption`
CHANGE COLUMN `itemOptionID` `ItemOptionID` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `xipli`.`Addon`
CHANGE COLUMN `AddonID` `AddonID` INT(11) NOT NULL AUTO_INCREMENT ;

INSERT INTO `xipli`.`Ingredient` (`Name`, `Price`) VALUES ('Coclate', '10');

INSERT INTO `xipli`.`Item` (`Name`, `Description`, `ShopID`, `Price`) VALUES ('Latte Coffee', 'Latte Coffee', '1', '20');

INSERT INTO `xipli`.`ItemAddon` (`ItemID`, `AddonID`) VALUES ('1', '1');



ALTER TABLE `xipli`.`Shop`
CHANGE COLUMN `ShopID` `shopID` INT(11) NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `Name` `name` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `Address1` `address1` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `Address2` `address2` VARCHAR(45) NULL DEFAULT NULL ,
CHANGE COLUMN `City` `city` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `State` `state` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `Zip` `zip` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `Phone` `phone` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `Latitude` `latitude` DECIMAL(20,4) NULL DEFAULT NULL ,
CHANGE COLUMN `Longitute` `longitute` DECIMAL(20,4) NULL DEFAULT NULL ,
CHANGE COLUMN `Rating` `rating` INT(20) NULL DEFAULT NULL ,
CHANGE COLUMN `Active` `status` TINYINT(1) NOT NULL DEFAULT '1' ;

