

-- Create New category table
CREATE TABLE xipli.category (
  category_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NULL,
  shop_id INT NOT NULL,
  status VARCHAR(45) NULL,
  PRIMARY KEY (category_id),
  UNIQUE INDEX category_id_UNIQUE (category_id ASC)
);


