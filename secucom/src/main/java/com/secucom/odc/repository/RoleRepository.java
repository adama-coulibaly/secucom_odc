package com.secucom.odc.repository;

import java.util.Optional;

import com.secucom.odc.models.ERole;
import com.secucom.odc.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
 // Role findByName(String name);

  @Modifying
  @Transactional
  @Query(value = "INSERT INTO ROLES (name) VALUES ('ROLE_ADMIN'), ('ROLE_USER');",nativeQuery = true)
  void creationRole();


}
