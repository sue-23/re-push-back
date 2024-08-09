package org.shooong.push.domain.user.mypage.dto.addressSettings;

import org.shooong.push.domain.address.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private Long addressId;
    private String name;
    private String addrPhone;
    private String zonecode;
    private String roadAddress;
    private String jibunAddress;
    private String detailAddress;
    private String extraAddress;
    private boolean defaultAddress;


    public static AddressDto fromEntity(Address address){
        return AddressDto.builder()
                .addressId(address.getAddressId())
                .name(address.getName())
                .addrPhone(address.getAddrPhone())
                .zonecode(address.getZonecode())
                .roadAddress(address.getRoadAddress())
                .jibunAddress(address.getJibunAddress())
                .detailAddress(address.getDetailAddress())
                .extraAddress(address.getExtraAddress())
                .defaultAddress(address.getDefaultAddress())
                .build();
    }
}
