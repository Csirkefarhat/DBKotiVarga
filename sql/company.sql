CREATE TABLE IF NOT EXISTS department (
  Dname VARCHAR(15) NOT NULL,
  Dnumber INT NOT NULL,
  Mgr_ssn CHAR(9) NOT NULL,
  Mgr_start_date VARCHAR(10) NULL DEFAULT NULL,

  PRIMARY KEY (Dnumber),

  UNIQUE INDEX Dname_UNIQUE (Dname ASC),

  INDEX (Mgr_ssn ASC)

  -- CONSTRAINT mngr_ssn
  -- FOREIGN KEY (Mgr_ssn)
  -- REFERENCES employee (Ssn)
  -- ON DELETE RESTRICT
  -- ON UPDATE RESTRICT

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -----------------------------------------------------
-- Table EMPLOYEE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS employee (
  Fname VARCHAR(15) NOT NULL,
  Minit CHAR NULL DEFAULT NULL,
  Lname VARCHAR(15) NOT NULL,
  Ssn CHAR(9) NOT NULL,
  Bdate VARCHAR(10) NULL DEFAULT NULL,
  Address VARCHAR(30) NULL DEFAULT NULL,
  Sex CHAR NULL DEFAULT NULL,
  Salary DECIMAL(10,2) NULL DEFAULT NULL,
  Super_ssn CHAR(9) NULL DEFAULT NULL,
  Dno INT NOT NULL,
  PRIMARY KEY (Ssn),
  INDEX superssn (Super_ssn ASC),
  INDEX dno (Dno ASC),
  CONSTRAINT superssn
    FOREIGN KEY (Super_ssn)
    REFERENCES employee (Ssn)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT depno
    FOREIGN KEY (Dno)
    REFERENCES department (Dnumber)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -----------------------------------------------------
-- Table DEPT_LOCATIONS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS dept_locations (
  Dnumber INT NOT NULL,
  Dlocation VARCHAR(15) NOT NULL,
  PRIMARY KEY (Dnumber, Dlocation),
  INDEX dnum (Dnumber),
  CONSTRAINT dnumber 
    FOREIGN KEY (Dnumber)
    REFERENCES department (Dnumber)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -----------------------------------------------------
-- Table PROJECT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS project (
  Pname VARCHAR(15) NOT NULL,
  Pnumber INT NOT NULL,
  Plocation VARCHAR(15) NULL DEFAULT NULL,
  Dnum INT NOT NULL,
  PRIMARY KEY (Pnumber),
  UNIQUE INDEX Pname_UNIQUE (Pname ASC),
  INDEX (Dnum ASC),
  CONSTRAINT depnum
    FOREIGN KEY (Dnum)
    REFERENCES department (Dnumber)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -----------------------------------------------------
-- Table WORKS_ON
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS works_on (
  Essn CHAR(9) NOT NULL,
  Pno INT NOT NULL,
  Hours DECIMAL(3,1) NOT NULL,
  PRIMARY KEY (Essn, Pno),
  INDEX essn (Essn),
  INDEX pno (Pno ASC),
  CONSTRAINT Essn_Ssn
    FOREIGN KEY (Essn)
    REFERENCES employee (Ssn),
  CONSTRAINT pnum
    FOREIGN KEY (Pno)
    REFERENCES project (Pnumber)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -----------------------------------------------------
-- Table DEPENDENT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS dependent (
  Essn CHAR(9) NOT NULL,
  Dependent_name VARCHAR(15) NOT NULL,
  Sex CHAR NULL DEFAULT NULL,
  Bdate VARCHAR(10) NULL DEFAULT NULL,
  Relationship VARCHAR(8) NULL DEFAULT NULL,
  PRIMARY KEY (Essn, Dependent_name),
  INDEX essdep (Essn),
  CONSTRAINT EssnDep_Ssn
    FOREIGN KEY (Essn)
    REFERENCES employee (Ssn)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


SET FOREIGN_KEY_CHECKS=1;