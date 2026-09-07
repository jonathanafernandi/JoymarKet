# JoymarKet

JoymarKet is a prototype Java-based desktop marketplace application focusing on fresh food, meats, and groceries, delivered instantly to customers' doors. Built with the **MVC (Model-View-Controller)** architecture, it was developed as a laboratory project for the COMP6115001 – Object-Oriented Analysis and Design course.

## Table of Contents

- [Overview](#overview)
- [Group Members](#group-members)
- [Project Structure](#project-structure)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Features](#features)
- [User Roles](#user-roles)
- [Validation Rules](#validation-rules)
- [Getting Started](#getting-started)
- [Application Guide](#application-guide)

## Overview

JoymarKet addresses the growing demand for digital convenience in the e-grocery segment by providing a comprehensive system to manage marketplace operations end-to-end, from customer registration and shopping to order fulfillment and delivery tracking. The system supports three distinct user roles (Customer, Admin, Courier), each with dedicated dashboards and permissions.

## Group Members

| Name | Student ID |
|---|---|
| Jonathan Alvindo Fernandi | 2602089143 |
| Aditya Eka Pratama | 2702376055 |

**Course**: COMP6115001 – Object-Oriented Analysis and Design

## Project Structure

```
JoymarKet/
├── src/
│   ├── main/
│   │   └── Main.java   # JavaFX application entry point
│   ├── model/
│   │   ├── User.java
│   │   ├── Customer.java
│   │   ├── Admin.java
│   │   ├── Courier.java
│   │   ├── Product.java
│   │   ├── CartItem.java
│   │   ├── OrderHeader.java
│   │   ├── OrderDetail.java
│   │   ├── Delivery.java
│   │   └── Promo.java
│   ├── database/
│   │   ├── DatabaseConnection.java   # Singleton MySQL connection manager
│   │   ├── UserDA.java
│   │   ├── CustomerDA.java
│   │   ├── AdminDA.java
│   │   ├── CourierDA.java
│   │   ├── ProductDA.java
│   │   ├── CartItemDA.java
│   │   ├── OrderHeaderDA.java
│   │   ├── OrderDetailDA.java
│   │   ├── DeliveryDA.java
│   │   └── PromoDA.java
│   ├── controller/
│   │   ├── CustomerHandler.java
│   │   ├── ProductHandler.java
│   │   ├── OrderHeaderHandler.java
│   │   ├── PromoHandler.java
│   │   ├── DeliveryHandler.java
│   │   └── UserHandler.java
│   └── view/
│       ├── UserWindow.java
│       ├── CustomerWindow.java
│       ├── ProductWindow.java
│       ├── CartItemWindow.java
│       ├── OrderHeaderWindow.java
│       └── DeliveryWindow.java
├── mysql/   # Database schema for the "joymarket" database
├── docs/
│   ├── COMP6115001-OOAD_LabProject.pdf
│   ├── diagram.vpp
│   ├── COMP6115001-OOAD_LabProject_AssessmentCriteria.pdf
│   └── JoymarKet_ApplicationGuide.pdf
├── .gitignore
└── README.md
```

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 11 |
| UI Framework | JavaFX |
| Database | MySQL, accessed via JDBC (MySQL Connector/J) |
| IDE | Eclipse |
| Database Tooling | XAMPP |
| Design Tooling | Visual Paradigm |

## Architecture

The application strictly follows the **MVC (Model-View-Controller)** pattern:

- **Model**: Represents core business entities (`User`, `Customer`, `Admin`, `Courier`, `Product`, `CartItem`, `OrderHeader`, `OrderDetail`, `Delivery`, `Promo`) and exposes data-access operations via corresponding DAO classes.
- **View**: Contains all JavaFX UI screens (login, registration, dashboards, product browsing, cart, order history, delivery management), built programmatically without FXML.
- **Controller**: Handles input validation and business logic, delegating requests from the View layer to the Model layer (implemented as `*Handler` classes).

## Features

### Authentication
- Login with email and password, validated against stored database records.
- Customer self-registration with input validation (see [Validation Rules](#validation-rules)).
- Profile editing for full name, phone, and address (email cannot be changed after registration).
- Logout, returning the user to the Login view.

### Customer Features
- Browse available products and view product details.
- Add products to a shopping cart, respecting available stock.
- Update or remove items in the cart.
- Apply a promo code at checkout (validated against existing promo records).
- Checkout, with balance sufficiency validated against the order total.
- Top up account balance (minimum Rp10,000 per transaction).
- View order history ("My Orders").
- Edit personal profile information.

### Admin Features
- View all orders placed by customers.
- Manage products by updating stock quantities (must remain non-negative).
- Assign an available courier to an order that is ready for delivery.
- Edit personal profile information.

### Courier Features
- View all deliveries assigned to the courier ("My Deliveries").
- Update delivery status through defined stages: **Pending → In Progress → Delivered**.
- Edit personal profile information.

## User Roles

| Role | Key Capabilities |
|---|---|
| **Customer** | Register, browse products, manage cart, checkout with promo, top up balance, view order history |
| **Admin** | View all orders, manage product stock, assign couriers to orders |
| **Courier** | View assigned deliveries, update delivery status |

## Validation Rules

| Field | Rule |
|---|---|
| Customer Email | Required, must end with `@gmail.com`, must be unique |
| Customer Password | Minimum 6 characters |
| Confirm Password | Must match Password |
| Customer Phone | Required, numeric, 10–13 digits |
| Customer Address | Required |
| Top-Up Amount | Required, numeric, minimum Rp10,000 |
| Cart Item Count | Required, numeric, between 1 and available stock |
| Promo Code | If entered, must exist in the database |
| Checkout Balance | Must be greater than or equal to the order total |
| Product Stock (Admin) | Numeric, cannot be negative |
| Courier Assignment | Must be selected and must exist in the database |
| Delivery Status | Must be one of: Pending, In Progress, Delivered |
| Login Email/Password | Must match an existing record in the database |

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11
- MySQL server (e.g., via XAMPP)
- MySQL Connector/J (JDBC driver)
- Eclipse (or any Java IDE with JavaFX support)

### Installation

```bash
git clone https://github.com/jonathanafernandi/JoymarKet.git
cd JoymarKet
```

1. Import the project into Eclipse as a Java project.
2. Add the JavaFX SDK and MySQL Connector/J to the project's build path.
3. Start MySQL (via XAMPP or a standalone server) and create a database named `joymarket`.
4. Import the schema from the `mysql/` folder into the `joymarket` database.
5. Configure database connection details (host, port, username, password).
6. Run `Main.java` to launch the application.

## Application Guide

1. **Login**: Enter your email and password, then click **Login**.
2. **Register**: New customers click **Create New Account**, fill in full name, email (must end with `@gmail.com`), password, confirm password, phone number, and address, then click **Register**.
3. **Customer Dashboard**: View your name and balance. Use **Browse Products** to add items to your cart, **View Cart** to review and check out (with an optional promo code), **My Orders** to view purchase history, and **Top Up Balance** to add funds.
4. **Courier Dashboard**: View assigned deliveries under **My Deliveries** and update each delivery's status through the dropdown and **Update Status** button.
5. **Admin Dashboard**: Use **View All Orders** to monitor orders, **Manage Products** to update stock, and **Assign Courier** to assign a courier to a ready order.
6. **Edit Profile**: Available to all roles; update name, phone, and address (email is fixed after registration).
7. **Log Out**: Returns to the Login view.
