package com.winmart.userservice.repository;

import com.winmart.common.repository.BaseRepository;
import com.winmart.userservice.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
