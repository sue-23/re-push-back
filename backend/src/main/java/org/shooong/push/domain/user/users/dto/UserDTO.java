package org.shooong.push.domain.user.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;
@Getter
@Setter
@Builder(builderClassName = "CustomUserDTOBuilder", builderMethodName = "customBuilder")
@ToString
public class UserDTO extends User {

    private Long userId;
    private String email;
    private String password;
    private int grade;
    private String nickname;
    private String phoneNum;
    private String profileImg;
    private boolean role;
    private boolean social;



    public UserDTO(Long userId, String email, String password, int grade, String nickname, String phoneNum, String profileImg, boolean role, boolean social) {

        super(email, password, true, true, true, true, List.of(role ? new SimpleGrantedAuthority("ROLE_ADMIN") : new SimpleGrantedAuthority("ROLE_USER")));

        this.userId = userId;
        this.email = email;
        this.password = password;
        this.grade = grade;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.profileImg = profileImg;
        this.role = role;
        this.social = social;
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("password", password);
        claims.put("nickname", nickname);
        claims.put("grade", grade);
        claims.put("phone", phoneNum);
        claims.put("profileImg", profileImg);
        claims.put("role", role);
        claims.put("social", social);

        return claims;
    }
}
