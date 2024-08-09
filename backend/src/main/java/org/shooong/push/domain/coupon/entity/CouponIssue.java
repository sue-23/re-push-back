package org.shooong.push.domain.coupon.entity;

import org.shooong.push.domain.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.shooong.push.domain.user.users.entity.Users;

@Entity
@Builder
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CouponIssue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCouponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couponId")
    private Coupon coupon;

    @Column(updatable = false)
    private LocalDateTime endDate; // 만료날짜

    @Column(nullable = false)
    private boolean useStatus; // 쿠폰 사용 여부

    @Column
    private LocalDateTime useDate; // 쿠폰 사용 날짜

    public void useCoupon(boolean useStatus){
        this.useStatus = useStatus;
    }

    public void useDate(){
        this.useDate = LocalDateTime.now();
    }
}
