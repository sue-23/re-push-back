package org.shooong.push.domain.orders.dto;


import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class SalesNowDto {
    Long buyingBiddingId;
    Long addressId;
    BigDecimal Price;

}
