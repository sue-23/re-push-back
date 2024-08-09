package org.shooong.push.domain.shop.repository;

import org.shooong.push.domain.product.dto.AllProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ShopProduct{
    Slice<AllProductDto> allProduct(Pageable pageable);

    Slice<AllProductDto> getProductsBySubDepartment(Pageable pageable, List<String> subDepartment);
}
