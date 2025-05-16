package com.ivay.dtos.addressdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressResponseDto {
    private Long id;
    private Long userId;
    private String address;
    private String zipCode;
    private String province;
    private String locality;
}
