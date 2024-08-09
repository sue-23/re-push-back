package org.shooong.push.domain.coupon.dto;

import org.shooong.push.domain.coupon.entity.Coupon;
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
public class UserCouponDto {
    Long userId;
    Coupon coupon;

}
