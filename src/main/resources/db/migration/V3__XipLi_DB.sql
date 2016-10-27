
CREATE TABLE xipli.user (
  user_id int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  first_name varchar(255) NOT NULL,
  last_name varchar(255) DEFAULT NULL,
  email_address varchar(255) DEFAULT NULL,
  phone varchar(20) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  password_salt varchar(45) DEFAULT NULL,
  provider_type varchar(45) DEFAULT NULL,
  provider_user_id varchar(45) DEFAULT NULL,
  status tinyint(3) UNSIGNED NULL DEFAULT '1',
  PRIMARY KEY (user_id)
);


CREATE TABLE xipli.Addon (
  AddonID int(11) NOT NULL,
  Name varchar(255) NOT NULL,
  Price decimal(20,2) NOT NULL,
  ShopID int(11) NOT NULL,
  Active TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (AddonID)
);


CREATE TABLE xipli.Ingredient (
  IngredientID int(11) NOT NULL AUTO_INCREMENT,
  Name varchar(255) NOT NULL,
  Price decimal(20,2) NOT NULL,
  Active TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (IngredientID,Name)
) ;


CREATE TABLE xipli.Item (
  ItemID int(11) NOT NULL AUTO_INCREMENT,
  Name varchar(255) NOT NULL,
  Description varchar(255) DEFAULT NULL,
  ShopID int(11) NOT NULL,
  Price decimal(20,2) DEFAULT NULL,
  Active TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (ItemID)
) ;

CREATE TABLE xipli.ItemAddon (
  ItemID int(11) NOT NULL,
  AddonID int(11) NOT NULL,
  Active TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (ItemID,AddonID)
) ;

CREATE TABLE xipli.ItemIngredient (
  ItemID int(11) NOT NULL,
  IngredientID int(11) NOT NULL,
  Active TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (ItemID,IngredientID)
) ;

CREATE TABLE xipli.ItemOption (
  itemOptionID int(11) NOT NULL AUTO_INCREMENT,
  ItemOption varchar(45) NOT NULL,
  Price decimal(20,2) NOT NULL,
  ItemID int(11) NOT NULL,
  Active TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (itemOptionID)
);

CREATE TABLE xipli.XipLiOrder (
  XipLiOrderID int(11) NOT NULL AUTO_INCREMENT,
  XipliXipliUserID int(11) NOT NULL,
  ShopID int(11) NOT NULL,
  TotalPrice decimal(20,2) NOT NULL,
  Status varchar(45) NOT NULL,
  OrderTime datetime NOT NULL,
  AcknowledgeTime datetime DEFAULT NULL,
  CompleteTIme datetime DEFAULT NULL,
  PickupTIme datetime DEFAULT NULL,
  OrderBarCode datetime DEFAULT NULL,
  Active TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (XipLiOrderID)
) ;
CREATE TABLE xipli.OrderAddon (
  OrderLineID int(11) NOT NULL,
  AddOnID varchar(45) NOT NULL,
  Active TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (OrderLineID,AddOnID)
) ;
CREATE TABLE xipli.OrderLine (
  OrderLineID int(11) NOT NULL AUTO_INCREMENT,
  OrderID int(11) NOT NULL,
  ItemID int(11) NOT NULL,
  OptionID int(11) NOT NULL,
  Price decimal(20,2) NOT NULL,
  Status varchar(45) NOT NULL,
  StartTime datetime DEFAULT NULL,
  EndTime datetime DEFAULT NULL,
  Active TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (OrderLineID)
) ;
CREATE TABLE xipli.Shop (
  ShopID int(11) NOT NULL AUTO_INCREMENT,
  Name varchar(45) NOT NULL,
  Address1 varchar(45) NOT NULL,
  Address2 varchar(45) DEFAULT NULL,
  City varchar(45) NOT NULL,
  State varchar(45) NOT NULL,
  Zip varchar(45) NOT NULL,
  Phone varchar(45) NOT NULL,
  latitude decimal(20,4) NULL,
  Longitute decimal(20,4)  NULL,
  Rating int(20)  NULL,
  Active TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (ShopID)
) ;

CREATE TABLE xipli.UserRole (
  UserRoleID int(11) NOT NULL AUTO_INCREMENT,
  XipliUserID int(11) NOT NULL,
  Role varchar(45) NOT NULL,
  Active TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (UserRoleID)
);

CREATE TABLE xipli.ShopUser (
  XipliUserID int(11) NOT NULL,
  ShopID int(11) NOT NULL,
  Active TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (XipliUserID,ShopID)
);

ALTER TABLE `xipli`.`XipLiOrder`
CHANGE COLUMN `XipLiOrderID` `OrderID` INT(11) NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `XipliXipliUserID` `UserID` INT(11) NOT NULL ,
CHANGE COLUMN `OrderBarCode` `OrderBarCode` VARCHAR(255) NULL DEFAULT NULL;

ALTER TABLE `xipli`.`UserRole`
DROP COLUMN `UserRoleID`,
CHANGE COLUMN `XipliUserID` `UserID` INT(11) NOT NULL ,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`UserID`, `Role`);

ALTER TABLE `xipli`.`ShopUser`
CHANGE COLUMN `XipliUserID` `UserID` INT(11) NOT NULL ;


ALTER TABLE `xipli`.`Shop`
CHANGE COLUMN `latitude` `Latitude` DECIMAL(20,4) NULL DEFAULT NULL ;

ALTER TABLE `xipli`.`ItemOption`
CHANGE COLUMN `itemOptionID` `ItemOptionID` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `xipli`.`ItemOption`
CHANGE COLUMN `itemOptionID` `ItemOptionID` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `xipli`.`Addon`
CHANGE COLUMN `AddonID` `AddonID` INT(11) NOT NULL AUTO_INCREMENT ;


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
