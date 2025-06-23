ALTER TABLE booking
ADD CONSTRAINT booking_id_unique UNIQUE (id),
ADD CONSTRAINT car_id_unique UNIQUE (car_id),
ADD CONSTRAINT user_id_unique UNIQUE (user_id);
