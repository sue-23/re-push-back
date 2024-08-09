package org.shooong.push.domain.bookmark.bookmarkProduct.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkProductDto {
    private Long userId;
    private Long productId;
    private String productImg;
    private String modelNum;
    private String productSize;
    private String productBrand;
    private String productName;
    private BigDecimal salesBiddingPrice;
    private String mainDepartment;
}