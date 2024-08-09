package org.shooong.push.domain.admin.dto;

import org.shooong.push.domain.luckyDraw.luckyDraw.entity.LuckyDraw;
import org.shooong.push.domain.product.entity.Product;
import org.shooong.push.domain.bidding.salesBidding.entity.SalesBidding;
import org.shooong.push.domain.enumData.LuckyProcessStatus;
import org.shooong.push.domain.enumData.ProductStatus;
import org.shooong.push.domain.enumData.SalesStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminRespDto {

    //다건조회 DTO
    @Setter
    @Getter
    public static class ReqProductsRespDto {
        private List<ProductDto> products = new ArrayList<>();
        private int totalPages;
        private long totalElements;
        private int number;
        private int pageSize;
        private boolean isLast;

        public ReqProductsRespDto(Page<Product> products) {
            this.products = products.stream().map(ProductDto::new).collect(Collectors.toList());
            this.totalPages = products.getTotalPages();
            this.totalElements = products.getTotalElements();
            this.number = products.getNumber();
            this.pageSize = products.getSize();
            this.isLast = products.isLast();
        }
        @Setter
        @Getter
        public class ProductDto {

            private Long productId;
            private String productName;
            private String productBrand;
            private ProductStatus productStatus;
            private String modelNum;
            public ProductDto(Product product) {
                this.productId = product.getProductId();
                this.productName = product.getProductName();
                this.productBrand = product.getProductBrand();
                this.productStatus = product.getProductStatus();
                this.modelNum = product.getModelNum();
            }
        }
    }

    @Getter
    @Setter
    public static class ReqProductRespDto{
        private Long productId;
        private String productName;
        private String productBrand;
        private String productImg;
        private String modelNum;
        private BigDecimal originalPrice;
        private String productSize;

        public ReqProductRespDto(Product product) {
            this.productId = product.getProductId();
            this.productName = product.getProductName();
            this.productBrand = product.getProductBrand();
            this.productImg = product.getProductImg();
            this.modelNum = product.getModelNum();
            this.originalPrice = product.getOriginalPrice();
            this.productSize = product.getProductSize();
        }
    }

    @Getter
    @Setter
    public static class RegProductRespDto{
        private Long productId;
        private String productName;
        private String productBrand;
        private String productImg;
        private String modelNum;
        private BigDecimal originalPrice;
        private String productSize;
        private ProductStatus productStatus;
        public RegProductRespDto(Product product) {

            this.productId = product.getProductId();
            this.productName = product.getProductName();
            this.productBrand = product.getProductBrand();
            this.productImg = product.getProductImg();
            this.modelNum = product.getModelNum();
            this.originalPrice = product.getOriginalPrice();
            this.productSize = product.getProductSize();
            this.productStatus = product.getProductStatus();
        }
    }

    //판매입찰 상태 검수변경 dto
    @Getter
    @Setter
    public static class ChangeRespDto{
        private Long salesBiddingId;
        private Long productId;
        private int productQuantity;
        private SalesStatus salesStatus;

        public ChangeRespDto(SalesBidding salesBidding, Product product) {
            this.salesBiddingId = salesBidding.getSalesBiddingId();
            this.salesStatus = salesBidding.getSalesStatus();
            this.productId = product.getProductId();
            this.productQuantity = product.getProductQuantity();
        }
    }

    //럭키드로우 상태별로 전체 조회
    @Getter
    @Setter
    public static class LuckyDrawsRespDto{
        private LuckyProcessStatus luckyProcessStatus;
        private List<LuckyDrawDto> luckyDraws = new ArrayList<>();

        public LuckyDrawsRespDto(LuckyProcessStatus luckyProcessStatus,Page<LuckyDraw> luckyDraws) {
            this.luckyProcessStatus = luckyProcessStatus;
            this.luckyDraws = luckyDraws.stream().map(LuckyDrawDto::new).collect(Collectors.toList());
        }
        @Setter
        @Getter
        public class LuckyDrawDto {

            private Long luckyId;
            private String luckyName;
            private LuckyProcessStatus luckyProcessStatus;

            public LuckyDrawDto(LuckyDraw luckyDraw) {
                this.luckyId = luckyDraw.getLuckyId();
                this.luckyName = luckyDraw.getLuckyName();
                this.luckyProcessStatus = luckyDraw.getLuckyProcessStatus();
            }
        }
    }



    //상품 상세 응답 Dto
    @Getter
    @Setter
    public static class AdminProductDetailRespDto {
        private String modelNum;
        private String productSize;
        private List<AdminProductRespDto> detailedProducts;

        public AdminProductDetailRespDto(String modelNum, String productSize, List<AdminProductRespDto> detailedProducts) {
            this.modelNum = modelNum;
            this.productSize = productSize;
            this.detailedProducts = detailedProducts;
        }

        // getter, setter, etc.
    }



}
