package org.shooong.push.domain.user.users.service;

import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.user.users.entity.Users;
import org.shooong.push.global.exception.CustomUserException;
import org.shooong.push.domain.user.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("========loadUserByUsername 메서드 호출========");

        Users user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 email 입니다."));

        if (user.isUnregistered()) {
            throw new CustomUserException("User is unregistered");
        }

        log.info("User found: {}", user);

        UserDTO userDTO = new UserDTO(
                user.getUserId(),
                user.getEmail(),
                user.getPassword(),
                user.getGrade(),
                user.getNickname(),
                user.getPhoneNum(),
                user.getProfileImg(),
                user.isRole(),
                user.isSocial()
        );

        log.info(userDTO);

        return userDTO;
    }
}
