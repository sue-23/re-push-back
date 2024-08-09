package org.shooong.push.domain.shop.service;

import org.shooong.push.domain.product.dto.AllProductDto;
import org.shooong.push.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class ShopService {

    private final ProductRepository productRepository;

    public Slice<AllProductDto> getTotalProduct(Pageable pageable) {

        return productRepository.allProduct(pageable);
    }

    public Slice<AllProductDto> getSubDepartmentFilter(Pageable pageable, List<String> subDepartment){
        return productRepository.getProductsBySubDepartment(pageable, subDepartment);
    }
}
