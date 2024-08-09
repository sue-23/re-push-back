package org.shooong.push.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyinBiddingDto {

    private Long buyingBiddingId;
    private BigDecimal buyingPrice;
    private BigDecimal buyingBiddingPrice;
    private LocalDateTime buyingBiddingTime;
    private AdminUserDto buyer;

}
