SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;


-- -----------------------------------------------------
-- Schema TRAVEL
-- -----------------------------------------------------
-- Uncomment for creating the schema "travel" (if allowed)
-- DROP SCHEMA IF EXISTS `travel` ;
-- CREATE SCHEMA IF NOT EXISTS `travel` DEFAULT CHARACTER SET utf8mb4 ; 
-- USE `travel`; 

-- -----------------------------------------------------
-- Tables TRAVEL. 
-- At the bottom linking table to COMPANY
-- -----------------------------------------------------


DROP TABLE IF EXISTS hotel;
CREATE TABLE IF NOT EXISTS hotel (
  HotelId varchar(20) NOT NULL DEFAULT '',
  hotelname varchar(40) DEFAULT NULL,
  hotelcity varchar(20) DEFAULT NULL,
  hotelcapacity int(11) DEFAULT NULL,
  PRIMARY KEY (HotelId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS tourguide;
CREATE TABLE IF NOT EXISTS tourguide (
  GuideId varchar(20) NOT NULL,
  guidename varchar(20) NOT NULL,
  guidephone int(11) NOT NULL,
  PRIMARY KEY (GuideId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS languages;
CREATE TABLE IF NOT EXISTS languages (
  GuideId varchar(20) NOT NULL DEFAULT '',
  Lang varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (GuideId,Lang),
  INDEX lgid (GuideId),
  CONSTRAINT lg_tg FOREIGN KEY (GuideId)
        REFERENCES tourguide (GuideId)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS customer;
CREATE TABLE IF NOT EXISTS customer (
    CustomerId VARCHAR(20) NOT NULL DEFAULT '',
    custname VARCHAR(40) DEFAULT NULL,
    custaddress VARCHAR(40) DEFAULT NULL,
    custphone INT(11) DEFAULT NULL,
    PRIMARY KEY (CustomerId)
)  ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS trip;
CREATE TABLE IF NOT EXISTS trip (
  TripTo varchar(20) NOT NULL DEFAULT '',
  DepartureDate varchar(10) NOT NULL DEFAULT '0000-00-00',
  Numdays int(11) DEFAULT NULL,
  CityDeparture varchar(20) DEFAULT NULL,
  GuideId varchar(20) DEFAULT NULL,
  Ppday int(11) DEFAULT NULL,
  PRIMARY KEY (TripTo,DepartureDate),
  INDEX gid (GuideId),
  CONSTRAINT ht_tg FOREIGN KEY (GuideId)
        REFERENCES tourguide (GuideId)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS hotel_trip;
CREATE TABLE hotel_trip (
    TripTo VARCHAR(20) NOT NULL DEFAULT '',
    DepartureDate varchar(10) NOT NULL DEFAULT '0000-00-00',
    HotelId VARCHAR(20) NOT NULL DEFAULT '',
    price DECIMAL,
    PRIMARY KEY (TripTo , DepartureDate , HotelId),
    INDEX ttdd (TripTo , DepartureDate),
    INDEX h (HotelId),
    CONSTRAINT ht_t FOREIGN KEY (TripTo , DepartureDate)
        REFERENCES trip (TripTo , DepartureDate)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT ht_h FOREIGN KEY (HotelId)
        REFERENCES hotel (HotelId)
        ON UPDATE CASCADE ON DELETE RESTRICT  
)  ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS hotel_trip_customer;
CREATE TABLE IF NOT EXISTS hotel_trip_customer (
    TripTo VARCHAR(20) NOT NULL DEFAULT '',
    DepartureDate varchar(10) NOT NULL DEFAULT '0000-00-00',
    HotelId VARCHAR(20) NOT NULL DEFAULT '',
    CustomerId VARCHAR(20) NOT NULL DEFAULT '',
    NumNights INT,
    PRIMARY KEY (TripTo , DepartureDate , HotelId , CustomerId),
    INDEX ttddh (TripTo , DepartureDate , HotelId),
    INDEX cid (CustomerId),
    CONSTRAINT htc_ht FOREIGN KEY (TripTo , DepartureDate , HotelId)
        REFERENCES hotel_trip (TripTo , DepartureDate , HotelId)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT htc_cus FOREIGN KEY (CustomerId)
        REFERENCES customer (CustomerId)
        ON UPDATE CASCADE ON DELETE RESTRICT
)  ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS optional_excursion;
CREATE TABLE IF NOT EXISTS optional_excursion (
  TripTo varchar(20) NOT NULL DEFAULT '',
  DepartureDate varchar(10) NOT NULL DEFAULT '0000-00-00',
  CodeExc int(11) NOT NULL DEFAULT '0',
  ExcursionTo varchar(20) DEFAULT NULL,
  DepartTime time DEFAULT NULL,
  DepartPlace varchar(20) DEFAULT NULL,
  Price int(11) DEFAULT NULL,
  PRIMARY KEY (TripTo,DepartureDate,CodeExc),
  INDEX ttdd_oe (TripTo,DepartureDate),
  CONSTRAINT oe_t FOREIGN KEY (TripTo , DepartureDate)
        REFERENCES trip (TripTo , DepartureDate)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS excur_opt_customer;
CREATE TABLE IF NOT EXISTS excur_opt_customer (
    TripTo VARCHAR(20) NOT NULL DEFAULT '',
    DepartureDate varchar(10) NOT NULL DEFAULT '0000-00-00',
    CodeExc INT(11) NOT NULL DEFAULT '0',
    CustomerId VARCHAR(20) NOT NULL DEFAULT '',
    PRIMARY KEY (TripTo , DepartureDate , CodeExc , CustomerId),
    INDEX ttddce_eoc (TripTo , DepartureDate , CodeExc),
    CONSTRAINT eoc_oe FOREIGN KEY (TripTo , DepartureDate , CodeExc)
        REFERENCES optional_excursion (TripTo , DepartureDate , CodeExc)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    INDEX eoc_cid (CustomerId),
    CONSTRAINT eoc_cus FOREIGN KEY (CustomerId)
        REFERENCES customer (CustomerId)
        ON UPDATE CASCADE ON DELETE RESTRICT
)  ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- EMPLOYEE-CUSTOMER links both schemas 
-- 

-- ~ -- UNCOMMENT IT FOR LINKING TO THE COMPANY DATABASE
/*
DROP TABLE IF EXISTS employee_customer;
CREATE TABLE IF NOT EXISTS employee_customer (
    Emp_id CHAR(9) NOT NULL  DEFAULT '',
    Cust_Id VARCHAR(20) NOT NULL UNIQUE DEFAULT '',
    PRIMARY KEY (Emp_id , Cust_Id),
    INDEX eid (Emp_id),
    INDEX cid (Cust_Id)
#     ,CONSTRAINT Emp_Emp FOREIGN KEY (Emp_id)
#         REFERENCES employee (Ssn)
#         ON UPDATE CASCADE ON DELETE RESTRICT
        ,CONSTRAINT Cust_Cust FOREIGN KEY (Cust_id)
         REFERENCES customer (CustomerId)
         ON UPDATE CASCADE ON DELETE RESTRICT
)  ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

*/

-- SET FOREIGN_KEY_CHECKS=1;
