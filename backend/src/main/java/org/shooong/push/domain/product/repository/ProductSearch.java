package org.shooong.push.domain.product.repository;


import org.shooong.push.domain.admin.dto.ProductRespDto;
import org.shooong.push.domain.product.dto.Detail.*;
import org.shooong.push.domain.product.dto.ProductResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductSearch {

    List<ProductRespDto> findProductsByDepartment(@Param("mainDepartment")String mainDepartment);

    List<ProductResponseDto> searchAllProduct(String mainDepartment);

    List<ProductResponseDto> searchAllProductManyBid(String mainDepartment);

    List<ProductResponseDto> searchAllProductNewBuying(String mainDepartment);

    List<ProductResponseDto> searchAllProductNewSelling(String mainDepartment);

    // 소분류 상품 전체 보기
    Slice<ProductResponseDto> subProductInfo(String subDepartment, Pageable pageable);

    // 상품 최고, 최저 입찰 희망가격 조회
    org.shooong.push.domain.product.dto.Detail.ProductDetailDto searchProductPrice(String modelNum);

    // 해당 상품의 기존 체결가가 있는지 확인
    List<org.shooong.push.domain.product.dto.Detail.SalesBiddingDto> recentlyTransaction(String modelNum);

    List<org.shooong.push.domain.product.dto.Detail.SalesHopeDto> salesHopeInfo(String modelNum);

    List<org.shooong.push.domain.product.dto.Detail.BuyingHopeDto> buyingHopeInfo(String modelNum);

    List<org.shooong.push.domain.product.dto.Detail.GroupByBuyingDto> groupByBuyingSize(String modelNum);

    List<org.shooong.push.domain.product.dto.Detail.GroupBySalesDto> groupBySalesSize(String modelNum);

    org.shooong.push.domain.product.dto.Detail.BidResponseDto BuyingBidResponse(org.shooong.push.domain.product.dto.Detail.BidRequestDto bidRequestDto);

    List<AveragePriceDto> getAllContractData(String modelNum, LocalDateTime startDate, LocalDateTime endDate);

}
