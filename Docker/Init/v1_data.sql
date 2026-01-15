-- =========================================
-- YUMMY FOOD – przykładowe dane testowe
-- =========================================

SET search_path TO yummyfood, public;

-- 👤 USERS
INSERT INTO users (id, username, email, password_hash, full_name, phone, role)
VALUES
    (gen_random_uuid(), 'admin', 'admin@yummy.com', '{bcrypt}$2a$10$hashadmin', 'System Admin', '500000000', 'ADMIN'),
    (gen_random_uuid(), 'john_doe', 'john@yummy.com', '{bcrypt}$2a$10$hashjohn', 'John Doe', '501123456', 'USER'),
    (gen_random_uuid(), 'mary_chef', 'mary@pizzahub.com', '{bcrypt}$2a$10$hashchef', 'Mary Chef', '502987654', 'RESTAURANT');

-- 🍕 RESTAURANTS
INSERT INTO restaurants (id, name, description, phone, email, is_active, avg_rating)
VALUES
    (gen_random_uuid(), 'Pizza Hub', 'Najlepsza pizza w mieście, wypiekana w piecu opalanym drewnem.', '602111222', 'contact@pizzahub.com', true, 4.8),
    (gen_random_uuid(), 'Sushi Zen', 'Tradycyjne sushi i ramen w nowoczesnym wydaniu.', '603333444', 'info@sushizen.com', true, 4.5),
    (gen_random_uuid(), 'Burger Point', 'Soczyste burgery i domowe frytki.', '604555666', 'hello@burgerpoint.com', true, 4.3);

-- 🍽 MENU ITEMS
-- Pizza Hub
INSERT INTO menu_items (id, restaurant_id, name, description, price, is_available, image_url)
SELECT gen_random_uuid(), r.id, m.name, m.description, m.price, true, m.image_url
FROM restaurants r
         JOIN (VALUES
                   ('Margherita', 'Klasyczna pizza z mozzarellą, pomidorami i bazylią.', 28.50, 'https://picsum.photos/200?pizza1'),
                   ('Pepperoni', 'Ostra pizza z pepperoni i papryczkami jalapeño.', 33.90, 'https://picsum.photos/200?pizza2'),
                   ('Funghi', 'Z grzybami i kremowym sosem.', 31.20, 'https://picsum.photos/200?pizza3')
) AS m(name, description, price, image_url)
              ON r.name = 'Pizza Hub';

-- Sushi Zen
INSERT INTO menu_items (id, restaurant_id, name, description, price, is_available, image_url)
SELECT gen_random_uuid(), r.id, m.name, m.description, m.price, true, m.image_url
FROM restaurants r
         JOIN (VALUES
                   ('Sushi Set Classic', 'Zestaw 12 sztuk sushi: łosoś, tuńczyk, awokado.', 45.90, 'https://picsum.photos/200?sushi1'),
                   ('Ramen Tonkotsu', 'Ramen z bulionem wieprzowym i makaronem pszenicznym.', 39.50, 'https://picsum.photos/200?sushi2'),
                   ('Sashimi Mix', 'Zestaw sashimi: łosoś, tuńczyk, okoń morski.', 49.90, 'https://picsum.photos/200?sushi3')
) AS m(name, description, price, image_url)
              ON r.name = 'Sushi Zen';

-- Burger Point
INSERT INTO menu_items (id, restaurant_id, name, description, price, is_available, image_url)
SELECT gen_random_uuid(), r.id, m.name, m.description, m.price, true, m.image_url
FROM restaurants r
         JOIN (VALUES
                   ('Classic Burger', 'Wołowina 150g, ser cheddar, pomidor, sos BBQ.', 29.00, 'https://picsum.photos/200?burger1'),
                   ('Double Bacon', 'Podwójne mięso, bekon i sos serowy.', 36.50, 'https://picsum.photos/200?burger2'),
                   ('Veggie Burger', 'Kotlet warzywny, rukola, sos ziołowy.', 27.00, 'https://picsum.photos/200?burger3')
) AS m(name, description, price, image_url)
              ON r.name = 'Burger Point';

-- 🧾 ORDERS
INSERT INTO orders (id, user_id, restaurant_id, status, items_total, delivery_fee, grand_total, notes)
SELECT
    gen_random_uuid(),
    u.id,
    r.id,
    'DELIVERED',
    58.40,
    5.00,
    63.40,
    'Dodatkowy sos pomidorowy, proszę!'
FROM users u, restaurants r
WHERE u.username = 'john_doe' AND r.name = 'Pizza Hub';

-- ORDER_ITEMS (dla zamówienia Johna)
INSERT INTO order_items (id, order_id, menu_item_id, name_snapshot, unit_price, quantity, line_total)
SELECT
    gen_random_uuid(),
    o.id,
    m.id,
    m.name,
    m.price,
    2,
    (m.price * 2)
FROM orders o
         JOIN restaurants r ON o.restaurant_id = r.id
         JOIN menu_items m ON m.restaurant_id = r.id
WHERE o.notes LIKE '%sos pomidorowy%' AND m.name = 'Margherita';

-- 💳 PAYMENTS
INSERT INTO payments (id, order_id, method, status, amount, provider, provider_ref)
SELECT
    gen_random_uuid(),
    o.id,
    'CARD',
    'COMPLETED',
    o.grand_total,
    'Stripe',
    'STRP-123456'
FROM orders o
WHERE o.status = 'DELIVERED';

-- ⭐ REVIEWS
INSERT INTO reviews (id, user_id, restaurant_id, rating, comment)
SELECT gen_random_uuid(), u.id, r.id, 5, 'Świetna pizza, szybka dostawa!'
FROM users u, restaurants r
WHERE u.username = 'john_doe' AND r.name = 'Pizza Hub';

INSERT INTO reviews (id, user_id, restaurant_id, rating, comment)
SELECT gen_random_uuid(), u.id, r.id, 4, 'Dobre sushi, choć porcja mogłaby być większa.'
FROM users u, restaurants r
WHERE u.username = 'john_doe' AND r.name = 'Sushi Zen';
