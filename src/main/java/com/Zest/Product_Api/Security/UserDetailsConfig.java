package com.Zest.Product_Api.Security;

import com.Zest.Product_Api.Repository.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserDetailsConfig {

    @Bean
    public UserDetailsService userDetailsService(UserRepo userRepository) {

        return username ->
                userRepository.findByEmail(username)
                        .map(user ->
                                User.builder()
                                        .username(user.getEmail())
                                        .password(user.getPassword())
                                        .roles(user.getRole())
                                        .build()
                        )
                        .orElseThrow(() ->
                                new UsernameNotFoundException("User not found"));
    }
}