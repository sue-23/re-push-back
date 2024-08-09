package org.shooong.push.domain.orders.entity;

import org.shooong.push.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;
    @Column(nullable = false , name = "order_name")
    private String orderName;
    @Column(nullable = false , name = "pay_amount")
    private BigDecimal payAmount;
    @Column(nullable = false , name = "payment_key")
    private String paymentKey;
    @Column
    private boolean isCancel;
    @Column
    private String cancelReason;
    @Column
    private boolean isSuccess;
    @Column
    private String failReason;

}
