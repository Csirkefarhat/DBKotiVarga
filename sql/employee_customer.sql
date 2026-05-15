CREATE TABLE employee_customer (
    Emp_id CHAR(9) NOT NULL,
    Cust_Id VARCHAR(20) NOT NULL,

    PRIMARY KEY (Emp_id, Cust_Id),

    CONSTRAINT fk_employee
        FOREIGN KEY (Emp_id)
        REFERENCES employee(Ssn)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_customer
        FOREIGN KEY (Cust_Id)
        REFERENCES customer(CustomerId)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);