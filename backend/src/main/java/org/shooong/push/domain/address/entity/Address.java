package org.shooong.push.domain.address.entity;

import org.shooong.push.domain.user.mypage.dto.addressSettings.AddressReqDto;
import jakarta.persistence.*;
import lombok.*;
import org.shooong.push.domain.user.users.entity.Users;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    // 수령인 이름
    @Column(length = 255)
    private String name;

    // 수령인 전화번호
    @Column(length = 255)
    private String addrPhone;

    // 우편 번호
    @Column(length = 20, nullable = false)
    private String zonecode;

    // 도로명 주소
    @Column(length = 255)
    private String roadAddress;

    // 지번 주소
    @Column(length = 255)
    private String jibunAddress;

    // 상세 주소
    @Column(length = 255, nullable = false)
    private String detailAddress;

    // 추가 주소 정보
    @Column(length = 20, nullable = false)
    private String extraAddress;

    // 기본 배송지 설정 여부
    @Column(nullable = false)
    private Boolean defaultAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Users user;


    public void updateAddress(AddressReqDto addressReqDto) {
        this.name = addressReqDto.getName();
        this.addrPhone = addressReqDto.getAddrPhone();
        this.zonecode = addressReqDto.getZonecode();
        this.roadAddress = addressReqDto.getRoadAddress();
        this.jibunAddress = addressReqDto.getJibunAddress();
        this.detailAddress = addressReqDto.getDetailAddress();
        this.extraAddress = addressReqDto.getExtraAddress();
        this.defaultAddress = addressReqDto.isDefaultAddress();
    }

    public void updateDefaultAddress(Boolean isDefaultAddress) {
        this.defaultAddress = isDefaultAddress;
    }
}
