
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
