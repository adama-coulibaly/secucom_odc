package com.secucom.odc.Services;

import com.secucom.odc.models.Role;
import com.secucom.odc.models.User;
import org.springframework.stereotype.Service;

@Service
public interface AddRoleUserImpl {

    void addRole(String username,String roleName);
    void ajoutrol(Role role);
    void ajouterUser(User user);
}
