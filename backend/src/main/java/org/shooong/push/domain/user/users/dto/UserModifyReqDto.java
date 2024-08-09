package org.shooong.push.domain.user.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserModifyReqDto {

    private String email;
    @NotBlank
    private String password;
    private String nickname;
    private String phoneNum;
    private String profileImg;
}
