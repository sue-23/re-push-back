package org.shooong.push.domain.orders.dto;

import org.shooong.push.domain.user.mypage.dto.addressSettings.AddressDto;
import org.shooong.push.domain.enumData.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDto {
    private Long orderId;
    private Long userId;
    private OrderProductDto product;
    private BuyingBiddingDto biddingBidding;
    private SalesBiddingDto salesBidding;
    private AddressDto address;
    private OrderStatus orderStatus;
    private BigDecimal orderPrice;
    private LocalDateTime orderDate;

}
