package org.shooong.push.domain.serviceCenter.notice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.shooong.push.domain.luckyDraw.luckyDraw.entity.LuckyDraw;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Getter
@Setter
public class LuckyDrawNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long luckyAnnouncementId;

    @Column(nullable = false, length = 100)
    private String luckyTitle;

    @Column(nullable = false, length = 255)
    private String luckyContent;

    @OneToOne
    @JoinColumn(name = "luckyId")
    private LuckyDraw luckyDraw;
}
