# 🚀 Quick Start - Login & Register

## Cách nhanh nhất để test Login API

### 1️⃣ Khởi động server
```bash
cd winmart-service
./mvnw spring-boot:run
```

Server sẽ chạy tại: `http://localhost:3333`

### 2️⃣ Test bằng cURL

**Đăng ký:**
```bash
curl -X POST http://localhost:3333/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Đăng nhập:**
```bash
curl -X POST http://localhost:3333/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### 3️⃣ Test bằng Postman
Import file: `Winmart_Auth_API.postman_collection.json`

## 📊 Response Format

**Success:**
```json
{
  "code": "00",
  "message": "Login successful",
  "data": {
    "userId": 1,
    "fullName": "Test User",
    "email": "test@example.com",
    "phone": "0123456789",
    "role": "CUSTOMER",
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400
  }
}
```

**Error:**
```json
{
  "code": "01",
  "message": "Invalid email or password"
}
```

## 🔐 User Roles
- **CUSTOMER** - Khách hàng (mặc định)
- **EMPLOYEE** - Nhân viên
- **ADMIN** - Quản trị viên

## 📚 Tài liệu chi tiết
Xem file `LOGIN_API_GUIDE.md` để biết thêm chi tiết!

## 🔑 Sử dụng JWT Token

**Gửi request với token:**
```bash
curl -X GET http://localhost:3333/api/users \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

**Token Details:**
- `accessToken`: Dùng để authenticate API requests (expires in 24h)
- `refreshToken`: Dùng để lấy token mới khi access token hết hạn (expires in 7 days)
- `tokenType`: "Bearer" - standard OAuth 2.0 token type
- `expiresIn`: Thời gian token còn hiệu lực (seconds)

## ⚠️ Lưu ý
- Password tối thiểu 6 ký tự
- Email phải hợp lệ và unique
- Tất cả password đều được mã hóa bằng BCrypt
- Endpoints `/login` và `/register` là public (không cần authentication)
- JWT token được mã hóa bằng HS256 algorithm
- Secret key được config trong application.yml

