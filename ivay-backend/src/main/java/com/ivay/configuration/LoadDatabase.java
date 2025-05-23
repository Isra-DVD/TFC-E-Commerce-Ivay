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

import javax.sql.DataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@Configuration
public class LoadDatabase {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DataSource dataSource;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {

            // Si ya hay usuarios, no hacer nada
            if (userRepository.count() > 0) {
                System.out.println("🔁 Usuarios ya existentes en la base de datos. Saltando carga inicial.");
                return;
            }

            System.out.println("🚀 Ejecutando carga inicial de datos...");

            // Insertar roles
            Role roleSuperAdmin = Role.builder().roleName("SUPERADMIN").build();
            Role roleAdmin = Role.builder().roleName("ADMIN").build();
            Role roleManager = Role.builder().roleName("MANAGER").build();
            Role roleClient = Role.builder().roleName("CLIENT").build();

            List<Role> savedRoles = roleRepository.saveAll(
                List.of(roleSuperAdmin, roleAdmin, roleManager, roleClient)
            );

            // Insertar usuarios
            UserEntity userAlex = UserEntity.builder()
                .name("Alexis").fullName("Alexis mi Capitán").email("correo1@gmail.com")
                .password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC")
                .phone("111222333").userAddress("address1").isEnabled(true)
                .accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true)
                .role(savedRoles.get(0)).build();

            UserEntity userJose = UserEntity.builder()
                .name("Jose").fullName("Jose El Pajuelo").email("correo2@gmail.com")
                .password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC")
                .phone("444555666").userAddress("address2").isEnabled(true)
                .accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true)
                .role(savedRoles.get(1)).build();

            UserEntity userDaniel = UserEntity.builder()
                .name("Daniel").fullName("Daniel Rovira").email("correo3@gmail.com")
                .password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC")
                .phone("777888999").userAddress("address3").isEnabled(true)
                .accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true)
                .role(savedRoles.get(2)).build();

            UserEntity userAndres = UserEntity.builder()
                .name("Andres").fullName("Andres Teresitas").email("correo4@gmail.com")
                .password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC")
                .phone("123456789").userAddress("address4").isEnabled(true)
                .accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true)
                .role(savedRoles.get(3)).build();

            userRepository.saveAll(List.of(userAlex, userJose, userDaniel, userAndres));

            // Ejecutar script SQL solo si es la primera vez
            Resource sqlScript = new ClassPathResource("sql/data.sql");
            ScriptUtils.executeSqlScript(dataSource.getConnection(), sqlScript);

            System.out.println("✅ Carga inicial completada.");
        };
    }
}
