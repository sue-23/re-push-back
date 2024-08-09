package org.shooong.push.domain.bookmark.bookmarkFeed.repository;

import org.shooong.push.domain.bookmark.bookmarkFeed.entity.BookmarkFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkFeedRepository extends JpaRepository<BookmarkFeed, Long> {
    List<BookmarkFeed> findByStyleFeed_FeedId(Long feedId);
    List<BookmarkFeed> findByUser_UserId(Long userId);
    boolean existsByUser_UserIdAndStyleFeed_FeedId(Long userId, Long feedId);
}
