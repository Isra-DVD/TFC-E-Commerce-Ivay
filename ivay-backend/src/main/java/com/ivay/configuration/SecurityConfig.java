package com.ivay.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ivay.jwt.JwtAuthenticationFilter;
import com.ivay.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Rutas públicas de docs y error
        String[] PUBLIC_DOCS = {
            "/doc/swagger-ui.html",
            "/doc/swagger-ui/**",
            "/v3/api-docs/**",
            "/error"
        };
        // GET públicos
        String[] PUBLIC_GET = {
            "/api/products",
            "/api/products/filter",
            "/api/products/{productId}",
            "/api/categories",
            "/api/categories/filter",
            "/api/categories/{categoryId}",
            "/api/categories/{categoryId}/products"
        };
        // POST públicos ( auth / registro )
        String[] PUBLIC_POST = {
            "/api/auth/**",
            "/api/users"
        };
        // Rutas que sólo requieren autenticación (cualquier rol)
        String[] AUTHENTICATED = {
            "/api/users/me/**",
            "/api/cart-items/**",
            "/api/users/{userId}/cart-items",
            "/api/orders"
        };

        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                // públicas
                .requestMatchers(PUBLIC_DOCS).permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()
                .requestMatchers(HttpMethod.POST, PUBLIC_POST).permitAll()

                // autenticado genérico
                .requestMatchers(AUTHENTICATED).authenticated()

                // direcciones
                .requestMatchers(HttpMethod.GET, "/api/addresses/users/{userId}").authenticated()
                .requestMatchers("/api/addresses/**").hasAnyRole("SUPERADMIN", "ADMIN")

                // órdenes
                .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("SUPERADMIN", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/orders").hasAnyRole("SUPERADMIN", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/orders/{orderId}").hasAnyRole("SUPERADMIN", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/orders/{orderId}").hasAnyRole("SUPERADMIN", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/orders").authenticated()

                // order-items
                .requestMatchers("/api/order-items/**").hasAnyRole("SUPERADMIN", "ADMIN")

                // categorías
                .requestMatchers(HttpMethod.POST, "/api/categories/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.PUT,  "/api/categories/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")

                // productos
                .requestMatchers(HttpMethod.POST,   "/api/products/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.PUT,    "/api/products/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET,    "/api/products/{productId}/cart-items")
                    .hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET,    "/api/products/{productId}/order-items")
                    .hasAnyRole("SUPERADMIN", "ADMIN")

                // roles
                .requestMatchers("/api/roles/**").hasAnyRole("SUPERADMIN", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/roles/**").hasRole("SUPERADMIN")
                .requestMatchers(HttpMethod.PUT,  "/api/roles/**").hasRole("SUPERADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/roles/**").hasRole("SUPERADMIN")

                // suppliers
                .requestMatchers("/api/suppliers/**")
                    .hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")

                // usuarios (sólo ADMIN/SUPERADMIN pueden gestionar)
                .requestMatchers(HttpMethod.GET,    "/api/users/**").hasAnyRole("SUPERADMIN", "ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/users/{id}").hasAnyRole("SUPERADMIN", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAnyRole("SUPERADMIN", "ADMIN")

                // cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
