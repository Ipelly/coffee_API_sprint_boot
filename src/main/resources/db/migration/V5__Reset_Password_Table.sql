
CREATE TABLE xipli.password_reset_code (
  code_id varchar(15) NOT NULL COMMENT 'A unique id that is linked to a user',
  user_id int(11) UNSIGNED NOT NULL,
  code varchar(255) NOT NULL COMMENT 'Hash of the code',
  PRIMARY KEY (code_id),
  INDEX (user_id)
);
