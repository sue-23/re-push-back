package org.shooong.push.domain.user.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserModifyResDto {

    private String email;
    private String nickname;
    private String phoneNum;
    private String profileImg;
}
