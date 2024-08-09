package org.shooong.push.domain.user.users.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserFindEmailResDto {

    private String email;

    public UserFindEmailResDto(String email) {
        this.email = email;
    }
}
