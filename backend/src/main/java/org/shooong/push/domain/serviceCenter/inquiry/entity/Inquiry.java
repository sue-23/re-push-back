package org.shooong.push.domain.serviceCenter.inquiry.entity;


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
public class Inquiry extends BaseEntity {

    // 고객센터 - 1:1 문의
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;

    @Column(nullable = false, length = 150)
    private String inquiryTitle;

    @Column(nullable = false, length = 300)
    private String inquiryContent;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    public void change(String inquiryTitle, String inquiryContent) {
        this.inquiryContent = inquiryContent;
        this.inquiryTitle = inquiryTitle;
    }
}