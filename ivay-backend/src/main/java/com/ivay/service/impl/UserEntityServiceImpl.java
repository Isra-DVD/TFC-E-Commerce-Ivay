package com.ivay.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ivay.dtos.userdto.ChangePasswordRequestDto;
import com.ivay.dtos.userdto.UpdateProfileRequestDto;
import com.ivay.dtos.userdto.UserRequestDto;
import com.ivay.dtos.userdto.UserResponseDto;
import com.ivay.entity.Address;
import com.ivay.entity.Role;
import com.ivay.entity.UserEntity;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.mappers.UserMapper;
import com.ivay.repository.UserRepository;
import com.ivay.service.UserEntityService;
import com.ivay.repository.AddressRepository;
import com.ivay.repository.RoleRepository;

@Service
public class UserEntityServiceImpl implements UserEntityService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<UserResponseDto> getAllUsers() {
		return userRepository.findAll().stream().map(userMapper::toUserResponse).collect(Collectors.toList());
	}

	@Override
	public UserResponseDto getUserById(Long id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));
		return userMapper.toUserResponse(user);
	}

	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		
		if (userRepository.findUserEntityByName(userRequestDto.getName()) != null) {
            throw new IllegalArgumentException("Ese nombre de cuenta ya está en uso");
        }
		
		Role role = roleRepository.findById(userRequestDto.getRoleId()).orElseThrow(
				() -> new ResourceNotFoundException("Role with id: " + userRequestDto.getRoleId() + " not found"));

		UserEntity user = userMapper.toUser(userRequestDto);
		user.setRole(role);

		String rawPassword = userRequestDto.getPassword();
		user.setPassword(passwordEncoder.encode(rawPassword));

		UserEntity saved = userRepository.save(user);
		return userMapper.toUserResponse(saved);
	}

	@Override
	public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
		
		UserEntity existingUser = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));
		
		Role role = roleRepository.findById(userRequestDto.getRoleId()).orElseThrow(
				() -> new ResourceNotFoundException("Role with id: " + userRequestDto.getRoleId() + " not found"));

		existingUser.setName(userRequestDto.getName());
		existingUser.setFullName(userRequestDto.getFullName());
		existingUser.setEmail(userRequestDto.getEmail());
		existingUser.setPhone(userRequestDto.getPhone());
		existingUser.setIsEnabled(userRequestDto.getIsEnabled());
		existingUser.setAccountNoExpired(userRequestDto.getAccountNoExpired());
		existingUser.setAccountNoLocked(userRequestDto.getAccountNoLocked());
		existingUser.setCredentialNoExpired(userRequestDto.getCredentialNoExpired());
		existingUser.setRole(role);

		String newRawPassword = userRequestDto.getPassword();
		if (newRawPassword != null && !newRawPassword.isBlank()) {
			existingUser.setPassword(passwordEncoder.encode(newRawPassword));
		}

		UserEntity updated = userRepository.save(existingUser);
		return userMapper.toUserResponse(updated);
	}

	@Override
	public void deleteUser(Long id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));
		userRepository.delete(user);
	}

	@Override
	public UserResponseDto getByUsername(String username) {
		UserEntity currentUser = userRepository.findUserEntityByName(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
		return userMapper.toUserResponse(currentUser);
	}

	@Override
	public UserResponseDto updateProfile(String username, UpdateProfileRequestDto UpdateProfileRequestDto) {
		UserEntity existingUser = userRepository.findUserEntityByName(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
		
		existingUser.setName(UpdateProfileRequestDto.getName());
		existingUser.setFullName(UpdateProfileRequestDto.getFullName());
		existingUser.setEmail(UpdateProfileRequestDto.getEmail());
		existingUser.setPhone(UpdateProfileRequestDto.getPhone());
		UserEntity savedUser = userRepository.save(existingUser);
		return userMapper.toUserResponse(savedUser);
	}

	@Override
	public void changePassword(String username, ChangePasswordRequestDto dto) {
		UserEntity user = userRepository.findUserEntityByName(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

		if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
			throw new BadCredentialsException("Current password is incorrect");
		}

		user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		userRepository.save(user);
	}
}