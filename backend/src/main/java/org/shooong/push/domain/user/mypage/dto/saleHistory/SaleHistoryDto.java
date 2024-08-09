package org.shooong.push.domain.user.mypage.dto.saleHistory;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SaleHistoryDto {

    private Long allCount;                                  // 전체

    private List<SalesStatusCountDto> salesStatusCounts;    // 검수 중, 입찰 중, 종료

    private List<SaleDetailsDto> saleDetails;               // 상품사진, 상품명, 사이즈, 판매단가, 판매상태

}
