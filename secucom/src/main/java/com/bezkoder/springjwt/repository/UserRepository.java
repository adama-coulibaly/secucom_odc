package com.bezkoder.springjwt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.User;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  //User findByUsername(String user);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);


  @Query(value = "SELECT * FROM `user_roles` WHERE user_roles.user_id = '1' and user_roles.role_id = '1';",nativeQuery = true)
  String Verifier();

  @Modifying
  @Transactional
  @Query(value = "INSERT INTO USERS (email,password,username) VALUES ('adama@c.com',  'adama123', 'adama');",nativeQuery = true)
  void creationUsers();

  @Modifying
  @Transactional
  @Query(value = "INSERT INTO user_roles (user_id, role_id) VALUES ('1', '1')",nativeQuery = true)
  void AddRoleUser();
}
