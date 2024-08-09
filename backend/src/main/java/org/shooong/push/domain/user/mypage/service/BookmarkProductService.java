package org.shooong.push.domain.user.mypage.service;

import org.shooong.push.domain.user.mypage.dto.main.BookmarkProductsDto;
import org.shooong.push.domain.user.mypage.dto.main.ProductDetailsDto;
import org.shooong.push.domain.bidding.buyingBidding.repository.BuyingBiddingRepository;
import org.shooong.push.domain.bookmark.bookmarkProduct.repository.BookmarkProductRepository;
import org.shooong.push.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookmarkProductService {

    private final BookmarkProductRepository bookmarkProductRepository;
    private final BuyingBiddingRepository buyingBiddingRepository;
    private final ProductRepository productRepository;

    public Long getNowLowPrice(List<Long> productIdList) {

        Long nowLowPrice = buyingBiddingRepository.findLowPrice(productIdList);
        log.info("nowLowPrice for productIdList {}: {}", productIdList, nowLowPrice);

        return Optional.ofNullable(nowLowPrice).orElse(0L);
    }

    public List<BookmarkProductsDto> getAllBookmarkProducts(Long userId) {
        return getBookmarkProducts(userId, Pageable.unpaged());
    }

    public List<BookmarkProductsDto> getLatestBookmarkProducts(Long userId) {
        return getBookmarkProducts(userId, PageRequest.of(0, 8));
    }

    // TODO: 다른 방법 생각해보기
    public List<BookmarkProductsDto> getBookmarkProducts(Long userId, Pageable pageable) {

        // 회원의 모든 관심상품 productId 조회
        List<Long> bookmarkProductIdList =  bookmarkProductRepository.findBookmarkProductIdList(userId, pageable);
        log.info("bookmarkProductIdList: {}", bookmarkProductIdList);

        // 관심상품 productId들과 연관된 모든 productId 조회
        List<Long> relatedProductIdList = productRepository.findProductIdsByModelNum(bookmarkProductIdList);
        log.info("relatedProductIdList: {}", relatedProductIdList);

        // 관심 상품의 상세 정보 조회
        List<ProductDetailsDto> productDetailsDtoList = productRepository.findProductsDetails(bookmarkProductIdList);

        // relatedProductIdList를 기반으로 각 modelNum에 대한 productId를 그룹화
        Map<String, List<Long>> productIdListByModelNum = productRepository.findProductIdAndModelNum(relatedProductIdList).stream()
                .collect(Collectors.groupingBy(
                        row -> (String) row[1],
                        Collectors.mapping(row -> (Long) row[0], Collectors.toList())
                ));
//        Map<String, List<Long>> productIdListByModelNum = productRepository.findProductsDetails(relatedProductIdList)
//                .stream()
//                .collect(Collectors.groupingBy(ProductsDetailsDto::getModelNum,
//                        Collectors.mapping(ProductsDetailsDto::getProductId, Collectors.toList())));

        // 각 modelNum에 대한 구매입찰 최저가 계산
        Map<String, Long> modelNumToLowPriceMap = productIdListByModelNum.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> getNowLowPrice(entry.getValue())));


        return productDetailsDtoList.stream()
                .map(detailsDto -> {
                    Long nowLowPrice = modelNumToLowPriceMap.get(detailsDto.getModelNum());

                    return BookmarkProductsDto.builder()
                            .productDetailsDto(detailsDto)
                            .nowLowPrice(nowLowPrice)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
