# Sample Data để Test API

## 👤 Users / Register

### Customer 1
```json
{
  "fullName": "Nguyen Van A",
  "email": "customer1@winmart.com",
  "phone": "0901234567",
  "password": "password123",
  "address": "123 Nguyen Hue, Q1, TP.HCM",
  "role": "CUSTOMER"
}
```

### Customer 2
```json
{
  "fullName": "Tran Thi B",
  "email": "customer2@winmart.com",
  "phone": "0902345678",
  "password": "password123",
  "address": "456 Le Loi, Q3, TP.HCM",
  "role": "CUSTOMER"
}
```

### Employee
```json
{
  "fullName": "Le Van C",
  "email": "employee@winmart.com",
  "phone": "0903456789",
  "password": "employee123",
  "address": "789 Hai Ba Trung, Q1, TP.HCM",
  "role": "EMPLOYEE"
}
```

### Admin
```json
{
  "fullName": "Pham Thi D",
  "email": "admin@winmart.com",
  "phone": "0904567890",
  "password": "admin123",
  "address": "321 Vo Van Tan, Q3, TP.HCM",
  "role": "ADMIN"
}
```

---

## 🏷️ Categories

### Category 1 - Đồ uống
```json
{
  "name": "Đồ uống",
  "description": "Các loại nước ngọt, nước suối, trà, cà phê"
}
```

### Category 2 - Thực phẩm tươi sống
```json
{
  "name": "Thực phẩm tươi sống",
  "description": "Rau củ quả, thịt cá, hải sản tươi sống"
}
```

### Category 3 - Đồ ăn vặt
```json
{
  "name": "Đồ ăn vặt",
  "description": "Snack, kẹo, bánh các loại"
}
```

### Category 4 - Gia vị
```json
{
  "name": "Gia vị",
  "description": "Muối, đường, nước mắm, dầu ăn"
}
```

### Category 5 - Đồ dùng gia đình
```json
{
  "name": "Đồ dùng gia đình",
  "description": "Đồ nhà bếp, vệ sinh, tẩy rửa"
}
```

---

## 🛒 Products

### Product 1 - Coca Cola
```json
{
  "categoryId": 1,
  "name": "Coca Cola 330ml",
  "description": "Nước ngọt có gas Coca Cola lon 330ml",
  "price": 10000,
  "stock": 500,
  "imageUrl": "https://example.com/images/coca-cola.jpg"
}
```

### Product 2 - Pepsi
```json
{
  "categoryId": 1,
  "name": "Pepsi 330ml",
  "description": "Nước ngọt có gas Pepsi lon 330ml",
  "price": 9500,
  "stock": 400,
  "imageUrl": "https://example.com/images/pepsi.jpg"
}
```

### Product 3 - Gạo ST25
```json
{
  "categoryId": 2,
  "name": "Gạo ST25 5kg",
  "description": "Gạo thơm đặc sản ST25 túi 5kg",
  "price": 125000,
  "stock": 100,
  "imageUrl": "https://example.com/images/gao-st25.jpg"
}
```

### Product 4 - Thịt heo ba chỉ
```json
{
  "categoryId": 2,
  "name": "Thịt heo ba chỉ",
  "description": "Thịt heo ba chỉ tươi (giá/kg)",
  "price": 150000,
  "stock": 50,
  "imageUrl": "https://example.com/images/thit-ba-chi.jpg"
}
```

### Product 5 - Snack Oishi
```json
{
  "categoryId": 3,
  "name": "Snack Oishi vị tảo biển 42g",
  "description": "Snack khoai tây Oishi vị tảo biển gói 42g",
  "price": 8000,
  "stock": 300,
  "imageUrl": "https://example.com/images/oishi.jpg"
}
```

### Product 6 - Mì Hảo Hảo
```json
{
  "categoryId": 3,
  "name": "Mì Hảo Hảo tôm chua cay",
  "description": "Mì ăn liền Hảo Hảo vị tôm chua cay gói 75g",
  "price": 4500,
  "stock": 600,
  "imageUrl": "https://example.com/images/hao-hao.jpg"
}
```

### Product 7 - Nước mắm Nam Ngư
```json
{
  "categoryId": 4,
  "name": "Nước mắm Nam Ngư 500ml",
  "description": "Nước mắm truyền thống Nam Ngư chai 500ml",
  "price": 35000,
  "stock": 200,
  "imageUrl": "https://example.com/images/nuoc-mam.jpg"
}
```

### Product 8 - Dầu ăn Simply
```json
{
  "categoryId": 4,
  "name": "Dầu ăn Simply 1L",
  "description": "Dầu đậu nành Simply chai 1 lít",
  "price": 45000,
  "stock": 150,
  "imageUrl": "https://example.com/images/dau-an.jpg"
}
```

### Product 9 - Nước rửa chén Sunlight
```json
{
  "categoryId": 5,
  "name": "Nước rửa chén Sunlight 750g",
  "description": "Nước rửa chén Sunlight hương chanh chai 750g",
  "price": 28000,
  "stock": 250,
  "imageUrl": "https://example.com/images/sunlight.jpg"
}
```

### Product 10 - Bột giặt OMO
```json
{
  "categoryId": 5,
  "name": "Bột giặt OMO 3kg",
  "description": "Bột giặt OMO Matic cửa trên túi 3kg",
  "price": 120000,
  "stock": 100,
  "imageUrl": "https://example.com/images/omo.jpg"
}
```

---

## 🧾 Invoice (Create Invoice)

### Invoice 1 - Đơn hàng cơ bản
```json
{
  "userId": 1,
  "customerName": "Nguyen Van A",
  "totalAmount": 50000,
  "status": "PENDING",
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "price": 10000
    },
    {
      "productId": 5,
      "quantity": 3,
      "price": 8000
    },
    {
      "productId": 6,
      "quantity": 2,
      "price": 4500
    }
  ]
}
```

### Invoice 2 - Đơn hàng lớn
```json
{
  "userId": 2,
  "customerName": "Tran Thi B",
  "totalAmount": 380000,
  "status": "PENDING",
  "items": [
    {
      "productId": 3,
      "quantity": 2,
      "price": 125000
    },
    {
      "productId": 7,
      "quantity": 1,
      "price": 35000
    },
    {
      "productId": 8,
      "quantity": 1,
      "price": 45000
    },
    {
      "productId": 9,
      "quantity": 2,
      "price": 28000
    }
  ]
}
```

---

## 💳 Payment

### Payment 1 - Thanh toán QR
```json
{
  "invoiceId": 1,
  "amount": 50000,
  "paymentMethod": "QR_CODE",
  "qrCodeUrl": "https://example.com/qr/payment-123",
  "paymentStatus": "PAID"
}
```

### Payment 2 - Thanh toán tiền mặt
```json
{
  "invoiceId": 2,
  "amount": 380000,
  "paymentMethod": "CASH",
  "qrCodeUrl": null,
  "paymentStatus": "PAID"
}
```

### Payment 3 - Thanh toán thẻ
```json
{
  "invoiceId": 1,
  "amount": 50000,
  "paymentMethod": "CARD",
  "qrCodeUrl": null,
  "paymentStatus": "UNPAID"
}
```

---

## 🎁 Promotion

### Promotion 1 - Giảm giá Tết
```json
{
  "code": "TET2025",
  "discountPercent": 15.00,
  "startDate": "2025-01-20",
  "endDate": "2025-02-10",
  "status": "ACTIVE"
}
```

### Promotion 2 - Giảm giá cuối tuần
```json
{
  "code": "WEEKEND",
  "discountPercent": 10.00,
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "status": "ACTIVE"
}
```

### Promotion 3 - Black Friday
```json
{
  "code": "BLACKFRIDAY",
  "discountPercent": 50.00,
  "startDate": "2024-11-25",
  "endDate": "2024-11-30",
  "status": "EXPIRED"
}
```

---

## 📦 Inventory Log

### Inventory Log 1 - Nhập kho
```json
{
  "productId": 1,
  "userId": 3,
  "actionType": "IMPORT",
  "quantity": 100,
  "note": "Nhập hàng đợt 1 tháng 10"
}
```

### Inventory Log 2 - Xuất kho
```json
{
  "productId": 1,
  "userId": 3,
  "actionType": "EXPORT",
  "quantity": 50,
  "note": "Xuất hàng cho đơn bán lẻ"
}
```

### Inventory Log 3 - Nhập kho sản phẩm mới
```json
{
  "productId": 3,
  "userId": 4,
  "actionType": "IMPORT",
  "quantity": 200,
  "note": "Nhập gạo ST25 đợt mới"
}
```

---

## 🔍 Search Request Examples

### Search với filter
```json
{
  "page": 0,
  "size": 10,
  "sorts": [
    {
      "field": "name",
      "direction": "ASC"
    }
  ],
  "filters": [
    {
      "field": "role",
      "operator": "EQUALS",
      "value": "CUSTOMER"
    }
  ]
}
```

### Search với sort nhiều field
```json
{
  "page": 0,
  "size": 20,
  "sorts": [
    {
      "field": "price",
      "direction": "DESC"
    },
    {
      "field": "name",
      "direction": "ASC"
    }
  ],
  "filters": []
}
```

---

## 📌 Lưu ý khi test:

1. **Thứ tự tạo dữ liệu:**
   - Tạo Users trước (qua Register hoặc Create User)
   - Tạo Categories
   - Tạo Products (cần categoryId)
   - Tạo Invoices (cần userId và productId)
   - Tạo Payments (cần invoiceId)
   - Tạo Inventory Logs (cần productId và userId)

2. **ID sẽ tự động tăng** từ 1, 2, 3... nên nhớ thay đổi các ID tham chiếu cho phù hợp

3. **Kiểm tra response** để lấy ID của entity vừa tạo và dùng cho các request tiếp theo

