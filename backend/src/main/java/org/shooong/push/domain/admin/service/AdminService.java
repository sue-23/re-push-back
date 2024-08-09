//package com.example.backend.service;
//
//
//import com.example.backend.dto.admin.*;
//import org.shooong.push.domain.luckyDraw.luckyDraw.dto.LuckyDrawsDto;
//import com.example.backend.dto.mypage.accountSettings.SalesSummaryDto;
//import com.example.backend.dto.mypage.accountSettings.SalesSummaryRespDto;
//import org.shooong.push.domain.luckyDraw.luckyDraw.entity.LuckyDraw;
//import org.shooong.push.domain.product.entity.Product;
//import org.shooong.push.domain.bidding.salesBidding.entity.SalesBidding;
//import org.shooong.push.domain.user.users.entity.Users;
//import com.example.backend.entity.enumData.LuckyProcessStatus;
//import com.example.backend.entity.enumData.ProductStatus;
//import com.example.backend.entity.enumData.SalesStatus;
//import com.example.backend.exception.CustomApiException;
//import org.shooong.push.domain.bidding.salesBidding.repository.SalesBiddingRepository;
//import org.shooong.push.domain.luckyDraw.luckyDraw.repository.LuckyDrawRepository;
//import org.shooong.push.domain.orders.repository.OrdersRepository;
//import org.shooong.push.domain.product.repository.ProductRepository;
//import org.shooong.push.domain.user.users.repository.UserRepository;
//import com.example.backend.service.objectstorage.ObjectStorageService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//@Log4j2
//@Transactional(readOnly = true)
//public class AdminService {
//
//    private final ProductRepository productRepository;
//    private final SalesBiddingRepository salesBiddingRepository;
//    private final LuckyDrawRepository luckyDrawRepository;
//    private final UserRepository userRepository;
//    private final ObjectStorageService objectStorageService;
//    private final OrdersRepository ordersRepository;
//
//    //요청상품 다건 조회
//    public AdminRespDto.ReqProductsRespDto reqProducts(Pageable pageable){
//        Page<Product> products = productRepository.findByProductStatus(ProductStatus.REQUEST,pageable);
//        return new AdminRespDto.ReqProductsRespDto(products);
//    }
//
//    // 요청상품 단건 조회
//    public AdminRespDto.ReqProductRespDto reqProduct(Long productId) {
//        //productId로 상품찾기
//        Optional<Product> reqProduct = productRepository.findById(productId);
//        Product result = reqProduct.orElseThrow();
//        if (result.getModelNum()==null){
//            result.setModelNum("test-modelNum");
//        }
//
//        return new AdminRespDto.ReqProductRespDto(result);
//    }
//
//    @Transactional
//    public void acceptRequest(Long productId, ProductReqDto productReqDto, MultipartFile productPhoto) {
//        String bucketName = "push";
//        String directoryPath = "shooong/products/";
//        Optional<Product> productPs = productRepository.findByProductIdAndProductStatus(productId, ProductStatus.REQUEST);
//        Product request = productPs.orElseThrow();
//
////    Pageable pageable = PageRequest.ofSize(10);
//        List<Product> registerProducts = productRepository.findByProductStatus(ProductStatus.REGISTERED);
//
//        boolean isDuplicate = registerProducts.stream().anyMatch(registered ->
//                productReqDto.getModelNum().equals(registered.getModelNum()) &&
//                        productReqDto.getProductSize().equals(registered.getProductSize()));
//        if (isDuplicate) {
//            throw new RuntimeException("이미 기존 상품으로 등록되어 있습니다.");
//        } else {
//            if (productPhoto != null && !productPhoto.isEmpty()) {
//                String imageUrl = objectStorageService.uploadFile(bucketName, directoryPath, productPhoto);
//                productReqDto.setProductImg(imageUrl);
//            }
//
//            request.registerProduct(productReqDto);
//
//        }
//    }
//
//
//    @Transactional
//    //사용자가 요청한 상품, 중복시 거절
//    //중복 판별은 위에서 함
//    public String deleteRequest(Long productId) {
//
//        Optional<Product> productPs = productRepository.findById(productId);
//        productPs.ifPresent(product -> {product.changeProductStatus(ProductStatus.REJECTED);});
//
//        String message = productId.toString()+"거절 완료";
//
//        return message;
//    }
//
//    //판매상품 관리(카테고리별)조회
//    public AdminProductResponseDto getProducts(String mainDepartment, String subDepartment, Pageable pageable) {
//        Page<AdminProductDto> adminProductDtoPage = productRepository.getProductsByDepartment(mainDepartment, subDepartment, pageable);
//        return new AdminProductResponseDto(mainDepartment, subDepartment, adminProductDtoPage);
//    }
//
//    //상품상세 조회 + 판매입찰 + 구매입찰 정보
//    public AdminRespDto.AdminProductDetailRespDto getDetailProduct(String modelNum, String productSize) {
//
//        log.info("modelNum{} productSize{}", modelNum, productSize);
//        /*
//        1. modelNum 기준으로 중복없이 상품 상세 정보 조회,
//        2. 상품 사이즈별로 수량,입찰(판매, 구매) 정보 같이 가져오기
//        * */
//        List<AdminProductRespDto> detailedProduct = productRepository.getDetailedProduct(modelNum,productSize);
//
//        return new AdminRespDto.AdminProductDetailRespDto(modelNum, productSize, detailedProduct);
//    }
//
//    //검수 승인 처리
//    @Transactional
//    public AdminRespDto.ChangeRespDto acceptSales(Long salesBiddingId) {
//
//        //해당 id의 판매입찰 정보 찾기
//        Optional<SalesBidding> salesBidding = salesBiddingRepository.findById(salesBiddingId);
//        SalesBidding acceptSales = salesBidding.orElseThrow();
//
//        //판매입찰 상태 검수 -> 판매중으로 변경
//        acceptSales.changeSalesStatus(SalesStatus.PROCESS);
//
//        //판매입찰의 상품아이디 가져오기
//        Long productId = acceptSales.getProduct().getProductId();
//
//        //해당 상품의 productId를 가진 상품의 수량 증가
//        Optional<Product> selectProduct = productRepository.findById(productId);
//        Product product = selectProduct.orElseThrow();
//        product.addQuantity(1);
//
//        return new AdminRespDto.ChangeRespDto(acceptSales, product);
//
//    }
//
//    //관리자 럭키드로우 상품 등록
//    @Transactional
//    public AdminReqDto.AdminLuckDrawDto insertLucky(AdminReqDto.AdminLuckDrawDto adminLuckDrawDto) {
//
//        String bucketName = "push";
//        String directoryPath = "shooong/luckydraw";
//        // S3에 이미지 업로드
//        String imageUrl = objectStorageService.uploadFile(bucketName, directoryPath, adminLuckDrawDto.getLuckyphoto());
//
//        //관리자가 luckyDrawDto 폼에 등록한 변수
//        LuckyDraw luckyDraw = LuckyDraw.builder()
//                .luckyName(adminLuckDrawDto.getLuckyName())
//                .content(adminLuckDrawDto.getContent())
//                .luckyImage(imageUrl)
//                .luckyProcessStatus(LuckyProcessStatus.READY)
//                .luckyPeople(adminLuckDrawDto.getLuckyPeople())
//                .build();
//
//        LuckyDraw insertLucky = luckyDrawRepository.save(luckyDraw);
//        return new AdminReqDto.AdminLuckDrawDto(insertLucky);
//    }
//
//    //test
//    //매주 첫째주 11시에 시작
//    //럭키 드로우 상품 상태 READY -> PROCESS
//    @Scheduled(cron = "0 30 20 ? * FRI")
////    @Scheduled(cron = "0 52 14 * * MON")
//    @Transactional
//    public void cronJob() {
//        //스케줄 실행시, 데이터 베이스에 저장되어 있는 럭키드로우 데이터 startDate, endDate, LuckDate 등록
//
//        //LuckyProcessStatus = READY인 상품 조회
//        List<LuckyDraw> ready = luckyDrawRepository.findByLuckyProcessStatus(LuckyProcessStatus.READY);
//
//        //시작 날짜 매주 월요일 11:00:00
////        LocalDateTime startDate = LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0);
//        LocalDateTime startDate = LocalDateTime.now();
//        // 마감 날짜 시작 날짜 + 7
////        LocalDateTime endDate = startDate.plusDays(7).withHour(11).withMinute(0).withSecond(0).withNano(0);
//        LocalDateTime endDate = startDate.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
//        // 발표일 : 마감 날짜 + 1 18:00:00
//        LocalDateTime luckDate = endDate.plusDays(1).withHour(0).withMinute(1).withSecond(0).withNano(0);
//        //상품 등록
//        for (LuckyDraw luckyDraw : ready) {
//            luckyDraw.changeDate(startDate, endDate, luckDate);
//            luckyDraw.changeLuckyProcessStatus(LuckyProcessStatus.PROCESS);
//        }
//
//    }
//
//    //관리자 페이지 럭키드로우 상품 다건 조회
//    public AdminRespDto.LuckyDrawsRespDto getLuckyDraws(LuckyProcessStatus luckyProcessStatus, Pageable pageable) {
//
//        Page<LuckyDraw> luckyDrawList = luckyDrawRepository.findByLuckyProcessStatus(luckyProcessStatus, pageable);
//
//        return new AdminRespDto.LuckyDrawsRespDto(luckyProcessStatus,luckyDrawList);
//
//    }
//
//    //관리자 페이지 럭키드로우 상품 단건 조회
//    public LuckyDrawsDto getLucky(Long luckyId) {
//
//        LuckyDraw luckyDraw = luckyDrawRepository.findById(luckyId).orElseThrow(
//                () -> new CustomApiException("해당 럭키드로우를 찾을 수 없습니다."));
//
//        return LuckyDrawsDto.fromEntity(luckyDraw);
//
//    }
//
//    //마이 페이지, 판매 내역 정산
//    public SalesSummaryRespDto getSalesSummary(Long userId,Pageable pageable) {
//
//        Page<SalesSummaryDto> salesSummary = ordersRepository.findSalesHistoryByUserId(userId,pageable);
//        BigDecimal totalSalesPrice = ordersRepository.findTotalSalesAmountByUserId(userId);
//
//
//        return SalesSummaryRespDto.builder()
//                .totalSalesPrice(totalSalesPrice)
//                .totalSalesCount(salesSummary.getTotalElements())
//                .salesSummaryList(salesSummary)
//                .build();
//
//    }
//
//
//
//}
//
