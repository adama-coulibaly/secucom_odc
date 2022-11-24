package com.bezkoder.springjwt.Services;

import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import org.springframework.stereotype.Service;

@Service
public interface AddRoleUserImpl {

    void addRole(String username,String roleName);
    void ajoutrol(Role role);
    void ajouterUser(User user);
}
