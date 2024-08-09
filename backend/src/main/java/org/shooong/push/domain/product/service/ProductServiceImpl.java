package org.shooong.push.domain.product.service;

import org.shooong.push.domain.admin.dto.ProductRespDto;
import org.shooong.push.domain.orders.dto.OrderProductDto;
import org.shooong.push.domain.product.dto.*;
import org.shooong.push.domain.product.dto.Detail.*;
import org.shooong.push.domain.bidding.buyingBidding.repository.BuyingBiddingRepository;
import org.shooong.push.domain.bidding.salesBidding.repository.SalesBiddingRepository;
import org.shooong.push.domain.photoReview.repository.PhotoReviewRepository;
import org.shooong.push.domain.product.repository.ProductRepository;
import org.shooong.push.domain.user.users.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.shooong.push.domain.bidding.buyingBidding.entity.BuyingBidding;
import org.shooong.push.domain.bidding.salesBidding.entity.SalesBidding;
import org.shooong.push.domain.photoReview.entity.PhotoReview;
import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.user.users.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BuyingBiddingRepository buyingBiddingRepository;
    private final PhotoReviewRepository photoReviewRepository;
    private final UserRepository userRepository;
    private final SalesBiddingRepository salesBiddingRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              BuyingBiddingRepository buyingBiddingRepository,
                              PhotoReviewRepository photoReviewRepository,
                              UserRepository userRepository,
                              SalesBiddingRepository salesBiddingRepository) {
        this.productRepository = productRepository;
        this.buyingBiddingRepository = buyingBiddingRepository;
        this.photoReviewRepository = photoReviewRepository;
        this.userRepository = userRepository;
        this.salesBiddingRepository = salesBiddingRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    private LocalDateTime lastCheckedTime;
    private boolean isUpdated = false;

    //실제 판매중인 상품 대분류별 조회
    public List<ProductRespDto> findProductsByDepartment(String mainDepartment) {

        return productRepository.findProductsByDepartment(mainDepartment);
    }

    // 존재하는 대분류 중 최신순으로 조회
    @Override
    public List<ProductResponseDto> getAllProducts(String mainDepartment) {

        return productRepository.searchAllProduct(mainDepartment);
    }

    @Override
    public List<ProductResponseDto> getAllProductsManyBid(String mainDepartment) {
        return productRepository.searchAllProductManyBid(mainDepartment);
    }

    @Override
    public List<ProductResponseDto> getAllProductsNewBuyBid(String mainDepartment) {
        return productRepository.searchAllProductNewBuying(mainDepartment);
    }

    @Override
    public List<ProductResponseDto> getAllProductsNewSaleBid(String mainDepartment) {
        return productRepository.searchAllProductNewSelling(mainDepartment);
    }

    // 상품 소분류 조회
    @Override
    @Transactional(readOnly = true)
    public Slice<ProductResponseDto> selectCategoryValue(String subDepartment, Pageable pageable) {
        return productRepository.subProductInfo(subDepartment, pageable);
    }

    // 상품의 상세정보 조회
    @Override
    @Transactional
    public ProductDetailDto productDetailInfo(String modelNum) {
        log.info("modelNum : {}", modelNum);

        List<Product> products = productRepository.findAllByModelNumAndStatus(modelNum);

        if (!products.isEmpty()) {
            // 가장 먼저 나온 결과를 사용하거나, 추가 조건을 통해 단일 결과 선택
            Product product = products.get(0);

            ProductDetailDto priceValue = productRepository.searchProductPrice(modelNum);

            log.info("priceValue : {}", priceValue);

            List<ProductsContractListDto> contractInfoList = selectSalesContract(modelNum);

            log.info("contractInfoList : {}", contractInfoList);

            List<SalesHopeDto> salesHopeDtoList = selectSalesHope(modelNum);

            log.info("salesHopeDtoList : {}", salesHopeDtoList);

            List<BuyingHopeDto> buyingHopeDtoList = selectBuyingHope(modelNum);

            log.info("buyingHopeDtoList : {}", buyingHopeDtoList);

            List<PhotoReviewDto> photoReviewDtoList = selectPhotoReview(modelNum);

            log.info("photoReviewDtoList : {}", photoReviewDtoList);

            List<GroupByBuyingDto> groupByBuyingDtoList = productRepository.groupByBuyingSize(modelNum);

            log.info("groupByBuyingDtoList : {}", groupByBuyingDtoList);

            List<GroupBySalesDto> groupBySalesDtoList = productRepository.groupBySalesSize(modelNum);

            log.info("groupBySalesDtoList : {}", groupBySalesDtoList);

            RecentlyPriceDto recentlyContractPrice = selectRecentlyPrice(modelNum);

            log.info("recentlyContractPrice : {}", recentlyContractPrice);

            AveragePriceResponseDto averagePriceResponseDtoList = getAveragePrices(modelNum);

            log.info("averagePriceResponseDtoList : {}", averagePriceResponseDtoList);


            // 업데이트(SaleStatus=COMPLETE)가 이뤄지면 상세 상품 변환 이 안나옴
            // 빈 리스트 또는 기본 값 설정
            if (contractInfoList == null) {
                contractInfoList = new ArrayList<>();
            }
            if (salesHopeDtoList == null) {
                salesHopeDtoList = new ArrayList<>();
            }
            if (buyingHopeDtoList == null) {
                buyingHopeDtoList = new ArrayList<>();
            }
            if (photoReviewDtoList == null) {
                photoReviewDtoList = new ArrayList<>();
            }
            if (groupByBuyingDtoList == null) {
                groupByBuyingDtoList = new ArrayList<>();
            }
            if (groupBySalesDtoList == null) {
                groupBySalesDtoList = new ArrayList<>();
            }
            if (recentlyContractPrice == null) {
                recentlyContractPrice = new RecentlyPriceDto();
            }
            if (averagePriceResponseDtoList == null) {
                averagePriceResponseDtoList = new AveragePriceResponseDto();
            }

            ProductDetailDto productDetailDto = ProductDetailDto.builder()
                    .productId(product.getProductId())
                    .productImg(product.getProductImg())
                    .productBrand(product.getProductBrand())
                    .modelNum(product.getModelNum())
                    .productName(product.getProductName())
                    .createDate(product.getCreateDate())
                    .originalPrice(product.getOriginalPrice())
                    .productLike(product.getProductLike())
                    .subDepartment(product.getSubDepartment())

                    .buyingBiddingPrice(priceValue.getBuyingBiddingPrice())
                    .salesBiddingPrice(priceValue.getSalesBiddingPrice())

                    .latestPrice(recentlyContractPrice.getLatestPrice())
                    .previousPrice(recentlyContractPrice.getPreviousPrice())
                    .changePercentage(recentlyContractPrice.getChangePercentage())
                    .recentlyContractDate(recentlyContractPrice.getSalesBiddingTime())
                    .differenceContract(recentlyContractPrice.getDifferenceContract())

                    .contractInfoList(contractInfoList)
                    .buyingHopeList(buyingHopeDtoList)
                    .salesHopeList(salesHopeDtoList)

                    .photoReviewList(photoReviewDtoList)

                    .groupByBuyingList(groupByBuyingDtoList)
                    .groupBySalesList(groupBySalesDtoList)

                    .averagePriceResponseList(averagePriceResponseDtoList)
                    .build();

            log.info("상세상품 변환 완료 : {}", productDetailDto);
            return productDetailDto;
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateDate(Long recentlyProductId) {
        if (isUpdated) {
            productRepository.findProductsByProductId(recentlyProductId)
                    .ifPresent(product -> {
                        product.updateLatestDate(LocalDateTime.now());
                        productRepository.save(product);
                        log.info("서버 종료 시점 저장 완료 : {}", LocalDateTime.now());
                        productRepository.flush();
                        entityManager.clear();
                    });
        }
    }

    // 최근 체결가 계산
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RecentlyPriceDto selectRecentlyPrice(String modelNum) {
        Optional<Product> oldContractValue = productRepository.findFirstByModelNumOrderByLatestDateDesc(modelNum);
        lastCheckedTime = oldContractValue.map(Product::getLatestDate).orElse(LocalDateTime.now());
        log.info("!!! 서버가 마지막까지 유지했던 시간 : {}", lastCheckedTime);

        List<SalesBiddingDto> newAllContractSelect = productRepository.recentlyTransaction(modelNum);
        log.info("최근 체결 내역 조회 : {} ", newAllContractSelect.toString());
        if (newAllContractSelect.isEmpty()) {
            log.info("체결된 거래가 없습니다.");
            return new RecentlyPriceDto();
        }

        SalesBiddingDto recentlyContractValue = newAllContractSelect.get(0);
        LocalDateTime recentlyContractTime = recentlyContractValue.getSalesBiddingTime();
        log.info("최근 체결 내역 시간 : {}", recentlyContractTime);

        RecentlyPriceDto recentlyPriceDto = RecentlyPriceDto.builder()
                .latestPrice(recentlyContractValue.getSalesBiddingPrice())
                .salesBiddingTime(recentlyContractTime)
                .salesBiddingPrice(recentlyContractValue.getSalesBiddingPrice())
                .build();

        if (lastCheckedTime.isBefore(recentlyContractTime)) {
            for (SalesBiddingDto product : newAllContractSelect) {
                if (product.getPreviousPrice() == null || product.getPreviousPercentage() == null) {
                    productRepository.resetPreviousPrice(product.getProductId());
                    log.info("기본값 설정 완료");
                }
            }
            List<Product> products = productRepository.findByModelNum(modelNum);
            for (Product product : products) {
                BigDecimal recentlyContractPrice = recentlyContractValue.getSalesBiddingPrice();
                BigDecimal previousContractPrice = product.getLatestPrice();

                log.info("업데이트 전 productId : {}, recentlyContractPrice : {}", product.getProductId(), recentlyContractPrice);

                if (previousContractPrice != null) {
                    productRepository.updatePreviousPrice(product.getProductId(), previousContractPrice);
                    log.info("Updated previousPrice for productId: {} with price: {}", product.getProductId(), previousContractPrice);
                } else {
                    log.warn("previousContractPrice is null, skipping update for previousPrice");
                }
                productRepository.updateLatestPriceAndDate(product.getProductId(), recentlyContractPrice, recentlyContractTime);
                log.info("Updated latestPrice for productId: {}", product.getProductId());

                BigDecimal result = recentlyContractPrice.subtract(previousContractPrice != null ? previousContractPrice : BigDecimal.ZERO);
                BigDecimal changePercentageBD = BigDecimal.ZERO;
                if (previousContractPrice != null && previousContractPrice.compareTo(BigDecimal.ZERO) != 0) {
                    changePercentageBD = recentlyContractPrice.subtract(previousContractPrice)
                            .divide(previousContractPrice, MathContext.DECIMAL128)
                            .multiply(BigDecimal.valueOf(100));
                }

                Long resultAsLong = result.longValueExact();

                double changePercentage = changePercentageBD.doubleValue();
                DecimalFormat df = new DecimalFormat("#.#");
                String format = df.format(changePercentage);
                double finalChangePercentage = Double.parseDouble(format);
                productRepository.updateRecentlyContractPercentage(product.getProductId(), finalChangePercentage);
                productRepository.updateDifferenceContract(product.getProductId(), resultAsLong);

                recentlyPriceDto.setDifferenceContract(resultAsLong);
                recentlyPriceDto.setChangePercentage(finalChangePercentage);
                recentlyPriceDto.setPreviousPrice(previousContractPrice);
            }

            lastCheckedTime = recentlyContractTime;
            isUpdated = true;
            log.info("최근 체결 내역 업데이트 완료");
        } else {
            log.info("현재 등록된 거래가 최신입니다.");
            return RecentlyPriceDto.builder()
                    .latestPrice(oldContractValue.get().getLatestPrice())
                    .differenceContract(oldContractValue.get().getDifferenceContract())
                    .previousPrice(oldContractValue.get().getPreviousPrice())
                    .changePercentage(oldContractValue.get().getPreviousPercentage())
                    .salesBiddingTime(oldContractValue.get().getLatestDate())
                    .salesBiddingPrice(oldContractValue.get().getLatestPrice())
                    .build();
        }
        return recentlyPriceDto;
    }



    // 체결 내역 관리(리스트)
    @Override
    public List<ProductsContractListDto> selectSalesContract(String modelNum) {

        List<SalesBiddingDto> temp = productRepository.recentlyTransaction(modelNum);

        return temp.stream()
                .map(contractValue -> ProductsContractListDto.builder()
                        .productSize(contractValue.getProductSize())
                        .productContractPrice(contractValue.getSalesBiddingPrice())
                        .productContractDate(contractValue.getSalesBiddingTime())
                        .build())
                .collect(Collectors.toList());
    }

    // 입찰 판매 희망 내역(리스트)
    @Override
    public List<SalesHopeDto> selectSalesHope(String modelNum) {
        List<SalesHopeDto> temp = productRepository.salesHopeInfo(modelNum);

        // LinkedHashMap 사용하여 순서 보장
        Map<String, SalesHopeDto> groupedResults = new LinkedHashMap<>();

        for (SalesHopeDto hope : temp) {
            // 동일한 사이즈와 가격을 기준으로 키 생성
            String key = hope.getProductSize() + "-" + hope.getSalesBiddingPrice();

            // 이미 존재하는 항목이면 수량 증가, 아니면 새로 추가
            if (groupedResults.containsKey(key)) {
                SalesHopeDto existing = groupedResults.get(key);
                existing.setSalesQuantity(existing.getSalesQuantity() + hope.getSalesQuantity());
            } else {
                groupedResults.put(key, hope);
            }
        }
        // 결과를 다시 리스트로 변환
        List<SalesHopeDto> resultList = new ArrayList<>(groupedResults.values());

        log.info("값 리스트로 변환 확인 : {}, ", resultList);
        return resultList;
    }

    // 입찰 구매 희망 내역(리스트)
    @Override
    public List<BuyingHopeDto> selectBuyingHope(String modelNum) {
        List<BuyingHopeDto> temp = productRepository.buyingHopeInfo(modelNum);

        log.info("구매 입찰 내역 확인 : {} ", temp);
        Map<String, BuyingHopeDto> groupedResults = new LinkedHashMap<>();

        Optional<BuyingHopeDto> lowestPrice = temp.stream()
                .min(Comparator.comparing(BuyingHopeDto::getBuyingBiddingPrice));

        if (lowestPrice.isPresent()) {
            BuyingHopeDto lowest = lowestPrice.get();
            log.info("최저가 항목 : {}", lowest);
        }

        for (BuyingHopeDto hope : temp) {
            // 동일한 사이즈와 가격을 기준으로 키 생성
            String key = hope.getProductSize() + "-" + hope.getBuyingBiddingPrice();

            // 이미 존재하는 항목이면 수량 증가, 아니면 새로 추가
            groupedResults.merge(key, hope, (existing, newHope) -> {
                existing.setBuyingQuantity(existing.getBuyingQuantity() + newHope.getBuyingQuantity());
                return existing;
            });
        }
        // 결과를 다시 리스트로 변환
        List<BuyingHopeDto> resultList = new ArrayList<>(groupedResults.values());

        log.info("리스트로 변환 : {}, ", resultList);


        return resultList.stream()
                .map(this::convertBuyingDto)
                .collect(Collectors.toList());
    }

    private BuyingHopeDto convertBuyingDto(BuyingHopeDto hope) {
        return BuyingHopeDto.builder()
                .productSize(hope.getProductSize())
                .buyingQuantity(hope.getBuyingQuantity())
                .buyingBiddingPrice(hope.getBuyingBiddingPrice())
                .build();
    }

    @Transactional
    public void addPhotoReview(PhotoRequestDto photoRequestDto) {
        Product product = productRepository.findFirstByModelNum(photoRequestDto.getModelNum())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + photoRequestDto.getModelNum()));

        Users user = userRepository.findById(photoRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + photoRequestDto.getUserId()));

        PhotoReview photoReview = PhotoReview.builder()
                .products(product)
                .user(user)
                .reviewLike(0)
                .reviewImg(photoRequestDto.getReviewImg())
                .reviewId(photoRequestDto.getReviewId())
                .reviewContent(photoRequestDto.getReviewContent())
                .build();

        photoReviewRepository.save(photoReview);
        log.info("성공!!");
    }

    @Transactional
    public void updatePhotoReview(PhotoRequestDto photoRequestDto) {
        PhotoReview review = photoReviewRepository.findById(photoRequestDto.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다: " + photoRequestDto.getReviewId()));

        Product product = productRepository.findFirstByModelNum(photoRequestDto.getModelNum())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + photoRequestDto.getModelNum()));

        Users user = userRepository.findById(photoRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + photoRequestDto.getUserId()));

        if (!review.getUser().getUserId().equals(photoRequestDto.getUserId())) {
            throw new IllegalArgumentException("해당 리뷰를 수정할 권한이 없습니다.");
        }
        // 리뷰 수정
        PhotoReview photoReview = PhotoReview.builder()
                .products(product)
                .user(user)
                .reviewId(review.getReviewId())
                .reviewLike(0)
                .reviewImg(photoRequestDto.getReviewImg())
                .reviewContent(photoRequestDto.getReviewContent())
                .build();

        photoReviewRepository.save(photoReview);
        log.info("리뷰가 성공적으로 수정되었습니다.");
    }

    @Override
    public void deletePhotoReview(Long reviewId, Long userId) {
        PhotoReview photoReview = photoReviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if (!photoReview.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 리뷰를 삭제할 권한이 없습니다.");
        }
        photoReviewRepository.delete(photoReview);
    }

    // 해당 상품에 대한 스타일 리뷰 조회(리스트)
    @Override
    public List<PhotoReviewDto> selectPhotoReview(String modelNum) {
        List<PhotoReview> photoReviewList = photoReviewRepository.findProductStyleReview(modelNum);

        return photoReviewList.stream()
                .map(productStyleReview -> PhotoReviewDto.builder()
                        .userId(productStyleReview.getUser().getUserId())
                        .reviewImg(productStyleReview.getReviewImg())
                        .reviewContent(productStyleReview.getReviewContent())
                        .reviewLike(productStyleReview.getReviewLike())
                        .build())
                .collect(Collectors.toList());
    }

    // 상세 상품의 거래 체결 조회
    @Override
    public BidResponseDto selectBidInfo(BidRequestDto bidRequestDto) {

        Long userId = bidRequestDto.getUserId();
        boolean check = userRepository.existsByUserId(userId);
        if (check) {
            log.info("해당 계정은 합격");
            // 상품 기본정보 뽑기
            Optional<Product> products = productRepository.findBidProductInfo(bidRequestDto.getModelNum(), bidRequestDto.getProductSize());
            log.info("상품의 기본 정보 확인 : {}", products);

            if (products.isEmpty()) {
                log.info("해당 상품의 모델번호나 사이즈가 일치하지 않습니다.");
                throw new IllegalArgumentException("해당 상품의 모델번호나 사이즈가 일치하지 않습니다.");
            }
            // 해당 상품의 사이즈에 대한 가격 뽑기, 구매 / 판매 둘다
            BidResponseDto bidResponseDto = productRepository.BuyingBidResponse(bidRequestDto);
            if (bidResponseDto == null) {
                log.info("해당 상품의 가격이 존재하지 않습니다.");
                throw new IllegalArgumentException("해당 상품의 가격이 존재하지 않습니다.");
            }
            log.info("해당 사이즈에 대한 가격 뽑기 : {}", bidResponseDto);


            return BidResponseDto.builder()
                    .productImg(products.get().getProductImg())
                    .productName(products.get().getProductName())
                    .productSize(products.get().getProductSize())
                    .productBuyPrice(bidResponseDto.getProductBuyPrice())
                    .productSalePrice(bidResponseDto.getProductSalePrice())
                    .build();
        }
        return null;
    }

    @Override
    public void saveTemporaryBid(InsertBidDto insertBidDto) {
        Users user = userRepository.findById(insertBidDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID 입니다."));
        Product product = productRepository.findBidProductInfo(insertBidDto.getModelNum(), insertBidDto.getSize())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품 ID 입니다."));

        if (insertBidDto.getType().equals("buy")) {
            BuyingBidding buyingBidding = insertBidDto.toBuyingBidding(user, product);
            buyingBiddingRepository.save(buyingBidding);
        } else if (insertBidDto.getType().equals("sale")) {
            SalesBidding salesBidding = insertBidDto.toSalesBidding(user, product);
            salesBiddingRepository.save(salesBidding);
        }
    }

    @Override
    @Transactional
    public AveragePriceResponseDto getAveragePrices(String modelNum) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime temp = LocalDateTime.of(2022, 1, 1, 0, 0, 0);

        List<AveragePriceDto> allContractData = productRepository.getAllContractData(modelNum, temp, now);

        List<AveragePriceDto> threeDayPrices = calculateAveragePrice(allContractData, now.minusDays(3), now, 3);
        List<AveragePriceDto> oneMonthPrices = calculateAveragePrice(allContractData, now.minusMonths(1), now, 24);
        List<AveragePriceDto> sixMonthPrices = calculateAveragePrice(allContractData, now.minusMonths(6), now, 168);
        List<AveragePriceDto> oneYearPrices = calculateAveragePrice(allContractData, now.minusYears(1), now, 720);
        List<AveragePriceDto> totalExecutionPrice = calculateAveragePrice(allContractData, now.minusYears(2), now, 720);

        return AveragePriceResponseDto.builder()
                .threeDayPrices(threeDayPrices)
                .oneMonthPrices(oneMonthPrices)
                .sixMonthPrices(sixMonthPrices)
                .oneYearPrices(oneYearPrices)
                .totalExecutionPrice(totalExecutionPrice)
                .build();
    }


    @Override
    @Transactional
    public List<AveragePriceDto> calculateAveragePrice(List<AveragePriceDto> allContractData, LocalDateTime firstContractDateTime, LocalDateTime endDate, int intervalHours) {
        List<AveragePriceDto> result = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");

        while (firstContractDateTime.isBefore(endDate)) {
            LocalDateTime nextInterval = firstContractDateTime.plusHours(intervalHours);

            List<AveragePriceDto> intervalData = getAllContractData(allContractData, firstContractDateTime, nextInterval);

            if (intervalData.isEmpty()) {
                result.add(new AveragePriceDto(firstContractDateTime, BigDecimal.ZERO)); // 체결 내역이 없으면 0
            } else {
                BigDecimal sum = BigDecimal.ZERO;
                for (AveragePriceDto data : intervalData) {
                    if (data.getAveragePrice() != null) {
                        sum = sum.add(data.getAveragePrice());
                    }
                }
                BigDecimal average = sum.divide(BigDecimal.valueOf(intervalData.size()), MathContext.DECIMAL128);

                BigDecimal formattedAverage = new BigDecimal(df.format(average));
                result.add(new AveragePriceDto(firstContractDateTime, formattedAverage));
            }

            firstContractDateTime = nextInterval;
        }
        log.info("result : {}", result.toString());

        return result;
    }


    public List<AveragePriceDto> getAllContractData(List<AveragePriceDto> allContractData, LocalDateTime startDate, LocalDateTime endDate) {
        return allContractData.stream()
                .filter(data -> data.getContractDateTime().isAfter(startDate) && data.getContractDateTime().isBefore(endDate))
                .collect(Collectors.toList());
    }

    // 상품 좋아요
    @Override
    @Transactional
    public void incrementProductLikes(String modelNum) {

        List<Product> products = productRepository.findAllByModelNum(modelNum);

        for (Product product : products) {
            product.setProductLike(product.getProductLike() + 1);
            productRepository.save(product);
        }
    }

    // 싱품 랭킹
    @Override
    public List<ProductRankingDto> getAllProductsByLikes() {
        return productRepository.searchAllProductByLikes();
    }

    @Override
    public OrderProductDto getProductOne(Long productId) {
        Product product = productRepository.findProductsByProductId(productId)
                .orElseThrow(() -> new RuntimeException("not found product"));

        System.out.println("product = " + product);
        OrderProductDto orderProductDto = OrderProductDto.builder()
                .productId(product.getProductId())
                .productImg(product.getProductImg())
                .productBrand(product.getProductBrand())
                .productName(product.getProductName())
                .modelNum(product.getModelNum())
                .productSize(product.getProductSize())
                .subDepartment(product.getSubDepartment())
                .build();

        return orderProductDto;
    }
}