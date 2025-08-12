package com.example.system.action.Config;

import com.example.system.action.filter.CaptcheFilter;
import com.example.system.action.filter.JwtCookieFilter;
import com.example.system.action.handler.LoginHandler;
import com.example.system.action.handler.LogoutHandler;
import com.example.system.action.handler.PermissionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {



    //密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    @Autowired
    private CaptcheFilter captcheFilter;

    @Bean//跨域配置
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);
//        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    CorsConfiguration config = new CorsConfiguration();
    @Autowired
    LoginHandler loginHandler;
    @Autowired
    LogoutHandler logoutHandler;

    @Autowired
    JwtCookieFilter jwtCookieFilter;
    @Autowired
    PermissionHandler permissionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/captcha").permitAll()
                        .anyRequest().authenticated() // 所有请求需认证
                )
                .formLogin((login)->
                        login.successHandler(loginHandler).failureHandler(loginHandler)
                )// 登录管理

                .addFilterBefore(captcheFilter, UsernamePasswordAuthenticationFilter.class) //添加过滤器
                .addFilterBefore(jwtCookieFilter, LogoutFilter.class)

                .cors(cors -> cors.configurationSource(corsConfigurationSource)) //跨域配置

                .logout(logout -> logout.logoutSuccessHandler(logoutHandler))//注销管理

                .sessionManagement(session -> session.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )//禁用session

                .exceptionHandling(handling -> handling
                        .accessDeniedHandler(permissionHandler)//权限不足处理
                )


                .httpBasic(withDefaults())            // Basic认证
                .csrf((csrf)->csrf.disable())                 // CSRF保护
                .headers(withDefaults());              // 安全头

        return http.build();

    }


}
