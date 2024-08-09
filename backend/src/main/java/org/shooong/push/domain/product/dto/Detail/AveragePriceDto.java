package org.shooong.push.domain.product.dto.Detail;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class AveragePriceDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH", timezone = "Asia/Seoul")
    private LocalDateTime contractDateTime;
    private BigDecimal averagePrice;

    public AveragePriceDto(LocalDateTime contractDateTime, BigDecimal averagePrice) {
        this.contractDateTime = contractDateTime;
        this.averagePrice = averagePrice;
    }

    @Override
    public String toString() {
        return "AveragePriceDto{" +
                "contractDateTime=" + contractDateTime +
                ", averagePrice=" + averagePrice +
                '}';
    }
}