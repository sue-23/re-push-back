package org.shooong.push.domain.coupon.repository;

import org.shooong.push.domain.coupon.entity.Coupon;
import org.shooong.push.domain.coupon.entity.CouponIssue;
import org.shooong.push.domain.user.users.entity.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponIssueRepository extends JpaRepository<CouponIssue,Long> {

    @Query("SELECT COUNT(ci) FROM CouponIssue ci WHERE ci.user.userId = :userId AND ci.endDate >= CURRENT_DATE AND ci.useStatus = false")
    Long countValidCouponsByUserId(Long userId);

    // 해당 회원의 사용 가능한 발급 쿠폰 내역 조회
    @Query("SELECT ci.coupon FROM CouponIssue ci WHERE ci.user.userId = :userId AND ci.endDate >= CURRENT_DATE AND ci.useStatus = false")
    List<Coupon> findCouponsByUserId(Long userId);

//    List<Coupon> findByCouponTitleContaining(String keyword);

    @Query("SELECT ci FROM CouponIssue ci WHERE ci.user = :user AND ci.coupon = :coupon AND ci.useStatus = false")
    Optional<CouponIssue> findByUsersAndCouponAndUseStatusFalse(Users user, Coupon coupon);

    @Query("SELECT ci FROM CouponIssue ci JOIN FETCH ci.coupon c WHERE ci.user.userId = :userId AND ci.useStatus = false AND c.endDate >= CURRENT_DATE")
    List<CouponIssue> findUnusedCouponsByUserId(Long userId);
}
