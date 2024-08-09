package org.shooong.push.domain.feed.styleFeed.service;

import org.shooong.push.domain.feed.styleFeed.dto.FeedBookmarkDto;
import org.shooong.push.domain.feed.styleFeed.dto.StyleFeedDto;
import org.shooong.push.domain.feed.styleFeed.entity.StyleFeed;

import java.io.IOException;
import java.util.List;

public interface StyleFeedService {

    // 최신순으로 피드 조회
    List<StyleFeedDto> getAllStyleFeedList();

    // 좋아요순으로 피드 조회
    List<StyleFeedDto> getAllStyleFeedRanking();

    // 피드 상세 조회
    StyleFeedDto getStyleFeedById(Long feedId);

    // 피드 등록
    StyleFeed createStyleFeed(StyleFeedDto styleFeedDTO);

    // 피드 수정
    StyleFeedDto updateStyleFeed(Long feedId, StyleFeedDto styleFeedDTO);

//    // 피드 삭제
    void deleteStyleFeed(final long feedId, Long userId);

    // 관심피드 조회
    List<FeedBookmarkDto> getUserFeedBookmarks(Long userId);

    // 관심피드 등록
    FeedBookmarkDto createFeedBookmark(FeedBookmarkDto feedBookmarkDTO) throws IOException;

    // 관심피드 삭제
    void deleteFeedBookmark(final long styleSavedId, Long userId);

    void increaseLikeCount(Long feedId);
}
