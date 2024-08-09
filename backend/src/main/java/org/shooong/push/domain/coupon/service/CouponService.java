package org.shooong.push.domain.coupon.service;

import org.shooong.push.domain.coupon.dto.CouponCreateDto;
import org.shooong.push.domain.coupon.dto.CouponDto;
import org.shooong.push.domain.coupon.entity.Coupon;
import org.shooong.push.domain.coupon.entity.CouponIssue;
import org.shooong.push.domain.user.users.entity.Users;
import org.shooong.push.domain.enumData.CouponCondition;
import org.shooong.push.domain.enumData.DiscountType;
import org.shooong.push.domain.coupon.repository.CouponIssueRepository;
import org.shooong.push.domain.coupon.repository.RedisRepository;
import org.shooong.push.domain.coupon.repository.CouponRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponService {

    private final CouponRepository couponRepository;
    private final RedisRepository redisRepository;
    private final CouponIssueRepository couponIssueRepository;

    public void createCoupon(CouponCreateDto couponCreateDto) {
        Coupon coupon = couponCreateDto.toEntity();


        /* MySQL에 Coupon 저장
         * */

        Coupon savedCoupon = couponRepository.save(coupon);
        String couponSaveId = String.valueOf(savedCoupon.getCouponId());

        /* Redis에 Coupon 발급조건 저장
         *  발급 수량, 발급 시작 날짜&시간 , 발급 종료 날짜&시간, 쿠폰 발급코드
         * */


        redisRepository.saveCouponCondition(couponSaveId, CouponCondition.MAX_QUANTITY,
            String.valueOf(coupon.getMaxQuantity()));
        redisRepository.saveCouponCondition(couponSaveId, CouponCondition.START_DATE,
            String.valueOf(coupon.getStartDate()));
        redisRepository.saveCouponCondition(couponSaveId, CouponCondition.END_DATE,
            String.valueOf(coupon.getEndDate()));
        redisRepository.saveCouponCondition(couponSaveId, CouponCondition.COUPON_CODE, String.valueOf(coupon.getCouponCode()));
    }

    // 이벤트 쿠폰 조회
    public List<CouponDto> searchCouponsByTitle(String keyword) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Coupon> coupons = couponRepository.findActiveCouponsByTitleAndDate(keyword, currentDateTime);
        return coupons.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    public BigDecimal applyCoupon(Users user, Coupon coupon, BigDecimal originalAmount) {
        CouponIssue couponIssue = couponIssueRepository.findByUsersAndCouponAndUseStatusFalse(user, coupon)
            .orElseThrow(() -> new RuntimeException("Coupon not valid"));

        // 쿠폰 타입에 따라 할인 계산
        Coupon userCoupon = couponIssue.getCoupon();
        if (userCoupon.getDiscountType() == DiscountType.FIXED) {
            // 고정 할인
            return originalAmount.subtract(coupon.getAmount());
        } else if (userCoupon.getDiscountType() == DiscountType.PERCENT) {
            // 퍼센트 할인
            BigDecimal discount = originalAmount.multiply(userCoupon.getAmount().divide(BigDecimal.valueOf(100)));
            return originalAmount.subtract(discount);
        }



        return originalAmount;
    }




    private CouponDto convertToDto(Coupon coupon) {
        return CouponDto.builder()
            .couponId((coupon.getCouponId()))
            .couponTitle(coupon.getCouponTitle())
            .couponQuantity(coupon.getCouponQuantity())
            .maxQuantity(coupon.getMaxQuantity())
            .couponCode(coupon.getCouponCode())
            .expDay(coupon.getExpDay())
            .discountType(coupon.getDiscountType())
            .amount(coupon.getAmount())
            .startDate(coupon.getStartDate())
            .endDate(coupon.getEndDate())
            .content(coupon.getContent())
            .build();
    }
}
