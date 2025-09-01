package com.yasir.userService.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

}
