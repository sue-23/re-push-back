package org.shooong.push.domain.product.service;

import org.shooong.push.domain.admin.dto.ProductRespDto;
import org.shooong.push.domain.orders.dto.OrderProductDto;
import org.shooong.push.domain.product.dto.*;
import org.shooong.push.domain.product.dto.Detail.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {

    List<ProductRespDto> findProductsByDepartment(@Param("mainDepartment") String mainDepartment);

    List<ProductResponseDto> getAllProducts(String mainDepartment);

    List<ProductResponseDto> getAllProductsManyBid(String mainDepartment);

    List<ProductResponseDto> getAllProductsNewBuyBid(String mainDepartment);

    List<ProductResponseDto> getAllProductsNewSaleBid(String mainDepartment);

    // 상품 카테고리에 따라 상품 정보 조회 (소분류)
    Slice<ProductResponseDto> selectCategoryValue(String subDepartment, Pageable pageable);

    // 상세 상품 기본 정보 조회
    ProductDetailDto productDetailInfo(String modelNum);

    RecentlyPriceDto selectRecentlyPrice(String modelNum);

    List<ProductsContractListDto> selectSalesContract(String modelNum);

    List<SalesHopeDto> selectSalesHope(String modelNum);

    List<BuyingHopeDto> selectBuyingHope(String modelNum);

    void addPhotoReview(PhotoRequestDto photoRequestDto);

    void updatePhotoReview(PhotoRequestDto photoRequestDto);

    void deletePhotoReview(Long reviewId, Long userId);

    List<PhotoReviewDto> selectPhotoReview(String modelNum);

    BidResponseDto selectBidInfo(BidRequestDto bidRequestDto);

    void saveTemporaryBid(InsertBidDto insertBidDto);

    AveragePriceResponseDto getAveragePrices(String modelNum);

    List<AveragePriceDto> calculateAveragePrice(List<AveragePriceDto> allContractData, LocalDateTime firstContractDateTime, LocalDateTime endDate, int intervalHours);

    void incrementProductLikes(String modelNum);

    List<ProductRankingDto> getAllProductsByLikes();

    OrderProductDto getProductOne(Long productId);
}