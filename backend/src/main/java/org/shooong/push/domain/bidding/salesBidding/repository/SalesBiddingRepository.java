package org.shooong.push.domain.bidding.salesBidding.repository;

import org.shooong.push.domain.user.mypage.dto.saleHistory.SaleDetailsDto;
import org.shooong.push.domain.user.mypage.dto.saleHistory.SalesStatusCountDto;
import org.shooong.push.domain.bidding.salesBidding.entity.SalesBidding;
import org.shooong.push.domain.enumData.SalesStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesBiddingRepository extends JpaRepository<SalesBidding, Long> {

    // 전체 구매 입찰 건수
    Long countAllByUserUserId(Long userId);

    // 상태별 건수
    @Query("SELECT new org.shooong.push.domain.user.mypage.dto.saleHistory.SalesStatusCountDto(s.salesStatus, COUNT(s)) " +
            "FROM SalesBidding s WHERE s.user.userId = :userId " +
            "GROUP BY s.salesStatus")
    List<SalesStatusCountDto> countSalesStatus(Long userId);

    // TODO: QueryDSL로 변경할 것
    // 판매 입찰 상세 정보
    @Query("SELECT new org.shooong.push.domain.user.mypage.dto.saleHistory.SaleDetailsDto(p.productImg, p.productName, p.productSize, s.salesBiddingId, s.salesBiddingPrice, s.salesStatus) " +
            "FROM SalesBidding s JOIN s.product p JOIN s.user u " +
            "WHERE u.userId = :userId " +
            "ORDER BY s.modifyDate DESC")
    List<SaleDetailsDto> findSaleDetailsByUserId(Long userId);

    // 판매 입찰 상세 정보 - 최근 3건 조회
    @Query("SELECT new org.shooong.push.domain.user.mypage.dto.saleHistory.SaleDetailsDto(p.productImg, p.productName, p.productSize, s.salesBiddingId, s.salesBiddingPrice, s.salesStatus) " +
            "FROM SalesBidding s JOIN s.product p JOIN s.user u " +
            "WHERE u.userId = :userId " +
            "ORDER BY s.modifyDate DESC")
    List<SaleDetailsDto> findRecentSaleDetailsByUserId(Long userId, Pageable pageable);

    Optional<SalesBidding> findBySalesBiddingIdAndUserUserId(Long salesBiddingId, Long userId);

    //판매 입찰 상태 COMPLETE 인 판매 내역 = 판매 정산 내역 조회
    List<SalesBidding> findBySalesStatusAndUser_UserId(SalesStatus salesStatus, Long userId);

    @Query("SELECT MIN(s.salesBiddingPrice) FROM SalesBidding s WHERE s.product.productId = :productId")
    BigDecimal findLowestSalesBiddingPriceByProductId(@Param("productId") Long productId);
}
