package com.ivay.dtos.productdto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaginatedProductResponseDto {

	private List<ProductResponseDto> content;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private boolean hasNext;

}
