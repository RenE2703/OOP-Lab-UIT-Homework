-- Lab 3 - Homework: schema for the Orders & Inventory Management API
-- Run with: mysql -u root -p < sql/schema.sql

DROP DATABASE IF EXISTS ecommerce_order_db;
CREATE DATABASE ecommerce_order_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE ecommerce_order_db;

CREATE TABLE customers (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    full_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    phone      VARCHAR(15),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

CREATE TABLE products (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(150) NOT NULL,
    price        DECIMAL(12, 2) NOT NULL,
    stock        INT NOT NULL DEFAULT 0,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

CREATE TABLE orders (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    customer_id  INT NOT NULL,
    status       ENUM('pending', 'completed', 'cancelled') NOT NULL DEFAULT 'pending',
    total_amount DECIMAL(12, 2) NOT NULL DEFAULT 0,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers (id),
    INDEX idx_orders_customer (customer_id),
    INDEX idx_orders_status (status)
) ENGINE = InnoDB;

CREATE TABLE order_items (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    order_id   INT NOT NULL,
    product_id INT NOT NULL,
    quantity   INT NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    CONSTRAINT fk_items_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_items_product FOREIGN KEY (product_id) REFERENCES products (id),
    INDEX idx_items_order (order_id),
    INDEX idx_items_product (product_id)
) ENGINE = InnoDB;
