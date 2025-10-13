package com.winmart.userservice.repository;

import com.winmart.common.repository.BaseRepository;
import com.winmart.userservice.entity.InvoiceEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends BaseRepository<InvoiceEntity, Long> {}
