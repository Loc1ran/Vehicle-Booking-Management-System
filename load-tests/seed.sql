DO $$ BEGIN
    IF current_database() <> 'vehicle_resume_bench' THEN
        RAISE EXCEPTION 'Refusing to seed a non-benchmark database';
    END IF;
    IF EXISTS (SELECT 1 FROM car) THEN
        RAISE EXCEPTION 'Benchmark requires an empty car table';
    END IF;
END $$;
INSERT INTO car(reg_number, rental_price_per_day, brand, is_electric)
SELECT 'BENCH-' || lpad(n::text, 3, '0'), 50 + n, 'TESLA', true
FROM generate_series(1, 100) n;
ANALYZE car;
