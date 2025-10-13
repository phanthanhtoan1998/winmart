package com.winmart.userservice.dto.request;

import com.winmart.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest extends BaseDto {
  private String fullName;
  private String email;
  private String phone;
  private String password;
  private String address;
  private String role;
}