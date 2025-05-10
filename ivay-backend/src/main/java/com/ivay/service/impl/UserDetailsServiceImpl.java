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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	public Collection<GrantedAuthority> mapToAuthorities(Role role) {
	    return List.of(
	        new SimpleGrantedAuthority("ROLE_" + role.getRoleName())
	    );
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("username-->" + username);
		
		UserEntity userEntity = userRepository.findUserEntityByName(username)
											  .orElseThrow(() -> new ResourceNotFoundException("Username: " + username + " not found!"));
		
		
		return new User(userEntity.getName(),
						userEntity.getPassword(),
						userEntity.getIsEnabled(),
						userEntity.getAccountNoExpired(),
						userEntity.getCredentialNoExpired(),
						userEntity.getAccountNoLocked(),
				mapToAuthorities(userEntity.getRole())
				);
	}

	private Authentication authenticate(String username, String password) {
		System.out.println("authenticate -->" + username);
		UserDetails userDetails = this.loadUserByUsername(username);
		
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid username or password");
		}
		
		return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
	}
	
	public AuthResponseDto login(AuthLoginRequestDto authLoginRequest) {
		
		System.out.println("Auth--> " + authLoginRequest.getUsername());
		
		Authentication authentication = this.authenticate(authLoginRequest.getUsername(), authLoginRequest.getPassword());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String accessToken = jwtTokenProvider.generateToken(authentication);
		
		return new AuthResponseDto(accessToken);
	}
	
}