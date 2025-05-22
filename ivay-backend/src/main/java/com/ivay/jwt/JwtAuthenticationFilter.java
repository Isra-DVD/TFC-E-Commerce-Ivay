package com.ivay.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ivay.service.impl.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter that handles JWT authentication for incoming HTTP requests.
 *
 * On each request, this filter performs the following steps:
 * - Extracts the token from the Authorization header ("Bearer &lt;token&gt;")
 * - Validates the token using JwtTokenProvider
 * - Loads the user details using UserDetailsServiceImpl
 * - Creates an authentication token and stores it in the SecurityContext
 *
 * This enables stateless JWT-based authentication for secured endpoints.
 *
 * @since 1.0.0
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Extracts the JWT from the Authorization header of the request.
     *
     * @param request the current HTTP servlet request
     * @return the JWT string without the "Bearer " prefix, or null if not present
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Filters each HTTP request to perform JWT validation and set authentication.
     *
     * If a valid token is found:
     * - Retrieves the username (subject) from the token
     * - Loads the corresponding UserDetails
     * - Builds a UsernamePasswordAuthenticationToken
     * - Sets the authentication in the SecurityContext
     *
     * @param request     the current HTTP servlet request
     * @param response    the current HTTP servlet response
     * @param filterChain the filter chain to pass the request and response to next filters
     * @throws ServletException if an error occurs during filtering
     * @throws IOException      if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getSubjectFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );

            authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
