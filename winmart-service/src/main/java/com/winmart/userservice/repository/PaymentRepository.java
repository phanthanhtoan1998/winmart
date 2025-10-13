package com.winmart.userservice.repository;

import com.winmart.common.repository.BaseRepository;
import com.winmart.userservice.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends BaseRepository<PaymentEntity, Long> {}
