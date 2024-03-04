package com.bitain.qrcode.dto.pagination;

import java.util.List;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagination<T> {
	private Integer pageNumber;
	private Integer limit;
	private Integer totalPage;
	private Integer totalItem;
	private List<T> listResult;
}
