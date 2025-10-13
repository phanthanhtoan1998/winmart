-- Script tạo database và user cho Winmart
-- Chạy với user exam: docker exec -i postgres_container psql -U exam -d examdb < setup_winmart_db.sql

-- Tạo user winmart nếu chưa tồn tại
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'winmart') THEN
        CREATE USER winmart WITH PASSWORD 'winmart';
        RAISE NOTICE 'User winmart đã được tạo';
    ELSE
        RAISE NOTICE 'User winmart đã tồn tại';
    END IF;
END $$;

-- Tạo database winmart_db nếu chưa tồn tại
SELECT 'CREATE DATABASE winmart_db OWNER winmart ENCODING ''UTF8'''
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'winmart_db')\gexec

-- Cấp toàn bộ quyền trên database
GRANT ALL PRIVILEGES ON DATABASE winmart_db TO winmart;

\echo '=== Kết nối vào database winmart_db để cấp quyền schema ==='

-- Kết nối vào database winmart_db
\c winmart_db

-- Cấp toàn bộ quyền trên schema public
GRANT ALL ON SCHEMA public TO winmart;
GRANT CREATE ON SCHEMA public TO winmart;
ALTER SCHEMA public OWNER TO winmart;

-- Cấp quyền mặc định cho các object sẽ tạo sau này
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO winmart;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO winmart;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON FUNCTIONS TO winmart;

\echo ''
\echo '========================================='
\echo '✅ Setup hoàn thành!'
\echo '========================================='
\echo 'Database: winmart_db'
\echo 'User: winmart'
\echo 'Password: winmart'
\echo ''
\echo 'Bây giờ có thể restart ứng dụng Spring Boot!'
\echo '========================================='

