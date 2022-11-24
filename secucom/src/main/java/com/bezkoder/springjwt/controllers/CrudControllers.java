package com.bezkoder.springjwt.controllers;


import com.bezkoder.springjwt.Services.UsersCrud;
import com.bezkoder.springjwt.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class CrudControllers {

    @Autowired
    private UsersCrud usersCrud;


    // µµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµ

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/afficher")
    public  List<User> AfficherUsers(){
        return usersCrud.Afficher();
    }

    // µµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµ   MODIFIER
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping({"/modifier"})
    public String ModierUser(@RequestBody User users){

        usersCrud.Modifier(users);
        return "Modification reussie avec succès";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/Supprimer/{id_users}")
    public String Supprimer(@PathVariable("id_users") Long id_users){
        usersCrud.Supprimer(id_users);
        return "Suppression reussie";
    }




}
