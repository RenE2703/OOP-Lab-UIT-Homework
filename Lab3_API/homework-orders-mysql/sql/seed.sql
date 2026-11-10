-- Lab 3 - Homework: sample data (3-5 records per table)
-- Run after schema.sql: mysql -u root -p < sql/seed.sql

USE ecommerce_order_db;

INSERT INTO customers (id, full_name, email, phone) VALUES
    (1, 'Nguyen Van An', 'an.nguyen@example.com', '0901234567'),
    (2, 'Tran Thi Binh', 'binh.tran@example.com', '0912345678'),
    (3, 'Le Van Cuong', 'cuong.le@example.com', '0923456789'),
    (4, 'Pham Thi Dung', 'dung.pham@example.com', '0934567890');

INSERT INTO products (id, product_name, price, stock) VALUES
    (1, 'Laptop Dell XPS 15', 35000000.00, 10),
    (2, 'iPhone 15 Pro', 28000000.00, 15),
    (3, 'Sony WH-1000XM5', 7000000.00, 20),
    (4, 'Logitech MX Master 3S', 2500000.00, 30),
    (5, 'Samsung Galaxy Tab S9', 18000000.00, 8);

INSERT INTO orders (id, customer_id, status, total_amount) VALUES
    (1, 1, 'completed', 63000000.00),
    (2, 2, 'pending', 9500000.00),
    (3, 3, 'completed', 36000000.00),
    (4, 1, 'completed', 14000000.00),
    (5, 4, 'cancelled', 2500000.00);

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
    (1, 1, 1, 35000000.00),
    (1, 2, 1, 28000000.00),
    (2, 3, 1, 7000000.00),
    (2, 4, 1, 2500000.00),
    (3, 5, 2, 18000000.00),
    (4, 3, 2, 7000000.00),
    (5, 4, 1, 2500000.00);
