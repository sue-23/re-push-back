package org.shooong.push.domain.luckyDraw.draw.dto;

import org.shooong.push.domain.luckyDraw.draw.entity.Draw;
import org.shooong.push.domain.luckyDraw.luckyDraw.entity.LuckyDraw;
import org.shooong.push.domain.user.users.entity.Users;
import org.shooong.push.domain.enumData.LuckyStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DrawDto {

    private LuckyStatus luckyStatus;
    private Users user;
    private LuckyDraw luckyDraw;


    /**
     * 응모 POST 요청 결과 응답으로 Entity를 DTO로 변환 후 클라이언트에 전달
     */
    public static DrawDto fromEntity(Draw draw) {
        return DrawDto.builder()
                .luckyStatus(draw.getLuckyStatus())
                .user(draw.getUser())
                .luckyDraw(draw.getLuckyDraw())
                .build();
    }
}
