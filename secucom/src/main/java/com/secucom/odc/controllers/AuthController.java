package com.secucom.odc.controllers;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.secucom.odc.models.ERole;
import com.secucom.odc.models.Role;
import com.secucom.odc.models.User;
import com.secucom.odc.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secucom.odc.payload.request.LoginRequest;
import com.secucom.odc.payload.request.SignupRequest;
import com.secucom.odc.payload.response.MessageResponse;
import com.secucom.odc.repository.UserRepository;
import com.secucom.odc.security.jwt.JwtUtils;
import com.secucom.odc.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
//@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  private OAuth2AuthorizedClientService authorizedClientService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;





  @PostMapping("/signin")
  public String authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
   String jwt = jwtUtils.generateJwtToken(authentication);

// ICI ON RECUPERE LE ROLE DES UTILISATEURS

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());



    return "Connection reussie. \n "+"Nom utilisateur: "+userDetails.getUsername()+"\n Email: "+userDetails.getEmail()+
    "\n Roles: "+roles+"\n Token: "+jwt;
           /* ResponseEntity.ok(new JwtResponse(jwt,
                         userDetails.getId(),
                         userDetails.getUsername(),
                         userDetails.getEmail(),
                         roles));
            */


  }


  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Erreur: Utilisateur existe !"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Erreur: Email existe déjas!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Erreur: Role n'est pas touver."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Erreur: Role n'est pas touver."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Erreur: Role n'est pas touver."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Erreur: Role n'est pas touver."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("Collaborateur ajouter avec succes!"));
  }


  @RequestMapping("/**")
  private StringBuffer getOauth2LoginInfo(Principal user){   // ICI NOUS ALLONS RECUPERER LES INFOS DE USER

    StringBuffer protectedInfo = new StringBuffer();

System.out.println(user);
try {
  OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);   // ON FAIT LE CASTING ICI USER => EN OAUTH-USER
  OAuth2AuthorizedClient authClient =
          this.authorizedClientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());
  if(authToken.isAuthenticated()){

    Map<String,Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();

    String userToken = authClient.getAccessToken().getTokenValue();
    protectedInfo.append("Bienvenue, " + userAttributes.get("name")+"<br><br>");
    protectedInfo.append("e-mail: " + userAttributes.get("email")+"<br><br>");
    protectedInfo.append("Access Token: " + userToken+"<br><br>");
  }
  else{
    protectedInfo.append("NA");
  }
  return protectedInfo;

}catch (Exception e){
 // LoginRequest loginRequests = n;
  // Authentication authentication = authenticationManager.authenticate(
  //        new UsernamePasswordAuthenticationToken(loginRequests.getUsername(), loginRequests.getPassword()));

  // SecurityContextHolder.getContext().setAuthentication(authentication);
  // String jwt = jwtUtils.generateJwtToken(authentication);

  protectedInfo.append("Bienvenue, "+user.getName());
  protectedInfo.append("Bienvenue, ");


  return protectedInfo;
}


  }




}
