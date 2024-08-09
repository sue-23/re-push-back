package org.shooong.push.domain.address.service;

import org.shooong.push.domain.user.mypage.dto.addressSettings.AddressDto;
import org.shooong.push.domain.user.mypage.dto.addressSettings.AddressReqDto;
import org.shooong.push.domain.address.entity.Address;
import org.shooong.push.domain.user.users.entity.Users;
import org.shooong.push.domain.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    /**
     * 배송지 전체 조회
     * 1 : N
     * 기본 배송지로 설정된 배송지가 가장 상위에 정렬
     */
    public List<AddressDto> getAllAddress(Long userId) {
        return addressRepository.findAllByUserId(userId).stream()
                .map(AddressDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 등록된 기본 배송지가 존재하는지 확인
     */
    public boolean validateDefaultAddress(Long userId) {
        return addressRepository.existsDefaultAddressByUserUserId(userId);
    }

    /**
     * 배송지 등록
     */
    @Transactional
    public AddressDto addAddress(Long userId, AddressReqDto addressReqDto) {

        boolean existAddress = addressRepository
                .existsByZonecodeAndRoadAddressAndUserUserId(addressReqDto.getZonecode(), addressReqDto.getRoadAddress(), userId);

        if (existAddress) {
            throw new IllegalStateException("이미 존재하는 주소지입니다.");
        }

        if (!validateDefaultAddress(userId)) {
            addressReqDto.setDefaultAddress(true);
        } else {
            updateDefaultAddress(userId, addressReqDto);
        }

        Address address = Address.builder()
                .user(Users.builder().userId(userId).build())
                .name(addressReqDto.getName())
                .addrPhone(addressReqDto.getAddrPhone())
                .zonecode(addressReqDto.getZonecode())
                .roadAddress(addressReqDto.getRoadAddress())
                .jibunAddress(addressReqDto.getJibunAddress())
                .detailAddress(addressReqDto.getDetailAddress())
                .extraAddress(addressReqDto.getExtraAddress())
                .defaultAddress(addressReqDto.isDefaultAddress())
                .build();

        addressRepository.save(address);

        return AddressDto.fromEntity(address);
    }

    /**
     * 배송지 수정
     */
    @Transactional
    public AddressDto updateAddress(Long userId, Long addressId, AddressReqDto addressReqDto) {

        Address address = validationAddress(userId, addressId);

        updateDefaultAddress(userId, addressReqDto);

        address.updateAddress(addressReqDto);

        addressRepository.save(address);

        return AddressDto.fromEntity(address);
    }

    /**
     * 배송지 삭제
     */
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Address address = validationAddress(userId, addressId);

        addressRepository.delete(address);

        List<Address> remainAddresses =  addressRepository.findAllByUserId(userId);

        if (remainAddresses.size() == 1) {
            Address remainAddress = remainAddresses.get(0);
            if (!remainAddress.getDefaultAddress()) {
                remainAddress.updateDefaultAddress(true);
                addressRepository.save(remainAddress);
            }
        }
    }

    /**
     * 기본 배송지를 선택했는지 확인
     * 기존 기본 배송지에서 새로 입력한 배송지로 기본 배송지 변경
     */
    @Transactional
    public void updateDefaultAddress(Long userId, AddressReqDto addressReqDto) {
        if (addressReqDto.isDefaultAddress()) {
            addressRepository.updateDefaultAddress(userId);
        }
    }

    /**
     * 존재하는 배송지인지 확인
     */
    public Address validationAddress(Long userId, Long addressId) {
        return addressRepository.findByAddressIdAndUserUserId(addressId, userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배송지 입니다."));
    }
}
