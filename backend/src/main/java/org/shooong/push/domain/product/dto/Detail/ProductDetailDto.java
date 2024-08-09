package org.shooong.push.domain.product.dto.Detail;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDto {
    private Long productId;
    private String productImg;
    private String productBrand;
    private String modelNum;
    private String productName;
    private BigDecimal originalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createDate;
    private int productLike;
    private String subDepartment;

    private BigDecimal buyingBiddingPrice;
    private BigDecimal salesBiddingPrice;

    private BigDecimal latestPrice;
    private BigDecimal previousPrice;
    private Double changePercentage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime recentlyContractDate;
    private Long differenceContract;

    private List<ProductsContractListDto> contractInfoList; // 체결 내역
    private List<SalesHopeDto> salesHopeList;   // 구매 희망 가격
    private List<BuyingHopeDto> buyingHopeList; // 판매 희망 가격

    private List<PhotoReviewDto> photoReviewList;   // 해당 상품의 스타일 리뷰

    private List<GroupByBuyingDto> groupByBuyingList;
    private List<GroupBySalesDto> groupBySalesList;

    private AveragePriceResponseDto averagePriceResponseList;

}
