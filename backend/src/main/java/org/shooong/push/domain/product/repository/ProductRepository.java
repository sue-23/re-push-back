package org.shooong.push.domain.product.repository;


import org.shooong.push.domain.user.mypage.dto.main.ProductDetailsDto;
import org.shooong.push.domain.product.dto.ProductRankingDto;
import org.shooong.push.domain.shop.repository.ShopProduct;
import org.shooong.push.domain.shop.repository.ShopSearch;
import org.shooong.push.domain.admin.repository.AdminProduct;
import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.enumData.ProductStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, AdminProduct, ProductSearch, ShopProduct, ShopSearch {
    //상품상태에 따른 상품 찾기
    Page<Product> findByProductStatus(ProductStatus productStatus, Pageable pageable);
    List<Product> findByProductStatus(ProductStatus productStatus);

    //상품아이디와 상품상태에 따른 상품 찾기
    Optional<Product> findByProductIdAndProductStatus(Long productId, ProductStatus productStatus);

    List<Product> findByModelNum(String modelNum);

    // 상품 모델번호에 따른 1개의 정보만 가져오기 - 모델번호가 똑같다는 것은 같은 상품이라는 것
    @Query("SELECT p FROM Product p WHERE p.modelNum = :modelNum AND p.productStatus = 'REGISTERED'")
    List<Product> findAllByModelNumAndStatus(@Param("modelNum") String modelNum);

    // 사이즈가 일치할 경우 같은 상품 찾기
    @Query("SELECT p FROM Product p WHERE p.modelNum = :modelNum AND p.productStatus = 'REGISTERED' and p.productSize = :productSize")
    Optional<Product> findBidProductInfo(@Param("modelNum") String modelNum, @Param("productSize") String productSize);

    Optional<Product> findFirstByModelNum(String modelNum);

    // 최근에 실행시킨 서버 날짜 가져오기
    Optional<Product> findFirstByModelNumOrderByLatestDateDesc(String modelNum);

    // 거래가 새롭게 체결된 날짜를 업데이트
    Optional<Product> findProductsByProductId(Long productId);

    // 이전 체결가 및 변동률이 없을 경우 0으로 초기화
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.previousPrice = 0, p.previousPercentage = 0.0 WHERE p.productId = :productId")
    void resetPreviousPrice(@Param("productId") Long productId);

    // 신규 체결가 있을때 기존 신규 체결가 -> 신규 이전 체결가
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.previousPrice = :previousContractPrice WHERE p.productId = :recentlyProductId")
    void updatePreviousPrice(@Param("recentlyProductId") Long recentlyProductId, @Param("previousContractPrice") BigDecimal previousContractPrice);

    // 해당 Id의 변동률 계산
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.previousPercentage = :changePercentage WHERE p.productId = :recentlyProductId")
    void updateRecentlyContractPercentage(@Param("recentlyProductId") Long recentlyProductId, @Param("changePercentage") Double changePercentage);

    // 최근 체결 상품의 최근 체결 가격 저장
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.latestPrice = :latestPrice, p.latestDate = :latestDate WHERE p.productId = :productId")
    void updateLatestPriceAndDate(@Param("productId") Long productId, @Param("latestPrice") BigDecimal latestPrice, @Param("latestDate") LocalDateTime latestDate);

    // 최근 체결 상품의 차이 저장
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.differenceContract = :differenceContract WHERE p.productId = :productId")
    void updateDifferenceContract(@Param("productId") Long productId, @Param("differenceContract") Long differenceContract);


    // TODO: QueryDSL로 변경
    // 회원의 관심상품 productIdList 로 상품 상세 정보 조회
    @Query("SELECT new org.shooong.push.domain.user.mypage.dto.main.ProductDetailsDto(p.productId, p.productImg, p.productBrand, p.productName, p.modelNum) " +
            "FROM Product p WHERE p.productId IN :productIdList")
    List<ProductDetailsDto> findProductsDetails(List<Long> productIdList);

    // 각 productId에 해당하는 modelNum 조회 후, 같은 modelNum 을 가진 모든 productId 조회
    @Query("SELECT DISTINCT p2.productId FROM Product p " +
            "JOIN Product p2 ON p.modelNum = p2.modelNum " +
            "WHERE p.productId IN :productIdList")
    List<Long> findProductIdsByModelNum(List<Long> productIdList);

    @Query("SELECT p.productId, p.modelNum " +
            "FROM Product p WHERE p.productId IN :productIdList")
    List<Object[]> findProductIdAndModelNum(List<Long> productIdList);

    boolean existsByModelNumAndProductSize(String modelNum, String productSize);
    Optional<Product> findByModelNumAndProductSize(String modelNum, String productSize);
    List<Product> findAllByModelNum(String modelNum);

    // 좋아요순으로 상품 조회
    @Query("SELECT DISTINCT new org.shooong.push.domain.product.dto.ProductRankingDto(p.productId, p.productImg, p.productBrand, p.productName, p.modelNum, bb.buyingBiddingPrice, p.createDate, p.productLike, p.mainDepartment) " +
            "FROM Product p LEFT JOIN BuyingBidding bb ON p.productId = bb.product.productId " +
            "GROUP BY p.modelNum " +
            "ORDER BY p.productLike DESC")
    List<ProductRankingDto> searchAllProductByLikes();





}