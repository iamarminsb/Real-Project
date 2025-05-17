INSERT INTO users (username, password, role)
VALUES
    ('admin', '1234', 'DOCTOR'),
    ('nurse', '4321', 'NURSE')
    ON CONFLICT DO NOTHING;

-- تنظیم sequence برای جلوگیری از تداخل در مقدار id بعدی
SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT COALESCE(MAX(id), 1) FROM users));

COMMIT;