package com.ivay.dtos.supplierdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplierResponseDto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String imageUrl;
}
