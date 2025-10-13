-- Script tạo database winmart_db với user exam (đã có sẵn trong Docker)
-- Chạy: docker exec -i postgres_container psql -U exam -d examdb < create_winmart_database.sql

-- Tạo database winmart_db nếu chưa tồn tại
SELECT 'CREATE DATABASE winmart_db OWNER exam ENCODING ''UTF8'''
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'winmart_db')\gexec

-- Kết nối vào database winmart_db
\c winmart_db

-- Cấp toàn bộ quyền cho user exam
GRANT ALL ON SCHEMA public TO exam;
GRANT CREATE ON SCHEMA public TO exam;

-- Cấp quyền mặc định cho các bảng sẽ được tạo
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO exam;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO exam;

\echo ''
\echo '========================================='
\echo '✅ Database winmart_db đã được tạo!'
\echo '========================================='
\echo 'Database: winmart_db'
\echo 'User: exam'
\echo 'Password: exam123'
\echo ''
\echo 'Bây giờ restart ứng dụng Spring Boot!'
\echo 'Hibernate sẽ tự động tạo các bảng.'
\echo '========================================='

