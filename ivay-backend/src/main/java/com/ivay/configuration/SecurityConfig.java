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
import org.springframework.security.config.http.SessionCreationPolicy;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ivay.jwt.JwtAuthenticationFilter;
import com.ivay.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    return http
	        .csrf(csrf -> csrf.disable())
	        .cors(Customizer.withDefaults())
	        .authorizeHttpRequests(auth -> auth

	        		 .requestMatchers(
	        	              "/doc/swagger-ui.html",
	        	              "/doc/swagger-ui/**",
	        	              "/v3/api-docs/**"
	        	          ).permitAll()
	        		
	        		.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
	        		
	        		.requestMatchers(HttpMethod.GET, "/api/addresses").hasAnyRole("SUPERADMIN", "ADMIN")  
	        		.requestMatchers(HttpMethod.GET, "/api/addresses/{id}").permitAll()
	        		.requestMatchers(HttpMethod.GET, "/api/addresses/users/{userId}").hasAnyRole("SUPERADMIN", "ADMIN")
	        		.requestMatchers(HttpMethod.DELETE, "/api/addresses/{id}").hasAnyRole("SUPERADMIN", "ADMIN")
	        		.requestMatchers(HttpMethod.POST, "/api/addresses").permitAll()
	        		.requestMatchers(HttpMethod.PUT, "/api/addresses/{id}").permitAll()
	        		
	        		.requestMatchers(HttpMethod.GET, "/api/cart-items/{cartItemId}").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		.requestMatchers(HttpMethod.GET, "/api/users/{userId}/cart-items").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		.requestMatchers(HttpMethod.POST, "/api/cart-items").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		.requestMatchers(HttpMethod.PATCH, "/api/cart-items/{cartItemId}/quantity").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		.requestMatchers(HttpMethod.DELETE, "/api/cart-items/{cartItemId}").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		.requestMatchers(HttpMethod.DELETE, "/api/users/{userId}/cart-items").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		
	        		.requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
	        		.requestMatchers(HttpMethod.GET, "/api/categories/{categoryId}/products").permitAll()
	        		.requestMatchers(HttpMethod.GET, "/api/categories/filter").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.GET, "/api/categories/{categoryId}").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.POST, "/api/categories").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.PUT, "/api/categories/{categoryId}").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.DELETE, "/api/categories/{categoryId}").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		
	        		.requestMatchers(HttpMethod.GET, "/api/orders").hasAnyRole("SUPERADMIN", "ADMIN")
	        		.requestMatchers(HttpMethod.GET, "/api/users/{userId}/orders").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		.requestMatchers(HttpMethod.GET, "/api/orders/{orderId}").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		.requestMatchers(HttpMethod.GET, "/api/orders/{orderId}/items").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		.requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		.requestMatchers(HttpMethod.PUT, "/api/orders/{orderId}").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.DELETE, "/api/orders/{orderId}").hasAnyRole("SUPERADMIN", "ADMIN")
	        		
	        		.requestMatchers(HttpMethod.GET, "api/order-items/{orderItemId}").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		
	        		.requestMatchers(HttpMethod.GET, "/api/products").permitAll()
	        		.requestMatchers(HttpMethod.GET, "/api/products/filter").permitAll()
	        		.requestMatchers(HttpMethod.GET, "/api/products/{productId}").permitAll()
	        		.requestMatchers(HttpMethod.GET, "/api/products/{productId}/order-items").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.GET, "/api/products/{productId}/cart-items").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.POST, "/api/products").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.PUT, "/api/products/{productId}").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.DELETE, "/api/products/{productId}").hasAnyRole("SUPERADMIN", "ADMIN","MANAGER")
	        		
	        		.requestMatchers(HttpMethod.GET, "/api/roles").hasAnyRole("SUPERADMIN", "ADMIN")
	        		.requestMatchers(HttpMethod.GET, "/api/roles/{id}").hasAnyRole("SUPERADMIN", "ADMIN")
	        		.requestMatchers(HttpMethod.POST, "/api/roles").hasAnyRole("SUPERADMIN")
	        		.requestMatchers(HttpMethod.PUT, "/api/roles/{id}").hasAnyRole("SUPERADMIN")
	        		.requestMatchers(HttpMethod.DELETE, "/api/roles/{id}").hasAnyRole("SUPERADMIN")
	        		
	        		.requestMatchers(HttpMethod.GET, "/api/suppliers").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.GET, "/api/suppliers/{id}").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.POST, "/api/suppliers").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.PUT, "/api/suppliers/{id}").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		.requestMatchers(HttpMethod.DELETE, "/api/suppliers/{id}").hasAnyRole("SUPERADMIN", "ADMIN", "MANAGER")
	        		
	        		.requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("SUPERADMIN", "ADMIN")
	        		.requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		.requestMatchers(HttpMethod.POST, "/api/users").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		.requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAnyRole("SUPERADMIN", "ADMIN", "CLIENT")
	        		.requestMatchers(HttpMethod.DELETE, "/api/users").hasAnyRole("SUPERADMIN", "ADMIN")
	        )
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
	        .httpBasic(Customizer.withDefaults())
	        .build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception { // se puede inyectar por constructor el authCon

		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) throws Exception {
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