package org.shooong.push.domain.orders.dto;

import java.math.BigDecimal;
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
public class PaymentDto {
    String paymentKey;
    String orderId;
    BigDecimal amount;

}
