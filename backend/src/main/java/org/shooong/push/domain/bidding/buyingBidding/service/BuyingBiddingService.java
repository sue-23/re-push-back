package org.shooong.push.domain.bidding.buyingBidding.service;

import static org.shooong.push.domain.enumData.BiddingStatus.PROCESS;
import static org.shooong.push.domain.enumData.SalesStatus.COMPLETE;

import org.shooong.push.domain.user.mypage.dto.buyHistory.BuyDetailsDto;
import org.shooong.push.domain.user.mypage.dto.buyHistory.BuyDetailsProcessDto;
import org.shooong.push.domain.user.mypage.dto.buyHistory.BuyHistoryAllDto;
import org.shooong.push.domain.orders.dto.BuyNowDto;
import org.shooong.push.domain.orders.dto.BuyOrderDto;
import org.shooong.push.domain.orders.dto.BuyingBiddingDto;
import org.shooong.push.domain.orders.dto.OrderProductDto;
import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.address.entity.Address;
import org.shooong.push.domain.bidding.buyingBidding.entity.BuyingBidding;
import org.shooong.push.domain.coupon.entity.Coupon;
import org.shooong.push.domain.coupon.entity.CouponIssue;
import org.shooong.push.domain.orders.entity.Orders;
import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.bidding.salesBidding.entity.SalesBidding;
import org.shooong.push.domain.user.users.entity.Users;
import org.shooong.push.domain.enumData.BiddingStatus;
import org.shooong.push.domain.enumData.OrderStatus;
import org.shooong.push.domain.bidding.buyingBidding.repository.BuyingBiddingRepository;
import org.shooong.push.domain.bidding.salesBidding.repository.SalesBiddingRepository;
import org.shooong.push.domain.coupon.repository.CouponIssueRepository;
import org.shooong.push.domain.orders.repository.OrdersRepository;
import org.shooong.push.domain.product.repository.ProductRepository;
import org.shooong.push.domain.user.users.repository.UserRepository;
import org.shooong.push.domain.coupon.repository.CouponRepository;
import org.shooong.push.domain.address.repository.AddressRepository;
import org.shooong.push.domain.coupon.service.CouponService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuyingBiddingService {

    private final OrdersRepository ordersRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final BuyingBiddingRepository buyingBiddingRepository;
    private final CouponIssueRepository couponIssueRepository;
    private final CouponService couponService;
    private final SalesBiddingRepository salesBiddingRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;


    /**
     *  구매입찰 등록
     */
//    public void registerBuyingBidding(UserDTO userDTO, BiddingRequestDto buyingInfo) {
//
//        Product product = productRepository.findById(buyingInfo.getProductId())
//            .orElseThrow(() -> new RuntimeException("Product not valid"));
//
//        Users user = userRepository.findById(userDTO.getUserId())
//            .orElseThrow(() -> new RuntimeException("User not valid"));
//
//        BuyingBidding buyingBidding = BuyingBidding.builder()
//            .buyingBiddingPrice(buyingInfo.getPrice())
//            .product(product)
//            .user(user)
//            .buyingBiddingTime(LocalDateTime.now().plusDays(buyingInfo.getExp()))
//            .biddingStatus(PROCESS)
//            .build();
//
//        buyingBiddingRepository.save(buyingBidding);
//    }

    /**
     * 구매입찰
     */
    @Transactional
    public Long registerBuyingBidding(UserDTO userDTO, BuyOrderDto buyOrderDto) {

        Product product = productRepository.findById(buyOrderDto.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not valid"));

        Users user = userRepository.findById(userDTO.getUserId())
            .orElseThrow(() -> new RuntimeException("User not valid"));



        BuyingBidding buyingBidding = BuyingBidding.builder()
            .buyingBiddingPrice(buyOrderDto.getPrice())
            .product(product)
            .user(user)
            .buyingBiddingTime(LocalDateTime.now().plusDays(buyOrderDto.getExp()))
            .biddingStatus(PROCESS)
            .build();

        buyingBiddingRepository.save(buyingBidding);

        Address address = addressRepository.findById(buyOrderDto.getAddressId()).orElseThrow(()->new RuntimeException("Address not found"));

        BigDecimal totalAmount = buyingBidding.getBuyingBiddingPrice(); // 입찰 가격 가져옴
        Coupon coupon = null;

        if (buyOrderDto.getCouponId() != null) { // 쿠폰 사용 확인, 적용
            coupon = couponRepository.findById(buyOrderDto.getCouponId())
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
            CouponIssue userCoupon = couponIssueRepository.findByUsersAndCouponAndUseStatusFalse(
                    user, coupon)
                .orElseThrow(() -> new RuntimeException("Coupon not valid"));
            totalAmount = couponService.applyCoupon(user, coupon, totalAmount)
                .setScale(2, RoundingMode.HALF_UP);

            userCoupon.useCoupon(true);
            userCoupon.useDate();
            couponIssueRepository.save(userCoupon);
        }


        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmount = BigDecimal.ZERO;
        }

        Orders order = Orders.builder() // order 데이터 생성 후 저장
            .user(user)
            .product(buyingBidding.getProduct())
            .buyingBidding(buyingBidding)
            .coupon(coupon)
            .orderStatus((buyingBidding.getBiddingStatus() == BiddingStatus.COMPLETE)
                ? OrderStatus.COMPLETE
                : OrderStatus.WAITING)
            .orderPrice(totalAmount)
            .address((address))
            .build();

        buyingBidding.changeBiddingStatus(BiddingStatus.COMPLETE);

        Long orderId = ordersRepository.save(order).getOrderId();

        return orderId;
    }


    /**
     * 즉시구매시 판매입찰 데이터로 구매입찰 생성(COMPLETE)
     */
    @Transactional
    public Long applyBuyNow(UserDTO userDTO, BuyNowDto buyNowDto) {
        SalesBidding salesBidding = salesBiddingRepository.findById(buyNowDto.getSalesBiddingId()) // 판매입찰 데이터 가져오기
            .orElseThrow(() -> new RuntimeException("SalesBidding not valid"));
        Users user = userRepository.findById(userDTO.getUserId())
            .orElseThrow(() -> new RuntimeException("User not valid"));
        Orders youOrder = ordersRepository.findBySalesBiddingId(buyNowDto.getSalesBiddingId())
            .orElseThrow(() -> new RuntimeException("Order not valid"));

        BuyingBidding buyingBidding = BuyingBidding.builder() // 즉시구매 데이터 생성
            .user(user)
            .product(salesBidding.getProduct())
            .buyingBiddingPrice(salesBidding.getSalesBiddingPrice())
            .buyingQuantity(salesBidding.getSalesQuantity())
            .buyingBiddingTime(LocalDateTime.now())
            .biddingStatus(BiddingStatus.COMPLETE)
            .build();

        salesBidding.changeSalesStatus(COMPLETE); // 해당 판매입찰건 완료

        buyingBiddingRepository.save(buyingBidding);

        Address address = addressRepository.findById(buyNowDto.getAddressId()).orElseThrow(()->new RuntimeException("Address not found"));

        BigDecimal totalAmount = buyingBidding.getBuyingBiddingPrice(); // 입찰 가격 가져옴
        Coupon coupon = null;

        if (buyNowDto.getCouponId() != null) { // 쿠폰 사용 확인, 적용
            coupon = couponRepository.findById(buyNowDto.getCouponId())
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
            CouponIssue userCoupon = couponIssueRepository.findByUsersAndCouponAndUseStatusFalse(
                    user, coupon)
                .orElseThrow(() -> new RuntimeException("Coupon not valid"));
            totalAmount = couponService.applyCoupon(user, coupon, totalAmount)
                .setScale(2, RoundingMode.HALF_UP);

            userCoupon.useCoupon(true);
            userCoupon.useDate();
            couponIssueRepository.save(userCoupon);
        }


        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmount = BigDecimal.ZERO;
        }

        Orders order = Orders.builder() // order 데이터 생성 후 저장
            .user(user)
            .product(buyingBidding.getProduct())
            .buyingBidding(buyingBidding)
            .coupon(coupon)
            .orderStatus((buyingBidding.getBiddingStatus() == BiddingStatus.COMPLETE)
                ? OrderStatus.COMPLETE
                : OrderStatus.WAITING)
            .orderPrice(totalAmount)
            .address((address))
            .build();

        buyingBidding.changeBiddingStatus(BiddingStatus.COMPLETE);
        youOrder.changeOrderStatus(OrderStatus.COMPLETE);

        Long orderId = ordersRepository.save(order).getOrderId();

        return orderId;

    }

    /**
     * 구매입찰 Id로 해당 입찰 데이터 가져오기
     */
    public BuyingBiddingDto getBuyingBiddingDto(Long buyingBiddingId) {
        BuyingBidding buyingBidding = buyingBiddingRepository.findById(buyingBiddingId)
            .orElseThrow(() -> new RuntimeException("BuyingBidding not valid"));

        return BuyingBiddingDto.builder()
            .buyingBiddingId(buyingBidding.getBuyingBiddingId())
            .product(
                OrderProductDto.builder()
                    .productId(buyingBidding.getProduct().getProductId())
                    .productName(buyingBidding.getProduct().getProductName())
                    .productImg(buyingBidding.getProduct().getProductImg())
                    .productBrand(buyingBidding.getProduct().getProductBrand())
                    .modelNum(buyingBidding.getProduct().getModelNum())
                    .productSize(buyingBidding.getProduct().getProductSize())
                    .build())
            .buyingQuantity(buyingBidding.getBuyingQuantity())
            .buyingBiddingPrice(buyingBidding.getBuyingBiddingPrice())
            .buyingBiddingTime(buyingBidding.getBuyingBiddingTime())
            .biddingStatus(buyingBidding.getBiddingStatus())
            .build();
    }


    @Transactional
    public void cancelBuyingBidding(Long userId, Long buyingBiddingId) {
        BuyingBidding buyingBidding = buyingBiddingRepository.findByBuyingBiddingIdAndUserUserId(
                buyingBiddingId, userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매 입찰 내역입니다."));

        buyingBidding.changeBiddingStatus(BiddingStatus.CANCEL);
        buyingBiddingRepository.save(buyingBidding);

        Optional<Orders> optOrder = ordersRepository.findByBuyingBiddingBuyingBiddingId(
            buyingBiddingId);
        optOrder.ifPresent(order -> {
            order.changeOrderStatus(OrderStatus.CANCEL);
            ordersRepository.save(order);
        });
    }


    /**
     * 구매 내역
     * 전체/입찰 중/종료 건수 및 조건별 구매 내역 (상품사진, 상품명, 상품사이즈, 결제금액, 주문상태) 주문날짜 기준 최신순 정렬
     */
    public BuyHistoryAllDto getAllBuyHistory(Long userId) {
        Long allCount = buyingBiddingRepository.countAllByUserUserId(userId);
        Long processCount = buyingBiddingRepository.countProcessByUserId(userId);
        Long completeCount = buyingBiddingRepository.countCompleteByUserId(userId);

        List<BuyDetailsDto> buyDetailsDto = buyingBiddingRepository.findAllBuyDetails(userId);

        return BuyHistoryAllDto.builder()
                .allCount(allCount)
                .processCount(processCount)
                .completeCount(completeCount)
                .buyingDetails(buyDetailsDto)
                .build();
    }

    public List<BuyDetailsProcessDto> getBuyHistoryProcess(Long userId) {
        return buyingBiddingRepository.findBuyDetailsProcess(userId);
    }

    public List<BuyDetailsDto> getBuyHistoryComplete(Long userId) {
        return buyingBiddingRepository.findBuyDetailsComplete(userId);
    }

    public BuyHistoryAllDto getRecentBuyHistory(Long userId) {
        Long allCount = buyingBiddingRepository.countAllByUserUserId(userId);
        Long processCount = buyingBiddingRepository.countProcessByUserId(userId);
        Long completeCount = buyingBiddingRepository.countCompleteByUserId(userId);

        List<BuyDetailsDto> buyDetailsDto = buyingBiddingRepository.findRecentBuyDetails(
                userId, PageRequest.of(0, 3));

        return BuyHistoryAllDto.builder()
                .allCount(allCount)
                .processCount(processCount)
                .completeCount(completeCount)
                .buyingDetails(buyDetailsDto)
                .build();
    }
}
