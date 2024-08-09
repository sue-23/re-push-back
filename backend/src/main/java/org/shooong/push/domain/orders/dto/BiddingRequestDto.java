package org.shooong.push.domain.orders.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BiddingRequestDto {
    private Long productId; // 상품 아이디
    private BigDecimal price; // 구매 희망가, 즉시구매가
    private Long exp; // 입찰기간
}
