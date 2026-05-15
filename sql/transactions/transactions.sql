--Query 1
SELECT Fname, Lname, Salary
FROM employee
WHERE Salary > (
    SELECT AVG(Salary)
    FROM employee
);

--Query 2
SELECT city, COUNT(*) AS restaurant_count
FROM restaurant
GROUP BY city
HAVING COUNT(*) > 2;

--Query 3
SELECT r.restaurname, r.city
FROM restaurant r
WHERE EXISTS
(
    SELECT *
    FROM serves s
    WHERE s.restaurname = r.restaurname
    AND s.price > 20
);

--Insert

START TRANSACTION;

INSERT INTO customer
VALUES ('C006', 'Daniel', 'Madrid', 111222);

INSERT INTO employee_customer
VALUES ('123456789', 'C006');

COMMIT;

--Update
START TRANSACTION;

UPDATE employee
SET Salary = Salary * 1.10
WHERE Dno =
(
    SELECT Dnumber
    FROM department
    WHERE Dname = 'IT'
);

COMMIT;