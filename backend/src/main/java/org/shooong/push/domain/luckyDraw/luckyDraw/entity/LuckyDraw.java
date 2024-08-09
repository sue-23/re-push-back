package org.shooong.push.domain.luckyDraw.luckyDraw.entity;


import org.shooong.push.domain.enumData.LuckyProcessStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LuckyDraw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long luckyId;

    @Column(nullable = false, length = 100)
    private String luckyName;

    @Column(length = 100)
    private String content;

    @Column(length = 50)
    private String luckySize;

    @Column(nullable = false, length = 255)
    private String luckyImage;

    // 응모 시작일
    private LocalDateTime luckyStartDate;

    // 응모 마감일
    private LocalDateTime luckyEndDate;

    // 당첨 발표일
    private LocalDateTime luckyDate;

    // 당첨 인원
    @Column(nullable = false)
    private Integer luckyPeople;

    // 마감 여부
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LuckyProcessStatus luckyProcessStatus;



    //날짜 변경
    public void changeDate(LocalDateTime luckyStartDate, LocalDateTime luckyEndDate, LocalDateTime luckyDate) {
        this.luckyStartDate = luckyStartDate;
        this.luckyEndDate = luckyEndDate;
        this.luckyDate = luckyDate;
    }

    public void changeLuckyProcessStatus(LuckyProcessStatus luckyProcessStatus) {
        this.luckyProcessStatus = luckyProcessStatus;
    }

}



