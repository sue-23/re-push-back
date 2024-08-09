package org.shooong.push.domain.luckyDraw.luckyDraw.dto;

import org.shooong.push.domain.luckyDraw.luckyDraw.entity.LuckyDraw;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LuckyDrawsDto {

    private Long luckyId;
    private String luckyName;
    private String content;
    private String luckyImage;
    private LocalDateTime luckyStartDate;
    private LocalDateTime luckyEndDate;
    private LocalDateTime luckyDate;
    private Integer luckyPeople;


    public static LuckyDrawsDto fromEntity(LuckyDraw luckyDraw){
        return LuckyDrawsDto.builder()
                .luckyId(luckyDraw.getLuckyId())
                .luckyName(luckyDraw.getLuckyName())
                .content(luckyDraw.getContent())
                .luckyImage(luckyDraw.getLuckyImage())
                .luckyStartDate(luckyDraw.getLuckyStartDate())
                .luckyEndDate(luckyDraw.getLuckyEndDate())
                .luckyDate(luckyDraw.getLuckyDate())
                .luckyPeople(luckyDraw.getLuckyPeople())
                .build();
    }
}


