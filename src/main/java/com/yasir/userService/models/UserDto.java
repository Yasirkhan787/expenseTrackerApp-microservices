package com.yasir.userService.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;
}

