package org.shooong.push.domain.product.dto.Detail;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuyingHopeDto {
    private String productSize;
    private BigDecimal buyingBiddingPrice;
    private int buyingQuantity;
}
