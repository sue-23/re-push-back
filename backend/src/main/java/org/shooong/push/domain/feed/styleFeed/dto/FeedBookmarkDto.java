package org.shooong.push.domain.feed.styleFeed.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedBookmarkDto {
    private Long userId;
    private Long feedId;
    private String feedImage;
    private String feedTitle;
    private String nickName;

    public FeedBookmarkDto(Long feedBookmarkId, Long userId, Long feedId) {
    }
}
