package com.ivay.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ivay.dtos.auth.AuthLoginRequestDto;
import com.ivay.dtos.auth.AuthResponseDto;
import com.ivay.entity.Role;
import com.ivay.entity.UserEntity;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.jwt.JwtTokenProvider;
import com.ivay.repository.UserRepository;

/**
 * Service that integrates Spring Security user loading and authentication logic.
 * 
 * This implementation retrieves user details from the database, verifies credentials,
 * and issues JWT tokens upon successful login.
 *
 * @since 1.0.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Converts a {@link Role} entity into a collection of Spring Security authorities.
     *
     * @param role the role assigned to the user
     * @return a collection containing a {@link SimpleGrantedAuthority} prefixed with "ROLE_"
     */
    public Collection<GrantedAuthority> mapToAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
    }

    /**
     * Loads the user identified by the given username.
     *
     * Retrieves the {@link UserEntity} from the repository and maps it to Spring Security's
     * {@link UserDetails}, including account status flags and authorities.
     *
     * @param username the username to look up
     * @return a {@link UserDetails} object for authentication
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByName(username)
            .orElseThrow(() -> new ResourceNotFoundException("Username: " + username + " not found!"));

        return new User(
            userEntity.getName(),
            userEntity.getPassword(),
            userEntity.getIsEnabled(),
            userEntity.getAccountNoExpired(),
            userEntity.getCredentialNoExpired(),
            userEntity.getAccountNoLocked(),
            mapToAuthorities(userEntity.getRole())
        );
    }

    /**
     * Performs credential validation against the stored user details.
     *
     * Compares the raw password to the encoded password from the user details.
     *
     * @param username the username to authenticate
     * @param password the raw password provided by the client
     * @return an {@link Authentication} token upon successful validation
     * @throws BadCredentialsException if the provided password does not match
     */
    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(
            username,
            userDetails.getPassword(),
            userDetails.getAuthorities()
        );
    }

    /**
     * Processes a login request and issues a JWT upon successful authentication.
     *
     * Validates credentials, sets the authentication context,
     * and generates a token with {@link JwtTokenProvider}.
     *
     * @param authLoginRequest the login request containing username and password
     * @return an {@link AuthResponseDto} containing the issued JWT
     */
    public AuthResponseDto login(AuthLoginRequestDto authLoginRequest) {
        Authentication authentication = authenticate(
            authLoginRequest.getUsername(),
            authLoginRequest.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateToken(authentication);
        return new AuthResponseDto(accessToken);
    }
}
