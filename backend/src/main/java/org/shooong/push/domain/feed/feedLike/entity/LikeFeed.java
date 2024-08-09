package org.shooong.push.domain.feed.feedLike.entity;

import jakarta.persistence.*;
import lombok.*;
import org.shooong.push.domain.feed.styleFeed.entity.StyleFeed;
import org.shooong.push.domain.user.users.entity.Users;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class LikeFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId", nullable = false)
    private StyleFeed styleFeed;

}
