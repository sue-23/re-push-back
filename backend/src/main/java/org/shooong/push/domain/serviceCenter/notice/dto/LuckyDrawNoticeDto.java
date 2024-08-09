package org.shooong.push.domain.serviceCenter.notice.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LuckyDrawNoticeDto {

    private Long luckyAnnouncementId;
    private Long luckyId;
    private String luckyTitle;
    private String luckyContent;
}
