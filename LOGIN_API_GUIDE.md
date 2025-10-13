# Hướng dẫn sử dụng API Login - Winmart

## Tổng quan
API Login và Register đã được tích hợp vào hệ thống Winmart với các tính năng:
- ✅ Đăng ký người dùng mới
- ✅ Đăng nhập với email và password
- ✅ Mã hóa mật khẩu bằng BCrypt
- ✅ Validation dữ liệu đầu vào
- ✅ Public endpoints (không cần authentication)
- ✅ Logging cho security tracking

## Endpoints

### 1. Đăng ký người dùng mới (Register)

**Endpoint:** `POST /api/users/register`

**Public:** ✅ Không cần authentication

**Request Body:**
```json
{
  "fullName": "Nguyễn Văn A",
  "email": "nguyenvana@example.com",
  "phone": "0123456789",
  "password": "password123",
  "address": "123 Đường ABC, Quận 1, TP.HCM",
  "role": "CUSTOMER"
}
```

**Validation Rules:**
- `fullName`: Bắt buộc, tối đa 100 ký tự
- `email`: Bắt buộc, định dạng email hợp lệ, tối đa 100 ký tự
- `phone`: Không bắt buộc, chỉ chấp nhận số và ký tự đặc biệt (+, -, space, ()), tối đa 20 ký tự
- `password`: Bắt buộc, tối thiểu 6 ký tự, tối đa 255 ký tự
- `address`: Không bắt buộc
- `role`: Không bắt buộc (mặc định là CUSTOMER), các giá trị: CUSTOMER, EMPLOYEE, ADMIN

**Success Response (200 OK):**
```json
{
  "code": "00",
  "message": "Registration successful",
  "data": {
    "id": 1,
    "fullName": "Nguyễn Văn A",
    "email": "nguyenvana@example.com",
    "phone": "0123456789",
    "address": "123 Đường ABC, Quận 1, TP.HCM",
    "role": "CUSTOMER",
    "createdAt": "2025-10-13T10:30:00"
  }
}
```

**Error Response (400 Bad Request):**
```json
{
  "code": "01",
  "message": "Registration failed"
}
```

**Lỗi phổ biến:**
- Email đã tồn tại trong hệ thống
- Validation lỗi (email không hợp lệ, password quá ngắn, etc.)

---

### 2. Đăng nhập (Login)

**Endpoint:** `POST /api/users/login`

**Public:** ✅ Không cần authentication

**Request Body:**
```json
{
  "email": "nguyenvana@example.com",
  "password": "password123"
}
```

**Validation Rules:**
- `email`: Bắt buộc, định dạng email hợp lệ
- `password`: Bắt buộc, tối thiểu 6 ký tự

**Success Response (200 OK):**
```json
{
  "code": "00",
  "message": "Login successful",
  "data": {
    "userId": 1,
    "fullName": "Nguyễn Văn A",
    "email": "nguyenvana@example.com",
    "phone": "0123456789",
    "role": "CUSTOMER",
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImVtYWlsIjoibmd1eWVudmFuYUBleGFtcGxlLmNvbSIsInJvbGUiOiJDVVNUT01FUiIsInN1YiI6Im5ndXllbnZhbmFAZXhhbXBsZS5jb20iLCJpYXQiOjE2OTcxNjcyMDAsImV4cCI6MTY5NzI1MzYwMH0.abc123...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImVtYWlsIjoibmd1eWVudmFuYUBleGFtcGxlLmNvbSIsInR5cGUiOiJyZWZyZXNoIiwic3ViIjoibmd1eWVudmFuYUBleGFtcGxlLmNvbSIsImlhdCI6MTY5NzE2NzIwMCwiZXhwIjoxNjk3NzcyMDAwfQ.xyz789...",
    "tokenType": "Bearer",
    "expiresIn": 86400
  }
}
```

**Response Fields:**
- `userId`: ID của user trong database
- `fullName`: Tên đầy đủ của user
- `email`: Email của user
- `phone`: Số điện thoại (có thể null)
- `role`: Vai trò của user (CUSTOMER/EMPLOYEE/ADMIN)
- `accessToken`: JWT access token để authenticate các API request (hết hạn sau 24h)
- `refreshToken`: JWT refresh token để lấy access token mới (hết hạn sau 7 ngày)
- `tokenType`: Loại token (luôn là "Bearer")
- `expiresIn`: Thời gian access token còn hiệu lực (tính bằng giây)

**Error Response (400 Bad Request):**
```json
{
  "code": "01",
  "message": "Invalid email or password"
}
```

**Lỗi phổ biến:**
- Email không tồn tại
- Password sai
- Validation lỗi

---

## Ví dụ sử dụng

### Sử dụng cURL

#### Đăng ký:
```bash
curl -X POST http://localhost:3333/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Nguyễn Văn A",
    "email": "nguyenvana@example.com",
    "phone": "0123456789",
    "password": "password123",
    "address": "123 Đường ABC, Quận 1, TP.HCM"
  }'
```

#### Đăng nhập:
```bash
curl -X POST http://localhost:3333/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "nguyenvana@example.com",
    "password": "password123"
  }'
```

### Sử dụng Postman

1. **Import collection:** Sử dụng file `Winmart_Full_API.postman_collection.json`

2. **Đăng ký:**
   - Method: POST
   - URL: `http://localhost:3333/api/users/register`
   - Headers: `Content-Type: application/json`
   - Body (raw JSON): Copy request body từ ví dụ trên

3. **Đăng nhập:**
   - Method: POST
   - URL: `http://localhost:3333/api/users/login`
   - Headers: `Content-Type: application/json`
   - Body (raw JSON): Copy request body từ ví dụ trên

### Sử dụng JavaScript/Fetch

#### Đăng ký:
```javascript
async function register() {
  try {
    const response = await fetch('http://localhost:3333/api/users/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        fullName: 'Nguyễn Văn A',
        email: 'nguyenvana@example.com',
        phone: '0123456789',
        password: 'password123',
        address: '123 Đường ABC, Quận 1, TP.HCM'
      })
    });
    
    const data = await response.json();
    
    if (response.ok && data.code === '00') {
      console.log('Registration successful:', data.data);
    } else {
      console.error('Registration failed:', data.message);
    }
  } catch (error) {
    console.error('Error:', error);
  }
}
```

#### Đăng nhập:
```javascript
async function login() {
  try {
    const response = await fetch('http://localhost:3333/api/users/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email: 'nguyenvana@example.com',
        password: 'password123'
      })
    });
    
    const data = await response.json();
    
    if (response.ok && data.code === '00') {
      console.log('Login successful:', data.data);
      // Lưu thông tin user vào localStorage hoặc state management
      localStorage.setItem('user', JSON.stringify(data.data));
    } else {
      console.error('Login failed:', data.message);
    }
  } catch (error) {
    console.error('Error:', error);
  }
}
```

---

## Security Features

### 1. JWT Token Authentication
- Sử dụng **JWT (JSON Web Token)** cho authentication
- Access token hết hạn sau **24 giờ**
- Refresh token hết hạn sau **7 ngày**
- Token được mã hóa bằng **HS256 algorithm**
- Token chứa thông tin: userId, email, role

**Cách sử dụng token:**
```bash
# Thêm token vào header Authorization
curl -X GET http://localhost:3333/api/users \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

**Cấu trúc JWT Token:**
```
Header.Payload.Signature
```
- **Header**: Chứa algorithm (HS256) và type (JWT)
- **Payload**: Chứa userId, email, role, expiration time
- **Signature**: Được tạo từ Header + Payload + Secret Key

### 2. Password Encryption
- Sử dụng **BCrypt** để mã hóa mật khẩu
- Password không bao giờ được lưu dưới dạng plain text
- Mỗi password có một salt riêng biệt

### 3. Validation
- Email phải có định dạng hợp lệ
- Password tối thiểu 6 ký tự
- Tất cả trường bắt buộc đều được validate

### 4. Security Logging
- Mọi attempt đăng nhập đều được log
- Log bao gồm: successful login, failed login (user not found, wrong password)
- Giúp tracking và phát hiện các hành vi đáng ngờ

### 5. Public Endpoints
- Login và Register endpoints được đánh dấu `@Public`
- Không cần authentication để truy cập
- Các endpoint khác yêu cầu JWT token trong header

---

## Database Schema

### Table: users
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    address TEXT,
    role VARCHAR(20) DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### User Roles
- **CUSTOMER**: Khách hàng thông thường (mặc định)
- **EMPLOYEE**: Nhân viên
- **ADMIN**: Quản trị viên

---

## Testing Scenarios

### Test Case 1: Đăng ký thành công
```
Input:
- fullName: "Test User"
- email: "test@example.com"
- password: "password123"

Expected Output:
- Status: 200 OK
- Response code: "00"
- Response contains user data with id
```

### Test Case 2: Đăng ký với email đã tồn tại
```
Input:
- Email đã được đăng ký trước đó

Expected Output:
- Status: 400 Bad Request
- Response code: "01"
- Message: "Registration failed"
```

### Test Case 3: Đăng nhập thành công
```
Input:
- Correct email and password

Expected Output:
- Status: 200 OK
- Response code: "00"
- Response contains user info (userId, fullName, email, phone, role)
```

### Test Case 4: Đăng nhập với password sai
```
Input:
- Correct email, wrong password

Expected Output:
- Status: 400 Bad Request
- Response code: "01"
- Message: "Invalid email or password"
```

### Test Case 5: Validation errors
```
Input:
- Invalid email format
OR
- Password too short (< 6 characters)

Expected Output:
- Status: 400 Bad Request
- Validation error message
```

---

## Troubleshooting

### Lỗi: "Registration failed"
**Nguyên nhân:**
- Email đã tồn tại trong database
- Internal server error

**Giải pháp:**
- Sử dụng email khác
- Kiểm tra logs để xem chi tiết lỗi

### Lỗi: "Invalid email or password"
**Nguyên nhân:**
- Email không tồn tại
- Password không đúng

**Giải pháp:**
- Kiểm tra lại email và password
- Thử đăng ký lại nếu chưa có account

### Lỗi: Validation errors
**Nguyên nhân:**
- Dữ liệu input không hợp lệ

**Giải pháp:**
- Đảm bảo email có format hợp lệ
- Password tối thiểu 6 ký tự
- Tất cả trường bắt buộc đều có giá trị

---

## JWT Configuration

Cấu hình JWT trong `application.yml`:

```yaml
jwt:
  secret: winmart-secret-key-for-jwt-token-generation-must-be-at-least-256-bits-long
  expiration: 86400000 # 24 hours in milliseconds
  refresh-expiration: 604800000 # 7 days in milliseconds
```

**Lưu ý bảo mật:**
- Secret key phải dài tối thiểu 256 bits
- Không commit secret key thật lên Git
- Nên sử dụng environment variables trong production
- Thay đổi secret key định kỳ

## Roadmap (Future Enhancements)

- [x] JWT Token authentication ✅
- [x] Refresh token mechanism ✅
- [ ] Refresh token endpoint (để lấy access token mới)
- [ ] Email verification
- [ ] Password reset functionality
- [ ] Two-factor authentication (2FA)
- [ ] OAuth2 integration (Google, Facebook login)
- [ ] Rate limiting để prevent brute force attacks
- [ ] Account lockout after failed attempts
- [ ] Blacklist token khi logout

---

## Contact & Support

Nếu có vấn đề hoặc câu hỏi, vui lòng liên hệ team phát triển hoặc tạo issue trên repository.

