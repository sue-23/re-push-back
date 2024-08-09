//package com.example.backend.controller.requesstproduct;
//
//import org.shooong.push.domain.serviceCenter.requestProduct.dto.RequestProductDto;
//import org.shooong.push.domain.serviceCenter.requestProduct.dto.RequestProductListDto;
//import com.example.backend.service.objectstorage.ObjectStorageService;
//import org.shooong.push.domain.serviceCenter.requestProduct.service.RequestProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/product")
//public class RequestProductController {
//
//    @Autowired
//    private RequestProductService requestProductService;
//
////    @Autowired
////    private ObjectStorageService objectStorageService;
////
////    private String bucketName = "push";
//
//
//    // 미등록 상품 등록 요청글 등록
//    @PostMapping("/request")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void createRequestProduct(@ModelAttribute RequestProductDto requestProductDto,
//                                     @RequestParam("files")List<MultipartFile> files) {
//
//        List<RequestProductDto> requestProductDtos = new ArrayList<>();
//        for(MultipartFile file : files) {
//            String fileName = objectStorageService.uploadFile(bucketName, "shooong/products/", file);
//            if(fileName != null) {
//                RequestProductDto requestProductDto1 = new RequestProductDto();
//                requestProductDto1.setProductImg(fileName);
//                requestProductDtos.add(requestProductDto1);
//            }
//        }
//        if(!requestProductDtos.isEmpty()) {
//            requestProductDto.setProductImg(requestProductDtos.get(0).getProductImg());
//        }
//        requestProductService.createRequestProduct(requestProductDto);
//    }
//
//    // 미등록 상품 등록 요청글 조회
//    @GetMapping("/requestList")
//    public List<RequestProductListDto> getRequestProducts() {
//        return requestProductService.getRequestProducts();
//    }
//
//    // 미등록 상품 등록 요청글 상세조회
//    @GetMapping("/request/{productId}")
//    public RequestProductDto getRequestProductById(@PathVariable Long productId) {
//        return requestProductService.getRequestProductById(productId);
//    }
//}
