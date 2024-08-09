package org.shooong.push.domain.feed.styleFeed.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StyleFeedDto {
    private Long feedId;
    private String feedTitle;
    private String feedImage;
    private int likeCount;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private Long userId;
    private String nickName;
}
