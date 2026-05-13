package com.example.bankcards.service;

import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.dto.RefreshRequest;
import com.example.bankcards.entity.RefreshToken;
import com.example.bankcards.entity.User;
import com.example.bankcards.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        String accessToken = jwtService.generateAccessToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails);


        return new AuthResponse(accessToken, refreshToken.getToken());

    }

    public AuthResponse refresh(RefreshRequest refreshRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyToken(refreshRequest.refreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken.getUser().getEmail());
        String accessToken = jwtService.generateAccessToken(userDetails);
        return new AuthResponse(accessToken, refreshRequest.refreshToken());
    }

    public void logout(String refreshToken){
        refreshTokenService.deleteToken(refreshToken);
    }

}
