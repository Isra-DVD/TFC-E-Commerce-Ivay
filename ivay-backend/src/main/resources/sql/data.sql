-- 2. Categories
INSERT INTO ecommerce2_categories (name) VALUES
('Electronics'),
('Books'),
('Clothing'),
('Home Appliances');

-- 3. Suppliers
INSERT INTO ecommerce2_suppliers (name, email, address, phone) VALUES
('TechGlobal Supplies', 'sales@techglobal.com', '123 Innovation Drive, Tech City', '555-0101'),
('Bookworm Distribution', 'orders@bookwormdist.com', '456 Library Lane, Readsville', '555-0102'),
('FashionForward Inc.', 'contact@fashionforward.com', '789 Style Street, Trendytown', '555-0103');

-- 5. Addresses
INSERT INTO ecommerce2_addresses (user_id, address) VALUES
(1, '10 Admin Avenue, Suite 100, Admin City'),
(2, '20 Consumer Close, User Town'),
(2, '21 Shopping Street, Apt 2B, User Town'), -- Bob's second address
(4, '40 Buyer Boulevard, Marketville');

-- 6. Products
INSERT INTO ecommerce2_products (name, description, price, stock, category_id, supplier_id, discount) VALUES
('Smart Laptop X1', 'Latest generation laptop with AI features.', 1499.99, 50, 1, 1, 0.05), -- 5% discount
('The Coding Manual', 'Comprehensive guide to modern programming.', 49.95, 200, 2, 2, 0.00),
('Wireless Ergonomic Mouse', 'Comfortable mouse for long hours.', 35.50, 150, 1, 1, 0.10), -- 10% discount
('Organic Cotton T-Shirt', 'Soft and sustainable plain t-shirt.', 24.99, 300, 3, 3, 0.00),
('Bluetooth Headphones', 'Noise-cancelling over-ear headphones.', 199.00, 80, 1, 1, 0.00),
('Classic Science Fiction', 'A collection of short stories.', 19.95, 120, 2, 2, 0.00),
('Smart Coffee Maker', 'Wi-Fi enabled coffee maker.', 89.99, 60, 4, NULL, 0.15); -- 15% discount, No specific supplier listed

-- 7. Orders
INSERT INTO ecommerce2_orders (user_id, bill_date, payment_method, global_discount, total_amount, total_amount_discounted, tax) VALUES
(2, '2023-10-26 10:30:00', 'Credit Card', 0.00, 1535.49, 1453.24, 95), -- Bob's order (Laptop + Mouse)
(4, '2023-10-27 14:00:00', 'PayPal', 0.05, 223.99, 212.79, 15), -- David's order (Headphones + T-Shirt), 5% global discount
(2, '2023-10-28 09:15:00', 'Credit Card', 0.00, 69.90, 69.90, 8); -- Bob's second order (Books)

-- 8. Order Items
-- Order 1 (ID: 1) - Bob - Laptop (ID: 1) and Mouse (ID: 3)
-- Laptop: 1 * 1499.99 * (1 - 0.05) = 1424.99
-- Mouse:  1 *   35.50 * (1 - 0.10) =   31.95
-- Order Total Pre-Tax/Global Discount: 1424.99 + 31.95 = 1456.94 (Note: This calculation slightly differs from the simplified `total_amount` in the order table, reflecting item discounts being applied first)
INSERT INTO ecommerce2_order_items (order_id, product_id, quantity, discount, price, total_price) VALUES
(1, 1, 1, 0.05, 1499.99, 1424.99), -- Laptop item, using product's discount
(1, 3, 1, 0.10, 35.50, 31.95);    -- Mouse item, using product's discount

-- Order 2 (ID: 2) - David - Headphones (ID: 5) and T-Shirt (ID: 4)
-- Headphones: 1 * 199.00 * (1 - 0.00) = 199.00
-- T-Shirt:    1 *  24.99 * (1 - 0.00) =  24.99
-- Order Total Pre-Tax/Global Discount: 199.00 + 24.99 = 223.99
INSERT INTO ecommerce2_order_items (order_id, product_id, quantity, discount, price, total_price) VALUES
(2, 5, 1, 0.00, 199.00, 199.00), -- Headphones item
(2, 4, 1, 0.00, 24.99, 24.99);    -- T-Shirt item

-- Order 3 (ID: 3) - Bob - Coding Manual (ID: 2) and SciFi Book (ID: 6)
-- Coding Manual: 1 * 49.95 * (1 - 0.00) = 49.95
-- SciFi Book:    1 * 19.95 * (1 - 0.00) = 19.95
-- Order Total Pre-Tax/Global Discount: 49.95 + 19.95 = 69.90
INSERT INTO ecommerce2_order_items (order_id, product_id, quantity, discount, price, total_price) VALUES
(3, 2, 1, 0.00, 49.95, 49.95),
(3, 6, 1, 0.00, 19.95, 19.95);

-- 9. Cart Items (Represents current state, less likely to be pre-populated extensively)
-- User Bob (ID: 2) has a Coffee Maker (ID: 7) and another T-Shirt (ID: 4) in his cart
INSERT INTO ecommerce2_cart_items (user_id, product_id, quantity) VALUES
(2, 7, 1),
(2, 4, 2);
-- User David (ID: 4) has a Laptop (ID: 1) in his cart
INSERT INTO ecommerce2_cart_items (user_id, product_id, quantity) VALUES
(4, 1, 1);