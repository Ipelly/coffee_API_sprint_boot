
CREATE TABLE xipli.item (
  item_id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  description varchar(255) DEFAULT NULL,
  shop_id int(11) NOT NULL,
  price decimal(20,2) DEFAULT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (item_id)
);


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


CREATE TABLE xipli.item_addon (
  item_addon_id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(45) NOT NULL,
  price decimal(20,2) NOT NULL,
  item_id int(11) NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (item_addon_id),
  UNIQUE KEY (item_id, name)
);


CREATE TABLE xipli.item_option (
  item_option_id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(45) NOT NULL,
  price decimal(20,2) NOT NULL,
  item_id int(11) NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (item_option_id),
  UNIQUE KEY (item_id, name)
);


CREATE TABLE xipli.shop (
  shop_id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  address1 varchar(255) NOT NULL,
  address2 varchar(255) DEFAULT NULL,
  city varchar(45) NOT NULL,
  state varchar(45) NOT NULL,
  zip varchar(10) NOT NULL,
  phone varchar(20) NOT NULL,
  latitude decimal(20,4) DEFAULT NULL,
  longitude decimal(20,4) DEFAULT NULL,
  rating tinyint(3) UNSIGNED DEFAULT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (shop_id)
);


CREATE TABLE xipli.user_role (
  user_id int(11) NOT NULL AUTO_INCREMENT,
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_id,role)
);
