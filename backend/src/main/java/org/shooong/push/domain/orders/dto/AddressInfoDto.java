package org.shooong.push.domain.orders.dto;

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
public class AddressInfoDto {
    private Long addressId;
    private String name;
    private String addrPhone;
    private String roadAddress;
    private String detailAddress;
    private String zonecode;
}
