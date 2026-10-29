# Reproduce local vehicle API measurements

Requirements: Java, Maven, PostgreSQL, and k6. Use an isolated database named `vehicle_resume_bench`. Bind the app/database to loopback and keep S3 mock mode enabled. This measures authenticated reads of 100 seeded vehicles; it does not measure booking writes, image delivery, AWS capacity, or multiple machines.

1. Set the datasource URL/username/password to that empty database; set `SPRING_PROFILES_ACTIVE=benchmark`, `AWS_S3_MOCK=true`, `SPRING_JPA_SHOW_SQL=false`, and `SERVER_PORT=18080`. Build/start the backend with a fixed JVM heap, for example `-Xms256m -Xmx1024m`. Its four Flyway migrations create the schema.
2. Execute `psql -v ON_ERROR_STOP=1 -d vehicle_resume_bench -f load-tests/seed.sql`, with the matching local host/port/credentials. Seeding refuses other databases and nonempty car tables.
3. Warm up with `k6 run -e BASE_URL=http://127.0.0.1:18080 -e VUS=10 -e DURATION=15s -e SUMMARY_PATH=warmup.json load-tests/load.js`.
4. Measure with `k6 run -e BASE_URL=http://127.0.0.1:18080 -e VUS=50 -e DURATION=30s -e SUMMARY_PATH=run-1.json load-tests/load.js`. Repeat three times using distinct summary filenames. Increase `VUS` only when deliberately measuring a higher load, and report unsuccessful runs too.

Use the metrics tagged `phase:measure` to exclude login and setup. Each response must return HTTP 200 and exactly 100 records. The run passes only with zero HTTP errors, all response checks passing, and p95 below 500 ms.

Save each JSON result alongside the source commit, dataset, JVM/PostgreSQL versions, heap settings, CPU/RAM, duration, and user count. Each virtual user pauses for 100 ms between requests. Because the client and server share one computer, these results describe local performance rather than AWS capacity.
