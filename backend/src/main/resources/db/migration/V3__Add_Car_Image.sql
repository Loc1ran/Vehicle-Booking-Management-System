ALTER TABLE car
ADD COLUMN car_images VARCHAR(36);

ALTER TABLE car
ADD CONSTRAINT car_image_unique UNIQUE(car_images);