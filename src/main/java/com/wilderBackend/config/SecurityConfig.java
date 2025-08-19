package com.wilderBackend.config;


import com.wilderBackend.security.filter.AsyncSecurityContextPropagationFilter;
import com.wilderBackend.security.filter.AuthEntryPointJwt;
import com.wilderBackend.security.filter.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthEntryPointJwt authEntryPointJwt;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public AsyncSecurityContextPropagationFilter asyncSecurityContextPropagationFilter() {
        return new AsyncSecurityContextPropagationFilter();
    }

    @Bean
    public FilterRegistrationBean<AsyncSecurityContextPropagationFilter> disableDefaultRegistration(
            AsyncSecurityContextPropagationFilter filter) {
        FilterRegistrationBean<AsyncSecurityContextPropagationFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setEnabled(false); // prevent container auto-registration; we'll add into Security chain explicitly
        return reg;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Enable CORS with custom configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Disable CSRF for stateless REST APIs
                .csrf(AbstractHttpConfigurer::disable)
                // Set session management to stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Handle unauthorized access
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPointJwt))
                // Define endpoint permissions
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/error").permitAll()
                        .anyRequest().authenticated()
                );

        // add jwt filter as before
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // IMPORTANT: add this BEFORE AuthorizationFilter
        http.addFilterBefore(asyncSecurityContextPropagationFilter(), org.springframework.security.web.access.intercept.AuthorizationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
