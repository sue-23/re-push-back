package org.shooong.push.global.cache;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public class UserCacheValue {

    private String nickname;
    private String email;

    private UserCacheValue(String nickname, String email) {
        if (Objects.isNull(nickname))
            throw new IllegalArgumentException("Username cannot be null");
        if (Objects.isNull(email))
            throw new IllegalArgumentException("Email cannot be null");

        this.nickname = nickname;
        this.email = email;
    }

    @JsonCreator
    public static UserCacheValue of(@JsonProperty("nickname") String nickname,
                                    @JsonProperty("email") String email) {
        return new UserCacheValue(nickname, email);
    }
}
