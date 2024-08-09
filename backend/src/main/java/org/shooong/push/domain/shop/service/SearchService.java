package org.shooong.push.domain.shop.service;

import org.shooong.push.domain.product.dto.AllProductDto;
import org.shooong.push.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class SearchService {
    private final ProductRepository productRepository;

    public Slice<AllProductDto> getSearch(String keyword, Pageable pageable) {

        return productRepository.getKeywordSearch(keyword, pageable);
    }
}
