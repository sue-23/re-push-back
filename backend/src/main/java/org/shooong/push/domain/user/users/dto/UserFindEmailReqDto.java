package org.shooong.push.domain.user.users.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserFindEmailReqDto {

    private String nickname;
    private String phoneNum;
}
