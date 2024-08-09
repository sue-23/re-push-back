package org.shooong.push.domain.user.users.controller;

import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.user.users.dto.UserFindEmailReqDto;
import org.shooong.push.domain.user.users.dto.UserFindEmailResDto;
import org.shooong.push.domain.user.users.dto.UserRegisterDTO;
import org.shooong.push.global.util.JWTUtil;
import org.shooong.push.domain.user.users.service.RefreshTokenService;
import org.shooong.push.domain.user.users.service.TokenBlacklistService;
import org.shooong.push.domain.user.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Log4j2
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JWTUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    // 일반회원 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestPart(required = false) MultipartFile file,
                                          @RequestPart UserRegisterDTO userRegisterDTO) {

        userService.registerUser(userRegisterDTO, file, false);
        return ResponseEntity.ok("User registered successfully");
    }

    // 관리자 회원가입
    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestPart(required = false) MultipartFile file,
                                           @RequestPart UserRegisterDTO userRegisterDTO) {

        userService.registerUser(userRegisterDTO, file,  true);
        return ResponseEntity.ok("Admin registered successfully");
    }

    // 이메일 찾기
    @PostMapping("/findEmail")
    public ResponseEntity<?> findEmail(@RequestBody UserFindEmailReqDto userFindEmailReqDto) {
        UserFindEmailResDto userFindEmailResDto = userService.findEmail(userFindEmailReqDto.getNickname(), userFindEmailReqDto.getPhoneNum());

        if (userFindEmailResDto != null) {
            return ResponseEntity.ok(userFindEmailResDto);
        } else {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {

        return ResponseEntity.ok().build();
    }

    // 카카오 소셜 로그인
    @GetMapping("/kakao")
    public Map<String, Object> getUserFromKakao(@RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.substring(7);

        UserDTO userDTO = userService.getKakaoMember(accessToken);

        Map<String, Object> claims = userDTO.getClaims();

        String kakaoAccessToken = jwtUtil.generateToken(claims, 30);         // 30분
        String kakaoRefreshToken = jwtUtil.generateToken(claims, 60*24);     // 1일

        claims.put("accessToken", kakaoAccessToken);
        claims.put("refreshToken", kakaoRefreshToken);

        return claims;
    }

    // 토큰 재발행
    @PostMapping("/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, String> token) {
        String refreshToken = token.get("refreshToken");

        return refreshTokenService.refreshToken(authHeader, refreshToken);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        tokenBlacklistService.addBlackList(authHeader);

        return ResponseEntity.ok().build();
    }

    // 회원 탈퇴
    @PostMapping("/unregister")
    public ResponseEntity<Void> unregisterUser(@RequestHeader("Authorization") String authHeader, @AuthenticationPrincipal UserDTO userDTO) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        tokenBlacklistService.addBlackList(authHeader);

        userService.unregisterUser(userDTO.getUserId());

        return ResponseEntity.ok().build();
    }

}
