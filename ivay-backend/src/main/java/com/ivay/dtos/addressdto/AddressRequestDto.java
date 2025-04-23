package com.ivay.dtos.addressdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressRequestDto {
    private Long userId;
    private String address;
}
