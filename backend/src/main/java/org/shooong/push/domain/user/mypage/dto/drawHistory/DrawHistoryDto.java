package org.shooong.push.domain.user.mypage.dto.drawHistory;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DrawHistoryDto {

    private Long allCount;          // 전체
    private Long processCount;      // 진행 중
    private Long luckyCount;        // 당첨

    private List<DrawDetailsDto> drawDetails;       // 상품사진, 상품명, (사이즈), 당첨발표일, 당첨여부

}
