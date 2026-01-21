package com.education.platform.security.config;

import com.education.platform.security.config.filter.JwtTokenValidator;
import com.education.platform.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    //jwtUtils
    private final JwtUtils jwtUtils;

    public SecurityConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

        return http
                     .csrf(csrf ->  csrf.disable())
                     .httpBasic(Customizer.withDefaults())
                     .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                     .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                     .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider AuthenticationProvider (UserDetailsService userDetailsService) throws Exception {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider
                                             (userDetailsService);
        provider.setPasswordEncoder( bCryptPasswordEncoder() );
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration)throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

}
