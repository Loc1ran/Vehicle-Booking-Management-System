CREATE TABLE customer(
    regNumber TEXT PRIMARY KEY,
    rentalPricePerDay numeric(10, 2) NOT NULL,
    brand TEXT NOT NULL ,
    is_electric BOOLEAN NOT NULL
);



