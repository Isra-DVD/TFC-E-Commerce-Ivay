package com.ivay.dtos.userdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequestDto {
    private String name;
    private String email;
    private String password; //to change
    private String phone;
    private Boolean isEnabled;
    private Boolean accountNoExpired;
    private Boolean accountNoLocked;
    private Boolean credentialNoExpired;
    private Long roleId;
}
