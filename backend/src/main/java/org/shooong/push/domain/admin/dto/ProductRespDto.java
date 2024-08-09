package org.shooong.push.domain.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

//대분류별 상품 조회
@Getter
@Setter
public class ProductRespDto {

    private String brand;
    private String productName;
    private String modelNum;
    private String productImg;
    private String mainDepartment;
    private BigDecimal buyingBiddingPrice;

    public ProductRespDto(String brand, String productName, String modelNum, String productImg, String mainDepartment, BigDecimal buyingBiddingPrice) {
        this.brand = brand;
        this.productName = productName;
        this.modelNum = modelNum;
        this.productImg = productImg;
        this.mainDepartment = mainDepartment;
        this.buyingBiddingPrice = buyingBiddingPrice;
    }
}
