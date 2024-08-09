package org.shooong.push.domain.product.dto.Detail;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoReviewDto {
    private Long userId;
    private String reviewImg;
    private String reviewContent;
    private int reviewLike;
}
