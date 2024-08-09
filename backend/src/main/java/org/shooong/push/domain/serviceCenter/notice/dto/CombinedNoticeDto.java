package org.shooong.push.domain.serviceCenter.notice.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CombinedNoticeDto {

    private List<NoticeDto> notices;
    private List<LuckyDrawNoticeDto> luckyDrawAnnouncements;
}
