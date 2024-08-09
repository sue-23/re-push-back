package org.shooong.push.domain.user.mypage.dto.buyHistory;

import org.shooong.push.domain.enumData.BiddingStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BuyDetailsProcessDto {

    private Long buyingBiddingId;

    private String productImg;
    private String productName;
    private String productSize;

    private BigDecimal buyingBiddingPrice;
    private BiddingStatus biddingStatus;

    public BuyDetailsProcessDto(Long buyingBiddingId, String productImg, String productName, String productSize, BigDecimal buyingBiddingPrice, BiddingStatus biddingStatus) {
        this.buyingBiddingId = buyingBiddingId;
        this.productImg = productImg;
        this.productName = productName;
        this.productSize = productSize;
        this.buyingBiddingPrice = buyingBiddingPrice;
        this.biddingStatus = biddingStatus;
    }
}
