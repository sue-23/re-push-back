package org.shooong.push.global.auth.handler;

import org.shooong.push.domain.user.users.repository.TokenRepository;
import org.shooong.push.global.util.JWTUtil;
import org.shooong.push.domain.user.users.dto.UserDTO;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final TokenRepository tokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("-------------onAuthenticationSuccess--------------");

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        Map<String, Object> claims = userDTO.getClaims();

        String accessToken = jwtUtil.generateToken(claims, 300);
        String refreshToken = jwtUtil.generateToken(claims, 60*24);

        tokenRepository.saveToken(userDTO.getUsername(), refreshToken, jwtUtil.extractExpiration(refreshToken));

        log.info("redis set key: {}, value: {}", userDTO.getUsername(), refreshToken);

        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        Gson gson = new Gson();
//        claims.remove("accessToken");
//        claims.remove("refreshToken");
        String jsonStr = gson.toJson(claims);

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();
    }
}
