package org.shooong.push.domain.user.mypage.dto.buyHistory;

import org.shooong.push.domain.enumData.OrderStatus;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderDetailsDto {

    private String productImg;
    private String productName;
    private String productSize;

    private BigDecimal orderPrice;
    private OrderStatus orderStatus;


    public OrderDetailsDto(String productImg, String productName, String productSize, BigDecimal orderPrice, OrderStatus orderStatus) {
        this.productImg = productImg;
        this.productName = productName;
        this.productSize = productSize;
        this.orderPrice = orderPrice;
        this.orderStatus = orderStatus;
    }
}
