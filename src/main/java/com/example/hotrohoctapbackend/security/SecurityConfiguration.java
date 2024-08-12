package com.example.hotrohoctapbackend.security;


import com.example.hotrohoctapbackend.fillter.JwtFilter;
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

@Configuration
public class SecurityConfiguration {

    @Autowired
    private JwtFilter jwtFilter;

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
        http.authorizeHttpRequests(
                config -> config
                        .requestMatchers(HttpMethod.GET, Endpoint.PUBLIC_GET_ENDPOINS).permitAll()
                        .requestMatchers(HttpMethod.POST, Endpoint.PUBLIC_POST_ENDPOINS).permitAll()
                        .requestMatchers(HttpMethod.PUT, Endpoint.PUBLIC_PUT_ENDPOINS).permitAll()
                        .requestMatchers(HttpMethod.DELETE, Endpoint.PUBLIC_DELETE_ENDPOINS).permitAll()


                        .requestMatchers(HttpMethod.GET, Endpoint.ADMIN_GET_ENDPOINS).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, Endpoint.ADMIN_POST_ENDPOINS).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, Endpoint.ADMIN_DELETE_ENDPOINS).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, Endpoint.ADMIN_PUT_ENDPOINS).hasAuthority("ADMIN")

//                        .requestMatchers(HttpMethod.GET, Endpoint.USERVIP_GET_ENDPOINS).hasAuthority("USER_VIP")

        );
//        http.cors(cors -> {
//            cors.configurationSource(request -> {
//                CorsConfiguration corsConfig = new CorsConfiguration();
//                corsConfig.addAllowedOrigin(Endpoint.front_end_host);
//                corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//                corsConfig.addAllowedHeader("*");
//                return corsConfig;
//            });
//        });

        //Fillter truoc khi vo Check quyen
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);



        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
