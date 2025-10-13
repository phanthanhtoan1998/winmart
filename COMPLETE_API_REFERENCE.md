# 📚 Tài Liệu Đầy Đủ Các API - BaseController

## 🎯 Tổng Quan

BaseController cung cấp **17 API endpoints** cho TẤT CẢ controllers kế thừa nó.

**Controllers có sẵn:**
- ProductController → `/api/products`
- CategoryController → `/api/categories`
- UserController → `/api/users`
- InvoiceController → `/api/invoices`
- PaymentController → `/api/payments`
- PromotionController → `/api/promotions`
- InventoryLogController → `/api/inventory-logs`

---

## 📝 CREATE APIs - TẠO MỚI

### 1. **POST** `/create` - Tạo mới 1 record

**Endpoint:** `POST /api/products/create`

**Body:**
```json
{
  "name": "Coca Cola 330ml",
  "categoryId": 1,
  "price": 10000,
  "stock": 500
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "data": {
    "id": 1,
    "name": "Coca Cola 330ml",
    "categoryId": 1,
    "price": 10000,
    "stock": 500
  }
}
```

---

### 2. **POST** `/createList` - Tạo mới nhiều records

**Endpoint:** `POST /api/products/createList`

**Body:**
```json
[
  {
    "name": "Coca Cola 330ml",
    "price": 10000,
    "stock": 500
  },
  {
    "name": "Pepsi 330ml",
    "price": 9500,
    "stock": 400
  }
]
```

**Response:**
```json
{
  "status": "SUCCESS",
  "data": "Created 2 records successfully"
}
```

---

## ✏️ UPDATE APIs - CẬP NHẬT

### 3. **PUT** `/{id}` - Cập nhật 1 record

**Endpoint:** `PUT /api/products/1`

**Body:**
```json
{
  "id": 1,
  "name": "Coca Cola 500ml",
  "categoryId": 1,
  "price": 15000,
  "stock": 300
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "data": {
    "id": 1,
    "name": "Coca Cola 500ml",
    "price": 15000,
    "stock": 300
  }
}
```

---

### 4. **PUT** `/updateList` - Cập nhật nhiều records

**Endpoint:** `PUT /api/products/updateList`

**Body:**
```json
[
  {
    "id": 1,
    "name": "Coca Cola 500ml",
    "price": 15000
  },
  {
    "id": 2,
    "name": "Pepsi 500ml",
    "price": 14000
  }
]
```

**Response:**
```json
{
  "status": "SUCCESS",
  "data": "Updated 2 records successfully"
}
```

---

### 5. **POST** `/save` - Tạo mới HOẶC cập nhật (Universal)

**Endpoint:** `POST /api/products/save`

**Body (Create - không có ID):**
```json
{
  "name": "New Product",
  "price": 20000
}
```

**Body (Update - có ID):**
```json
{
  "id": 1,
  "name": "Updated Product",
  "price": 25000
}
```

---

### 6. **POST** `/saveList` - Tạo mới HOẶC cập nhật nhiều records

**Endpoint:** `POST /api/products/saveList`

**Body:**
```json
[
  {"id": 1, "name": "Update Product 1"},
  {"name": "New Product 2"}
]
```

---

## 🗑️ DELETE APIs - XÓA

### 7. **DELETE** `/{id}` - Xóa 1 record theo ID

**Endpoint:** `DELETE /api/products/1`

**Response:**
```json
{
  "status": "SUCCESS",
  "data": "Deleted record ID: 1"
}
```

---

### 8. **DELETE** `/deleteByIds` - Xóa nhiều records

**Endpoint:** `DELETE /api/products/deleteByIds`

**Body:**
```json
[1, 2, 3, 4, 5]
```

**Response:**
```json
{
  "status": "SUCCESS",
  "data": "Deleted 5 records"
}
```

---

### 9. **POST** `/deleteList` - Xóa nhiều records (POST method)

**Endpoint:** `POST /api/products/deleteList`

**Body:**
```json
[1, 2, 3]
```

**Response:**
```json
{
  "status": "SUCCESS",
  "data": "Deleted 3 records"
}
```

---

## 🔍 READ/FIND APIs - TÌM KIẾM & ĐỌC

### 10. **GET** `/{id}` - Tìm 1 record theo ID

**Endpoint:** `GET /api/products/1`

**Response:**
```json
{
  "status": "SUCCESS",
  "data": {
    "id": 1,
    "name": "Coca Cola",
    "price": 10000
  }
}
```

**Response (Not Found):**
```
HTTP 404 Not Found
```

---

### 11. **GET** `/findByIds` - Tìm nhiều records theo IDs

**Endpoint:** `GET /api/products/findByIds?ids=1&ids=2&ids=3`

**Response:**
```json
{
  "status": "SUCCESS",
  "data": [
    {"id": 1, "name": "Product 1"},
    {"id": 2, "name": "Product 2"},
    {"id": 3, "name": "Product 3"}
  ]
}
```

---

### 12. **GET** `/all` - Lấy TẤT CẢ records

**Endpoint:** `GET /api/products/all`

**Response:**
```json
{
  "status": "SUCCESS",
  "data": [
    {"id": 1, "name": "Product 1"},
    {"id": 2, "name": "Product 2"},
    ...
  ]
}
```

⚠️ **Cảnh báo:** Trả về toàn bộ dữ liệu - cẩn thận với bảng lớn!

---

### 13. **POST** `/search` - Tìm kiếm CÓ PHÂN TRANG

**Endpoint:** `POST /api/products/search`

**Body:**
```json
{
  "page": 0,
  "size": 10,
  "sorts": [
    {
      "field": "price",
      "direction": "DESC"
    }
  ],
  "filters": [
    {
      "field": "categoryId",
      "operator": "EQUALS",
      "value": "1"
    },
    {
      "field": "price",
      "operator": "GREATER_THAN",
      "value": "5000"
    }
  ]
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "data": {
    "content": [
      {"id": 1, "name": "Product 1", "price": 15000},
      {"id": 2, "name": "Product 2", "price": 12000}
    ],
    "totalElements": 25,
    "totalPages": 3,
    "size": 10,
    "number": 0
  }
}
```

---

### 14. **POST** `/dropdown` - Lấy danh sách cho dropdown

**Endpoint:** `POST /api/categories/dropdown`

**Body:**
```json
{
  "sorts": [
    {"field": "name", "direction": "ASC"}
  ],
  "filters": [
    {
      "field": "status",
      "operator": "EQUALS",
      "value": "ACTIVE"
    }
  ]
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "data": [
    {"id": 1, "name": "Category A"},
    {"id": 2, "name": "Category B"},
    {"id": 3, "name": "Category C"}
  ]
}
```

**Use Case:** Populate select/dropdown trong form

---

### 15. **POST** `/count` - Đếm số lượng records

**Endpoint:** `POST /api/products/count`

**Body (optional):**
```json
{
  "filters": [
    {
      "field": "categoryId",
      "operator": "EQUALS",
      "value": "1"
    }
  ]
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "data": 150
}
```

---

## 🛠️ UTILITY APIs - TIỆN ÍCH

### 16. **GET** `/exists/{id}` - Kiểm tra tồn tại

**Endpoint:** `GET /api/products/exists/1`

**Response (exists):**
```json
{
  "status": "SUCCESS",
  "data": true
}
```

**Response (not exists):**
```json
{
  "status": "SUCCESS",
  "data": false
}
```

---

## 📊 TỔNG HỢP

### Tất cả 17 APIs:

| # | Method | Endpoint | Mô tả |
|---|--------|----------|-------|
| 1 | POST | `/create` | Tạo mới 1 record |
| 2 | POST | `/createList` | Tạo mới nhiều records |
| 3 | PUT | `/{id}` | Cập nhật 1 record |
| 4 | PUT | `/updateList` | Cập nhật nhiều records |
| 5 | POST | `/save` | Tạo/cập nhật 1 record |
| 6 | POST | `/saveList` | Tạo/cập nhật nhiều records |
| 7 | DELETE | `/{id}` | Xóa 1 record |
| 8 | DELETE | `/deleteByIds` | Xóa nhiều records |
| 9 | POST | `/deleteList` | Xóa nhiều (POST) |
| 10 | GET | `/{id}` | Tìm 1 record |
| 11 | GET | `/findByIds` | Tìm nhiều records |
| 12 | GET | `/all` | Lấy tất cả |
| 13 | POST | `/search` | Tìm kiếm phân trang |
| 14 | POST | `/dropdown` | Danh sách dropdown |
| 15 | POST | `/count` | Đếm records |
| 16 | GET | `/exists/{id}` | Kiểm tra tồn tại |

---

## 🎯 Use Cases

### 1. Tạo mới Product
```bash
POST /api/products/create
```

### 2. Cập nhật Product
```bash
PUT /api/products/1
```

### 3. Xóa Product
```bash
DELETE /api/products/1
```

### 4. Tìm Product theo ID
```bash
GET /api/products/1
```

### 5. Tìm kiếm Products với filter
```bash
POST /api/products/search
```

### 6. Lấy danh sách Categories cho dropdown
```bash
POST /api/categories/dropdown
```

### 7. Xóa nhiều Products cùng lúc
```bash
DELETE /api/products/deleteByIds
Body: [1, 2, 3, 4, 5]
```

---

## ⚡ Ví Dụ Thực Tế

### Scenario: Quản lý sản phẩm

```javascript
// 1. Tạo sản phẩm mới
POST /api/products/create
{
  "name": "Coca Cola 330ml",
  "categoryId": 1,
  "price": 10000,
  "stock": 500
}

// 2. Lấy danh sách sản phẩm (phân trang)
POST /api/products/search
{
  "page": 0,
  "size": 20,
  "sorts": [{"field": "name", "direction": "ASC"}]
}

// 3. Cập nhật giá sản phẩm
PUT /api/products/1
{
  "id": 1,
  "price": 12000
}

// 4. Xóa sản phẩm hết hàng
DELETE /api/products/1

// 5. Kiểm tra sản phẩm còn tồn tại không
GET /api/products/exists/1
```

---

## 📌 Lưu Ý Quan Trọng

### CREATE vs UPDATE vs SAVE
- **`/create`**: Chỉ tạo mới (body không cần ID)
- **`/update`**: Chỉ cập nhật (body phải có ID)
- **`/save`**: Tự động - có ID = update, không có ID = create

### DELETE Methods
- **`DELETE /{id}`**: REST standard - xóa 1
- **`DELETE /deleteByIds`**: REST với body - xóa nhiều
- **`POST /deleteList`**: Alternative cho client không support DELETE with body

### SEARCH vs DROPDOWN vs ALL
- **`/search`**: Có phân trang, filter, sort → Dùng cho danh sách chính
- **`/dropdown`**: Không phân trang, có filter, sort → Dùng cho select/dropdown
- **`/all`**: Không filter gì cả → Chỉ dùng khi chắc chắn data ít

---

## 🚀 Kết Luận

Với **17 APIs** này, bạn có đầy đủ CRUD operations cho TẤT CẢ entities mà **KHÔNG CẦN viết thêm code** trong controller!

**Tổng số endpoints trong toàn hệ thống:**
- 7 controllers × 17 APIs = **119 API endpoints** 🎉
- Plus các API custom (login, register, etc.)

**= Hơn 120 APIs** từ chỉ **261 dòng code** trong BaseController!

