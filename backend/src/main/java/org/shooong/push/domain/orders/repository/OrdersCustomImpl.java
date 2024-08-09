package org.shooong.push.domain.orders.repository;

import org.shooong.push.domain.orders.entity.QOrders;
import org.shooong.push.domain.product.entity.QProduct;
import org.shooong.push.domain.bidding.salesBidding.entity.QSalesBidding;
import org.shooong.push.domain.user.mypage.dto.accountSettings.SalesSummaryDto;
import org.shooong.push.domain.enumData.OrderStatus;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class OrdersCustomImpl implements OrdersCustom {

    private final JPAQueryFactory queryFactory;
    QProduct product = QProduct.product;
    QOrders orders = QOrders.orders;
    QSalesBidding salesBidding = QSalesBidding.salesBidding;

    private BooleanExpression userEq(Long userId) {
        return orders.user.userId.eq(userId);
    }

    private BooleanExpression salesBiddingIsNotNull() {
        return orders.salesBidding.isNotNull();
    }

    private BooleanExpression orderStatusIsComplete() {
        return orders.orderStatus.eq(OrderStatus.COMPLETE);
    }

    @Override
    public Page<SalesSummaryDto> findSalesHistoryByUserId(Long userId, Pageable pageable) {
        JPAQuery<Tuple> query = queryFactory
                .select(
                        product.productImg,
                        product.productName,
//                        orders.orderPrice,
                        orders.salesBidding.salesBiddingPrice,
                        orders.modifyDate
                )
                .from(orders)
                .join(orders.product, product)
                .where(
                        userEq(userId),
                        salesBiddingIsNotNull(),
                        orderStatusIsComplete()
                );

        // 페이징을 위해 전체 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(orders.count())
                .from(orders)
                .join(orders.product, product)
                .where(
                        userEq(userId),
                        salesBiddingIsNotNull(),
                        orderStatusIsComplete()
                );

        // 페이징 적용
        List<Tuple> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<SalesSummaryDto> content = results.stream()
                .map(tuple -> SalesSummaryDto.builder()
                        .productImg(tuple.get(product.productImg))
                        .productName(tuple.get(product.productName))
                        .orderPrice(tuple.get(orders.salesBidding.salesBiddingPrice))
                        .orderDate(tuple.get(orders.modifyDate))
                        .build())
                .toList();

        long total = countQuery.fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public BigDecimal findTotalSalesAmountByUserId(Long userId) {
        return queryFactory
                .select(orders.salesBidding.salesBiddingPrice.sum())
                .from(orders)
                .where(
                        userEq(userId),
                        salesBiddingIsNotNull(),
                        orderStatusIsComplete()
                )
                .fetchOne();
    }

}
