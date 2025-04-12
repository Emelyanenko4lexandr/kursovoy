--create-start-users-credentials

INSERT INTO credentials (username, password)
VALUES ('admin', '$2a$10$LRX/rXnMwhacv8KJBIEpeemYeji.m8PJfmA45O0aAwGPDtIE3r5hi');

INSERT INTO users (credentials_id, fio, role, enable, balance)
SELECT id, 'Admin Users', 'ADMIN', true, 1000.10
FROM credentials
WHERE username = 'admin';