-- Product Service Database Initialization Script
-- Run this script to create the database and tables

-- Drop existing database (optional)
-- DROP DATABASE IF EXISTS product_db;

-- Create Database
CREATE DATABASE IF NOT EXISTS product_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE product_db;

-- Create Products Table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary key',
    name VARCHAR(255) NOT NULL COMMENT 'Product name',
    description TEXT COMMENT 'Product description',
    price DECIMAL(10, 2) NOT NULL COMMENT 'Product price',
    quantity INT NOT NULL DEFAULT 0 COMMENT 'Available quantity',
    category VARCHAR(100) NOT NULL COMMENT 'Product category',
    sku VARCHAR(100) UNIQUE NOT NULL COMMENT 'Stock Keeping Unit',
    is_active BOOLEAN DEFAULT true COMMENT 'Is product active',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    
    -- Indexes for performance
    INDEX idx_category (category),
    INDEX idx_name (name),
    INDEX idx_price (price),
    INDEX idx_sku (sku),
    INDEX idx_is_active (is_active),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Product catalog table';

-- Insert Sample Data
INSERT INTO products (name, description, price, quantity, category, sku, is_active) VALUES
('MacBook Pro 16', 'Premium laptop with M2 chip and 16GB RAM', 1999.99, 50, 'Electronics', 'MACBOOK-16-001', true),
('Dell XPS 13', 'Ultra-portable laptop with i7 processor', 1299.99, 35, 'Electronics', 'DELL-XPS-13-001', true),
('HP Pavilion 15', 'Affordable laptop for everyday use', 599.99, 75, 'Electronics', 'HP-PAV-15-001', true),
('iPad Pro 12.9', 'High-performance tablet with M2 chip', 1099.99, 40, 'Electronics', 'IPAD-PRO-12-001', true),
('Samsung Galaxy S24', 'Latest flagship smartphone', 999.99, 100, 'Electronics', 'SAMSUNG-S24-001', true),
('Wireless Mouse', 'Bluetooth wireless mouse', 49.99, 200, 'Accessories', 'MOUSE-BT-001', true),
('USB-C Hub', '7-in-1 USB-C hub with multiple ports', 79.99, 150, 'Accessories', 'USBHUB-7-001', true),
('Mechanical Keyboard', 'RGB Mechanical Gaming Keyboard', 149.99, 80, 'Accessories', 'KEYBOARD-RGB-001', true),
('Monitor 4K 27"', '4K Ultra HD Monitor with HDR', 599.99, 25, 'Electronics', 'MONITOR-4K-27-001', true),
('Laptop Stand', 'Aluminum laptop stand', 39.99, 120, 'Accessories', 'STAND-ALUM-001', true);

-- Verify data
SELECT COUNT(*) as total_products FROM products WHERE is_active = true;
SELECT category, COUNT(*) as count FROM products WHERE is_active = true GROUP BY category;
SELECT * FROM products ORDER BY category, name;

-- Display table structure
DESCRIBE products;

-- Create useful views
CREATE VIEW IF NOT EXISTS low_stock_products AS
SELECT * FROM products 
WHERE quantity < 20 AND is_active = true
ORDER BY quantity ASC;

CREATE VIEW IF NOT EXISTS products_by_category AS
SELECT category, COUNT(*) as product_count, AVG(price) as avg_price, SUM(quantity) as total_stock
FROM products
WHERE is_active = true
GROUP BY category;

-- Grant permissions (if needed for application user)
-- CREATE USER IF NOT EXISTS 'product_user'@'localhost' IDENTIFIED BY 'product_password';
-- GRANT ALL PRIVILEGES ON product_db.* TO 'product_user'@'localhost';
-- FLUSH PRIVILEGES;

-- Useful Queries for Testing

-- 1. Find products in a specific category
-- SELECT * FROM products WHERE category = 'Electronics' AND is_active = true ORDER BY name;

-- 2. Find products by price range
-- SELECT * FROM products WHERE price BETWEEN 500 AND 1500 AND is_active = true ORDER BY price;

-- 3. Search by product name
-- SELECT * FROM products WHERE name LIKE '%laptop%' AND is_active = true;

-- 4. Get low stock products
-- SELECT * FROM low_stock_products;

-- 5. Get products grouped by category
-- SELECT * FROM products_by_category;

-- 6. Count products by category
-- SELECT category, COUNT(*) as count FROM products WHERE is_active = true GROUP BY category;

-- 7. Find most expensive products
-- SELECT * FROM products WHERE is_active = true ORDER BY price DESC LIMIT 10;

-- 8. Find best stocked products
-- SELECT * FROM products WHERE is_active = true ORDER BY quantity DESC LIMIT 10;

-- 9. Complex search (name OR description, specific category, price range)
-- SELECT * FROM products 
-- WHERE (name LIKE '%gaming%' OR description LIKE '%gaming%')
--   AND category = 'Electronics'
--   AND price BETWEEN 500 AND 2000
--   AND is_active = true;

-- 10. Product statistics
-- SELECT 
--   COUNT(*) as total_products,
--   COUNT(DISTINCT category) as total_categories,
--   MIN(price) as min_price,
--   MAX(price) as max_price,
--   AVG(price) as avg_price,
--   SUM(quantity) as total_stock
-- FROM products WHERE is_active = true;
