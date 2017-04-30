
CREATE TABLE xipli.user (
  user_id int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  first_name varchar(255) NOT NULL,
  last_name varchar(255) DEFAULT NULL,
  email_address varchar(255) DEFAULT NULL,
  phone varchar(20) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  provider_type_id tinyint(3) NOT NULL DEFAULT 0,
  provider_user_id varchar(45) DEFAULT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  shop_id int(11) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (user_id)
);


CREATE TABLE xipli.provider (
  provider_type_id int(11) UNSIGNED NOT NULL,
  provider_name varchar(45) DEFAULT NULL,
  PRIMARY KEY (provider_type_id),
  UNIQUE KEY (provider_name)
);


INSERT INTO xipli.provider (provider_type_id, provider_name)
VALUES
  (1, 'FACEBOOK'),
  (2, 'GOOGLE'),
  (3, 'TWITTER');


CREATE TABLE xipli.addon (
  addon_id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  price decimal(20,2) NOT NULL,
  shop_id int(11) NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (addon_id)
);


CREATE TABLE xipli.ingredient (
  ingredient_id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (ingredient_id)
);


CREATE TABLE xipli.item (
  item_id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  description varchar(255) DEFAULT NULL,
  shop_id int(11) NOT NULL,
  price decimal(20,2) DEFAULT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (item_id)
);


CREATE TABLE xipli.item_addon (
  item_id int(11) NOT NULL,
  addon_id int(11) NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (item_id,addon_id)
);


CREATE TABLE xipli.item_ingredient (
  item_id int(11) NOT NULL,
  ingredient_id int(11) NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (item_id,ingredient_id)
);


CREATE TABLE xipli.item_option (
  item_option_id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(45) NOT NULL,
  price decimal(20,2) NOT NULL,
  item_id int(11) NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (item_option_id)
);

CREATE TABLE xipli.order (
  order_id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  shop_id int(11) NOT NULL,
  total_price decimal(20,2) NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  order_time datetime NOT NULL,
  acknowledge_time datetime DEFAULT NULL,
  complete_time datetime DEFAULT NULL,
  pickup_time datetime DEFAULT NULL,
  bar_code blob DEFAULT NULL,
  PRIMARY KEY (order_id)
);


CREATE TABLE xipli.order_line (
  order_line_id int(11) NOT NULL AUTO_INCREMENT,
  order_id int(11) NOT NULL,
  item_id int(11) NOT NULL,
  option_id int(11) NOT NULL,
  price decimal(20,2) NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  process_start_time datetime DEFAULT NULL,
  process_end_time datetime DEFAULT NULL,
  PRIMARY KEY (order_line_id)
);


CREATE TABLE xipli.order_addon (
  order_line_id int(11) NOT NULL,
  addon_id int(11) NOT NULL,
  PRIMARY KEY (order_line_id,addon_id)
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
