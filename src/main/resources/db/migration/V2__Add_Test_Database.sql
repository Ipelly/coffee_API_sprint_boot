
CREATE DATABASE IF NOT EXISTS demaze;

CREATE TABLE demaze.TestMenu (
  ID INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id of the menu item',
  Name VARCHAR(255) NOT NULL DEFAULT 'name of the menu item',
  Price FLOAT NOT NULL COMMENT 'price of the item',
  PRIMARY KEY (id)
);


INSERT INTO demaze.TestMenu (Name, Price) VALUES 
  ("Latte", 1.89),
  ("Cappuccino", 2.49),
  ("Macchiato", 2.05),
  ("Mocha", 2.25),
  ("Americano", 1.99);