package com.winmart.common.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class  BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_deleted", columnDefinition = "Boolean default false")
    private Boolean isDeleted = false;

    @Column(name = "is_active", columnDefinition = "Boolean default true")
    private Boolean isActive = true;

    @Basic
    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Date createdDate;

    @Basic
    @CreatedBy
    @Column(updatable = false, name = "created_by")
    private String createdBy;

    @Basic
    @LastModifiedDate
    @Column(name = "updated_date")
    private Date updatedDate;

    @Basic
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "system")
    private String system;

    @PrePersist
    public void prePersist() {
        this.updatedDate = null;
        this.updatedBy = null;
    }
}