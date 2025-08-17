Pharmacy Management System
Overview

The Pharmacy Management System is a Java-based console application that facilitates the management of pharmacy operations.It supports user authentication with roles (e.g., manager), allowing secure access to features like adding, updating, and deleting medicines.Users can view stock, search for medicines, add customers, and record sales with automatic stock updates and profit calculations.The system uses MySQL for data persistence and includes error handling for robust operation.
Features

User Authentication: Login system with role-based access (e.g., manager privileges for updates and deletions).
Medicine Management: Add, update, delete, view, and search medicines with details like price, cost, quantity, and expiry.
Customer Management: Add new customers with contact information.
Sales Recording: Record sales transactions, update stock, and calculate revenue/profit atomically.
Stock Alerts: Low stock warnings when viewing inventory.
Database Integration: Uses MySQL to store users, medicines, customers, and sales data.

Prerequisites

Java Development Kit (JDK): Version 8 or higher.
MySQL Database: Version 5.7 or higher, with a database named pharmacy_db.
MySQL JDBC Driver: Included via java.sql.*, but ensure the driver is in your classpath if needed.
Operating System: Any OS supporting Java and MySQL (Windows, macOS, Linux).
Text Editor or IDE: IntelliJ IDEA, Eclipse, or VS Code recommended.

Database Setup
Before running the application, set up the MySQL database:

Create the database:CREATE DATABASE pharmacy_db;


Create the required tables:USE pharmacy_db;


Update the password in the code: Replace "your MYSQL password@" with your actual MySQL root password.

Installation

Clone the Repository:git clone https://github.com/your-username/pharmacy-management-system.git


Navigate to the Project Directory:cd pharmacy-management-system


Set Up MySQL: Follow the Database Setup section above.
Update Database Credentials: In PharmacyManagementSystem.java, replace USER and PASS with your MySQL credentials.

Usage

Compile the Code:javac PharmacyManagementSystem.java


Run the Application:java PharmacyManagementSystem


Login: Use credentials from the users table (e.g., username: admin, password: password).
Interact with the Menu:
Options vary by role:
Add Medicine (all users).
Update/Delete Medicine (managers only).
View Stock, Search Medicine, Add Customer, Record Sale.
Logout to exit.





Project Structure
pharmacy-management-system/
├── src/
│   └── PharmacyManagementSystem.java  # Main Java source file
└── README.md  # Project documentation

(Note: Database files are managed externally via MySQL.)
Example Interaction
💊💊 ------------- Welcome to PHARMACY MANAGEMENT SYSTEM ------------- 💊💊
Username: admin
Password: password
✅ Login successfull!

========================================
--------------------PHARMACY MANAGEMENT SYSTEM--------------------
========================================
🧑🏻‍🦱 Logged in: admin (manager)
========================================
1. Add Medicine
2. Update Medicine
3. Delete Medicine
4. View Stock
5. Search Medicine
6. Add Customer
7. Record Sale
8. Logout
choose an option: 

Contributing

Fork the repository.
Create a new branch (git checkout -b feature-branch).
Make changes and commit (git commit -m "Add feature").
Push to the branch (git push origin feature-branch).
Create a pull request.


Implement full sales reporting and analytics.
Add GUI using JavaFX or Swing.
Enhance security (e.g., hashed passwords).
Support for multiple pharmacies or branches.
Input validation for dates, emails, etc.
