package org.shooong.push.domain.product.entity;

import org.shooong.push.domain.admin.dto.ProductReqDto;
import org.shooong.push.domain.BaseEntity;
import org.shooong.push.domain.enumData.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Setter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, length = 255)
    private String productImg;

    @Column(nullable = false, length = 50)
    private String productBrand;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(length = 100)
    private String modelNum;

    @Column(nullable = false)
    private BigDecimal originalPrice;

    @Column(nullable = false)
    @Builder.Default
    private int productLike = 0;

    @Column(nullable = false, length = 50)
    private String mainDepartment;

    @Column(length = 50)
    private String subDepartment;

    @Column(nullable = false)
    private int productQuantity;

    @Column(nullable = false, length = 50)
    private String productSize;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @Column
    private BigDecimal latestPrice;

    @Column
    private LocalDateTime latestDate;

    @Column
    private BigDecimal previousPrice;

    @Column
    private Double previousPercentage;

    @Column
    private Long differenceContract;


    public void changeProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
    public void registerProduct(ProductReqDto regProduct) {
        this.productImg = regProduct.getProductImg();
        this.productBrand = regProduct.getProductBrand();
        this.productName = regProduct.getProductName();
        this.modelNum = regProduct.getModelNum();
        this.productStatus = ProductStatus.REGISTERED;
    }

    //  상품 수량 증가 메서드
    public void addQuantity(int productQuantity) {
        this.productQuantity += productQuantity;
    }

    public void updateLatestDate(LocalDateTime latestDate) {
        this.latestDate = latestDate;
    }


}
