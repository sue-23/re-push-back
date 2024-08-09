package org.shooong.push.domain.product.dto.Detail;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecentlyPriceDto {
    private BigDecimal latestPrice;
    private BigDecimal previousPrice;
    private Double changePercentage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime salesBiddingTime;
    private BigDecimal salesBiddingPrice;
    private Long differenceContract;
}
