package org.shooong.push.domain.serviceCenter.requestProduct.service;

import org.shooong.push.domain.serviceCenter.requestProduct.dto.RequestProductDto;
import org.shooong.push.domain.serviceCenter.requestProduct.dto.RequestProductListDto;
import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.enumData.ProductStatus;
import org.shooong.push.domain.serviceCenter.requestProduct.repository.RequestProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestProductServiceImpl implements RequestProductService {

    @Autowired
    private RequestProductRepository requestProductRepository;

    // 미등록 상품 등록 요청글 등록
    @Override
    public void createRequestProduct(RequestProductDto requestProductDto) {

        requestProductDto.setProductStatus(ProductStatus.REQUEST);

        Product product = Product.builder()
                .productImg(requestProductDto.getProductImg())
                .productBrand(requestProductDto.getProductBrand())
                .productName(requestProductDto.getProductName())
                .originalPrice(requestProductDto.getOriginalPrice())
                .mainDepartment(requestProductDto.getMainDepartment())
                .subDepartment(requestProductDto.getSubDepartment())
                .productSize(requestProductDto.getProductSize())
                .productStatus(requestProductDto.getProductStatus())
                .modelNum(requestProductDto.getModelNum())
                .build();

        requestProductRepository.save(product);
    }

    // 미등록 상품 등록 요청글 조회
    @Override
    public List<RequestProductListDto> getRequestProducts() {
        List<ProductStatus> statuses = List.of(ProductStatus.REQUEST, ProductStatus.REJECTED);
        List<Product> products = requestProductRepository.findByProductStatusIn(statuses);
        return products.stream().map(product ->
                RequestProductListDto.builder()
                        .productId(product.getProductId())
                        .productBrand(product.getProductBrand())
                        .productName(product.getProductName())
                        .productStatus(product.getProductStatus())
                        .build()
        ).collect(Collectors.toList());
    }

    //  미등록 상품 등록 요청글 상세 조회
    @Override
    public RequestProductDto getRequestProductById(Long productId) {
        Optional<Product> product = requestProductRepository.findByProductIdAndProductStatusIn(
                productId, Arrays.asList(ProductStatus.REQUEST, ProductStatus.REJECTED)
        );
        return product.map(p -> RequestProductDto.builder()
                .productId(p.getProductId())
                .productImg(p.getProductImg())
                .productBrand(p.getProductBrand())
                .productName(p.getProductName())
                .originalPrice(p.getOriginalPrice())
                .mainDepartment(p.getMainDepartment())
                .subDepartment(p.getSubDepartment())
                .productSize(p.getProductSize())
                .productStatus(p.getProductStatus())
                .modelNum(p.getModelNum())
                .build()).orElse(null);
    }
}
