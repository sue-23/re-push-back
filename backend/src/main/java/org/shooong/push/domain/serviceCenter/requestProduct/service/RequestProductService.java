package org.shooong.push.domain.serviceCenter.requestProduct.service;

import org.shooong.push.domain.serviceCenter.requestProduct.dto.RequestProductDto;
import org.shooong.push.domain.serviceCenter.requestProduct.dto.RequestProductListDto;
import java.util.List;

public interface RequestProductService {
    void createRequestProduct(RequestProductDto requestProductDto);
    List<RequestProductListDto> getRequestProducts();
    RequestProductDto getRequestProductById(Long productId);
}
