package manager.ecommerce.security;

import manager.ecommerce.entity.contants.RoleEnum;
import lombok.RequiredArgsConstructor;
import manager.ecommerce.security.exception.AccessDenied;
import manager.ecommerce.security.principle.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import manager.ecommerce.security.exception.JwtEntryPoint;
import manager.ecommerce.security.jwt.JwtTokenFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
    private final MyUserDetailsService userDetailsService;
    private final JwtEntryPoint jwtEntryPoint;
    private final AccessDenied accessDenied;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
                .cors(cf -> cf.configurationSource(request ->
                {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:5173"));
                    config.setAllowedMethods(List.of("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(List.of("*"));
                    config.setExposedHeaders(List.of("*"));
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        url -> url
                                .requestMatchers(
                                        "/auth/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/api-docs/**",
                                        "/docs/**"
                                ).permitAll()
                                .requestMatchers("/api/v1/admin/**").hasAuthority(RoleEnum.ADMIN.toString())
                                .requestMatchers("/api/v1/user/**").hasAuthority(RoleEnum.USER.toString())
                                .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        exception -> exception
                                .authenticationEntryPoint(jwtEntryPoint)
                                .accessDeniedHandler(accessDenied)
                )
                .addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception
    {
        return auth.getAuthenticationManager();
    }
}
