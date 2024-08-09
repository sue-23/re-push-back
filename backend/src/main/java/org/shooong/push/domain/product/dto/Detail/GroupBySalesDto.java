package org.shooong.push.domain.product.dto.Detail;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupBySalesDto {
    private Long salesProductId;
    private String productImg;
    private String productName;
    private String modelNum;
    private String productSize;
    private BigDecimal productMaxPrice;
    private Long productId;
}
