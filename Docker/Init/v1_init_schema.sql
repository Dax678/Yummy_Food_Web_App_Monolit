-- =========================================
-- YUMMY FOOD – v1_init_schema.sql (PostgreSQL)
-- =========================================

-- Przydatne rozszerzenia
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- CREATE SCHEMA IF NOT EXISTS yummyfood;
-- SET search_path TO yummyfood, public;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_role') THEN
            CREATE TYPE user_role AS ENUM ('USER','ADMIN','RESTAURANT');
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'order_status') THEN
            CREATE TYPE order_status AS ENUM ('NEW','IN_PROGRESS','READY','OUT_FOR_DELIVERY','DELIVERED','CANCELLED');
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'payment_method') THEN
            CREATE TYPE payment_method AS ENUM ('CARD','BLIK','TRANSFER','CASH_ON_DELIVERY');
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'payment_status') THEN
            CREATE TYPE payment_status AS ENUM ('PENDING','COMPLETED','FAILED','CANCELLED','REFUNDED');
        END IF;
    END $$;

-- Table

-- USERS
CREATE TABLE IF NOT EXISTS users (
                                     id             uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
                                     username       varchar(50)  NOT NULL UNIQUE,
                                     email          varchar(255) NOT NULL UNIQUE,
                                     password_hash  varchar(255) NOT NULL,
                                     full_name      varchar(120),
                                     phone          varchar(32),
                                     role           user_role NOT NULL DEFAULT 'USER',
                                     created_at     timestamptz NOT NULL DEFAULT now(),
                                     updated_at     timestamptz NOT NULL DEFAULT now()
);

-- RESTAURANTS
CREATE TABLE IF NOT EXISTS restaurants (
                                           id          uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
                                           name        varchar(150) NOT NULL,
                                           description text,
                                           phone       varchar(32),
                                           email       varchar(255),
                                           is_active   boolean NOT NULL DEFAULT true,
                                           avg_rating  numeric(3,2) NOT NULL DEFAULT 0.00 CHECK (avg_rating BETWEEN 0 AND 5),
                                           created_at  timestamptz NOT NULL DEFAULT now(),
                                           updated_at  timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_restaurants_active ON restaurants(is_active);

-- MENU_ITEMS
CREATE TABLE IF NOT EXISTS menu_items (
                                          id            uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
                                          restaurant_id uuid NOT NULL REFERENCES restaurants(id) ON DELETE CASCADE,
                                          name          varchar(150) NOT NULL,
                                          description   text,
                                          price         numeric(10,2) NOT NULL CHECK (price >= 0),
                                          is_available  boolean NOT NULL DEFAULT true,
                                          image_url     varchar(500),
                                          created_at    timestamptz NOT NULL DEFAULT now(),
                                          updated_at    timestamptz NOT NULL DEFAULT now(),
                                          UNIQUE (restaurant_id, name)
);
CREATE INDEX IF NOT EXISTS idx_menu_items_restaurant ON menu_items(restaurant_id);

-- ORDERS
CREATE TABLE IF NOT EXISTS orders (
                                      id             uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
                                      user_id        uuid NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
                                      restaurant_id  uuid NOT NULL REFERENCES restaurants(id) ON DELETE RESTRICT,
                                      status         order_status NOT NULL DEFAULT 'NEW',
                                      items_total    numeric(10,2) NOT NULL DEFAULT 0 CHECK (items_total >= 0),
                                      delivery_fee   numeric(10,2) NOT NULL DEFAULT 0 CHECK (delivery_fee >= 0),
                                      grand_total    numeric(10,2) NOT NULL DEFAULT 0 CHECK (grand_total >= 0),
                                      notes          text,
                                      placed_at      timestamptz NOT NULL DEFAULT now(),
                                      updated_at     timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_orders_user        ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_restaurant  ON orders(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_orders_status      ON orders(status);

-- ORDER_ITEMS
CREATE TABLE IF NOT EXISTS order_items (
                                           id             uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
                                           order_id       uuid NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
                                           menu_item_id   uuid NOT NULL REFERENCES menu_items(id) ON DELETE RESTRICT,
                                           name_snapshot  varchar(150) NOT NULL,
                                           unit_price     numeric(10,2) NOT NULL CHECK (unit_price >= 0),
                                           quantity       int NOT NULL CHECK (quantity > 0),
                                           line_total     numeric(10,2) NOT NULL CHECK (line_total >= 0)
);
CREATE INDEX IF NOT EXISTS idx_order_items_order ON order_items(order_id);

-- PAYMENTS
CREATE TABLE IF NOT EXISTS payments (
                                        id            uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
                                        order_id      uuid NOT NULL UNIQUE REFERENCES orders(id) ON DELETE CASCADE,
                                        method        payment_method NOT NULL,
                                        status        payment_status NOT NULL DEFAULT 'PENDING',
                                        amount        numeric(10,2) NOT NULL CHECK (amount >= 0),
                                        provider      varchar(80),
                                        provider_ref  varchar(120),
                                        created_at    timestamptz NOT NULL DEFAULT now(),
                                        updated_at    timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_payments_status ON payments(status);

-- REVIEWS
CREATE TABLE IF NOT EXISTS reviews (
                                       id            uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
                                       user_id       uuid NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                       restaurant_id uuid REFERENCES restaurants(id) ON DELETE CASCADE,
                                       menu_item_id  uuid REFERENCES menu_items(id) ON DELETE CASCADE,
                                       rating        int NOT NULL CHECK (rating BETWEEN 1 AND 5),
                                       comment       text,
                                       created_at    timestamptz NOT NULL DEFAULT now(),
    -- dokładnie jedna z kolumn (restaurant_id, menu_item_id) musi być nie-NULL
                                       CONSTRAINT chk_review_target CHECK (
                                           (restaurant_id IS NOT NULL AND menu_item_id IS NULL)
                                               OR (restaurant_id IS NULL AND menu_item_id IS NOT NULL)
                                           )
);
CREATE INDEX IF NOT EXISTS idx_reviews_user       ON reviews(user_id);
CREATE INDEX IF NOT EXISTS idx_reviews_restaurant ON reviews(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_reviews_item       ON reviews(menu_item_id);

-- 5) Trigger do auto-aktualizacji updated_at
CREATE OR REPLACE FUNCTION set_updated_at()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END; $$ LANGUAGE plpgsql;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'trg_users_updated') THEN
            CREATE TRIGGER trg_users_updated
                BEFORE UPDATE ON users
                FOR EACH ROW EXECUTE FUNCTION set_updated_at();
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'trg_restaurants_updated') THEN
            CREATE TRIGGER trg_restaurants_updated
                BEFORE UPDATE ON restaurants
                FOR EACH ROW EXECUTE FUNCTION set_updated_at();
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'trg_menu_items_updated') THEN
            CREATE TRIGGER trg_menu_items_updated
                BEFORE UPDATE ON menu_items
                FOR EACH ROW EXECUTE FUNCTION set_updated_at();
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'trg_orders_updated') THEN
            CREATE TRIGGER trg_orders_updated
                BEFORE UPDATE ON orders
                FOR EACH ROW EXECUTE FUNCTION set_updated_at();
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'trg_payments_updated') THEN
            CREATE TRIGGER trg_payments_updated
                BEFORE UPDATE ON payments
                FOR EACH ROW EXECUTE FUNCTION set_updated_at();
        END IF;
    END $$;

