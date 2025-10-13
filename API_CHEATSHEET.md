# 🚀 API Cheat Sheet - Quick Reference

## 📝 CREATE - Tạo Mới
```bash
POST   /api/products/create          # Tạo 1 sản phẩm
POST   /api/products/createList      # Tạo nhiều sản phẩm
POST   /api/products/save            # Tạo hoặc cập nhật (auto)
```

## ✏️ UPDATE - Cập Nhật
```bash
PUT    /api/products/1               # Cập nhật sản phẩm ID=1
PUT    /api/products/updateList      # Cập nhật nhiều sản phẩm
POST   /api/products/save            # Tạo hoặc cập nhật (auto)
```

## 🗑️ DELETE - Xóa
```bash
DELETE /api/products/1               # Xóa sản phẩm ID=1
DELETE /api/products/deleteByIds     # Xóa nhiều: [1,2,3]
POST   /api/products/deleteList      # Xóa nhiều (POST): [1,2,3]
```

## 🔍 READ - Đọc/Tìm Kiếm
```bash
GET    /api/products/1               # Lấy sản phẩm ID=1
GET    /api/products/findByIds?ids=1&ids=2  # Lấy nhiều sản phẩm
GET    /api/products/all             # Lấy TẤT CẢ (⚠️ cẩn thận!)
POST   /api/products/search          # Tìm kiếm + phân trang
POST   /api/products/dropdown        # Danh sách dropdown
```

## 🛠️ UTILITY - Tiện Ích
```bash
GET    /api/products/exists/1        # Kiểm tra tồn tại
POST   /api/products/count           # Đếm số lượng
```

---

## 📊 Request Body Examples

### Create/Update Product
```json
{
  "name": "Coca Cola 330ml",
  "categoryId": 1,
  "price": 10000,
  "stock": 500
}
```

### Search with Filters
```json
{
  "page": 0,
  "size": 10,
  "sorts": [{"field": "price", "direction": "DESC"}],
  "filters": [
    {"field": "categoryId", "operator": "EQUALS", "value": "1"},
    {"field": "price", "operator": "GREATER_THAN", "value": "5000"}
  ]
}
```

### Delete Multiple
```json
[1, 2, 3, 4, 5]
```

---

## 🎯 All Endpoints for Each Controller

Replace `/api/products` with:
- `/api/categories`
- `/api/users`
- `/api/invoices`
- `/api/payments`
- `/api/promotions`
- `/api/inventory-logs`

**= 17 APIs × 7 controllers = 119 endpoints!** 🎉

---

## 💡 Quick Tips

| Tình Huống | Dùng API Nào |
|------------|--------------|
| Tạo sản phẩm mới | `POST /create` |
| Sửa giá sản phẩm | `PUT /{id}` |
| Xóa 1 sản phẩm | `DELETE /{id}` |
| Xóa nhiều sản phẩm | `DELETE /deleteByIds` |
| Xem chi tiết sản phẩm | `GET /{id}` |
| Danh sách sản phẩm có filter | `POST /search` |
| Dropdown danh mục | `POST /dropdown` |
| Kiểm tra còn hàng | `GET /exists/{id}` |

---

**📖 Chi tiết:** Xem file `COMPLETE_API_REFERENCE.md`

