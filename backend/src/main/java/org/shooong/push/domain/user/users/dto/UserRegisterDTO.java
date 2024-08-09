package org.shooong.push.domain.user.users.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRegisterDTO {

    private String email;
    private String password;
    private String nickname;
    private String phoneNum;
}
