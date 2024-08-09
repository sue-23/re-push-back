package org.shooong.push.domain.product.dto.Detail;

import lombok.*;
import org.shooong.push.domain.product.dto.Detail.AveragePriceDto;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AveragePriceResponseDto {
    private List<AveragePriceDto> threeDayPrices;
    private List<AveragePriceDto> oneMonthPrices;
    private List<AveragePriceDto> sixMonthPrices;
    private List<AveragePriceDto> oneYearPrices;
    private List<AveragePriceDto> totalExecutionPrice;
}
