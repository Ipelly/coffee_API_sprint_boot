
CREATE TABLE xipli.category (
  category_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NULL,
  shop_id int(11) UNSIGNED NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (category_id)
);
-- Alter item table by adding category_id
ALTER TABLE xipli.item
ADD COLUMN category_id INT(11) NOT NULL;

--
--ALTER TABLE xipli.item
--DROP COLUMN category_id;

-- Create a new table for item_category
CREATE TABLE xipli.item_category (
  item_id INT(11) NOT NULL,
  category_id INT(11) NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (item_id, category_id));

  ALTER TABLE `xipli`.`item_category`
  DROP COLUMN `status`,
  DROP PRIMARY KEY;

