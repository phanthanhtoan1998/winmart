package com.winmart.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winmart.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserResponse extends BaseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String role;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // Accept in request, don't return in response
    private String password;
}