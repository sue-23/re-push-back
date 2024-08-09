package org.shooong.push.domain.bookmark.bookmarkProduct.entity;

import jakarta.persistence.*;
import lombok.*;
import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.user.users.entity.Users;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class BookmarkProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;
}
