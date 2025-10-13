# 📮 HƯỚNG DẪN SỬ DỤNG POSTMAN

## 📥 1. Import Collection và Environment

### Import Collection:
1. Mở **Postman**
2. Click **Import** (góc trái trên)
3. Chọn file: `Winmart_Full_API.postman_collection.json`
4. Click **Import**

### Import Environment:
1. Click **Import** 
2. Chọn file: `Winmart.postman_environment.json`
3. Click **Import**
4. Chọn Environment **"Winmart Environment"** ở góc phải trên

---

## 🚀 2. Chạy Ứng Dụng

```bash
cd /home/toan/Documents/winmart/winmart-service
mvn clean install
mvn spring-boot:run
```

Đợi server khởi động thành công (port 3333)

---

## 🧪 3. Test APIs

### Collection bao gồm 7 nhóm API:

#### **1. Users API** (16 endpoints)
- ✅ Create User
- ✅ Create User List
- ✅ Save User (Create or Update)
- ✅ Save User List
- ✅ Update User by ID
- ✅ Update User List
- ✅ Get User by ID
- ✅ Find Users By IDs
- ✅ Get All Users
- ✅ Search Users (Paged)
- ✅ Dropdown Users (List)
- ✅ Count Users
- ✅ Check User Exists by ID
- ✅ Delete User by ID
- ✅ Delete Users By IDs (DELETE)
- ✅ Delete Users List (POST)

#### **2. Categories API** (5 endpoints)
- Create Category
- Save Category
- Get All Categories
- Search Categories
- Delete Category by ID

#### **3. Products API** (8 endpoints)
- Create Product
- Create Product List
- Update Product by ID
- Get Product by ID
- Get All Products
- Search Products
- Dropdown Products
- Delete Product by ID

#### **4. Invoices API** (3 endpoints)
- Create Invoice
- Get All Invoices
- Search Invoices

#### **5. Payments API** (3 endpoints)
- Create Payment
- Get All Payments
- Search Payments

#### **6. Promotions API** (3 endpoints)
- Create Promotion
- Get All Promotions
- Search Promotions

#### **7. Inventory Logs API** (3 endpoints)
- Create Inventory Log
- Get All Inventory Logs
- Search Inventory Logs

---

## 📝 4. Ví Dụ Test Workflow

### Workflow 1: Tạo và quản lý sản phẩm

```
1. Create Category
   POST /api/categories/create
   Body: {"name": "Đồ uống", "description": "Nước ngọt"}

2. Create Product
   POST /api/products/create
   Body: {"categoryId": 1, "name": "Coca", "price": 10000, "stock": 100}

3. Get All Products
   GET /api/products/all

4. Update Product
   PUT /api/products/1
   Body: {"id": 1, "price": 12000}

5. Search Products
   POST /api/products/search
   Body: {"page": 0, "size": 10, "sorts": [], "filters": []}
```

### Workflow 2: Tạo đơn hàng và thanh toán

```
1. Create User
   POST /api/users/create
   Body: {"fullName": "Nguyen Van A", "email": "a@example.com", ...}

2. Create Invoice
   POST /api/invoices/create
   Body: {"userId": 1, "totalAmount": 50000, "status": "PENDING"}

3. Create Payment
   POST /api/payments/create
   Body: {"invoiceId": 1, "amount": 50000, "paymentMethod": "QR_CODE"}

4. Search Invoices
   POST /api/invoices/search
```

---

## ⚙️ 5. Cấu Hình Environment Variables

Bạn có thể thay đổi biến environment:

- **baseUrl**: `http://localhost:3333` (mặc định)
- **id**: `1` (ID để test các endpoint /{id})

Cách thay đổi:
1. Click **Environments** ở sidebar trái
2. Chọn **Winmart Environment**
3. Sửa giá trị **Current Value**
4. Click **Save**

---

## 🎯 6. Response Format

Tất cả API trả về format:

```json
{
  "code": "200",
  "message": "Success",
  "data": { ... }
}
```

### Success Response:
```json
{
  "code": "200",
  "message": "Success",
  "data": {
    "id": 1,
    "name": "Coca Cola",
    "price": 10000
  }
}
```

### Error Response:
```json
{
  "code": "400",
  "message": "Bad Request",
  "data": null
}
```

---

## 📊 7. Test Data Mẫu

### User:
```json
{
  "fullName": "Nguyen Van A",
  "email": "nguyenvana@example.com",
  "phone": "0901234567",
  "password": "password123",
  "address": "123 Nguyen Hue, Q1, HCM",
  "role": "CUSTOMER"
}
```

### Category:
```json
{
  "name": "Đồ uống",
  "description": "Các loại nước ngọt, trà, cà phê"
}
```

### Product:
```json
{
  "categoryId": 1,
  "name": "Coca Cola 330ml",
  "description": "Nước ngọt có gas",
  "price": 10000,
  "stock": 500,
  "imageUrl": "https://example.com/coca.jpg"
}
```

### Invoice:
```json
{
  "userId": 1,
  "customerName": "Nguyen Van A",
  "totalAmount": 50000,
  "status": "PENDING"
}
```

### Payment:
```json
{
  "invoiceId": 1,
  "amount": 50000,
  "paymentMethod": "QR_CODE",
  "qrCodeUrl": "https://example.com/qr/payment123",
  "paymentStatus": "PAID"
}
```

### Promotion:
```json
{
  "code": "TET2025",
  "discountPercent": 15.00,
  "startDate": "2025-01-20",
  "endDate": "2025-02-10",
  "status": "ACTIVE"
}
```

### Inventory Log:
```json
{
  "productId": 1,
  "userId": 1,
  "actionType": "IMPORT",
  "quantity": 100,
  "note": "Nhập hàng tháng 10/2025"
}
```

---

## 🔥 8. Tips

1. **Test theo thứ tự**: Tạo Category → Product → User → Invoice → Payment
2. **Kiểm tra ID**: Sau khi tạo, lưu ID để dùng cho các API khác
3. **Sử dụng biến**: Dùng `{{baseUrl}}` và `{{id}}` thay vì hard-code
4. **Check response**: Luôn kiểm tra response để lấy ID hoặc dữ liệu cần thiết
5. **Delete cẩn thận**: API delete sẽ xóa vĩnh viễn

---

## ✅ Done!

Bạn đã sẵn sàng test tất cả APIs của Winmart! 🎉

Nếu gặp lỗi, kiểm tra:
- ✅ Server đã chạy chưa (port 3333)
- ✅ Database đã tạo chưa (winmart_db)
- ✅ Environment đã chọn đúng chưa
- ✅ Body JSON có đúng format chưa
