package org.shooong.push.domain.serviceCenter.requestProduct.repository;

import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.enumData.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductStatusIn(List<ProductStatus> productStatus);
    Optional<Product> findByProductIdAndProductStatusIn(Long productId, List<ProductStatus> statuses);
    Optional<Product> findByProductIdAndProductStatus(Long productId, ProductStatus productStatus);
}
