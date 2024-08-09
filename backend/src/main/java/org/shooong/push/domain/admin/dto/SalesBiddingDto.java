package org.shooong.push.domain.admin.dto;

import org.shooong.push.domain.enumData.SalesStatus;
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
public class SalesBiddingDto {
    private Long salesBiddingId;
    private BigDecimal  salesPrice;
    private BigDecimal salesBiddingPrice;
    private LocalDateTime salesBiddingTime;
    private AdminUserDto seller;
    private SalesStatus salesStatus;

}
