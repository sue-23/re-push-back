package org.shooong.push.domain.product.controller;

import org.shooong.push.domain.admin.dto.ProductRespDto;
import org.shooong.push.domain.product.dto.Detail.*;
import org.shooong.push.domain.product.dto.ProductRankingDto;
import org.shooong.push.domain.product.dto.ProductResponseDto;
import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.product.service.ProductService;
//import com.example.backend.service.objectstorage.ObjectStorageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Log4j2
public class ProductController {

    private final ProductService productService;

//    private ObjectStorageService objectStorageService;

//    private String bucketName = "push";

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
//        this.objectStorageService = objectStorageService;
    }

    // 해당 대분류 상품
    @GetMapping("/products/main/{mainDepartment}")
    public ResponseEntity<?> findProductsByDepartment(@PathVariable String mainDepartment) {

        //판매중인 상품 대분류별 조회
        //상품 이미지, 브랜드, 상품명, 모델명
        //즉시구매가 = salesBiddingTable 의 상품중 PROCESS 인 상품중 최저가(모든 사이즈별)
        List<ProductRespDto> mainValue = productService.findProductsByDepartment(mainDepartment);
        return new ResponseEntity<>(mainValue, HttpStatus.OK);
    }

    // 해당 대분류 상품 중 최신 등록순
    @GetMapping("/products/{mainDepartment}/all_product_createDate")
    public ResponseEntity<?> allProduct(@PathVariable String mainDepartment) {
        List<ProductResponseDto> productResponseDtoList = productService.getAllProducts(mainDepartment);
        return new ResponseEntity<>(productResponseDtoList, HttpStatus.OK);
    }

    // 해당 대분류 상품 중 구매입찰이 많이 등록된 순
    @GetMapping("/products/{mainDepartment}/all_product_manyBid")
    public ResponseEntity<?> allProductManyBid(@PathVariable String mainDepartment) {
        List<ProductResponseDto> productResponseDtoList = productService.getAllProductsManyBid(mainDepartment);
        log.info("productResponseDtoList: " + productResponseDtoList);
        return new ResponseEntity<>(productResponseDtoList, HttpStatus.OK);
    }

    // 해당 대분류 상품 중 최근 구매 체결이 생긴 순
    @GetMapping("/products/{mainDepartment}/all_product_NewBuyingBid")
    public ResponseEntity<?> allProductNewBuyingBid(@PathVariable String mainDepartment) {
        List<ProductResponseDto> productResponseDtoList = productService.getAllProductsNewBuyBid(mainDepartment);
        return new ResponseEntity<>(productResponseDtoList, HttpStatus.OK);
    }

    // 해당 대분류 상품 중 최근 판매 체결이 생긴 순
    @GetMapping("/products/{mainDepartment}/all_product_newSalesBid")
    public ResponseEntity<?> allProductNewSalesBid(@PathVariable String mainDepartment) {
        List<ProductResponseDto> productResponseDtoList = productService.getAllProductsNewSaleBid(mainDepartment);
        return new ResponseEntity<>(productResponseDtoList, HttpStatus.OK);
    }

    // 소분류 상품
    @GetMapping("/products/sub/{subDepartment}")
    public Slice<ProductResponseDto> products(@PathVariable String subDepartment, @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Slice<ProductResponseDto> products = productService.selectCategoryValue(subDepartment, pageable);
        return products;
    }

    // 해당 상품(상세) 정보 가져오기
    @GetMapping("/products/detailInfo/{modelNum}")
    public ProductDetailDto productDetailSelect(@PathVariable String modelNum) {

        // (상품의 기본 정보) && (해당 상품의 구매(최저) / 판매(최고)가 조회) && (해당 상품에 대한 최근 체결 정보) && 상품 체결 / 구매 / 판매 내역 리스트
        ProductDetailDto basicInformationDto = productService.productDetailInfo(modelNum);
        log.info("basicInformationDto : " + basicInformationDto);

        return basicInformationDto;
    }

    // 상세 상품에 대한 정보 확인(로그인해야함)
    @GetMapping("/api/products/details/{modelNum}/bid")
    public ResponseEntity<?> buyingBidSelect(
            @PathVariable String modelNum,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String type,
            @AuthenticationPrincipal UserDTO userDTO) {
        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        Long userId = userDTO.getUserId();
        BidRequestDto bidRequestDto = BidRequestDto.builder()
                .modelNum(modelNum)
                .productSize(size)
                .type(type)
                .userId(userId)
                .build();
        BidResponseDto bidResponseDto = productService.selectBidInfo(bidRequestDto);

        return new ResponseEntity<>(bidResponseDto, HttpStatus.OK);

    }

    // 입찰 걸기 -> productId, userId, 입찰 마감날짜를 입력받아서 뽑아와야함
    @PostMapping("/api/products/details/{modelNum}/bid")
    public ResponseEntity<?> bidApplication(
            @PathVariable String modelNum,
            @RequestBody InsertBidDto insertBidDto,
            @AuthenticationPrincipal UserDTO userDTO) {
        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        Long userId = userDTO.getUserId();
        insertBidDto.setUserId(userId);
        insertBidDto.setModelNum(modelNum);
        productService.saveTemporaryBid(insertBidDto);
        return ResponseEntity.ok("값이 정상적으로 저장되었습니다.");
    }


    // 해당 상세 상품에 대한 리뷰 작성
    @PostMapping("/api/products/details/{modelNum}/review")
    public ResponseEntity<?> productDetailReview(
            @PathVariable String modelNum,
            @RequestParam("reviewContent") String reviewContent,
            @RequestParam("temp_image_data") MultipartFile tempImageData,
            @AuthenticationPrincipal UserDTO userDTO) {

        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Long userId = userDTO.getUserId();
        PhotoRequestDto photoRequestDto = new PhotoRequestDto();
        photoRequestDto.setUserId(userId);
        photoRequestDto.setModelNum(modelNum);
        photoRequestDto.setReviewContent(reviewContent);

//        String fileName = objectStorageService.uploadFile(bucketName, "shooong/products/", tempImageData);
//        if (fileName != null) {
//            photoRequestDto.setReviewImg(fileName);
//        }

        productService.addPhotoReview(photoRequestDto);
        return ResponseEntity.ok("리뷰가 성공적으로 작성되었습니다.");
    }


    // 해당 userId가 일치할 경우 수정 가능 -> > 사실상 사용 X
    @PutMapping("/api/products/details/{modelNum}/review/{reviewId}")
    public ResponseEntity<?> updatePhotoReview(
            @PathVariable String modelNum,
            @PathVariable Long reviewId,
            @RequestBody PhotoRequestDto photoRequestDto,
            @AuthenticationPrincipal UserDTO userDTO) {

        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Long userId = userDTO.getUserId();
        photoRequestDto.setUserId(userId);
        photoRequestDto.setModelNum(modelNum);
        photoRequestDto.setReviewId(reviewId);

        productService.updatePhotoReview(photoRequestDto);
        return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
    }

    // 해당 userId가 일치할 경우 본인이 작성한 리뷰 삭제 -> 사실상 사용 X
    @DeleteMapping("/api/products/details/{modelNum}/review/{reviewId}")
    public ResponseEntity<?> deletePhotoReview(@PathVariable String modelNum, @PathVariable Long reviewId, @AuthenticationPrincipal UserDTO userDTO) {
        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Long userId = userDTO.getUserId();

        productService.deletePhotoReview(reviewId, userId);
        return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
    }

    // 상품 좋아요
    @PostMapping("/like/{modelNum}")
    public ResponseEntity<String> incrementProductLikes(@PathVariable String modelNum) {
        try {
            productService.incrementProductLikes(modelNum);
            return ResponseEntity.status(HttpStatus.OK).body("Likes incremented successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error incrementing likes.");
        }
    }

    // 상품 랭킹
    @GetMapping("/all_product_likes")
    public ResponseEntity<?> allProductByLikes() {
        List<ProductRankingDto> productRankingDtos = productService.getAllProductsByLikes();
        return new ResponseEntity<>(productRankingDtos, HttpStatus.OK);
    }

}