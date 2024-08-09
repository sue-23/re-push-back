package org.shooong.push.domain.user.mypage.dto.accountSettings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesSummaryDto {

    private String productImg;
    private String productName;
    private BigDecimal orderPrice;
    private LocalDateTime orderDate;
}
