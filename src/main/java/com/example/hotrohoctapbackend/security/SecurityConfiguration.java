package com.example.hotrohoctapbackend.security;


import com.example.hotrohoctapbackend.fillter.JwtFilter;
import com.example.hotrohoctapbackend.config.CustomOAuth2UserService;
import com.example.hotrohoctapbackend.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class SecurityConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Autowired
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
        dap.setUserDetailsService(userService);
        dap.setPasswordEncoder(passwordEncoder());
        return dap;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain");

        http.authorizeHttpRequests(
                        config -> config
                                .requestMatchers(HttpMethod.GET, Endpoint.PUBLIC_GET_ENDPOINS).permitAll()
                                .requestMatchers(HttpMethod.POST, Endpoint.PUBLIC_POST_ENDPOINS).permitAll()
                                .requestMatchers(HttpMethod.PUT, Endpoint.PUBLIC_PUT_ENDPOINS).permitAll()
                                .requestMatchers(HttpMethod.DELETE, Endpoint.PUBLIC_DELETE_ENDPOINS).permitAll()
//                                .requestMatchers(HttpMethod.PUT, "/api/account/update/**").hasAuthority("USER")

                                .requestMatchers(HttpMethod.GET, Endpoint.ADMIN_GET_ENDPOINT).hasAnyAuthority("ADMIN", "TEACHER")
                                .requestMatchers(HttpMethod.POST, Endpoint.ADMIN_POST_ENDPOINS).hasAnyAuthority("ADMIN", "TEACHER")
                                .requestMatchers(HttpMethod.DELETE, Endpoint.ADMIN_DELETE_ENDPOINS).hasAnyAuthority("ADMIN", "TEACHER")
                                .requestMatchers(HttpMethod.PUT, Endpoint.ADMIN_PUT_ENDPOINS).hasAnyAuthority("ADMIN", "TEACHER")


                                .requestMatchers(HttpMethod.GET, Endpoint.USER_GET_ENDPOINT).hasAnyAuthority("USER", "HUITSTUDENT", "USERVIP", "ADMIN")
                                .requestMatchers(HttpMethod.POST, Endpoint.USER_POST_ENDPOINT).hasAnyAuthority("USER", "HUITSTUDENT", "USERVIP", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, Endpoint.USER_PUT_ENDPOINT).hasAnyAuthority("USER", "HUITSTUDENT", "USERVIP", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, Endpoint.USER_DELETE_ENDPOINT).hasAnyAuthority("USER", "HUITSTUDENT", "USERVIP", "ADMIN")

                                .requestMatchers(HttpMethod.GET, Endpoint.HUIT_STUDENT_GET_ENDPOINT).hasAnyAuthority("HUITSTUDENT", "ADMIN")
                                .requestMatchers(HttpMethod.POST, Endpoint.HUIT_STUDENT_POST_ENDPOINT).hasAnyAuthority("HUITSTUDENT", "ADMIN", "TEACHER")


                                .requestMatchers(HttpMethod.GET, Endpoint.USER_VIP_GET_ENDPOINT).hasAuthority("USERVIP")
                                .requestMatchers(HttpMethod.POST, Endpoint.USER_VIP_POST_ENDPOINT).hasAuthority("USERVIP")
                                .requestMatchers(HttpMethod.PUT, Endpoint.USER_VIP_PUT_ENDPOINT).hasAuthority("USERVIP")
                                .requestMatchers(HttpMethod.DELETE, Endpoint.USER_VIP_DELETE_ENDPOINT).hasAuthority("USERVIP")


                                .requestMatchers(HttpMethod.GET, "/ws", "/ws/", "/ws/info", "/oauth2/authorization/google", "/account/oauth2/success").permitAll()
                                .requestMatchers(HttpMethod.POST, "/ws", "/ws/", "/ws/info", "/oauth2/authorization/google", "/account/oauth2/success").permitAll()
                                .requestMatchers("/ws/**").permitAll()
                                .anyRequest().authenticated()

                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/account/oauth2/success", true) // Chuyển hướng đến endpoint xử lý JWT
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService())
                        )
                );
        http.cors(Customizer.withDefaults());

        // Fillter truoc khi vo Check quyen
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Sử dụng custom access denied handler
        http.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler));

        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());

        logger.info("Security filter chain configured successfully");
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }
}
