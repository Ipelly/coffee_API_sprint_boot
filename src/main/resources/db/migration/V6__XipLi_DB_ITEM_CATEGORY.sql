
CREATE TABLE xipli.category (
  category_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NULL,
  shop_id int(11) UNSIGNED NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (category_id)
);

CREATE TABLE xipli.item_category (
  item_id INT(11) NOT NULL,
  category_id INT(11) NOT NULL,
  PRIMARY KEY (item_id, category_id)
);
