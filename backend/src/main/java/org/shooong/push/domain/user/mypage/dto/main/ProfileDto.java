package org.shooong.push.domain.user.mypage.dto.main;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileDto {

    private String profileImg;
    private String nickname;
    private String email;

    private int grade;
}
