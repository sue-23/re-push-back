package org.shooong.push.domain.orders.dto;


import org.shooong.push.domain.bidding.buyingBidding.entity.BuyingBidding;
import org.shooong.push.domain.enumData.BiddingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BuyingBiddingDto {
    private Long buyingBiddingId;

    private OrderProductDto product;

    private BigDecimal buyingBiddingPrice;

    private int buyingQuantity;

    private LocalDateTime buyingBiddingTime;

    private BiddingStatus biddingStatus;

    private Long addressId;

    public static BuyingBiddingDto fromEntity(BuyingBidding buyingBidding) {
        return BuyingBiddingDto.builder()
            .buyingBiddingId(buyingBidding.getBuyingBiddingId())
            .buyingQuantity(buyingBidding.getBuyingQuantity())
            .buyingBiddingTime(buyingBidding.getBuyingBiddingTime())
            .biddingStatus(buyingBidding.getBiddingStatus())
            .build();
    }

}
