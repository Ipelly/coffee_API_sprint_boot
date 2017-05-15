
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
