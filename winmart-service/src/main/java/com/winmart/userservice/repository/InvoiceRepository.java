package com.winmart.userservice.repository;

import com.winmart.common.repository.BaseRepository;
import com.winmart.userservice.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends BaseRepository<InvoiceEntity, Long> {
    
    @Query("SELECT COUNT(i) FROM InvoiceEntity i WHERE i.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(i) FROM InvoiceEntity i WHERE i.user.id = :userId AND i.status = :status")
    Long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") InvoiceEntity.InvoiceStatus status);
}
