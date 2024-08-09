//package com.example.backend.consumer;
//
//
//import org.shooong.push.domain.coupon.service.CouponIssueService;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CouponCreatedConsumer {
//
//    private final CouponIssueService couponIssueService;
//
//    public CouponCreatedConsumer(CouponIssueService couponIssueService) {
//        this.couponIssueService = couponIssueService;
//    }
//
//    @KafkaListener(topics = "CouponIssueTopic", groupId = "group_1")
//    public void listener(String key) {
//        String[] keyParts = key.split(":");
//        String couponPolicyId = keyParts[2];
//        String userId = keyParts[4];
//
//        couponIssueService.couponIssue(couponPolicyId, userId);
//    }
//}
