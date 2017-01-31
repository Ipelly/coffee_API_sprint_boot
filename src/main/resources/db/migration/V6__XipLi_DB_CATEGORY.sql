CREATE TABLE xipli.category (
  category_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NULL,
  shop_id int(11) UNSIGNED NOT NULL,
  status tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  PRIMARY KEY (category_id),
  UNIQUE INDEX category_id_UNIQUE (category_id ASC)
);

