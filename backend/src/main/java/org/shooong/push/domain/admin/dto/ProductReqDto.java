package org.shooong.push.domain.admin.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class ProductReqDto {

    private String productImg;
    private String productBrand;
    private String modelNum;
    private String productName;
    private BigDecimal originalPrice;
    private String productSize;






}
