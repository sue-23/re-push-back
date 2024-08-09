package org.shooong.push.domain.user.users.entity;

import org.shooong.push.domain.user.users.dto.UserModifyReqDto;
import org.shooong.push.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private int grade = 0;

    @Column(length = 50)
    private String nickname;

    @Column(length = 20)
    private String phoneNum;

    @Column(length = 255)
    private String profileImg;

    // 디폴트 false = 회원, true = 관리자
    @Column(nullable = false)
    private boolean role;

    // 디폴트 false = 일반회원, true = 소셜회원
    @Column(nullable = false)
    private boolean social;

    @Column(nullable = false)
    private boolean isUnregistered;

    public void updateUser(UserModifyReqDto userModifyReqDto, String imageUrl) {
        this.email = userModifyReqDto.getEmail();
        if (userModifyReqDto.getPassword() != null && !userModifyReqDto.getPassword().isBlank()) {
            this.password = userModifyReqDto.getPassword();
        }
        this.nickname = userModifyReqDto.getNickname();
        this.phoneNum = userModifyReqDto.getPhoneNum();
        this.profileImg = imageUrl;
    }

    public void unregisterUser(boolean isUnregistered) {
        this.isUnregistered = isUnregistered;
    }
}
