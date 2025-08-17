CREATE TABLE booking(
    id UUID PRIMARY KEY,
    car_id TEXT NOT NULL,
    user_id UUID NOT NULL
);