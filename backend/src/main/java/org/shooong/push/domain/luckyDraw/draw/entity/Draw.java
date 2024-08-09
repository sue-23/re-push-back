package org.shooong.push.domain.luckyDraw.draw.entity;


import org.shooong.push.domain.enumData.LuckyStatus;
import jakarta.persistence.*;
import lombok.*;
import org.shooong.push.domain.luckyDraw.luckyDraw.entity.LuckyDraw;
import org.shooong.push.domain.user.users.entity.Users;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Draw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drawId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LuckyStatus luckyStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "luckyId", nullable = false)
    private LuckyDraw luckyDraw;
}
