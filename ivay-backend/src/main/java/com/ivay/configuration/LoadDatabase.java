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

/**
 * Database initializer that seeds default roles and example users.
 *
 * On application startup, this component creates four roles:
 * SUPERADMIN, ADMIN, MANAGER, and CLIENT,
 * and four corresponding user accounts for development and testing purposes.
 *
 * @since 1.0.0
 */

// @Configuration 	// Uncomment to enable database initialization
public class LoadDatabase {

	/**
	 * Repository for performing CRUD operations on {@link Role} entities.
	 */
	@Autowired
	RoleRepository roleRepository;


	/**
	 * Defines and returns a {@link CommandLineRunner} bean that will run
	 * on application startup to seed the database.
	 *
	 * This runner performs the following steps:
	 * - Creates and saves four default roles.
	 * - Creates and saves four example users, each assigned one of the roles.
	 *
	 * @param userRepository the repository for performing CRUD operations on {@link UserEntity} instances
	 * @return a {@code CommandLineRunner} that initializes the database at startup
	 */
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
					.fullName("Alexis mi Capitán")
					.email("correo1@gmail.com")
					.password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC")
					.phone("111222333")
					.userAddress("address1")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.role(savedRoles.get(0))
					.build();

			UserEntity userJose = UserEntity.builder()
					.name("Jose")
					.fullName("Jose El Pajuelo")
					.email("correo2@gmail.com")
					.password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC")
					.phone("444555666")
					.userAddress("address2")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.role(savedRoles.get(1))
					.build();

			UserEntity userDaniel = UserEntity.builder()
					.name("Daniel")
					.fullName("Daniel Rovira")
					.email("correo3@gmail.com")
					.password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC")
					.phone("777888999")
					.userAddress("address3")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.role(savedRoles.get(2))
					.build();

			UserEntity userAndres = UserEntity.builder()
					.name("Andres")
					.fullName("Andres Teresitas")
					.email("correo4@gmail.com")
					.password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC")
					.phone("123456789")
					.userAddress("address4")
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