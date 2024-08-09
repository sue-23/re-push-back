package org.shooong.push.domain.product.dto.Detail;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewDto {
    private String modelNum;
    private Long userId;
}
