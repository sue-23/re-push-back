package org.shooong.push.domain.product.dto.Detail;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoRequestDto {
    private Long reviewId;
    private Long userId;
    private String modelNum;
    private String reviewImg;
    private String reviewContent;
    private int reviewLike;
}
