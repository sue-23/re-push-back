package org.shooong.push.domain.shop.repository;

import org.shooong.push.domain.bidding.buyingBidding.entity.QBuyingBidding;
import org.shooong.push.domain.bidding.salesBidding.entity.QSalesBidding;
import org.shooong.push.domain.product.dto.AllProductDto;
import org.shooong.push.domain.enumData.ProductStatus;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.shooong.push.domain.product.entity.QProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@PersistenceContext
public class ShopProductImpl implements ShopProduct {
    private final JPQLQueryFactory queryFactory;

    QProduct product = QProduct.product;
    QBuyingBidding buyingBidding = QBuyingBidding.buyingBidding;
    QSalesBidding salesBidding = QSalesBidding.salesBidding;

    // 필터링
    private BooleanExpression eqSub(List<String> subDepartment){
        if (StringUtils.isBlank(subDepartment.toString())){
            return null;
        }
        return product.subDepartment.in(subDepartment);
    }


    // 모든 상품 조회
    @Override
    public Slice<AllProductDto> allProduct(Pageable pageable) {

        int pageSize = pageable.getPageSize();

        List<AllProductDto> products = queryFactory
                .select(Projections.constructor(AllProductDto.class,
                        product.modelNum,
                        product.productId,
                        product.productBrand,
                        product.productName,
                        product.mainDepartment,
                        product.subDepartment,
                        product.productImg,
                        buyingBidding.buyingBiddingPrice.min()
                        ))
                .from(product)
                .leftJoin(buyingBidding).on(product.productId.eq(buyingBidding.product.productId))
                .leftJoin(salesBidding).on(product.productId.eq(salesBidding.product.productId))
                .where(product.productStatus.eq(ProductStatus.valueOf("REGISTERED")))
                .groupBy(product.modelNum)
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();

        // 다음 페이지 유무
        boolean hasNext = false;
        if(products.size() > pageSize){
            products.remove(pageSize);
            hasNext = true;
        }

        // Slice 객체 변환
        return new SliceImpl<>(products, pageable, hasNext);
    }

    @Override
    public Slice<AllProductDto> getProductsBySubDepartment(Pageable pageable, List<String> subDepartment) {
        int pageSize = pageable.getPageSize();

        List<AllProductDto> products = queryFactory
                .select(Projections.constructor(AllProductDto.class,
                        product.modelNum,
                        product.productId,
                        product.productBrand,
                        product.productName,
                        product.mainDepartment,
                        product.subDepartment,
                        product.productImg,
                        buyingBidding.buyingBiddingPrice.min()
                ))
                .from(product)
                .leftJoin(buyingBidding).on(product.productId.eq(buyingBidding.product.productId))
                .where(product.productStatus.eq(ProductStatus.valueOf("REGISTERED")), eqSub(subDepartment))
                .groupBy(product.modelNum)
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();

        // 다음 페이지 유무
        boolean hasNext = false;
        if(products.size() > pageSize){
            products.remove(pageSize);
            hasNext = true;
        }

        // Slice 객체 변환
        return new SliceImpl<>(products, pageable, hasNext);
    }
}
