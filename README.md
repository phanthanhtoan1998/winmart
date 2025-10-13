# Winmart - Hệ Thống Quản Lý Siêu Thị

## 📋 Thông Tin Cấu Hình

### Database PostgreSQL
- **Database**: `winmart_db`
- **Username**: `exam`
- **Password**: `exam123`
- **Host**: `localhost`
- **Port**: `5432`

### Application
- **Port**: `3333`
- **Base URL**: `http://localhost:3333`

---

## 🚀 Hướng Dẫn Chạy Ứng Dụng

### Bước 1: Kiểm tra PostgreSQL Container
```bash
docker ps | grep postgres
```

Nếu container chưa chạy, start nó lên:
```bash
docker start postgres_container
```

### Bước 2: Database đã được tạo ✅
Database `winmart_db` đã được setup với user `exam`.

### Bước 3: Build & Run Application
```bash
# Build project
cd /home/toan/Documents/winmart
mvn clean install

# Chạy ứng dụng
cd winmart-service
mvn spring-boot:run
```

### Bước 4: Kiểm tra Application đã chạy
- Mở browser: `http://localhost:3333`
- Hoặc check logs trong terminal

---

## 📚 API Documentation

### Import Postman Collection
1. Mở Postman
2. Import 2 files:
   - `Winmart_API_Collection.postman_collection.json`
   - `Winmart_Environment.postman_environment.json`
3. Chọn environment "Winmart Local Environment"

### Đọc Hướng Dẫn Chi Tiết
- **POSTMAN_GUIDE.md** - Hướng dẫn sử dụng API
- **SAMPLE_DATA.md** - Dữ liệu mẫu để test

---

## 🗂️ Cấu Trúc Project

```
winmart/
├── winmart-common/          # Module common (entities, utils, config)
├── winmart-service/         # Module service (controllers, services)
├── pom.xml                  # Parent POM
├── Winmart_API_Collection.postman_collection.json
├── Winmart_Environment.postman_environment.json
├── POSTMAN_GUIDE.md
├── SAMPLE_DATA.md
└── README.md
```

---

## 🔧 Cấu Hình Quan Trọng

### application.yml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/winmart_db
    username: exam
    password: exam123
  jpa:
    hibernate:
      ddl-auto: update  # Tự động tạo/cập nhật bảng
```

### Security
- **Trạng thái**: Đã tắt authentication (mọi API đều public)
- **Lý do**: Để dễ dàng test và phát triển
- File: `winmart-common/.../SecurityConfig.java`

---

## 📊 Database Schema

Hibernate sẽ tự động tạo các bảng sau khi bạn chạy ứng dụng:
- `users` - Người dùng
- `categories` - Danh mục sản phẩm
- `products` - Sản phẩm
- `invoices` - Hóa đơn
- `invoice_items` - Chi tiết hóa đơn
- `payments` - Thanh toán
- `promotions` - Khuyến mãi
- `inventory_logs` - Nhật ký kho

---

## 🧪 Test API

### Thứ tự test đề xuất:

1. **POST** `/api/auth/register` - Đăng ký user
2. **POST** `/api/auth/login` - Đăng nhập
3. **POST** `/api/categories/search` - Xem danh mục
4. **POST** `/api/products/search` - Xem sản phẩm
5. **POST** `/api/users/search` - Xem users

### Sample Request - Register User
```json
{
  "fullName": "Nguyen Van A",
  "email": "test@winmart.com",
  "phone": "0901234567",
  "password": "password123",
  "address": "123 Nguyen Hue, Q1, HCM",
  "role": "CUSTOMER"
}
```

---

## ❓ Troubleshooting

### Lỗi: relation "users" does not exist
✅ **Đã fix** - Database đã được tạo và user có đủ quyền

### Lỗi: password authentication failed
- Kiểm tra `application.yml` - username: `exam`, password: `exam123`
- Kiểm tra PostgreSQL container đang chạy

### Lỗi: Connection refused
- Kiểm tra PostgreSQL container: `docker ps`
- Kiểm tra port 5432 đã mở chưa

### Application không start
```bash
# Check logs
mvn spring-boot:run

# Hoặc check target/logs nếu có
```

---

## 📝 Notes

- **Hibernate DDL Mode**: `update` - Tự động tạo/cập nhật schema
- **Security**: Tất cả endpoints đều public (không cần authentication)
- **Port**: 3333 (có thể thay đổi trong application.yml)

---

## 👨‍💻 Developer

- **Project**: Winmart Management System
- **Tech Stack**: Spring Boot, PostgreSQL, JPA/Hibernate
- **Date**: October 2025

