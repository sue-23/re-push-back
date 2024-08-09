package org.shooong.push.domain.product.repository;

import org.shooong.push.domain.admin.dto.ProductRespDto;
import org.shooong.push.domain.product.dto.Detail.*;
import org.shooong.push.domain.bidding.buyingBidding.entity.QBuyingBidding;
import org.shooong.push.domain.bidding.salesBidding.entity.QSalesBidding;
import org.shooong.push.domain.enumData.BiddingStatus;
import org.shooong.push.domain.enumData.ProductStatus;
import org.shooong.push.domain.enumData.SalesStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.DateTimeTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.shooong.push.domain.product.dto.ProductResponseDto;
import org.shooong.push.domain.product.entity.QProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
@Log4j2

public class ProductSearchImpl implements ProductSearch {

    private final JPAQueryFactory queryFactory;

    private final QProduct product = QProduct.product;

    private final QBuyingBidding buying = QBuyingBidding.buyingBidding;
    private final QSalesBidding sales = QSalesBidding.salesBidding;

    // 판매 상품 대분류 조회
    @Override
    public List<ProductRespDto> findProductsByDepartment(String mainDepartment) {
        BooleanExpression eqMainDepartment = product.mainDepartment.eq(mainDepartment);
        BooleanExpression productCondition = product.productStatus.eq(ProductStatus.REGISTERED);

        return queryFactory.select(
                        Projections.constructor(ProductRespDto.class,
                                product.productBrand,
                                product.productName,
                                product.modelNum,
                                product.productImg,
                                product.mainDepartment,
                                Expressions.numberTemplate(BigDecimal.class, "coalesce({0}, {1})",
                                        sales.salesBiddingPrice.max(),
                                        product.originalPrice).as("buyingBiddingPrice")
                        )
                )
                .from(product)
                .leftJoin(sales).on(sales.product.modelNum.eq(product.modelNum))
                .where(eqMainDepartment.and(productCondition)
                        .and(sales.salesStatus.eq(SalesStatus.PROCESS)))
                .groupBy(product.modelNum)
                .fetch();
    }

    // 모든 상품에 대해 최신 등록순
    @Override
    public List<ProductResponseDto> searchAllProduct(String mainDepartment) {
        return queryFactory
                .select(Projections.constructor(ProductResponseDto.class,
                        product.productId,
                        product.productImg,
                        product.productBrand,
                        product.productName,
                        product.modelNum,
                        sales.salesBiddingPrice.max().as("biddingPrice"),  // 여기서 판매의 가장 비싼 가격을 가져옵니다.
                        product.createDate.as("registerDate"),
                        product.originalPrice
                ))
                .from(product)
                .leftJoin(sales).on(product.modelNum.eq(sales.product.modelNum))
                .where(product.productStatus.eq(ProductStatus.REGISTERED)
                        .and(product.mainDepartment.eq(mainDepartment))
                        .and(sales.salesStatus.eq(SalesStatus.PROCESS)))
                .orderBy(product.createDate.desc())
                .groupBy(product.modelNum)
                .fetch();
    }

    // 모든 상품 구매입찰 등록된게 많은 순서
    @Override
    public List<ProductResponseDto> searchAllProductManyBid(String mainDepartment) {
        return queryFactory
                .select(Projections.constructor(ProductResponseDto.class,
                        product.productId,
                        product.productImg,
                        product.productBrand,
                        product.productName,
                        product.modelNum,
                        sales.salesBiddingPrice.max().as("biddingPrice"), // 여기서 판매의 가장 비싼 가격을 가져옵니다.
                        product.createDate.as("registerDate"),
                        product.originalPrice
                ))
                .from(product)
                .leftJoin(sales).on(product.modelNum.eq(sales.product.modelNum))
                .where(product.productStatus.eq(ProductStatus.REGISTERED)
                        .and(product.mainDepartment.eq(mainDepartment))
                        .and(sales.salesStatus.eq(SalesStatus.PROCESS)))
                .orderBy(sales.count().desc()) // 여전히 구매 입찰 수로 정렬
                .groupBy(product.modelNum)
                .fetch();
    }


    // 가장 낮은 구매가격 + 가장 최신에 입찰이 들어온 순서
    // Dto 생성하기 애매해서 createDate를 받아오지만 실제 체결시간 기준으로 잘불러와지니까 신경쓰지 않아도됌
    @Override
    public List<ProductResponseDto> searchAllProductNewBuying(String mainDepartment) {
        return queryFactory
                .select(Projections.constructor(ProductResponseDto.class,
                        product.productId,
                        product.productImg,
                        product.productBrand,
                        product.productName,
                        product.modelNum,
                        sales.salesBiddingPrice.min().as("biddingPrice"),
                        product.createDate.as("registerDate"),
                        product.originalPrice
                ))
                .from(product)
                .leftJoin(sales).on(product.modelNum.eq(sales.product.modelNum))
                .where(product.productStatus.eq(ProductStatus.REGISTERED)
                        .and(product.mainDepartment.eq(mainDepartment))
                        .and(sales.salesStatus.eq(SalesStatus.PROCESS)))
                .orderBy(sales.salesBiddingTime.asc())
                .groupBy(product.modelNum)
                .fetch();
    }

    // 등록 역순 -> 인기 없는 상품인데 판다고 상술
    @Override
    public List<ProductResponseDto> searchAllProductNewSelling(String mainDepartment) {
        return queryFactory
                .select(Projections.constructor(ProductResponseDto.class,
                        product.productId,
                        product.productImg,
                        product.productBrand,
                        product.productName,
                        product.modelNum,
                        sales.salesBiddingPrice.max().as("biddingPrice"),
                        product.createDate.as("registerDate"),
                        product.originalPrice
                ))
                .from(product)
                .leftJoin(sales).on(product.modelNum.eq(sales.product.modelNum))
                .where(product.productStatus.eq(ProductStatus.REGISTERED)
                        .and(product.mainDepartment.eq(mainDepartment)))
                .orderBy(sales.salesBiddingTime.desc())
                .groupBy(product.modelNum)
                .fetch();
    }

    // 소분류 상품 조회
    @Override
    public Slice<ProductResponseDto> subProductInfo(String subDepartment, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        List<ProductResponseDto> products = queryFactory
                .select(Projections.constructor(ProductResponseDto.class,
                        product.productId,
                        product.productImg,
                        product.productBrand,
                        product.productName,
                        product.modelNum,
                        sales.salesBiddingPrice.max().as("biddingPrice"),
                        product.createDate.as("registerDate"),
                        product.originalPrice
                ))
                .from(product)
                .leftJoin(sales).on(product.modelNum.eq(sales.product.modelNum))
                .where(product.productStatus.eq(ProductStatus.REGISTERED)
                        .and(product.subDepartment.eq(subDepartment))
                        .and(sales.salesStatus.eq(SalesStatus.PROCESS)))
                .groupBy(product.modelNum)
                .orderBy(product.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();

        // 다음 페이지 유무
        boolean hasNext = false;
        if (products.size() > pageSize) {
            products.remove(pageSize);
            hasNext = true;
        }

        // Slice 객체 변환
        return new SliceImpl<>(products, pageable, hasNext);
    }

    // 해당 상품의 사이즈 상관없이 구매(최저), 판매(최고)가 조회
    @Override
    public org.shooong.push.domain.product.dto.Detail.ProductDetailDto searchProductPrice(String modelNum) {

        log.info("ModelNum : {}", modelNum);
        JPAQuery<Long> lowPriceQuery = queryFactory.select(buying.buyingBiddingPrice.min().castToNum(Long.class))
                .from(buying)
                .where(buying.biddingStatus.eq(BiddingStatus.PROCESS)
                        .and(buying.product.modelNum.eq(modelNum))
                        .and(product.productStatus.eq(ProductStatus.REGISTERED)));

        JPAQuery<Long> topPriceQuery = queryFactory.select(sales.salesBiddingPrice.max().castToNum(Long.class))
                .from(sales)
                .where(sales.salesStatus.eq(SalesStatus.PROCESS)
                        .and(sales.product.modelNum.eq(modelNum))
                        .and(product.productStatus.eq(ProductStatus.REGISTERED)));

        Long lowestPriceLong = lowPriceQuery.fetchOne();
        Long highestPriceLong = topPriceQuery.fetchOne();

        // Long 값을 BigDecimal로 변환
        BigDecimal lowestPrice = (lowestPriceLong != null) ? BigDecimal.valueOf(lowestPriceLong) : BigDecimal.ZERO;
        BigDecimal highestPrice = (highestPriceLong != null) ? BigDecimal.valueOf(highestPriceLong) : BigDecimal.ZERO;

        log.info("Lowest Price: {}", lowestPrice);
        log.info("Highest Price: {}", highestPrice);

        // ProductDetailDto 생성 및 설정
        ProductDetailDto priceValue = new ProductDetailDto();
        priceValue.setBuyingBiddingPrice(lowestPrice);
        priceValue.setSalesBiddingPrice(highestPrice);

        log.info("ProductDetailDto: {}", priceValue);

        return priceValue;
    }

    // 체결가 계산
    @Override
    public List<SalesBiddingDto> recentlyTransaction(String modelNum) {
        QProduct product = QProduct.product;
        QSalesBidding sales = QSalesBidding.salesBidding;
        QBuyingBidding buying = QBuyingBidding.buyingBidding;

        DateTimeTemplate<String> salesTimeString = Expressions.dateTimeTemplate(
                String.class, "DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", sales.salesBiddingTime);
        DateTimeTemplate<String> buyingTimeString = Expressions.dateTimeTemplate(
                String.class, "DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", buying.buyingBiddingTime);

        return queryFactory.select(Projections.bean(SalesBiddingDto.class,
                        product.productId,
                        product.modelNum,
                        product.productSize,
                        product.latestPrice,
                        product.previousPrice,
                        product.previousPercentage,
                        sales.salesBiddingTime.as("salesBiddingTime"),
                        sales.salesBiddingPrice.as("salesBiddingPrice")
                ))
                .from(product)
                .leftJoin(sales).on(sales.product.eq(product).and(sales.salesStatus.eq(SalesStatus.COMPLETE)))
                .leftJoin(buying).on(buying.product.eq(product).and(buying.biddingStatus.eq(BiddingStatus.COMPLETE)))
                .where(product.modelNum.eq(modelNum)
                        .and(product.productStatus.eq(ProductStatus.REGISTERED))
                        .and(salesTimeString.eq(buyingTimeString))
                        .and(sales.salesBiddingPrice.eq(buying.buyingBiddingPrice))
                        .and(sales.salesBiddingTime.isNotNull()))
                .orderBy(sales.salesBiddingTime.desc())
                .distinct()
                .fetch();
    }



    private BooleanExpression contractPermitOneSecond(DateTimePath<LocalDateTime> salesTime, DateTimePath<LocalDateTime> buyingTime) {
        return Expressions.numberTemplate(Long.class, "timestampdiff(SECOND, {0}, {1})", salesTime, buyingTime).loe(1);
    }

    @Override
    public List<SalesHopeDto> salesHopeInfo(String modelNum) {

        return queryFactory.select(Projections.bean(SalesHopeDto.class,
                        sales.salesBiddingPrice,
                        product.productSize,
                        sales.salesQuantity))
                .from(product)
                .leftJoin(sales).on(sales.product.eq(product))
                .where(product.modelNum.eq(modelNum)
                        .and(sales.salesStatus.eq(SalesStatus.PROCESS))
                        .and(product.productStatus.eq(ProductStatus.REGISTERED)))
                .orderBy(product.createDate.desc())
                .fetch();
    }

    @Override
    public List<BuyingHopeDto> buyingHopeInfo(String modelNum) {
        return queryFactory.select(Projections.bean(BuyingHopeDto.class,
                        buying.buyingBiddingPrice,
                        product.productSize,
                        buying.buyingQuantity))
                .from(product)
                .leftJoin(buying).on(buying.product.eq(product))
                .where(product.modelNum.eq(modelNum)
                        .and(buying.biddingStatus.eq(BiddingStatus.PROCESS))
                        .and(product.productStatus.eq(ProductStatus.REGISTERED)))
                .orderBy(product.createDate.desc())
                .fetch();
    }

    @Override
    public List<GroupByBuyingDto> groupByBuyingSize(String modelNum) {
        QBuyingBidding subBuying = new QBuyingBidding("subBuying");
        List<GroupByBuyingDto> groupByBuyingDtoList = queryFactory.select(Projections.bean(GroupByBuyingDto.class,
                        buying.buyingBiddingId.as("buyProductId"),
                        product.productImg,
                        product.productName,
                        product.modelNum,
                        product.productSize,
                        buying.buyingBiddingPrice.min().as("buyingBiddingPrice"),
                        product.productId.as("productId"))
                )
                .from(product)
                .leftJoin(buying).on(buying.product.eq(product))
                .where(product.modelNum.eq(modelNum)
                        .and(buying.biddingStatus.eq(BiddingStatus.PROCESS))
                        .and(product.productStatus.eq(ProductStatus.REGISTERED))
                        .and(buying.buyingBiddingPrice.eq(
                                JPAExpressions.select(subBuying.buyingBiddingPrice.min())
                                        .from(subBuying)
                                        .where(subBuying.product.eq(product)
                                                .and(subBuying.biddingStatus.eq(BiddingStatus.PROCESS)))
                        )))
                .groupBy(product.productSize, buying.buyingBiddingId)
                .orderBy(buying.buyingBiddingPrice.asc())
                .fetch();

        log.info("GroupByBuyingDtoList Success : {}", groupByBuyingDtoList);
        return groupByBuyingDtoList.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupBySalesDto> groupBySalesSize(String modelNum) {
        QSalesBidding subSales = new QSalesBidding("subSales");
        List<GroupBySalesDto> groupBySalesDtoList = queryFactory.select(Projections.bean(GroupBySalesDto.class,
                        sales.salesBiddingId.as("salesProductId"),
                        product.productImg,
                        product.productName,
                        product.modelNum,
                        product.productSize,
                        sales.salesBiddingPrice.max().as("productMaxPrice"),
                        product.productId.as("productId"))
                )
                .from(product)
                .leftJoin(sales).on(sales.product.eq(product))
                .where(product.modelNum.eq(modelNum)
                        .and(sales.salesStatus.eq(SalesStatus.PROCESS))
                        .and(product.productStatus.eq(ProductStatus.REGISTERED))
                        .and(sales.salesBiddingPrice.eq(
                                JPAExpressions.select(subSales.salesBiddingPrice.max())
                                        .from(subSales)
                                        .where(subSales.product.eq(product)
                                                .and(subSales.salesStatus.eq(SalesStatus.PROCESS)))
                        )))
                .groupBy(product.productSize, sales.salesBiddingId)
                .orderBy(sales.salesBiddingPrice.desc())
                .fetch();

        return groupBySalesDtoList.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public BidResponseDto BuyingBidResponse(BidRequestDto bidRequestDto) {

        JPAQuery<Long> lowPriceQuery = queryFactory.select(buying.buyingBiddingPrice.min().castToNum(Long.class))
                .from(buying)
                .where(buying.biddingStatus.eq(BiddingStatus.PROCESS)
                        .and(buying.product.modelNum.eq(bidRequestDto.getModelNum()))
                        .and(product.productStatus.eq(ProductStatus.REGISTERED))
                        .and(product.productSize.eq(bidRequestDto.getProductSize())));

        JPAQuery<Long> topPriceQuery = queryFactory.select(sales.salesBiddingPrice.max().castToNum(Long.class))
                .from(sales)
                .where(sales.salesStatus.eq(SalesStatus.PROCESS)
                        .and(sales.product.modelNum.eq(bidRequestDto.getModelNum()))
                        .and(product.productStatus.eq(ProductStatus.REGISTERED))
                        .and(product.productSize.eq(bidRequestDto.getProductSize())));

        Long lowestPriceLong = lowPriceQuery.fetchOne();
        Long highestPriceLong = topPriceQuery.fetchOne();

        log.info("lowestPriceLong : {}   highestPriceLong : {}", lowestPriceLong, highestPriceLong);

        // Long 값을 BigDecimal로 변환
        BigDecimal lowestPrice = (lowestPriceLong != null) ? BigDecimal.valueOf(lowestPriceLong) : BigDecimal.ZERO;
        BigDecimal highestPrice = (highestPriceLong != null) ? BigDecimal.valueOf(highestPriceLong) : BigDecimal.ZERO;

        // BuyingBidResponseDto 생성 및 설정
        BidResponseDto priceValue = BidResponseDto.builder()
                .productBuyPrice(lowestPrice)
                .productSalePrice(highestPrice)
                .build();

        log.info("BuyingBidResponseDto: {}", priceValue.toString());

        return priceValue;
    }

    @Override
    public List<AveragePriceDto> getAllContractData(String modelNum, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Model Number: " + modelNum);
        log.info("Start Date: " + startDate.toString());
        log.info("End Date: " + endDate.toString());

        List<AveragePriceDto> averagePriceDto = queryFactory.select(Projections.bean(AveragePriceDto.class,
                        sales.salesBiddingTime.as("contractDateTime"),
                        sales.salesBiddingPrice.as("averagePrice")))
                .from(product)
                .leftJoin(sales).on(sales.product.eq(product))
                .leftJoin(buying).on(buying.product.eq(product))
                .where(sales.salesBiddingTime.between(startDate, endDate)
                        .and(product.modelNum.eq(modelNum))
                        .and(product.productStatus.eq(ProductStatus.REGISTERED)))
                .fetch();

        log.info("Query Result: " + averagePriceDto.toString());

        return averagePriceDto;
    }


}