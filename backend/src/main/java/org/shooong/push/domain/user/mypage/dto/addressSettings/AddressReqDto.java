package org.shooong.push.domain.user.mypage.dto.addressSettings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressReqDto {

    private String name;

    @Pattern(regexp = "\\d{11}", message = "핸드폰 번호는 공백없이 11자리 숫자만 가능합니다.")
    private String addrPhone;

    @NotBlank
    private String zonecode;

    private String roadAddress;

    private String jibunAddress;

    private String detailAddress;

    private String extraAddress;

    private boolean defaultAddress;

}
