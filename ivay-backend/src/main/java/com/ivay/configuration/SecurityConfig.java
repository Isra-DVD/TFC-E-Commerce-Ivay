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

/**
 * Configuration class for Spring Security.
 *
 * This class defines the security filter chain, authentication provider,
 * password encoder, and JWT authentication filter for the application.
 *
 * It configures:
 * - CSRF protection disabled
 * - CORS with default settings
 * - Public access to API documentation and certain GET/POST endpoints
 * - Role-based access control for protected resources
 * - Stateless session management
 * - JWT-based authentication filter
 * - HTTP Basic authentication as a fallback
 *
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	/**
	 * Configures the main security filter chain.
	 *
	 * This method defines:
	 * - Disabled CSRF protection
	 * - CORS with default configuration
	 * - Public, authenticated, and role-restricted request matchers
	 * - Stateless session management
	 * - JWT authentication filter before UsernamePasswordAuthenticationFilter
	 * - HTTP Basic authentication support
	 *
	 * @param http the HttpSecurity to configure
	 * @return the configured SecurityFilterChain
	 * @throws Exception if an error occurs while building the security filter chain
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		String[] PUBLIC_DOCS = {
				"/doc/swagger-ui.html",
				"/doc/swagger-ui/**",
				"/v3/api-docs/**",
				"/error"
		};

		String[] PUBLIC_GET = {
				"/api/products",
				"/api/products/filter",
				"/api/products/{productId}",
				"/api/categories",
				"/api/categories/filter",
				"/api/categories/{categoryId}",
				"/api/categories/{categoryId}/products"
		};

		String[] PUBLIC_POST = {
				"/api/auth/**",
				"/api/users"
		};

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

				.requestMatchers(PUBLIC_DOCS).permitAll()
				.requestMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()
				.requestMatchers(HttpMethod.POST, PUBLIC_POST).permitAll()

				.requestMatchers(AUTHENTICATED).authenticated()

				.requestMatchers(HttpMethod.GET, "/api/addresses/users/{userId}").authenticated()
				.requestMatchers(HttpMethod.POST, "/api/addresses").authenticated()
				.requestMatchers(HttpMethod.PUT, "/api/addresses/{id}").authenticated()
				.requestMatchers("/api/addresses/**").hasAnyRole("SUPERADMIN", "ADMIN")

				.requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("SUPERADMIN", "ADMIN")
				.requestMatchers(HttpMethod.GET, "/api/users/{userId}/orders").hasAnyRole("SUPERADMIN", "ADMIN")
				.requestMatchers(HttpMethod.PUT, "/api/orders/{orderId}").hasAnyRole("SUPERADMIN", "ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/orders/{orderId}").hasAnyRole("SUPERADMIN", "ADMIN")
				.requestMatchers(HttpMethod.POST, "/api/orders").authenticated()

				.requestMatchers("/api/order-items/**").hasAnyRole("SUPERADMIN", "ADMIN")

				.requestMatchers(HttpMethod.POST, "/api/categories/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
				.requestMatchers(HttpMethod.PUT,  "/api/categories/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
				.requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")

				.requestMatchers(HttpMethod.POST,   "/api/products/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
				.requestMatchers(HttpMethod.PUT,    "/api/products/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
				.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
				.requestMatchers(HttpMethod.GET,    "/api/products/{productId}/cart-items")
				.hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
				.requestMatchers(HttpMethod.GET,    "/api/products/{productId}/order-items")
				.hasAnyRole("SUPERADMIN", "ADMIN")

				.requestMatchers("/api/roles/**").hasAnyRole("SUPERADMIN", "ADMIN")
				.requestMatchers(HttpMethod.POST, "/api/roles/**").hasRole("SUPERADMIN")
				.requestMatchers(HttpMethod.PUT,  "/api/roles/**").hasRole("SUPERADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/roles/**").hasRole("SUPERADMIN")

				.requestMatchers("/api/suppliers/**")
				.hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")

				.requestMatchers(HttpMethod.GET,    "/api/users/**").hasAnyRole("SUPERADMIN", "ADMIN")
				.requestMatchers(HttpMethod.PUT,    "/api/users/{id}").hasAnyRole("SUPERADMIN", "ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAnyRole("SUPERADMIN", "ADMIN")

				.anyRequest().authenticated()
				)
		.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
		.httpBasic(Customizer.withDefaults());

		return http.build();
	}

	/**
	 * Exposes the AuthenticationManager bean.
	 *
	 * @param authConfig the AuthenticationConfiguration to obtain the manager from
	 * @return the AuthenticationManager used by Spring Security
	 * @throws Exception if the authentication manager cannot be retrieved
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * Defines the AuthenticationProvider that uses a DAO-based approach.
	 *
	 * This provider uses a PasswordEncoder and a custom UserDetailsServiceImpl to authenticate users.
	 *
	 * @param userDetailsService the user details service implementation
	 * @return a configured DaoAuthenticationProvider
	 */
	@Bean
	AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

	/**
	 * Defines the PasswordEncoder bean.
	 *
	 * @return a BCryptPasswordEncoder instance for hashing passwords
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Defines the JWT authentication filter bean.
	 *
	 * This filter extracts and validates JWT tokens from incoming requests
	 * and sets the authentication in the security context.
	 *
	 * @return a new JwtAuthenticationFilter instance
	 */
	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}
}
