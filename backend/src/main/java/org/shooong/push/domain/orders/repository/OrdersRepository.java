package org.shooong.push.domain.orders.repository;

import org.shooong.push.domain.address.entity.Address;
import org.shooong.push.domain.orders.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long>,OrdersCustom {

    Long countByUserUserId(Long userId);

    // 종료 건수
    @Query("SELECT COUNT(o) FROM Orders o WHERE o.user.userId = :userId AND o.orderStatus ='COMPLETE'")
    Long countCompleteByUserId(Long userId);

    Optional<Orders> findByBuyingBiddingBuyingBiddingId(Long buyingBiddingId);

    /**
     * 주문 회원 기본 배송 정보
     */
    @Query("SELECT a FROM Address a WHERE a.user.userId = :userId AND a.defaultAddress = true")
    Optional<Address> findDefaultAddress(Long userId);

    Optional<Orders> findByOrderId(Long orderId);

    @Query("SELECT o FROM Orders o WHERE o.salesBidding.salesBiddingId = :salesBiddingId")
    Optional<Orders> findBySalesBiddingId(Long salesBiddingId);

    @Query("SELECT o FROM Orders o WHERE o.buyingBidding.buyingBiddingId = :buyingBiddingId")
    Optional<Orders> findByBuyingBiddingId(Long buyingBiddingId);

}
