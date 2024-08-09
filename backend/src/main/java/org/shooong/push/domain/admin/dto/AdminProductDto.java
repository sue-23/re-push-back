package org.shooong.push.domain.admin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductDto {

    private Long productId;
    private String productName;
    private String modelNum;
    private String productBrand;
    private String productSize;
    private String mainDepartment;
    private String subDepartment;


    public AdminProductDto(Long productId,String productName, String modelNum, String productBrand, String productSize,String mainDepartment,String subDepartment) {
        this.productId = productId;
        this.productName = productName;
        this.modelNum = modelNum;
        this.productBrand = productBrand;
        this.productSize = productSize;
        this.mainDepartment = mainDepartment;
        this.subDepartment = subDepartment;
    }
}
