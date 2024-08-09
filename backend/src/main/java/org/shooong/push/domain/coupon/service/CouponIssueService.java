package org.shooong.push.domain.coupon.service;

import org.shooong.push.domain.coupon.dto.CouponIssueDto;
import org.shooong.push.domain.coupon.dto.UserCouponDto;
import org.shooong.push.domain.coupon.entity.Coupon;
import org.shooong.push.domain.coupon.entity.CouponIssue;
import org.shooong.push.domain.user.users.entity.Users;
import org.shooong.push.domain.enumData.CouponCondition;
import org.shooong.push.global.exception.CouponConditionNotFoundException;
import org.shooong.push.global.exception.CouponLimitExceededException;
import org.shooong.push.global.exception.CouponNotFoundException;
import org.shooong.push.global.exception.CouponNotInPeriodException;
import org.shooong.push.global.exception.DuplicateCouponException;
import org.shooong.push.global.exception.ErrorCode;
import org.shooong.push.global.exception.UserNotFoundException;
//import com.example.backend.producer.CouponCreateProducer;
import org.shooong.push.domain.coupon.repository.CouponIssueRepository;
import org.shooong.push.domain.coupon.repository.RedisRepository;
import org.shooong.push.domain.user.users.repository.UserRepository;
import org.shooong.push.domain.coupon.repository.CouponRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponIssueService {
    private final CouponIssueRepository couponIssueRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
//    private final CouponCreateProducer couponCreateProducer;
    private final RedisRepository redisRepository;


    @Transactional
    public void couponIssue(String couponId, String userId){


        /* MySQL에 Coupon 저장
        * */
        Users user = userRepository.findById(Long.valueOf(userId))
            .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        Coupon coupon = couponRepository.findById(Long.valueOf(couponId))
            .orElseThrow(() -> new CouponNotFoundException(ErrorCode.COUPON_NOT_FOUND));

        CouponIssueDto couponIssueDto = new CouponIssueDto();
        CouponIssue couponIssue = couponIssueDto.toEntity(user, coupon);

        couponIssueRepository.save(couponIssue);


    }
    public void issueCoupon(Long couponId, Long userId) {

        try {
            // 해당 쿠폰 정책 발급수 INCR
            Long issuedCount = redisRepository.couponIssuedCount(couponId);
            Long MAX_QUANTITY = Long.valueOf(redisRepository.getCouponCondition(couponId, CouponCondition.MAX_QUANTITY.name()));

            if (issuedCount != null && issuedCount > MAX_QUANTITY) {
                redisRepository.issuedCancel(couponId);
                throw new CouponLimitExceededException(ErrorCode.COUPON_LIMIT_ISSUE);
            }

            String startDate = redisRepository.getCouponCondition(couponId, CouponCondition.START_DATE.name());
            String endDate = redisRepository.getCouponCondition(couponId, CouponCondition.END_DATE.name());
            LocalDateTime startDateTime = LocalDateTime.parse(startDate);
            LocalDateTime endDateTime = LocalDateTime.parse(endDate);

            LocalDateTime currentDateTime = LocalDateTime.now();

            if (currentDateTime.isBefore(startDateTime) || currentDateTime.isAfter(endDateTime)) {
                throw new CouponNotInPeriodException(ErrorCode.COUPON_NOT_IN_PERIOD);
            }

            Optional.ofNullable(redisRepository.registerCouponUser(couponId, userId))
                .filter(apply -> apply == 1)
                .orElseThrow(() -> {
                    redisRepository.issuedCancel(couponId);
                    throw new DuplicateCouponException(ErrorCode.ISSUANCE_DUPLICATE_COUPON);
                });

//            couponCreateProducer.create(couponId, userId);
        } catch (CouponConditionNotFoundException e) {
            throw new RuntimeException("Failed to issue coupon: " + e.getMessage(), e);
        }
    }

//    public void issueCoupon(Long couponId, Long userId) {
//
//        // 해당 쿠폰 정책 발급수 INCR
//        Long issuedCount = redisRepository.couponIssuedCount(couponId);
//        Long MAX_QUANTITY = Long.valueOf(
//            redisRepository.getCouponCondition(couponId, CouponCondition.MAX_QUANTITY.name()));
//
//        if (issuedCount != null && issuedCount > MAX_QUANTITY) { //
//            redisRepository.issuedCancel(couponId);
//            throw new CouponLimitExceededException(ErrorCode.COUPON_NOT_FOUND);
//        }
//
//
//        String startDate = redisRepository.getCouponCondition(couponId, CouponCondition.START_DATE.name());
//        String endDate = redisRepository.getCouponCondition(couponId, CouponCondition.END_DATE.name());
//        LocalDateTime startDateTime = LocalDateTime.parse(startDate);
//        LocalDateTime endDateTime = LocalDateTime.parse(endDate);
//
//        LocalDateTime currentDateTime = LocalDateTime.now();
//
//
//        if (currentDateTime.isBefore(startDateTime) || currentDateTime.isAfter(endDateTime)) {
//            throw new CouponNotInPeriodException(ErrorCode.COUPON_NOT_IN_PERIOD);
//        }
//
//        Optional.ofNullable(redisRepository.registerCouponUser(couponId, userId))
//            .filter(apply -> apply == 1)
//            .orElseThrow(() -> {
//                redisRepository.issuedCancel(couponId);
//                throw new DuplicateCouponException(ErrorCode.ISSUANCE_DUPLICATE_COUPON);
//            });
//
//
//        Long apply = redisRepository.registerCouponUser(couponId, userId);
//        if (apply != 1) {
//            redisRepository.issuedCancel(couponId);
//            throw new DuplicateCouponException(ErrorCode.ISSUANCE_DUPLICATE_COUPON);
//        }
//        couponCreateProducer.create(couponId, userId);
//    }
//    public List<CouponIssueDto> userCoupons(Long userId) {
//        List<CouponIssue> coupon = couponIssueRepository.findUnusedCouponsByUserId(userId);
//        for(CouponIssue couponIssue : coupon) {
//            convertToDto(couponIssue)
//        }
//
//
//    }

    public List<UserCouponDto> userCoupons(Long userId) {
        List<CouponIssue> coupons = couponIssueRepository.findUnusedCouponsByUserId(userId);
        return coupons.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    public UserCouponDto convertToDto(CouponIssue couponIssue) {
        return UserCouponDto.builder()
            .coupon(couponIssue.getCoupon())
            .userId(couponIssue.getUser().getUserId())
            .build();
    }
//
//    public void issuedCoupon(CouponIssueDto couponIssueDto){
//        Long apply = issueListRepository.add(couponIssueDto.getUserId());
//
//        if (apply != 1) {
//            return;
//        }
//
//        Long count = couponCountRepository.increment();
//
//        if (count > 10) {
//            return;
//        }
//        couponCreateProducer.create(couponIssueDto);
//
//
//    }

}
