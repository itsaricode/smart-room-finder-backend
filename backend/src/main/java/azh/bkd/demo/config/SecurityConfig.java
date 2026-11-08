
package azh.bkd.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors -> {})

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // CORS preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Public authentication endpoints
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/auth/mobile-login"
                ).permitAll()

                // Admin-only analytics
                .requestMatchers("/api/analytics/**")
                    .hasRole("ADMIN")

                // Admin-only room management
                .requestMatchers(HttpMethod.POST, "/api/rooms")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/rooms/**")
                    .hasRole("ADMIN")

                // Authenticated users can view rooms
                .requestMatchers(HttpMethod.GET, "/api/rooms/**")
                    .hasAnyRole("ADMIN", "STUDENT", "TEACHER", "CR_GR")

                // Mobile users can request bookings
                .requestMatchers(HttpMethod.POST, "/api/bookings/request")
                    .hasAnyRole("STUDENT", "TEACHER", "CR_GR")

// Mobile users can view only their own booking history
.requestMatchers(HttpMethod.GET, "/api/bookings/my")
    .hasAnyRole("STUDENT", "TEACHER", "CR_GR")

                // Admin-only booking management
                .requestMatchers("/api/bookings/all")
                    .hasRole("ADMIN")

                .requestMatchers(
                    "/api/bookings/available-again/**",
                    "/api/bookings/*/complete"
                ).hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/bookings/**")
                    .hasRole("ADMIN")

                // Legacy admin endpoints require an admin JWT
                .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN")

                // Everything else requires authentication
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
