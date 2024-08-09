package org.shooong.push.domain.coupon.repository;

import org.shooong.push.domain.coupon.entity.Coupon;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {



    @Query("SELECT COUNT(c) FROM Coupon c WHERE c.couponId = :couponId")
    long countByCouponId(Long couponId);

    @Query("SELECT c FROM Coupon c WHERE c.couponTitle LIKE %:keyword% AND c.endDate > :currentDate")
    List<Coupon> findActiveCouponsByTitleAndDate(String keyword, LocalDateTime currentDate);
//    List<Coupon> findByCouponTitleContaining(String keyword);


}
