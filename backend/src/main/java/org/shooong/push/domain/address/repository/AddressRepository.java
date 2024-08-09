package org.shooong.push.domain.address.repository;

import org.shooong.push.domain.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * userId로 Address 목록 전체 조회
     * default_address = true 인 데이터를 가장 상위에 정렬
     */
    @Query("SELECT a FROM Address a WHERE a.user.userId = :userId ORDER BY a.defaultAddress DESC")
    List<Address> findAllByUserId(Long userId);

    /**
     * zonecode와 roadAddress, userId로 Address 존재 여부 확인
     */
    boolean existsByZonecodeAndRoadAddressAndUserUserId(String zonecode, String roadAddress, Long userId);


    /**
     * addressId와 userId로 Address 조회
     */
    Optional<Address> findByAddressIdAndUserUserId(Long addressId, Long userId);

    /**
     * 기본 배송지가 존재하는지 확인
     */
    boolean existsDefaultAddressByUserUserId(Long userId);

    /**
     * 기존 기본 배송지 상태 변경
     */
    @Modifying
    @Query("UPDATE Address a SET a.defaultAddress = false WHERE a.user.userId = :userId AND a.defaultAddress = true")
    void updateDefaultAddress(Long userId);

    Optional<Address> findByUserUserIdAndDefaultAddressTrue(Long userId);
}
