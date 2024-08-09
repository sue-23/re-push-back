package org.shooong.push.domain.product.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@ToString(callSuper = true)
@AllArgsConstructor
public class AllProductDto {

    private String modelNum;
    private Long productId;
    private String productBrand;
    private String productName;
    private String mainDepartment;
    private String subDepartment;
    private String productImg;

    // 즉시 구매가를 위함
    private BigDecimal buyingBiddingPrice;

}
