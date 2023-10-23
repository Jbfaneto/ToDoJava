package com.joaoneto.todosimple.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaoneto.todosimple.controllers.exceptions.GlobalExceptionHandler;
import com.joaoneto.todosimple.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        setAuthenticationFailureHandler(new GlobalExceptionHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            User userCredentials = new ObjectMapper()
                    .readValue(req.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword(), new ArrayList<>());
            Authentication auth = this.authenticationManager.authenticate(authToken);
            return auth;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain filterChain,
                                            Authentication authentication) throws IOException, ServletException {
        UserSpringSecurity userSpringSecurity = (UserSpringSecurity) authentication.getPrincipal();

        String username = userSpringSecurity.getUsername();
        String token = this.jwtUtil.generateToken(username);
        res.addHeader("Authorization", "Bearer " + token);
        res.addHeader("access-control-expose-headers", "Authorization");
    }
}