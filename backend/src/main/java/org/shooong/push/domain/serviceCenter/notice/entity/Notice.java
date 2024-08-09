package org.shooong.push.domain.serviceCenter.notice.entity;


import jakarta.persistence.*;
import lombok.*;
import org.shooong.push.domain.BaseEntity;
import org.shooong.push.domain.user.users.entity.Users;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Getter
@Setter
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(nullable = false, length = 150)
    private String noticeTitle;

    @Column(nullable = false, length = 255)
    private String noticeContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    public void change(String noticeTitle, String noticeContent) {
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
    }

}