ALTER TABLE `xipli`.`ItemOption`
CHANGE COLUMN `itemOptionID` `ItemOptionID` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `xipli`.`Addon`
CHANGE COLUMN `AddonID` `AddonID` INT(11) NOT NULL AUTO_INCREMENT ;

INSERT INTO `xipli`.`Ingredient` (`Name`, `Price`) VALUES ('Coclate', '10');

INSERT INTO `xipli`.`Item` (`Name`, `Description`, `ShopID`, `Price`) VALUES ('Latte Coffee', 'Latte Coffee', '1', '20');

INSERT INTO `xipli`.`ItemAddon` (`ItemID`, `AddonID`) VALUES ('1', '1');
