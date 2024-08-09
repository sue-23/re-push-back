package org.shooong.push.domain.bookmark.bookmarkProduct.controller;

import org.shooong.push.domain.bookmark.bookmarkProduct.dto.BookmarkProductDto;
import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.bookmark.bookmarkProduct.service.BookmarkProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Log4j2
@RestController
public class BookmarkProductController {

    private final BookmarkProductService bookmarkProductService;

    @Autowired
    public BookmarkProductController(BookmarkProductService bookmarkProductService) {
        this.bookmarkProductService = bookmarkProductService;
    }

    // 관심상품 저장
    @PostMapping("/api/product/bookmark")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> saveBookmark(@AuthenticationPrincipal UserDTO userDTO,
                                               @RequestParam String modelNum,
                                               @RequestParam String productSize) {
        try {
            BookmarkProductDto bookmarkProductDto = BookmarkProductDto.builder()
                    .userId(userDTO.getUserId())
                    .modelNum(modelNum)
                    .productSize(productSize)
                    .build();

            bookmarkProductService.saveBookmark(bookmarkProductDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Bookmark saved successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    // 관심상품 조회
    @GetMapping("/select/bookmark")
    public List<BookmarkProductDto> getUserBookmarks(@AuthenticationPrincipal UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        List<BookmarkProductDto> bookmarkProducts = bookmarkProductService.getUserBookmarks(userId);
        log.info("bookmark product list : {}", bookmarkProducts);
        return bookmarkProducts;
    }

    // 관심상품 삭제
    @DeleteMapping("/delete/bookmark/{bookmarkProductId}")
    public void deleteBookmark(@PathVariable Long bookmarkProductId,
                               @AuthenticationPrincipal UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        bookmarkProductService.deleteBookmark(bookmarkProductId, userId);
    }

    // 해당 모델에 대한 북마크 수 조회
    @GetMapping("/product/bookmarkCount")
    public long getBookmarkCountByModelNum(@RequestParam String modelNum) {
        return bookmarkProductService.getBookmarkCountByModelNum(modelNum);
    }

    // 특정 상품의 어떠한 사이즈라도 북마크했는지 확인
    @GetMapping("/api/product/bookmark/exists")
    public ResponseEntity<Boolean> isAnySizeBookmarked(@AuthenticationPrincipal UserDTO userDTO,
                                                       @RequestParam String modelNum) {
        try {
            boolean isBookmarked = bookmarkProductService.isAnySizeBookmarked(userDTO.getUserId(), modelNum);
            return ResponseEntity.ok(isBookmarked);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }
}