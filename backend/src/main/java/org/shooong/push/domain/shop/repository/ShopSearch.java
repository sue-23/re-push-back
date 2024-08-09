package org.shooong.push.domain.shop.repository;

import org.shooong.push.domain.product.dto.AllProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ShopSearch {
    Slice<AllProductDto> getKeywordSearch(String keyword, Pageable pageable);
}
