package org.shooong.push.domain.coupon.dto;

import org.shooong.push.domain.coupon.entity.Coupon;
import org.shooong.push.domain.coupon.entity.CouponIssue;
import org.shooong.push.domain.user.users.entity.Users;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssueDto {
    private Long userId;
    private Long couponId;

    public CouponIssue toEntity(Users user, Coupon coupon){
        return CouponIssue.builder()
            .user(user)
            .coupon(coupon)
            .endDate(LocalDateTime.now().plusDays(coupon.getExpDay()))
            .build();
    }
}
