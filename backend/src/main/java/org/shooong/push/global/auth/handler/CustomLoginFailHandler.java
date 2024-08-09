package org.shooong.push.global.auth.handler;

import org.shooong.push.global.exception.CustomUserException;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
@Log4j2
public class CustomLoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.info("Login failed..." + exception);

        Gson gson = new Gson();
        PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        if (exception.getCause() instanceof CustomUserException) {
            printWriter.println(gson.toJson(Map.of("error", "UNREGISTER_USER")));
        } else {
            printWriter.println(gson.toJson(Map.of("error", "LOGIN_ERROR")));
        }

        printWriter.close();
    }
}
