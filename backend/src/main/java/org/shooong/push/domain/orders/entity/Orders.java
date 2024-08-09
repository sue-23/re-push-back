package org.shooong.push.domain.orders.entity;

import org.shooong.push.domain.address.entity.Address;
import org.shooong.push.domain.BaseEntity;
import org.shooong.push.domain.bidding.buyingBidding.entity.BuyingBidding;
import org.shooong.push.domain.coupon.entity.Coupon;
import org.shooong.push.domain.enumData.OrderStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;
import org.shooong.push.domain.bidding.salesBidding.entity.SalesBidding;
import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.user.users.entity.Users;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    private BigDecimal orderPrice;

    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyingBiddingId")
    private BuyingBidding buyingBidding;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesBiddingId")
    private SalesBidding salesBidding;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couponID")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressID")
    private Address address;

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
