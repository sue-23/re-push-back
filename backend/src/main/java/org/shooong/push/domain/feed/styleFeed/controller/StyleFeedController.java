package org.shooong.push.domain.feed.styleFeed.controller;

import org.shooong.push.domain.feed.styleFeed.dto.FeedBookmarkDto;
import org.shooong.push.domain.feed.styleFeed.dto.StyleFeedDto;
import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.feed.styleFeed.service.StyleFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Log4j2
public class StyleFeedController {

    @Autowired
    private StyleFeedService styleFeedService;

//    @Autowired
//    private ObjectStorageService objectStorageService;

//    private String bucketName = "push";

    // 피드 등록
    @PostMapping("/api/user/feedRegistration")
    @ResponseStatus(HttpStatus.CREATED)
    public void createStyleFeed(@ModelAttribute StyleFeedDto styleFeedDto,
                                @AuthenticationPrincipal UserDTO userDTO,
                                @RequestParam("files") List<MultipartFile> files) {
        Long userId = userDTO.getUserId();
        styleFeedDto.setUserId(userId);
        log.info("새로운 피드 생성: {}", styleFeedDto);

        List<StyleFeedDto> styleFeedDtos = new ArrayList<>();
//        for (MultipartFile file : files) {
//            String fileName = objectStorageService.uploadFile(bucketName, "shooong/", file);
//            if (fileName != null) {
//                StyleFeedDto styleFeedDto1 = new StyleFeedDto();
//                styleFeedDto1.setFeedImage(fileName);
//                styleFeedDtos.add(styleFeedDto1);
//            }
//        }
        if (!styleFeedDtos.isEmpty()) {
            styleFeedDto.setFeedImage(styleFeedDtos.get(0).getFeedImage());
        }

        styleFeedService.createStyleFeed(styleFeedDto);
    }

    // 최신순으로 피드 조회
    @GetMapping("/feedList")
    public List<StyleFeedDto> getAllStyleFeedList() {
        List<StyleFeedDto> styleFeeds = styleFeedService.getAllStyleFeedList();
        log.info("성공: {} 개의 피드 가져옴", styleFeeds);
        return styleFeeds;
    }

    // 좋아요 순으로 피드 조회(랭킹)
    @GetMapping("/feedRanking")
    public List<StyleFeedDto> getAllStyleFeedRanking() {
        List<StyleFeedDto> styleFeeds = styleFeedService.getAllStyleFeedRanking();
        log.info("성공: {} 개의 피드 가져옴", styleFeeds);
        return styleFeeds;
    }

    // 피드 상세 조회
    @GetMapping("/styleFeed/{feedId}")
    public StyleFeedDto getStyleFeedById(@PathVariable Long feedId) {
        StyleFeedDto styleFeedDto = styleFeedService.getStyleFeedById(feedId);
        log.info("피드 상세 조회: {}", styleFeedDto);
        return styleFeedDto;
    }

    // 피드 수정
    @PutMapping("/api/user/modifyFeed/{feedId}")
    public StyleFeedDto updateStyleFeed(@PathVariable Long feedId,
                                        @RequestBody StyleFeedDto styleFeedDto,
                                        @AuthenticationPrincipal UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        styleFeedDto.setUserId(userId);

        return styleFeedService.updateStyleFeed(feedId, styleFeedDto);
    }


    // 피드 삭제
    @DeleteMapping("/api/user/deleteFeed/{feedId}")
    public void deleteStyleFeed(@PathVariable Long feedId,
                                @AuthenticationPrincipal UserDTO userDTO) {
        Long userId = userDTO.getUserId();

        styleFeedService.deleteStyleFeed(feedId, userId);
    }

    // 관심피드 저장
    @PostMapping("/api/user/saveFeedBookmark")
    @ResponseStatus(HttpStatus.CREATED)
    public FeedBookmarkDto createFeedBookmark(@RequestBody FeedBookmarkDto feedBookmarkDTO,
                                              @AuthenticationPrincipal UserDTO userDTO) throws IOException {
        Long userId = userDTO.getUserId();
        feedBookmarkDTO.setUserId(userId);

        FeedBookmarkDto createdFeedBookmark = styleFeedService.createFeedBookmark(feedBookmarkDTO);
        log.info("새로운 북마크 생성: {}", createdFeedBookmark);
        return createdFeedBookmark;
    }

    // 관심피드 조회
    @GetMapping("/api/feedBookmark")
    public List<FeedBookmarkDto> getUserFeedBookmarks(@AuthenticationPrincipal UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        List<FeedBookmarkDto> feedBookmarks = styleFeedService.getUserFeedBookmarks(userId);
        log.info("성공: {} 개의 북마크 가져옴", feedBookmarks.size());
        return feedBookmarks;
    }

    // 관심피드 삭제
    @DeleteMapping("/api/user/deleteFeedBookmark/{styleSavedId}")
    public void deleteFeedBookmark(@PathVariable Long styleSavedId,
                                   @AuthenticationPrincipal UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        styleFeedService.deleteFeedBookmark(styleSavedId, userId);
    }

    // 피드 좋아요 추가
    @PostMapping("/api/user/likeFeed/{feedId}")
    @ResponseStatus(HttpStatus.OK)
    public void likeStyleFeed(@PathVariable Long feedId) {
        styleFeedService.increaseLikeCount(feedId);
        log.info("피드 좋아요 추가: {}", feedId);
    }
}
