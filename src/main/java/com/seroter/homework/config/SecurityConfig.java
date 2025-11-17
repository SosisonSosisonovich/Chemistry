package com.seroter.homework.config;

import com.seroter.homework.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // нет хэширования пароля намеренно
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/", "/index.html", "/login.html", "/experiment.html","/css/**", "/js/**", "/auth/**").permitAll()

                .antMatchers(HttpMethod.POST, "/calculate/**").permitAll()

                // только GET — можно всем
                .antMatchers(HttpMethod.GET, "/calculate/**", "/material/**").permitAll()

                // только авторизованным: POST, PUT, DELETE
                .antMatchers(HttpMethod.POST, "/material/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/material/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/material/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated();

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}
