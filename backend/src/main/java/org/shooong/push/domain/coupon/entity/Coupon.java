package org.shooong.push.domain.coupon.entity;

import org.shooong.push.domain.BaseEntity;
import org.shooong.push.domain.enumData.DiscountType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId; // 쿠폰 PK

    @Column(nullable = false, length = 150)
    private String couponTitle; // 쿠폰 이름

    @Column(nullable = false)
    private Long couponQuantity; // 쿠폰 수량

    @Column(nullable = false)
    private Long maxQuantity; // 최대 발급 수량

    @Column(nullable = false, length = 1000)
    private String couponCode; // 쿠폰 발급 코드

    @Column(nullable = false)
    private Long expDay; // 쿠폰 유효기간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType; //  PERCENT, FIXED

    @Column(nullable = false)
    private BigDecimal amount; // 할인 금액

    @Column(nullable = false)
    private LocalDateTime startDate; // 쿠폰 발급 시작 날짜, 시간 "2024-07-31T18:00:00"

    @Column(nullable = false)
    private LocalDateTime endDate; // 쿠폰 발급 종료 날짜 시간 "2024-07-31T18:00:00"

    @Column(length = 2000)
    private String content; // 쿠폰 설명

}
