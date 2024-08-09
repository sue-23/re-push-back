package org.shooong.push.domain.coupon.service;

import org.shooong.push.domain.coupon.repository.CouponIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final CouponIssueRepository userCouponRepository;

    public Long getValidCouponCount(Long userId) {
        return userCouponRepository.countValidCouponsByUserId(userId);
    }
}
