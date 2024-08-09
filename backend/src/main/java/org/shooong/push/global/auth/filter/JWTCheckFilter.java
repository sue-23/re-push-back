package org.shooong.push.global.auth.filter;

import org.shooong.push.global.exception.CustomJWTException;
import org.shooong.push.domain.user.users.repository.TokenRepository;
import org.shooong.push.global.util.JWTUtil;
import org.shooong.push.domain.user.users.dto.UserDTO;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JWTUtil jwtUtil;
    private final TokenRepository tokenRepository;


    private static final List<String> AUTHENTICATED_ENDPOINTS = List.of(
        "/api/**",
        "/*/api/**",
            "/user/unregister",
            "/feed/user/**",
            "/inquiry/*/delete",
            "/requestProduct/user/**",
            "/order/**",
            "/coupon/*/issue",
            "/products/details/**",
            "/coupon/user",
            "/alarm/subscribe",
            "/feed/feedBookmark",
            "/feed/styleFeed",
            "/inquiry/inquiryList",
            "/inquiry/admin/*",
            "/inquiry/inquiryResponseRegistration/*",
            "/inquiry/user/registration",
            "/notice/user/**",
            "/inquiry/**",
            "/api/products/details/**",
            "/announcementRegistration",
            "/product/bookmark",
            "/product/request",
            "/modifyAnnouncement/**",
            "/deleteAnnouncement/**",
            "/bid/**",
            "/select/bookmark",
            "/delete/bookmark/**"

    );

    /**
     * 체크하지 않을 경로나 메서드(GET/POST) 지정
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {


        // 1) Ajax 통신 시 Preflight로 전송되는 OPTIONS 방식 경로 제외
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

//        String path = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        String path = request.getRequestURI();

        log.info("check uri............" + path);

        // 3) 이미지 조회 경로 제외
        // TODO: 클라우드 DB 이미지 업로드 성공 시 경로 설정

        return AUTHENTICATED_ENDPOINTS.stream().noneMatch(endpoint -> pathMatcher.match(endpoint, path));
    }

    /**
     * 모든 요청에 대해 체크
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("------------------JWTCheckFilter...-----------------");

        String authHeader = request.getHeader("Authorization");

        try {
            String accessToken = authHeader.substring(7);

            if (tokenRepository.isBlacklist(accessToken)) {
                throw new CustomJWTException("TOKEN_BLACKLISTED");
            }

            Map<String, Object> claims = jwtUtil.validateToken(accessToken);

            log.info("###JWT claims: {}", claims);

            /**
             * 접근 권한별 처리를 위한 인증 컨텍스트 설정
             */
            Long userId = ((Integer) claims.get("userId")).longValue();
            String email = (String) claims.get("email");
            int grade = (int) claims.get("grade");
            String nickname = (String) claims.get("nickname");
            String phoneNum = (String) claims.get("phoneNum");
            String profileImg = (String) claims.get("profileImg");
            Boolean social = (Boolean) claims.get("social");
            Boolean role = (Boolean) claims.get("role");

            UserDTO userDTO = new UserDTO(userId, email, "", grade, nickname, phoneNum, profileImg, role, social);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDTO, "", userDTO.getAuthorities());
            log.info("authentication: {}", authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT Check Error: " + e.getMessage());

            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter printWriter = response.getWriter();
            printWriter.write(new Gson().toJson(Map.of("error", "ERROR_ACCESS_TOKEN")));
            printWriter.close();
        }
    }
}
