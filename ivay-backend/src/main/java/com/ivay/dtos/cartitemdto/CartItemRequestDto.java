package com.ivay.dtos.cartitemdto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemRequestDto {
	@NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;
	@NotNull(message = "El ID del producto es obligatorio")
    private Long productId;
	@NotNull(message = "La cantidad actualizada es obligatoria")
    private Integer quantity;
}
