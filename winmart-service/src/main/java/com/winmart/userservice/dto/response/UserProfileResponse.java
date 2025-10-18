package com.winmart.userservice.dto.response;

import com.winmart.common.dto.BaseDto;
import com.winmart.userservice.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponse extends BaseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private UserEntity.UserRole role;
    private Integer points;
    private Date createdDate;
}
