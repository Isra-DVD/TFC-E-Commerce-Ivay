package com.ivay.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ivay.entity.Role;
import com.ivay.entity.UserEntity;
import com.ivay.repository.RoleRepository;
import com.ivay.repository.UserRepository;

// @Configuration
public class LoadDatabase {

	@Autowired
	RoleRepository roleRepository;
	
	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository) {
		return arg -> {
			
			Role roleSuperAdmin = Role.builder()
								 .roleName("SUPERADMIN")
								 .build();
			
			Role roleAdmin = Role.builder()
					 			.roleName("ADMIN")
					 			.build();
			
			Role roleManager = Role.builder()
		 						   .roleName("MANAGER")
		 						   .build();
			
			Role roleClient= Role.builder()
					   			  .roleName("CLIENT")
					   			  .build();
			
			List<Role> savedRoles = roleRepository.saveAll(List.of(roleSuperAdmin, roleAdmin, roleManager, roleClient));
			
			
			UserEntity userAlex = UserEntity.builder()
											.name("Alexis")
											.email("correo1@gmail.com")
											.password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC")
											.phone("111222333")
											.isEnabled(true)
											.accountNoExpired(true)
											.accountNoLocked(true)
											.credentialNoExpired(true)
											.role(savedRoles.get(0))
											.build();
			
			UserEntity userJose = UserEntity.builder()
											.name("Jose")
											.email("correo2@gmail.com")
											.password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC")
											.phone("444555666")
											.isEnabled(true)
											.accountNoExpired(true)
											.accountNoLocked(true)
											.credentialNoExpired(true)
											.role(savedRoles.get(1))
											.build();
			
			UserEntity userDaniel = UserEntity.builder()
											  .name("Daniel")
											  .email("correo3@gmail.com")
											  .password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC")
											  .phone("777888999")
											  .isEnabled(true)
											  .accountNoExpired(true)
											  .accountNoLocked(true)
											  .credentialNoExpired(true)
											  .role(savedRoles.get(2))
											  .build();
			
			UserEntity userAndres = UserEntity.builder()
											  .name("Andres")
											  .email("correo4@gmail.com")
											  .password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC")
											  .phone("123456789")
											  .isEnabled(true)
											  .accountNoExpired(true)
											  .accountNoLocked(true)
											  .credentialNoExpired(true)
											  .role(savedRoles.get(3))
											  .build();
			
			userRepository.saveAll(List.of(userAlex, userJose, userDaniel, userAndres));
		};
	}
}