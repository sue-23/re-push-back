package org.shooong.push.domain.user.mypage.dto.accountSettings;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@Builder
@Data
public class SalesSummaryRespDto {
    private BigDecimal totalSalesPrice;
    private Long totalSalesCount;
    private Page<SalesSummaryDto> salesSummaryList;



}
