package org.shooong.push.domain.photoReview.entity;

import org.shooong.push.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.user.users.entity.Users;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class PhotoReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false, length = 255)
    private String reviewImg;

    @Column(length = 255)
    private String reviewContent;

    @Column(nullable = false)
    @Builder.Default
    private int reviewLike = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product products;
}
