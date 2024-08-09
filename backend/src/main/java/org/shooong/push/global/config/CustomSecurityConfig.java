package org.shooong.push.global.config;

import org.shooong.push.global.auth.filter.JWTCheckFilter;
import org.shooong.push.global.auth.handler.CustomAccessDeniedHandler;
import org.shooong.push.global.auth.handler.CustomLoginFailHandler;
import org.shooong.push.global.auth.handler.CustomLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@Log4j2
public class CustomSecurityConfig {

    private final JWTCheckFilter jwtCheckFilter;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomLoginFailHandler customLoginFailHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private static final String[] AUTHENTICATED_ENDPOINTS = {
            "/api/**",
            "/feed/user/**",
            "/inquiry/{inquiryId}/delete",
            "/requestProduct/user/**",
            "/api/products/details/**",
            "/order/**",
            "/coupon/{couponId}/issue",
            "/alarm/subscribe",
            "/products/details/**",
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return webSecurity -> {
//
//        }
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Custom Security Filter Chain..............");

        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        /**
         * CSRF 설정 - CSRF 보호 기능 비활성화
         */
        http.sessionManagement(sessionConfig ->
                sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(config -> {
            config.loginPage("/user/login");
            config.successHandler(customLoginSuccessHandler);
            config.failureHandler(customLoginFailHandler);
        });

        http.oauth2Login(config -> {
            config.loginPage("/oauth/authorize");
            config.successHandler(customLoginSuccessHandler);
        });

        // JWT 체크 필터 가장 먼저 실행되도록
        http.addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);

        // 접근 제한 시 CustomAccessDeniedHandler 사용
        http.exceptionHandling(config -> {
            config.accessDeniedHandler(customAccessDeniedHandler);
        });

        // 특정 경로에 대해 인증 없이 접근 가능하도록 설정
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(AUTHENTICATED_ENDPOINTS).authenticated();
//            auth.requestMatchers("/admin/**").hasRole("ADMIN");
            auth.anyRequest().permitAll();
        });

        // 시큐리티 전체 비활성화
//        http.csrf().disable()
//                .cors().and()
//                .authorizeRequests().anyRequest().permitAll();

        return http.build();
    }

    /**
     * CORS 설정 - Ajax 요청 허용
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // TODO: 추후 도메인 변경
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
//        configuration.setAllowedOriginPatterns(Arrays.asList("http://www.sho0ong.com"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
//        configuration.addExposedHeader("Set-Cookie");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
