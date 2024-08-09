package org.shooong.push.domain.bidding.salesBidding.entity;

import org.shooong.push.domain.BaseEntity;
import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.enumData.SalesStatus;
import jakarta.persistence.*;
import lombok.*;
import org.shooong.push.domain.user.users.entity.Users;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class SalesBidding extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesBiddingId;

    private BigDecimal salesBiddingPrice;

    private int salesQuantity;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private SalesStatus salesStatus;

    private LocalDateTime salesBiddingTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @ToString.Exclude
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    @ToString.Exclude
    private Product product;

    // 판매 입찰 상태 변경
    public void changeSalesStatus(SalesStatus salesStatus){
        this.salesStatus = salesStatus;
    }
}
