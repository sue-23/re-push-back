package org.shooong.push.domain.feed.styleFeed.repository;

import org.shooong.push.domain.feed.styleFeed.entity.StyleFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StyleFeedRepository extends JpaRepository<StyleFeed, Long> {
    List<StyleFeed> findAllByOrderByLikeCountDesc();
    List<StyleFeed> findAllByOrderByCreateDateDesc();
    Optional<StyleFeed> findByFeedId(Long feedId);
}
