package org.shooong.push.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductRankingDto {
    private Long productId;
    private String productImg;
    private String productBrand;
    private String productName;
    private String modelNum;
    private BigDecimal biddingPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime registerDate;
    private int productLike;
    private String mainDepartment;
}
