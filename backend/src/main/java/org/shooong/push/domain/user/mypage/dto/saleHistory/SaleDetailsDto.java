package org.shooong.push.domain.user.mypage.dto.saleHistory;

import org.shooong.push.domain.enumData.SalesStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SaleDetailsDto {

    private String productImg;
    private String productName;
    private String productSize;

    private Long salesBiddingId;
    private BigDecimal saleBiddingPrice;
    private SalesStatus salesStatus;


    public SaleDetailsDto(String productImg, String productName, String productSize, Long salesBiddingId, BigDecimal saleBiddingPrice, SalesStatus salesStatus) {
        this.productImg = productImg;
        this.productName = productName;
        this.productSize = productSize;
        this.salesBiddingId = salesBiddingId;
        this.saleBiddingPrice = saleBiddingPrice;
        this.salesStatus = salesStatus;
    }
}
