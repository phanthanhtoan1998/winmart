# Hướng Dẫn Setup Database

## ⚠️ Lỗi: relation "users" does not exist

Lỗi này xảy ra vì database `winmart_db` chưa được tạo hoặc các bảng chưa được khởi tạo.

## 🔧 Cách 1: Tạo Database Thủ Công (Đơn giản nhất)

### Bước 1: Kết nối vào PostgreSQL container
```bash
docker exec -it postgres_container psql -U exam
```

### Bước 2: Chạy các lệnh SQL sau trong psql:
```sql
-- Tạo user winmart
CREATE USER winmart WITH PASSWORD 'winmart';

-- Tạo database
CREATE DATABASE winmart_db OWNER winmart;

-- Cấp quyền
GRANT ALL PRIVILEGES ON DATABASE winmart_db TO winmart;

-- Kết nối vào database mới
\c winmart_db

-- Cấp quyền trên schema public
GRANT CREATE ON SCHEMA public TO winmart;
GRANT ALL ON SCHEMA public TO winmart;

-- Thoát
\q
```

### Bước 3: Restart ứng dụng Spring Boot
Sau khi tạo database, restart ứng dụng. Hibernate sẽ tự động tạo các bảng nhờ cấu hình:
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # Tự động tạo/cập nhật bảng
```

---

## 🔧 Cách 2: Chạy Script SQL

### Chạy lệnh này từ terminal:
```bash
cd /home/toan/Documents/winmart
docker exec -i postgres_container psql -U exam < create_database.sql
```

---

## 🔧 Cách 3: Tạo Schema Thủ Công (Nếu Hibernate không tự tạo)

Nếu Hibernate vẫn không tự động tạo bảng, chạy file `create_schema.sql`:

```bash
docker exec -i postgres_container psql -U winmart -d winmart_db < create_schema.sql
```

---

## ✅ Kiểm Tra Database Đã Được Tạo

```bash
# Kết nối vào PostgreSQL
docker exec -it postgres_container psql -U exam

# Liệt kê tất cả databases
\l

# Kết nối vào database winmart_db
\c winmart_db

# Xem danh sách bảng
\dt

# Xem chi tiết bảng users
\d users

# Thoát
\q
```

---

## 🚀 Sau Khi Setup Database

1. **Restart ứng dụng Spring Boot**
2. **Kiểm tra logs** - bạn sẽ thấy Hibernate tạo các bảng:
   ```
   Hibernate: create table users (...)
   Hibernate: create table products (...)
   ...
   ```
3. **Test API** bằng Postman

---

## 📝 Lưu Ý

- Cấu hình trong `application.yml`:
  - `ddl-auto: update` - Hibernate sẽ tự động tạo/cập nhật schema
  - Database: `winmart_db`
  - Username: `winmart`
  - Password: `winmart`

- Nếu bạn muốn **xóa và tạo lại** database mỗi lần chạy:
  ```yaml
  ddl-auto: create-drop  # Cảnh báo: sẽ XÓA dữ liệu mỗi lần restart!
  ```

- Nếu bạn muốn **giữ nguyên** schema (không tạo/xóa gì):
  ```yaml
  ddl-auto: none  # Phải tạo schema thủ công
  ```

