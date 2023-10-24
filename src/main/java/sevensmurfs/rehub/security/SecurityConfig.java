package sevensmurfs.rehub.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Bean for http request filtration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                   .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                           .requestMatchers("/v1/auth/login").permitAll())
                   .authorizeHttpRequests(
                           requestMatcherRegistry -> requestMatcherRegistry.requestMatchers(HttpMethod.POST, "/v1/patient").permitAll())
                   .authorizeHttpRequests(
                           requestMatcherRegistry -> requestMatcherRegistry.requestMatchers(HttpMethod.POST, "/v1/employee")
                                                                           .permitAll())
                   .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                           .anyRequest().authenticated())
                   .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthFilter JwtAuthFilter() {
        return new JwtAuthFilter();
    }

    /**
     * Bean for password encoding using BCrypt Password length is 60 characters after encoding
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
