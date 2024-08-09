package org.shooong.push.domain.bookmark.bookmarkFeed.entity;


import jakarta.persistence.*;
import lombok.*;
import org.shooong.push.domain.feed.styleFeed.entity.StyleFeed;
import org.shooong.push.domain.user.users.entity.Users;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class BookmarkFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedBookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId", nullable = false)
    private StyleFeed styleFeed;
}