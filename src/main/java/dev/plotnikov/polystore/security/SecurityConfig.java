package dev.plotnikov.polystore.security;

import dev.plotnikov.polystore.filters.CustomAuthenticationFilter;
import dev.plotnikov.polystore.filters.CustomAuthorizationFilter;
import dev.plotnikov.polystore.services.AuthService;
import dev.plotnikov.polystore.services.CheckTokenService;
import dev.plotnikov.polystore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserService userService;
    private final AuthService authService;
    private final CheckTokenService checkAuthorization;


    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    public static final String[] PUBLIC_PATHS = {
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"};

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager(), userService, authService);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/js/**", "/api/login/**", "/api/token/refresh/**", "/gs-guide-websocket/**").permitAll()
                        .requestMatchers(PUBLIC_PATHS).permitAll()
                        .requestMatchers(POST, "/api/role/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/users/**").hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest()
                        .authenticated())
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(checkAuthorization), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}