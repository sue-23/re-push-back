package org.shooong.push.domain.bidding.buyingBidding.repository;

import org.shooong.push.domain.user.mypage.dto.buyHistory.BuyDetailsDto;
import org.shooong.push.domain.user.mypage.dto.buyHistory.BuyDetailsProcessDto;
import org.shooong.push.domain.bidding.buyingBidding.entity.BuyingBidding;
import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.enumData.BiddingStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BuyingBiddingRepository extends JpaRepository<BuyingBidding, Long> {
    List<BuyingBidding> findByProduct(Product product);

    // 진행 중 건수
    @Query("SELECT COUNT(b) FROM BuyingBidding b WHERE b.user.userId = :userId AND b.biddingStatus ='PROCESS'")
    Long countProcessByUserId(Long userId);

    // 구매 입찰 - 즉시 구매가
    @Query("SELECT MIN(b.buyingBiddingPrice) FROM BuyingBidding b WHERE b.product.productId IN :productIdList AND b.biddingStatus = 'PROCESS'")
    Long findLowPrice(List<Long> productIdList);

    Optional<BuyingBidding> findByBuyingBiddingIdAndUserUserId(Long buyingBiddingId, Long userId);

    List<BuyingBidding> findByProductAndBiddingStatus(Product product, BiddingStatus biddingStatus);

    Long countAllByUserUserId(Long userId);

    @Query("SELECT COUNT(b) FROM BuyingBidding b WHERE b.user.userId = :userId AND b.biddingStatus ='COMPLETE'")
    Long countCompleteByUserId(Long userId);

    // TODO: QueryDSL로 변경할 것
    // 구매 내역 상세 정보 - 전체
    @Query("SELECT new org.shooong.push.domain.user.mypage.dto.buyHistory.BuyDetailsDto(p.productImg, p.productName, p.productSize, o.orderPrice, b.biddingStatus) " +
            "FROM BuyingBidding b " +
            "JOIN b.product p " +
            "JOIN b.user u " +
            "JOIN Orders o ON o.buyingBidding.buyingBiddingId = b.buyingBiddingId " +
            "WHERE u.userId = :userId " +
            "ORDER BY b.modifyDate DESC")
    List<BuyDetailsDto> findAllBuyDetails(Long userId);

    // 구매 내역 상세 정보 - 입찰 중
    @Query("SELECT new org.shooong.push.domain.user.mypage.dto.buyHistory.BuyDetailsProcessDto(b.buyingBiddingId, p.productImg, p.productName, p.productSize, b.buyingBiddingPrice, b.biddingStatus) " +
            "FROM BuyingBidding b JOIN b.product p JOIN b.user u " +
            "WHERE u.userId = :userId " +
            "AND b.biddingStatus = 'PROCESS' " +
            "ORDER BY b.createDate DESC")
    List<BuyDetailsProcessDto> findBuyDetailsProcess(Long userId);

    // 구매 내역 상세 정보 - 종료
    @Query("SELECT new org.shooong.push.domain.user.mypage.dto.buyHistory.BuyDetailsDto(p.productImg, p.productName, p.productSize, o.orderPrice, b.biddingStatus) " +
            "FROM BuyingBidding b " +
            "JOIN b.product p " +
            "JOIN b.user u " +
            "JOIN Orders o ON o.buyingBidding.buyingBiddingId = b.buyingBiddingId " +
            "WHERE u.userId = :userId " +
            "AND b.biddingStatus = 'COMPLETE' " +
            "ORDER BY b.modifyDate DESC")
    List<BuyDetailsDto> findBuyDetailsComplete(Long userId);

    // 주문 내역 상세 정보 - 전체 (최근 3건 조회)
    @Query("SELECT new org.shooong.push.domain.user.mypage.dto.buyHistory.BuyDetailsDto(p.productImg, p.productName, p.productSize, o.orderPrice, b.biddingStatus) " +
            "FROM BuyingBidding b " +
            "JOIN b.product p " +
            "JOIN b.user u " +
            "JOIN Orders o ON o.buyingBidding.buyingBiddingId = b.buyingBiddingId " +
            "WHERE u.userId = :userId " +
            "ORDER BY b.modifyDate DESC")
    List<BuyDetailsDto> findRecentBuyDetails(Long userId, Pageable pageable);

}
