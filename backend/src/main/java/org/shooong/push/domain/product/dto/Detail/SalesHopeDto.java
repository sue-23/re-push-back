package org.shooong.push.domain.product.dto.Detail;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesHopeDto {
    private String productSize;
    private BigDecimal salesBiddingPrice;
    private int salesQuantity;
}
