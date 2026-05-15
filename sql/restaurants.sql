--
-- Database/schema: restaurants
--

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
-- SET FOREIGN_KEY_CHECKS=0;

--DROP SCHEMA IF EXISTS restaurants ;
--CREATE SCHEMA IF NOT EXISTS restaurants DEFAULT CHARACTER SET utf8mb4 ;
--USE restaurants;

--
-- Table structure for table eats
--
DROP TABLE IF EXISTS eats;
CREATE TABLE IF NOT EXISTS eats (
  nameId varchar(40) NOT NULL,
  dish varchar(30) NOT NULL,
  PRIMARY KEY pk_name_dish (nameId,dish),
  INDEX idx_eats_nameId (nameId),
  INDEX eats_dish (dish),
  CONSTRAINT fk_eats_nameId 
	FOREIGN KEY (nameId) 
	REFERENCES person (nameId)
	ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_eats_dish
    FOREIGN KEY (dish)
    REFERENCES dishes (dish)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table frequents
--
DROP TABLE IF EXISTS frequents;
CREATE TABLE IF NOT EXISTS frequents (
  nameId varchar(40) NOT NULL,
  restaurname varchar(25) NOT NULL,
  PRIMARY KEY pk_nameid_restaur (nameId,restaurname),
  INDEX freq_nameId (nameId),
  INDEX freq_restaurname (restaurname),
  CONSTRAINT fk_freq_nameid 
	FOREIGN KEY (nameId) 
	REFERENCES person (nameId) ON UPDATE CASCADE,
  CONSTRAINT fk_freq_restaurname 
	FOREIGN KEY (restaurname) 
	REFERENCES restaurant (restaurname) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table person
--
DROP TABLE IF EXISTS person;
CREATE TABLE IF NOT EXISTS person (
  nameId varchar(40) NOT NULL,
  age int(2) DEFAULT NULL,
  gender char(6) DEFAULT NULL,
   id int(11) NOT NULL,
  PRIMARY KEY pk_nameid (nameId),
  INDEX idx_id (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Table structure for table customer
--

-- --------------------------------------------------------

--
-- Table structure for table dishes
--
DROP TABLE IF EXISTS dishes;
CREATE TABLE IF NOT EXISTS dishes (
  dish varchar(30) NOT NULL,
  cuisine varchar(45) DEFAULT NULL,
  category varchar(45) DEFAULT NULL,
  difficulty decimal(4,2) DEFAULT NULL,
  PRIMARY KEY pk_dish (dish)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- --------------------------------------------------------

--
-- Table structure for table restaurant
--
DROP TABLE IF EXISTS restaurant;
CREATE TABLE IF NOT EXISTS restaurant (
    restaurname VARCHAR(25) NOT NULL,
    city VARCHAR(45) DEFAULT NULL,
    capacity INT(3) DEFAULT NULL,
    rating DECIMAL(3 , 1 ) DEFAULT NULL,
    reportsresults2 VARCHAR(45) DEFAULT NULL,
    PRIMARY KEY pk_restaurname (restaurname),
    INDEX idx_reportsresuls (reportsresults2),
    CONSTRAINT fk_reportsresuls 
		FOREIGN KEY (reportsresults2)
        REFERENCES restaurant (restaurname)
        ON DELETE NO ACTION ON UPDATE NO ACTION
)  ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- --------------------------------------------------------

--
-- Table structure for table menu
--
DROP TABLE IF EXISTS menu;
CREATE TABLE IF NOT EXISTS menu (
    mtype INT(11) NOT NULL,
    mid INT(11) NOT NULL,
    price DECIMAL(10 , 0 ) DEFAULT NULL,
    PRIMARY KEY pk_mtyp_mid (mtype , mid)
)  ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- --------------------------------------------------------

--
-- Table structure for table menu_order
--
DROP TABLE IF EXISTS menu_order;
CREATE TABLE IF NOT EXISTS menu_order (
    numord INT(11) NOT NULL AUTO_INCREMENT,
    menu_mtype INT(11) NOT NULL,
    menu_id INT(11) NOT NULL,
    customer_id INT(11) NOT NULL,
    PRIMARY KEY pk_numord (numord),
    INDEX idx_menu_mtype (menu_mtype , menu_id),
    INDEX idx_customer (customer_id),
    CONSTRAINT menu_order_ibfk_1 
		FOREIGN KEY (menu_mtype , menu_id)
        REFERENCES menu (mtype , mid)
        ON UPDATE CASCADE,
    CONSTRAINT menu_order_ibfk_2 
		FOREIGN KEY (customer_id)
        REFERENCES person (id)
)  ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- --------------------------------------------------------

--
-- Table structure for table serves
--
DROP TABLE IF EXISTS serves;
CREATE TABLE IF NOT EXISTS serves (
    restaurname VARCHAR(25) NOT NULL,
    dish VARCHAR(30) NOT NULL,
    price DECIMAL(4 , 2 ) DEFAULT NULL,
    PRIMARY KEY pk_restaurname_dish (restaurname , dish),
    INDEX idx_restaurname (restaurname),
    INDEX fk_serves_dish_idx (dish),
    CONSTRAINT fk_restaurname_serves FOREIGN KEY (restaurname)
        REFERENCES restaurant (restaurname)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_serves_dish FOREIGN KEY (dish)
        REFERENCES dishes (dish)
        ON DELETE NO ACTION ON UPDATE NO ACTION
)  ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- --------------------------------------------------------

--
-- Table structure for table sales
-- date can be null

DROP TABLE IF EXISTS sales;
CREATE TABLE IF NOT EXISTS sales (
   restaurname VARCHAR(25) NOT NULL,
   dateOfSale DATE UNIQUE,
   amount decimal (10,2) DEFAULT NULL,
   PRIMARY KEY pk_restaurname_date (restaurname,dateOfSale),
   INDEX idx_restaurnamesales (restaurname),
   CONSTRAINT fk_restaurname_sales FOREIGN KEY (restaurname)
        REFERENCES restaurant (restaurname)
        ON DELETE NO ACTION ON UPDATE CASCADE
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



COMMIT;