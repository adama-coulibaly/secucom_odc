package com.bezkoder.springjwt;

import com.bezkoder.springjwt.Services.AddRoleUser;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class SecucomApplication implements  CommandLineRunner{

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddRoleUser addRoleUser;

	public static void main(String[] args) {
		SpringApplication.run(SecucomApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (roleRepository.findAll().size()==0){
			roleRepository.creationRole();
		}
		if(userRepository.findAll().size()==0){
			userRepository.creationUsers();

		}
		if(userRepository.Verifier() == null){
			userRepository.AddRoleUser();
		}

	}

 /*

	@Bean
	CommandLineRunner run(AddRoleUser collService) {
		return args -> {
			//crée des roles des linitialisation de lapp
			collService.ajoutrol(new Role(ERole.USER));
			//collService.ajoutrole(new Role(null,"ADMIN"));

			//crée des roles des linitialisation de lapp

			collService.ajouterUser(new User(null,"fcusername", "fc@gmail.com", "fcpassword", new ArrayList<>()));

			//collService.ajouterUser(new User(null, "coul", "lyd", "lydusername", "lyd@gmail.com", "lydpassword", new ArrayList<>()));

			//attribuer un role a un user
			collService.addRole("fcusername", "ERole.ADMIN");
			//collService.addRole("fcusername", "USER");

		};
	}

 */
}
