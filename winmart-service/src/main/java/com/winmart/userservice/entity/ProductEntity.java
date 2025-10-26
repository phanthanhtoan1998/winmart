package com.winmart.userservice.entity;

import com.winmart.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class ProductEntity extends BaseEntity {


    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "stock")
    private Integer stock = 0;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InvoiceItemEntity> invoiceItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InventoryLogEntity> inventoryLogs;

    // Trường categoryId để ModelMapper có thể map
    @Column(name = "category_id")
    private Long categoryId;

    // Relationship với Category để lấy tên category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    // Trường categoryName để hiển thị tên category
    public String getCategoryName() {
        // Check categoryId trước, nếu null hoặc category null thì return null
        if (categoryId == null || category == null) {
            return null;
        }
        return category.getName();
    }

    public Long getCategoryParentId() {
        return category != null && category.getParent() != null ? category.getParent().getId() : null;
    }
}
