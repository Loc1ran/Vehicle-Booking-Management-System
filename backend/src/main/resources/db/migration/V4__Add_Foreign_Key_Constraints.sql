ALTER TABLE booking
ADD CONSTRAINT fk_booking_car FOREIGN KEY (car_id) REFERENCES car (reg_number) ON DELETE CASCADE;

ALTER TABLE booking
ADD CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES user_info (id) ON DELETE CASCADE;