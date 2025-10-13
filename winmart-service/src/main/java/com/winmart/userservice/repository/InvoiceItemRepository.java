package com.winmart.userservice.repository;

import com.winmart.common.repository.BaseRepository;
import com.winmart.userservice.entity.InvoiceItemEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceItemRepository extends BaseRepository<InvoiceItemEntity, Long> {}
