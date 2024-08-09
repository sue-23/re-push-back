package org.shooong.push.domain.user.mypage.dto.buyHistory;

import org.shooong.push.domain.enumData.BiddingStatus;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BuyDetailsDto {

    private String productImg;
    private String productName;
    private String productSize;

    private BigDecimal orderPrice;
    private BiddingStatus biddingStatus;

    public BuyDetailsDto(String productImg, String productName, String productSize, BigDecimal orderPrice, BiddingStatus biddingStatus) {
        this.productImg = productImg;
        this.productName = productName;
        this.productSize = productSize;
        this.orderPrice = orderPrice;
        this.biddingStatus = biddingStatus;
    }
}
