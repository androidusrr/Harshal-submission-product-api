package com.Zest.Product_Api.Service;

import com.Zest.Product_Api.dto.*;
import com.Zest.Product_Api.Entity.RefreshToken;
import com.Zest.Product_Api.Entity.User;
import com.Zest.Product_Api.Repository.*;

import com.Zest.Product_Api.Security.JwtService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepo userRepository;
    private final RefreshTokenRepo refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final com.Zest.Product_Api.Security.JwtService jwtService;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public AuthService(
            UserRepo userRepository,
            RefreshTokenRepo refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Email already registered");
        }

        User user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails user =
                userDetailsService.loadUserByUsername(
                        request.getEmail());

        String accessToken =
                jwtService.generateAccessToken(user);

        String refreshToken =
                createRefreshToken(
                        userRepository
                                .findByEmail(request.getEmail())
                                .orElseThrow());

        return new LoginResponse(
                accessToken,
                refreshToken
        );
    }

    public LoginResponse refresh(String token) {

        RefreshToken oldToken =
                refreshTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new BadCredentialsException(
                                        "Invalid refresh token"));

        if (oldToken.isRevoked() ||
                oldToken.getExpiryDate()
                        .isBefore(LocalDateTime.now())) {

            throw new BadCredentialsException(
                    "Refresh token expired or revoked");
        }

        // Rotation: invalidate old token
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        UserDetails user =
                userDetailsService.loadUserByUsername(
                        oldToken.getUser().getEmail());

        String accessToken =
                jwtService.generateAccessToken(user);

        String newRefreshToken =
                createRefreshToken(oldToken.getUser());

        return new LoginResponse(
                accessToken,
                newRefreshToken
        );
    }

    private String createRefreshToken(User user) {

        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken =
                new RefreshToken();

        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setRevoked(false);

        refreshToken.setExpiryDate(
                LocalDateTime.now()
                        .plusNanos(
                                refreshExpiration * 1_000_000
                        )
        );

        refreshTokenRepository.save(refreshToken);

        return token;
    }
}