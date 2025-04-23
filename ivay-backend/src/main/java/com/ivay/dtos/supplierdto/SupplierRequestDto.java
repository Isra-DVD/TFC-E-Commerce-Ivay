package com.ivay.dtos.supplierdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplierRequestDto {
    private String name;
    private String email;
    private String address;
    private String phone;
}
