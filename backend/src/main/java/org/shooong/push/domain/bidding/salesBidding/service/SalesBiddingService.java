package org.shooong.push.domain.bidding.salesBidding.service;

import static org.shooong.push.domain.enumData.SalesStatus.INSPECTION;

import org.shooong.push.domain.user.mypage.dto.saleHistory.SaleDetailsDto;
import org.shooong.push.domain.user.mypage.dto.saleHistory.SaleHistoryDto;
import org.shooong.push.domain.user.mypage.dto.saleHistory.SalesStatusCountDto;
import org.shooong.push.domain.orders.dto.SaleOrderDto;
import org.shooong.push.domain.orders.dto.SalesBiddingDto;
import org.shooong.push.domain.orders.dto.OrderProductDto;
import org.shooong.push.domain.orders.dto.SalesNowDto;
import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.address.entity.Address;
import org.shooong.push.domain.bidding.buyingBidding.entity.BuyingBidding;
import org.shooong.push.domain.coupon.entity.Coupon;
import org.shooong.push.domain.orders.entity.Orders;
import org.shooong.push.domain.bidding.salesBidding.entity.SalesBidding;
import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.user.users.entity.Users;
import org.shooong.push.domain.enumData.BiddingStatus;
import org.shooong.push.domain.enumData.OrderStatus;
import org.shooong.push.domain.enumData.SalesStatus;
import org.shooong.push.domain.bidding.buyingBidding.repository.BuyingBiddingRepository;
import org.shooong.push.domain.bidding.salesBidding.repository.SalesBiddingRepository;
import org.shooong.push.domain.orders.repository.OrdersRepository;
import org.shooong.push.domain.product.repository.ProductRepository;
import org.shooong.push.domain.user.users.repository.UserRepository;
import org.shooong.push.domain.address.repository.AddressRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class    SalesBiddingService {

    private final SalesBiddingRepository salesBiddingRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrdersRepository ordersRepository;
    private final BuyingBiddingRepository buyingBiddingRepository;

    /**
     * 판매 입찰 등록
     */
//    public void registerSalesBidding(UserDTO userDTO, BiddingRequestDto salesInfo) {
//        Product product = productRepository.findById(salesInfo.getProductId())
//            .orElseThrow(() -> new RuntimeException("Product not valid"));
//
//        Users user = userRepository.findById(userDTO.getUserId())
//            .orElseThrow(() -> new RuntimeException("User not valid"));
//
//        SalesBidding salesBidding = SalesBidding.builder()
//            .salesBiddingPrice(salesInfo.getPrice())
//            .product(product)
//            .user(user)
//            .salesBiddingTime(LocalDateTime
//                .now().plusDays(salesInfo.getExp()))
//            .salesStatus(INSPECTION)
//            .build();
//
//        salesBiddingRepository.save(salesBidding);
//    }

    // 판매입찰 등록
    @Transactional
    public Long registerSalesBidding(UserDTO userDTO, SaleOrderDto saleOrderDto) {
        Product product = productRepository.findById(saleOrderDto.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not valid"));

        Users user = userRepository.findById(userDTO.getUserId())
            .orElseThrow(() -> new RuntimeException("User not valid"));



        SalesBidding salesBidding = SalesBidding.builder()
            .salesBiddingPrice(saleOrderDto.getPrice())
            .product(product)
            .user(user)
            .salesBiddingTime(LocalDateTime
                .now().plusDays(saleOrderDto.getExp()))
            .salesStatus(INSPECTION)
            .build();

        salesBiddingRepository.save(salesBidding);

        Address address = addressRepository.findById(saleOrderDto.getAddressId()).orElseThrow(()->new RuntimeException("Address not found"));

        BigDecimal totalAmount = salesBidding.getSalesBiddingPrice();
//        Coupon coupon = null;

        // SalesBidding의 상태에 따라 OrderStatus 설정

        Orders order = Orders.builder()
            .user(user)
            .product(salesBidding.getProduct())
            .salesBidding(salesBidding)
//            .coupon(coupon)
            .orderStatus(
                (salesBidding.getSalesStatus() == SalesStatus.COMPLETE)
                    ? OrderStatus.COMPLETE
                    : OrderStatus.WAITING)
            .orderPrice(totalAmount)
            .address(address)
            .build();

//        salesBidding.changeSalesStatus(SalesStatus.COMPLETE);


        return ordersRepository.save(order).getOrderId();

    }

    @Transactional
    public Long applySaleNow(UserDTO userDTO, SalesNowDto salesNowDto ) {
        BuyingBidding buyingBidding = buyingBiddingRepository.findById(salesNowDto.getBuyingBiddingId()) // 판매입찰 데이터 가져오기
            .orElseThrow(() -> new RuntimeException("buyingBidding not valid"));
        Users user = userRepository.findById(userDTO.getUserId())
            .orElseThrow(() -> new RuntimeException("User not valid"));

        Orders youOrder = ordersRepository.findByBuyingBiddingId(salesNowDto.getBuyingBiddingId()).orElseThrow(()->new RuntimeException("Order not found"));

        SalesBidding salesBidding = SalesBidding.builder() // 즉시구매 데이터 생성
            .user(user)
            .product(buyingBidding.getProduct())
            .salesBiddingPrice(buyingBidding.getBuyingBiddingPrice())
            .salesQuantity(buyingBidding.getBuyingQuantity())
            .salesBiddingTime(LocalDateTime.now())
            .salesStatus(SalesStatus.COMPLETE)
            .build();

        buyingBidding.changeBiddingStatus(BiddingStatus.COMPLETE); // 해당 판매입찰건 완료

        salesBiddingRepository.save(salesBidding);

        Address address = addressRepository.findById(salesNowDto.getAddressId()).orElseThrow(()->new RuntimeException("Address not found"));

        BigDecimal totalAmount = buyingBidding.getBuyingBiddingPrice(); // 입찰 가격 가져옴
        Coupon coupon = null;


        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmount = BigDecimal.ZERO;
        }

        Orders order = Orders.builder() // order 데이터 생성 후 저장
            .user(user)
            .product(buyingBidding.getProduct())
            .salesBidding(salesBidding)
            .coupon(coupon)
            .orderStatus((salesBidding.getSalesStatus() == SalesStatus.COMPLETE)
                ? OrderStatus.COMPLETE
                : OrderStatus.WAITING)
            .orderPrice(totalAmount)
            .address((address))
            .build();

        youOrder.changeOrderStatus(OrderStatus.COMPLETE);


        Long orderId = ordersRepository.save(order).getOrderId();

        return orderId;
    }

    /**
     * 구매입찰 Id로 해당 입찰 데이터 가져오기
     */
    public SalesBiddingDto getSalesBiddingDto(Long salesBiddingId) {
        SalesBidding salesBidding = salesBiddingRepository.findById(salesBiddingId)
            .orElseThrow(() -> new RuntimeException("SalesBidding not valid"));

        return SalesBiddingDto.builder()
            .salesBiddingId(salesBidding.getSalesBiddingId())
            .product(
                OrderProductDto.builder()
                    .productId(salesBidding.getProduct().getProductId())
                    .productName(salesBidding.getProduct().getProductName())
                    .productImg(salesBidding.getProduct().getProductImg())
                    .productBrand(salesBidding.getProduct().getProductBrand())
                    .modelNum(salesBidding.getProduct().getModelNum())
                    .productSize(salesBidding.getProduct().getProductSize())
                    .build())
            .salesQuantity(salesBidding.getSalesQuantity())
            .salesBiddingPrice(salesBidding.getSalesBiddingPrice())
            .salesBiddingTime(salesBidding.getSalesBiddingTime())
            .salesStatus(salesBidding.getSalesStatus())
            .build();
    }


    /**
     * 판매 내역
     * 전체, 진행 중, 종료 건수
     * 퍈매 내역 (상품사진, 상품명, 상품사이즈, 판매단가, 판매상태)
     * 판매 입찰 시간 기준 최신순 정렬
     */
    public SaleHistoryDto getSaleHistory(Long userId) {
        Long allCount = salesBiddingRepository.countAllByUserUserId(userId);
        List<SalesStatusCountDto> SalesStatusCountDto = salesBiddingRepository.countSalesStatus(userId);
        List<SaleDetailsDto> saleDetailsDTO = salesBiddingRepository.findSaleDetailsByUserId(userId);

        return SaleHistoryDto.builder()
                .allCount(allCount)
                .salesStatusCounts(SalesStatusCountDto)
                .saleDetails(saleDetailsDTO)
                .build();
    }

    public SaleHistoryDto getRecentSaleHistory(Long userId) {
        Long allCount = salesBiddingRepository.countAllByUserUserId(userId);
        List<SalesStatusCountDto> SalesStatusCountDto = salesBiddingRepository.countSalesStatus(userId);
        List<SaleDetailsDto> saleDetailsDTO = salesBiddingRepository.findRecentSaleDetailsByUserId(userId, PageRequest.of(0, 3));

        return SaleHistoryDto.builder()
                .allCount(allCount)
                .salesStatusCounts(SalesStatusCountDto)
                .saleDetails(saleDetailsDTO)
                .build();
    }

    @Transactional
    public void cancelSalesBidding(Long userId, Long salesBiddingId) {
        SalesBidding salesBidding = salesBiddingRepository.findBySalesBiddingIdAndUserUserId(salesBiddingId, userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 판매 입찰 내역입니다."));

        salesBidding.changeSalesStatus(SalesStatus.CANCEL);
        salesBiddingRepository.save(salesBidding);
    }
}
