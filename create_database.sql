-- Script tạo database và user cho Winmart

-- Kết nối vào PostgreSQL với user exam
-- Chạy lệnh: docker exec -i postgres_container psql -U exam < create_database.sql

-- 1. Tạo user winmart nếu chưa tồn tại
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_user WHERE usename = 'winmart') THEN
        CREATE USER winmart WITH PASSWORD 'winmart';
    END IF;
END
$$;

-- 2. Tạo database winmart_db nếu chưa tồn tại
SELECT 'CREATE DATABASE winmart_db OWNER winmart'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'winmart_db')\gexec

-- 3. Cấp quyền cho user winmart
GRANT ALL PRIVILEGES ON DATABASE winmart_db TO winmart;

-- Kết nối vào database winmart_db để cấp thêm quyền
\c winmart_db

-- Cấp quyền tạo schema
GRANT CREATE ON SCHEMA public TO winmart;
GRANT ALL ON SCHEMA public TO winmart;

-- Cấp quyền trên tất cả bảng trong schema public
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO winmart;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO winmart;

-- Thông báo hoàn thành
\echo 'Database winmart_db và user winmart đã được tạo thành công!'

