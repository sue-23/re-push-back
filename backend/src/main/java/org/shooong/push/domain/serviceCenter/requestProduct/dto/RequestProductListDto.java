package org.shooong.push.domain.serviceCenter.requestProduct.dto;

import org.shooong.push.domain.enumData.ProductStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestProductListDto {

    private Long productId;
    private String productBrand;
    private String productName;
    private ProductStatus productStatus;
}
