package org.shooong.push.domain.serviceCenter.requestProduct.dto;

import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.enumData.ProductStatus;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestProductDto {

    private Long productId;
    private String productImg;
    private String productBrand;
    private String productName;
    private BigDecimal originalPrice;
    private String mainDepartment;
    private String subDepartment;
    private String productSize;
    private ProductStatus productStatus;
    private String modelNum;

    public RequestProductDto(Product product) {
        this.productId = product.getProductId();
        this.productImg = product.getProductImg();
        this.productBrand = product.getProductBrand();
        this.productName = product.getProductName();
        this.originalPrice = product.getOriginalPrice();
        this.mainDepartment = product.getMainDepartment();
        this.subDepartment = product.getSubDepartment();
        this.productSize = product.getProductSize();
        this.productStatus = product.getProductStatus();
        this.modelNum = product.getModelNum();
    }
}
