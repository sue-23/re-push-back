package org.shooong.push.domain.user.users.repository;

import org.shooong.push.domain.user.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
//    @Cacheable(value = CacheLogin.USER, key = "#p0")
    Optional<Users> findByEmail(String email);

    @Query("SELECT u.email FROM Users u WHERE u.nickname = :nickname AND u.phoneNum = :phoneNum")
    String findEmail(String nickname, String phoneNum);

    boolean existsByEmail(String email);

    boolean existsByUserId(Long userId);


}
