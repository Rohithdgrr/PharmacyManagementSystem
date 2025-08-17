CREATE DATABASE IF NOT EXISTS pharmacy_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pharmacy_db;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS medicines;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE users (
     id INT AUTO_INCREMENT PRIMARY KEY,
     username VARCHAR(50) NOT NULL UNIQUE,
     password VARCHAR(50) NOT NULL,
     role ENUM('manager','salesman') NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     INDEX idx_username (username)
)  ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE medicines (
     id INT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(100) NOT NULL,
     price DECIMAL(10,2) NOT NULL CHECK (price >=0),
     cost_price DECIMAL(10 ,2) NOT NULL CHECK (cost_price>=0),
     quantity INT NOT NULL DEFAULT 0 CHECK (quantity >=0),
     expiry_date DATE,
     added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     INDEX idx_name (name),
     INDEX idx_expiry(expiry_date),
     INDEX idx_quantity (quantity)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE customers (
      id INT AUTO_INCREMENT PRIMARY KEY,
      name  VARCHAR(100) NOT NULL,
      phone VARCHAR(15),
      email VARCHAR(100),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX idx_name (name),
      INDEX idx_phone( phone)
) ENGINE= InnoDB DEFAULT CHARSET= utf8mb4;

CREATE TABLE sales (
      id INT AUTO_INCREMENT PRIMARY KEY,
      medicine_id INT NOT NULL,
      customer_id INT NOT NULL,
      quantity_sold INT NOT NULL CHECK (quantity_sold > 0),
      total_price DECIMAL(10 ,2) NOT NULL CHECK (total_price >= 0),
      sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX idx_medicine (medicine_id),
      INDEX idx_customer (customer_id),
      INDEX idx_date (sale_date),
      FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE,
      FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET =utf8mb4;

INSERT INTO users( username, password, role) VALUES
('manager', '123' , 'manager'),
('salesman', '123', 'salesman');

INSERT INTO medicines ( name , price , cost_price ,quantity , expiry_date) VALUES
('Paracetamol 500mg' ,10.00, 6.00,100, '2026-12-01'),
('Amoxicillin 250mg', 15.50, 9.00,50,'2025-08-15'),
('Omerprazole 20mg', 20.00,12.00,30, '2026-11-30');

INSERT INTO customers (name, phone ,email) VALUES 
('Rahul sharma', '9393329539' ,'rahul@gmail.com'),
('priya singh', '93949589839', 'priya@email.com'),
('Amit Patel', '9858649844' ,'amit@email.com');

INSERT INTO sales (medicine_id ,customer_id ,quantity_sold, total_price) 
VALUES( 1,1,2,20.00),
(2,2,3,46.50),
(1,3,1,10.00);
 
SELECT '✅ Tables created and simple data inserted!' AS Status;
SELECT * FROM users;
SELECT * FROM medicines;
SELECT *FROM  customers;
SELECT *FROM sales LIMIT 5;






















