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
public class InquiryResponse extends BaseEntity {

    // 고객센터 - 1:1 문의 답변
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;

    @Column(nullable = false, length = 300)
    private String response;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @OneToOne
    @JoinColumn(name = "inquiryId", nullable = false)
    private Inquiry inquiry;
}