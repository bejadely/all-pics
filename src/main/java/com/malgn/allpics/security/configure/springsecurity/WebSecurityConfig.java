package com.malgn.allpics.security.configure.springsecurity;

import com.malgn.allpics.security.handler.CustomLogoutSuccessHandler;
import com.malgn.allpics.security.handler.LoginFailureHandler;
import com.malgn.allpics.security.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {


    //Spring Security 인증 및 인가 처리 관련 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.formLogin(login -> login
                .loginPage("/auth/login-form")
                .loginProcessingUrl("/auth/login")
                .usernameParameter("id")
                .passwordParameter("pwd")
                .successHandler(loginSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .permitAll()
        );

        http.logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
        );

        http.sessionManagement(sessionManagement -> sessionManagement
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/auth/login-form?expired=true")
        );

        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("A2")
                .anyRequest().permitAll()
        );

        http.csrf(AbstractHttpConfigurer::disable);

        http.cors(AbstractHttpConfigurer::disable);

        return http.build();
    }

    //Spring Security 인증 예외 처리
    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new LoginFailureHandler();
    }

}
