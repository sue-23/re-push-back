package org.shooong.push.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductResponseDto {
    private Long productId;
    private String productImg;
    private String productBrand;
    private String productName;
    private String modelNum;
    private BigDecimal biddingPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime registerDate;
    private BigDecimal originalPrice;

    public ProductResponseDto(Long productId, String productImg, String productBrand, String productName, String modelNum, BigDecimal buyingBiddingPrice, LocalDateTime createDate, BigDecimal originalPrice) {
        this.productId = productId;
        this.productImg = productImg;
        this.productBrand = productBrand;
        this.productName = productName;
        this.modelNum = modelNum;
        this.biddingPrice = buyingBiddingPrice;
        this.registerDate = createDate;
        this.originalPrice = originalPrice;
    }
}
