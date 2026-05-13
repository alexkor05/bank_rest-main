package com.example.bankcards.service;

import com.example.bankcards.entity.RefreshToken;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.RefreshTokenRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${jwt.expiration.refresh}")
    private long jwtRefreshExpiration;



    public RefreshToken createRefreshToken(UserDetails userDetails){
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        refreshTokenRepository.deleteRefreshTokenByUser(user);

        String token = jwtService.generateRefreshToken(userDetails);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtRefreshExpiration));

        return refreshTokenRepository.save(refreshToken);
    }


    public RefreshToken verifyToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.
                findByToken(token).orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if(refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;

    }

    public void deleteToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }

}
