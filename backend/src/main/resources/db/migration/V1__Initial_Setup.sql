CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE TABLE booking(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    car_id TEXT NOT NULL,
    user_id UUID NOT NULL
);

CREATE TABLE car(
                    reg_number TEXT PRIMARY KEY,
                    rental_price_per_day numeric(10, 2) NOT NULL,
                    brand TEXT NOT NULL ,
                    is_electric BOOLEAN NOT NULL
);

CREATE TABLE user_info(
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          name TEXT NOT NULL,
                          password TEXT NOT NULL

);

