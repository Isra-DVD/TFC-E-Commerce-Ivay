package com.ivay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ivay.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
