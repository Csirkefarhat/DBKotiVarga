-- DEPARTMENTS
INSERT INTO department
VALUES ('IT', 1, '123456789', '2024-01-01');

INSERT INTO department
VALUES ('HR', 2, '987654321', '2024-02-01');

INSERT INTO department
VALUES ('SALES', 3, '555666777', '2024-03-01');


-- EMPLOYEES
INSERT INTO employee
VALUES (
'John',
'A',
'Smith',
'123456789',
'1990-01-01',
'Madrid',
'M',
5000,
NULL,
1
);

INSERT INTO employee
VALUES (
'Anna',
'B',
'Brown',
'987654321',
'1992-05-10',
'Bilbao',
'F',
6200,
'123456789',
2
);

INSERT INTO employee
VALUES (
'Carlos',
'C',
'Lopez',
'555666777',
'1988-11-20',
'Barcelona',
'M',
7100,
'123456789',
3
);

INSERT INTO employee
VALUES (
'Maria',
'D',
'Garcia',
'111222333',
'1995-03-15',
'Valencia',
'F',
4800,
'987654321',
1
);

INSERT INTO employee
VALUES (
'David',
'E',
'Martinez',
'444555666',
'1991-07-22',
'Sevilla',
'M',
5300,
'555666777',
2
);


-- CUSTOMERS
INSERT INTO customer
VALUES (
'C001',
'Linda',
'Madrid',
123456
);

INSERT INTO customer
VALUES (
'C002',
'Peter',
'Bilbao',
654321
);

INSERT INTO customer
VALUES (
'C003',
'Sophia',
'Barcelona',
777888
);

INSERT INTO customer
VALUES (
'C004',
'Michael',
'Valencia',
999111
);

INSERT INTO customer
VALUES (
'C005',
'Emma',
'Sevilla',
222333
);


-- EMPLOYEE_CUSTOMER LINKS
INSERT INTO employee_customer
VALUES (
'123456789',
'C001'
);

INSERT INTO employee_customer
VALUES (
'123456789',
'C002'
);

INSERT INTO employee_customer
VALUES (
'987654321',
'C003'
);

INSERT INTO employee_customer
VALUES (
'555666777',
'C004'
);

INSERT INTO employee_customer
VALUES (
'111222333',
'C005'
);


-- RESTAURANTS
INSERT INTO restaurant
VALUES (
'BurgerHouse',
'Madrid',
120,
4.5,
NULL
);

INSERT INTO restaurant
VALUES (
'PastaWorld',
'Barcelona',
80,
4.2,
'BurgerHouse'
);

INSERT INTO restaurant
VALUES (
'SushiTime',
'Bilbao',
60,
4.8,
'BurgerHouse'
);


-- DISHES
INSERT INTO dishes
VALUES (
'Burger',
'American',
'Main',
2.0
);

INSERT INTO dishes
VALUES (
'Pizza',
'Italian',
'Main',
3.0
);

INSERT INTO dishes
VALUES (
'Sushi',
'Japanese',
'Main',
4.5
);


-- SERVES
INSERT INTO serves
VALUES (
'BurgerHouse',
'Burger',
12.50
);

INSERT INTO serves
VALUES (
'PastaWorld',
'Pizza',
15.00
);

INSERT INTO serves
VALUES (
'SushiTime',
'Sushi',
22.00
);


-- PERSON
INSERT INTO person
VALUES (
'Linda',
23,
'Female',
1
);

INSERT INTO person
VALUES (
'Peter',
30,
'Male',
2
);

INSERT INTO person
VALUES (
'Sophia',
27,
'Female',
3
);


-- FREQUENTS
INSERT INTO frequents
VALUES (
'Linda',
'BurgerHouse'
);

INSERT INTO frequents
VALUES (
'Peter',
'PastaWorld'
);

INSERT INTO frequents
VALUES (
'Sophia',
'SushiTime'
);


-- TOURGUIDES
INSERT INTO tourguide
VALUES (
'G001',
'Alex',
123123
);

INSERT INTO tourguide
VALUES (
'G002',
'Laura',
555444
);


-- TRIPS
INSERT INTO trip
VALUES (
'Berlin',
'2025-06-01',
5,
'Madrid',
'G001',
120
);

INSERT INTO trip
VALUES (
'Paris',
'2025-07-15',
4,
'Barcelona',
'G002',
150
);


-- HOTELS
INSERT INTO hotel
VALUES (
'H001',
'Berlin Hotel',
'Berlin',
200
);

INSERT INTO hotel
VALUES (
'H002',
'Paris Inn',
'Paris',
150
);


-- HOTEL_TRIP
INSERT INTO hotel_trip
VALUES (
'Berlin',
'2025-06-01',
'H001',
300
);

INSERT INTO hotel_trip
VALUES (
'Paris',
'2025-07-15',
'H002',
450
);