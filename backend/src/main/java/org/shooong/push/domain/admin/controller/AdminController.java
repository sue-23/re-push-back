//package com.example.backend.controller;
//
//
//import com.example.backend.dto.admin.*;
//import org.shooong.push.domain.luckyDraw.luckyDraw.dto.LuckyDrawsDto;
//import com.example.backend.entity.enumData.LuckyProcessStatus;
//import com.example.backend.service.AdminService;
//import org.shooong.push.domain.serviceCenter.notice.service.NoticeService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//
//@RequestMapping("/api/admin")
//@RequiredArgsConstructor
//@Log4j2
//@RestController
//public class AdminController {
//
//    private final AdminService adminService;
//    private final NoticeService noticeService;
//    //요청상품 전체조회
//    @GetMapping("/requests")
//    public ResponseEntity<?> findReqProduct(Pageable pageable) {
//        AdminRespDto.ReqProductsRespDto regProductRespDto = adminService.reqProducts(pageable);
//        return new ResponseEntity<>(regProductRespDto, HttpStatus.OK);
//    }
//
//    //요청상품 단건 조회
//    @GetMapping("/requests/{productId}")
//    public ResponseEntity<?> findReqProduct(@PathVariable Long productId) {
//        AdminRespDto.ReqProductRespDto reqProductRespDto = adminService.reqProduct(productId);
//        return new ResponseEntity<>(reqProductRespDto, HttpStatus.OK);
//    }
//
//    //요청상품 판매 상품으로 등록 ver2
//    @PutMapping("/requests/{productId}")
//    public ResponseEntity<?> saveReqProduct(@PathVariable Long productId, @RequestPart(value = "productReqDto") ProductReqDto productReqDto, @RequestPart(value = "productPhoto", required = false) MultipartFile productPhoto){
//
//        if (productReqDto.getModelNum()== null){
//            productReqDto.setModelNum("");
//        }
//        adminService.acceptRequest(productId, productReqDto,productPhoto);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    //요청상품 거절
//    @PostMapping("/requests/{productId}")
//    public ResponseEntity<?> deleteReqProduct(@PathVariable Long productId) {
//        adminService.deleteRequest(productId);
//        return new ResponseEntity<>("중복 요청상품 삭제 완료",HttpStatus.OK);
//    }
//
//    //관리자 상품 카테고리별 조회(대분류, 소분류)
//    @GetMapping("/products/{mainDepartment}")
//    public ResponseEntity<?> getProductsByDepartment(@PathVariable String mainDepartment, @RequestParam(value = "subDepartment", required = false) String subDepartment, Pageable pageable) {
//
//        AdminProductResponseDto products= adminService.getProducts(mainDepartment, subDepartment,pageable);
//        return new ResponseEntity<>(products, HttpStatus.OK);
//    }
//
//    //관리자 상품 상세 조회(구매입찰 + 판매입찰)
//    @GetMapping("/products/detailed/{modelNum}")
//    public ResponseEntity<?> findDetailedProduct(@PathVariable String modelNum, @RequestParam(value = "productSize", required = false) String productSize) {
//
//        //modelnum을 이용해서 상품 상세 조회, size에 따라 카테고리 조회하듯이 입찰 정보 불러오기
//        AdminRespDto.AdminProductDetailRespDto detailedProduct =  adminService.getDetailProduct(modelNum, productSize);
//        return new ResponseEntity<>(detailedProduct,HttpStatus.OK);
//    }
//
//    //판매입찰(검수중) 상품 검수 승인
//    //위에서 판매입찰 id를 받아와서 해당 판매입찰 상태 변경
//    @PostMapping("/sales/{salesBiddingId}/approve")
//    public ResponseEntity<AdminRespDto.ChangeRespDto> acceptSaleBidding(@PathVariable Long salesBiddingId){
//        //salesBiddingId를 통하여 검수요청중인 salesStatus = INSPECTION ->PROCESS 으로 변경
//
//        AdminRespDto.ChangeRespDto acceptSaleRespDto = adminService.acceptSales(salesBiddingId);
//
//        return new ResponseEntity<>(acceptSaleRespDto,HttpStatus.OK);
//    }
//
//    //관리자 럭키드로우 LuckDrawStatus로 다건 조회
//    @GetMapping("/luckyList")
//    public ResponseEntity<?> findLuckydarwList(@RequestParam(defaultValue = "READY") LuckyProcessStatus luckyProcessStatus, Pageable pageable) {
//        AdminRespDto.LuckyDrawsRespDto luckyDrawList = adminService.getLuckyDraws(luckyProcessStatus,pageable);
//        return new ResponseEntity<>(luckyDrawList,HttpStatus.OK);
//    }
//
//    //관리자 럭키드로우 단건 조회
//    @GetMapping("/luckydraw/{luckyId}")
//    public ResponseEntity<?> findLuckydraw(@PathVariable Long luckyId) {
//
//        LuckyDrawsDto luckyOne = adminService.getLucky(luckyId);
//        return new ResponseEntity<>(luckyOne,HttpStatus.OK);
//    }
//
//    //럭키드로우 상품 등록
//    @PostMapping("/luckydraw/insert")
//    public ResponseEntity<?> registerLuckyDraw(@ModelAttribute AdminReqDto.AdminLuckDrawDto adminLuckDrawDto){
//
//        //관리자가 상품 폼만 등록, 실제 럭키드로우 상품을 schedule을 통하여 등록됨
//
//        AdminReqDto.AdminLuckDrawDto result = adminService.insertLucky(adminLuckDrawDto);
//
//        return new ResponseEntity<>(result,HttpStatus.OK);
//    }
//}
