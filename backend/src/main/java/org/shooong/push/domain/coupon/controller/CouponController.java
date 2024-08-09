package org.shooong.push.domain.coupon.controller;


import org.shooong.push.domain.coupon.dto.CouponCreateDto;
import org.shooong.push.domain.coupon.dto.CouponDto;
import org.shooong.push.domain.coupon.dto.UserCouponDto;
import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.enumData.AlarmType;
import org.shooong.push.domain.alarm.service.AlarmService;
import org.shooong.push.domain.coupon.service.CouponIssueService;
import org.shooong.push.domain.coupon.service.CouponService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class CouponController {

    private final CouponService couponService;
    private final CouponIssueService couponIssueService;

    private final AlarmService alarmService;

    @GetMapping("/coupon/time-attack")
    public ResponseEntity<List<CouponDto>> timeAttack(){
        List<CouponDto> couponDto = couponService.searchCouponsByTitle("timeAttack");

        return new ResponseEntity<>(couponDto, HttpStatus.OK);
    }
    @PostMapping("/api/coupon/create")
    public ResponseEntity<?> couponCreate(@RequestBody CouponCreateDto couponCreateDto){
        couponService.createCoupon(couponCreateDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponCreateDto);
    }

    @PostMapping("/api/coupon/{couponId}/issue")
    public ResponseEntity<?> couponIssue(@PathVariable Long couponId, @AuthenticationPrincipal UserDTO userDTO){
        couponIssueService.issueCoupon(couponId, userDTO.getUserId());

        // 알림 전송
        alarmService.sendNotification(userDTO.getUserId(), AlarmType.COUPON);

        return ResponseEntity.ok(200);
    }



    @GetMapping("/api/coupon/user")
    public ResponseEntity<List<UserCouponDto>> userCoupons(@AuthenticationPrincipal UserDTO userDTO){
        List<UserCouponDto> userCoupons= couponIssueService.userCoupons(userDTO.getUserId());

        return new ResponseEntity<>(userCoupons, HttpStatus.OK);
    }


}
