package com.secucom.odc.Services;

import com.secucom.odc.models.ERole;
import com.secucom.odc.models.Role;
import com.secucom.odc.models.User;
import com.secucom.odc.repository.RoleRepository;
import com.secucom.odc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddRoleUser implements AddRoleUserImpl{

    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @Autowired
   RoleRepository roleRepository;



   // CETTE METHODE PERMET DE DONNER UN DROIT A UN UTILSATEUR CREER

    @Override
    public void addRole(String username, String roleName) {
        Optional<User> use = userRepository.findByUsername(username);
        Optional<Role> role = roleRepository.findByName(ERole.ROLE_ADMIN);
        use.get().getRoles().add(role.get());
    }

    @Override
    public void ajoutrol(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void ajouterUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
         userRepository.save(user);

    }
}
