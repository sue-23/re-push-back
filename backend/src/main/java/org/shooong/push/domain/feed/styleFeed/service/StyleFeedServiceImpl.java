package org.shooong.push.domain.feed.styleFeed.service;

import org.shooong.push.domain.feed.styleFeed.dto.FeedBookmarkDto;
import org.shooong.push.domain.feed.styleFeed.dto.StyleFeedDto;
import org.shooong.push.domain.bookmark.bookmarkFeed.entity.BookmarkFeed;
import org.shooong.push.domain.feed.styleFeed.entity.StyleFeed;
import org.shooong.push.domain.user.users.entity.Users;
import org.shooong.push.domain.bookmark.bookmarkFeed.repository.BookmarkFeedRepository;
import org.shooong.push.domain.feed.styleFeed.repository.StyleFeedRepository;
import org.shooong.push.domain.user.users.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service
@Log4j2
public class StyleFeedServiceImpl implements StyleFeedService {

    @Autowired
    private StyleFeedRepository styleFeedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookmarkFeedRepository bookmarkFeedRepository;

    // 최신 등록순으로 피드 조회
    @Override
    public List<StyleFeedDto> getAllStyleFeedList() {
        List<StyleFeed> styleFeeds = styleFeedRepository.findAllByOrderByCreateDateDesc();
        log.info("Found {} StyleFeeds", styleFeeds.size());

        return styleFeeds.stream()
                .map(styleFeed -> new StyleFeedDto(
                        styleFeed.getFeedId(),
                        styleFeed.getFeedTitle(),
                        styleFeed.getFeedImage(),
                        styleFeed.getLikeCount(),
                        styleFeed.getCreateDate(),
                        styleFeed.getModifyDate(),
                        styleFeed.getUser().getUserId(),
                        styleFeed.getUser().getNickname()
                ))
                .collect(Collectors.toList());
    }

    // 좋아요 순으로 피드 조회
    @Override
    public List<StyleFeedDto> getAllStyleFeedRanking() {
        List<StyleFeed> styleFeeds = styleFeedRepository.findAllByOrderByLikeCountDesc();
        log.info("Found {} StyleFeeds", styleFeeds.size());

        return styleFeeds.stream()
                .map(styleFeed -> new StyleFeedDto(
                        styleFeed.getFeedId(),
                        styleFeed.getFeedTitle(),
                        styleFeed.getFeedImage(),
                        styleFeed.getLikeCount(),
                        styleFeed.getCreateDate(),
                        styleFeed.getModifyDate(),
                        styleFeed.getUser().getUserId(),
                        styleFeed.getUser().getNickname()
                ))
                .collect(Collectors.toList());
    }

    // 피드 상세 조회
    @Override
    public StyleFeedDto getStyleFeedById(Long feedId) {
        StyleFeed styleFeed = styleFeedRepository.findByFeedId(feedId)
                .orElseThrow(() -> new RuntimeException("StyleFeed not found"));

        return new StyleFeedDto(
                styleFeed.getFeedId(),
                styleFeed.getFeedTitle(),
                styleFeed.getFeedImage(),
                styleFeed.getLikeCount(),
                styleFeed.getCreateDate(),
                styleFeed.getModifyDate(),
                styleFeed.getUser().getUserId(),
                styleFeed.getUser().getNickname()
        );
    }

    // 피드 등록
    @Override
    public StyleFeed createStyleFeed(StyleFeedDto styleFeedDTO) {
        Users user = userRepository.findById(styleFeedDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        StyleFeed styleFeed = new StyleFeed();
        styleFeed.setFeedTitle(styleFeedDTO.getFeedTitle());
        styleFeed.setFeedImage(styleFeedDTO.getFeedImage()); // This now contains the cloud image path(s)
        styleFeed.setLikeCount(styleFeedDTO.getLikeCount());
        styleFeed.setUser(user);

        return styleFeedRepository.save(styleFeed);
    }

    // 피드 좋아요
    @Override
    public void increaseLikeCount(Long feedId) {
        StyleFeed styleFeed = styleFeedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("StyleFeed not found"));

        styleFeed.setLikeCount(styleFeed.getLikeCount() + 1);
        styleFeedRepository.save(styleFeed);
        log.info("피드 좋아요 수 증가: {}", feedId);
    }

    // 피드 수정
    @Override
    public StyleFeedDto updateStyleFeed(Long feedId, StyleFeedDto styleFeedDTO) {
        StyleFeed styleFeed = styleFeedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("StyleFeed not found"));

        Long userId = styleFeed.getUser().getUserId();
        Long requestUserId = styleFeedDTO.getUserId();

        if (!userId.equals(requestUserId)) {
            throw new RuntimeException("User is not authorized to update this feed");
        }

        if (styleFeedDTO.getFeedTitle() != null) {
            styleFeed.setFeedTitle(styleFeedDTO.getFeedTitle());
        }
        if (styleFeedDTO.getFeedImage() != null) {
            styleFeed.setFeedImage(styleFeedDTO.getFeedImage());
        }
        StyleFeed updatedFeed = styleFeedRepository.save(styleFeed);

        return new StyleFeedDto(
                updatedFeed.getFeedId(),
                updatedFeed.getFeedTitle(),
                updatedFeed.getFeedImage(),
                updatedFeed.getLikeCount(),
                updatedFeed.getCreateDate(),
                updatedFeed.getModifyDate(),
                updatedFeed.getUser().getUserId(),
                updatedFeed.getUser().getNickname()
        );
    }

    // 피드 삭제
    @Override
    public void deleteStyleFeed(final long feedId, Long userId) {

        StyleFeed styleFeed = styleFeedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("StyleFeed not found"));

        Long feedUserId = styleFeed.getUser().getUserId();

        if (!feedUserId.equals(userId)) {
            throw new RuntimeException("User is not authorized to delete this feed");
        }

        final List<BookmarkFeed> bookmarkFeeds = bookmarkFeedRepository.findByStyleFeed_FeedId(feedId);
        bookmarkFeedRepository.deleteAll(bookmarkFeeds);
        styleFeedRepository.deleteById(feedId);
    }

    // 관심피드 조회
    @Override
    public List<FeedBookmarkDto> getUserFeedBookmarks(Long userId) {
        List<BookmarkFeed> bookmarkFeeds = bookmarkFeedRepository.findByUser_UserId(userId);
        log.info("Found {} FeedBookmarks for user {}", bookmarkFeeds.size(), userId);

        return bookmarkFeeds.stream()
                .map(bookmarkFeed -> {
                    FeedBookmarkDto dto = new FeedBookmarkDto();
                    dto.setUserId(bookmarkFeed.getUser().getUserId());
                    dto.setFeedId(bookmarkFeed.getStyleFeed().getFeedId());
                    dto.setFeedImage(bookmarkFeed.getStyleFeed().getFeedImage());
                    dto.setFeedTitle(bookmarkFeed.getStyleFeed().getFeedTitle());
                    dto.setNickName(bookmarkFeed.getUser().getNickname());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 관심피드 저장
    @Override
    public FeedBookmarkDto createFeedBookmark(FeedBookmarkDto feedBookmarkDTO) {
        Long userId = feedBookmarkDTO.getUserId();
        Long feedId = feedBookmarkDTO.getFeedId();

        if (bookmarkFeedRepository.existsByUser_UserIdAndStyleFeed_FeedId(userId, feedId)) {
            throw new RuntimeException("이미 해당 피드를 저장하였습니다.");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        StyleFeed styleFeed = styleFeedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("StyleFeed not found"));

        BookmarkFeed bookmarkFeed = BookmarkFeed.builder()
                .user(user)
                .styleFeed(styleFeed)
                .build();

        BookmarkFeed savedBookmarkFeed = bookmarkFeedRepository.save(bookmarkFeed);
        log.info("새로운 북마크 생성: {}", savedBookmarkFeed);

        return new FeedBookmarkDto(
                savedBookmarkFeed.getFeedBookmarkId(),
                savedBookmarkFeed.getUser().getUserId(),
                savedBookmarkFeed.getStyleFeed().getFeedId()
        );
    }

    // 관심피드 삭제
    @Override
    public void deleteFeedBookmark(final long styleSavedId, Long userId) {
        // Check if the feed bookmark belongs to the logged-in user
        BookmarkFeed bookmarkFeed = bookmarkFeedRepository.findById(styleSavedId)
                .orElseThrow(() -> new RuntimeException("Feed bookmark not found"));

        if (!bookmarkFeed.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: Cannot delete feed bookmark");
        }
        bookmarkFeedRepository.deleteById(styleSavedId);
    }
}
