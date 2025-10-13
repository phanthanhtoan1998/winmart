# Tổng Quan Các API Có Sẵn

## 🎯 Cách Hoạt Động của BaseController

**BaseController** cung cấp **4 API CRUD cơ bản** cho TẤT CẢ các controller kế thừa nó.

### Các Controller Kế Thừa BaseController:
- ✅ `ProductController` → `/api/products`
- ✅ `CategoryController` → `/api/categories`
- ✅ `UserController` → `/api/users`
- ✅ `InvoiceController` → `/api/invoices`
- ✅ `PaymentController` → `/api/payments`
- ✅ `PromotionController` → `/api/promotions`
- ✅ `InventoryLogController` → `/api/inventory-logs`

---

## 📋 4 API Cơ Bản (Từ BaseController)

Mỗi controller đều có **4 API này**:

### 1. **POST** `/search` - Tìm kiếm có phân trang
```json
{
  "page": 0,
  "size": 10,
  "sorts": [{"field": "name", "direction": "ASC"}],
  "filters": [{"field": "status", "operator": "EQUALS", "value": "ACTIVE"}]
}
```

**Response**: Page<T> - Có phân trang, total elements, total pages

---

### 2. **POST** `/dropdown` - Lấy danh sách (không phân trang)
```json
{
  "sorts": [{"field": "name", "direction": "ASC"}],
  "filters": []
}
```

**Response**: List<T> - Toàn bộ danh sách (dùng cho dropdown, select box)

---

### 3. **GET** `/findByIds?ids=1&ids=2&ids=3` - Tìm theo IDs
**Response**: List<T> - Danh sách các records theo IDs

---

### 4. **POST** `/deleteByIds` - Xóa nhiều records
```json
[1, 2, 3, 4, 5]
```

**Response**: "Done"

---

## 📊 Tổng Hợp API Theo Module

### 🛍️ Products (`/api/products`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/products/search` | Tìm sản phẩm (phân trang) |
| POST | `/api/products/dropdown` | Dropdown sản phẩm |
| GET | `/api/products/findByIds` | Tìm theo IDs |
| POST | `/api/products/deleteByIds` | Xóa nhiều sản phẩm |

### 🏷️ Categories (`/api/categories`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/categories/search` | Tìm danh mục (phân trang) |
| POST | `/api/categories/dropdown` | Dropdown danh mục |
| GET | `/api/categories/findByIds` | Tìm theo IDs |
| POST | `/api/categories/deleteByIds` | Xóa nhiều danh mục |

### 👤 Users (`/api/users`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/users` | Tạo user mới |
| POST | `/api/users/login` | Đăng nhập |
| POST | `/api/users/register` | Đăng ký |
| POST | `/api/users/search` | Tìm users (phân trang) |
| POST | `/api/users/dropdown` | Dropdown users |
| GET | `/api/users/findByIds` | Tìm theo IDs |
| POST | `/api/users/deleteByIds` | Xóa nhiều users |

### 🔐 Auth (`/api/auth`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/auth/register` | Đăng ký tài khoản |
| POST | `/api/auth/login` | Đăng nhập |

### 🧾 Invoices (`/api/invoices`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/invoices/search` | Tìm hóa đơn (phân trang) |
| POST | `/api/invoices/dropdown` | Dropdown hóa đơn |
| GET | `/api/invoices/findByIds` | Tìm theo IDs |
| POST | `/api/invoices/deleteByIds` | Xóa nhiều hóa đơn |

### 💳 Payments (`/api/payments`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/payments/search` | Tìm thanh toán (phân trang) |
| POST | `/api/payments/dropdown` | Dropdown thanh toán |
| GET | `/api/payments/findByIds` | Tìm theo IDs |
| POST | `/api/payments/deleteByIds` | Xóa nhiều thanh toán |

### 🎁 Promotions (`/api/promotions`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/promotions/search` | Tìm khuyến mãi (phân trang) |
| POST | `/api/promotions/dropdown` | Dropdown khuyến mãi |
| GET | `/api/promotions/findByIds` | Tìm theo IDs |
| POST | `/api/promotions/deleteByIds` | Xóa nhiều khuyến mãi |

### 📦 Inventory Logs (`/api/inventory-logs`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/inventory-logs/search` | Tìm nhật ký kho (phân trang) |
| POST | `/api/inventory-logs/dropdown` | Dropdown nhật ký kho |
| GET | `/api/inventory-logs/findByIds` | Tìm theo IDs |
| POST | `/api/inventory-logs/deleteByIds` | Xóa nhiều nhật ký |

---

## 🚀 Tổng Cộng API

- **Auth**: 2 API
- **Users**: 7 API (4 base + 3 custom)
- **Products**: 4 API (base)
- **Categories**: 4 API (base)
- **Invoices**: 4 API (base)
- **Payments**: 4 API (base)
- **Promotions**: 4 API (base)
- **Inventory Logs**: 4 API (base)

**TỔNG: 33 API endpoints** 🎉

---

## 💡 Tại Sao Ít API?

Vì sử dụng **kiến trúc BaseController**:
- ✅ **DRY** (Don't Repeat Yourself) - Không lặp code
- ✅ Tất cả CRUD operations đều giống nhau → dùng chung 4 API
- ✅ Mở rộng dễ dàng: tạo controller mới chỉ cần kế thừa BaseController
- ✅ Custom API riêng: thêm method trong controller con (như login, register)

---

## 🔧 Muốn Thêm API?

### Ví dụ: Thêm API "Get Top Products" cho ProductController

```java
@RestController
@RequestMapping("/api/products")
public class ProductController extends BaseController {
    // ... constructor

    @GetMapping("/top")
    public ResponseEntity<ResponseDto<List<Product>>> getTopProducts(
            @RequestParam(defaultValue = "10") int limit) {
        List<Product> topProducts = productService.getTopProducts(limit);
        return ResponseConfig.success(topProducts);
    }
}
```

Vậy là bạn có thêm API: **GET** `/api/products/top?limit=10`

---

## 📖 Chi Tiết Request/Response

### Search Request
```json
{
  "page": 0,           // Trang hiện tại (0-based)
  "size": 10,          // Số items mỗi trang
  "sorts": [           // Sắp xếp
    {
      "field": "price",
      "direction": "DESC"  // ASC hoặc DESC
    }
  ],
  "filters": [         // Lọc dữ liệu
    {
      "field": "categoryId",
      "operator": "EQUALS",  // EQUALS, LIKE, GREATER_THAN, LESS_THAN, etc.
      "value": "1"
    }
  ]
}
```

### Search Response (Page)
```json
{
  "status": "SUCCESS",
  "data": {
    "content": [...],      // Danh sách items
    "totalElements": 100,  // Tổng số items
    "totalPages": 10,      // Tổng số trang
    "size": 10,            // Kích thước trang
    "number": 0            // Trang hiện tại
  }
}
```

### Dropdown Response (List)
```json
{
  "status": "SUCCESS",
  "data": [...]  // Toàn bộ danh sách
}
```

---

## 🎯 Best Practices

1. **Dùng `/search`** khi cần phân trang, filter, sort
2. **Dùng `/dropdown`** khi cần toàn bộ danh sách cho select/dropdown
3. **Dùng `/findByIds`** khi cần lấy nhiều items cụ thể
4. **Dùng `/deleteByIds`** khi cần xóa nhiều items cùng lúc

---

Bây giờ bạn hiểu tại sao BaseController chỉ có 4 API nhưng lại tạo ra 33+ endpoints rồi chứ? 😊

