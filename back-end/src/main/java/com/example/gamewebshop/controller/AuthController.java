package com.example.gamewebshop.controller;

import com.example.gamewebshop.config.JWTUtil;
import com.example.gamewebshop.dao.UserRepository;
import com.example.gamewebshop.dto.AuthenticationDTO;
import com.example.gamewebshop.dto.LoginResponse;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.services.CredentialValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userDAO;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private CredentialValidator validator;

    public AuthController(UserRepository userDAO, JWTUtil jwtUtil, AuthenticationManager authManager,
                          PasswordEncoder passwordEncoder, CredentialValidator validator) {
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

//    @PostMapping("/register")
//    public ResponseEntity<LoginResponse> register(@RequestBody AuthenticationDTO authenticationDTO) {
//        if (!validator.isValidEmail(authenticationDTO.email)) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "No valid email provided"
//            );
//        }
//
//        if (!validator.isValidPassword(authenticationDTO.password)) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "No valid password provided"
//            );
//        }
//
//        CustomUser customUser = userDAO.findByEmail(authenticationDTO.email);
//
//        if (customUser != null){
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, "Can not register with this email"
//            );
//        }
//        String encodedPassword = passwordEncoder.encode(authenticationDTO.password);
//
//        CustomUser registerdCustomUser = new CustomUser(authenticationDTO.name, authenticationDTO.infix, authenticationDTO.lastName, authenticationDTO.email, encodedPassword);
//        userDAO.save(registerdCustomUser);
//        String token = jwtUtil.generateToken(registerdCustomUser.getEmail());
//        LoginResponse loginResponse = new LoginResponse(registerdCustomUser.getEmail(), token);
//        return ResponseEntity.ok(loginResponse);
//    }



    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody AuthenticationDTO authenticationDTO) {
        if (!validator.isValidEmail(authenticationDTO.email)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No valid email provided"
            );
        }

        if (!validator.isValidPassword(authenticationDTO.password)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No valid password provided"
            );
        }

        CustomUser customUser = userDAO.findByEmail(authenticationDTO.email);

        if (customUser != null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Can not register with this email"
            );
        }
        String encodedPassword = passwordEncoder.encode(authenticationDTO.password);

        CustomUser registeredCustomUser = new CustomUser(authenticationDTO.name, authenticationDTO.infix, authenticationDTO.lastName, authenticationDTO.email, encodedPassword, "user");
        userDAO.save(registeredCustomUser);
        String token = jwtUtil.generateToken(registeredCustomUser.getEmail());
        LoginResponse loginResponse = new LoginResponse(registeredCustomUser.getEmail(), token);
        return ResponseEntity.ok(loginResponse);
    }





    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticationDTO body) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.email, body.password);

            authManager.authenticate(authInputToken);

            CustomUser customUser = userDAO.findByEmail(body.email);

            String token = jwtUtil.generateToken(body.email);
            LoginResponse loginResponse = new LoginResponse(customUser.getEmail(), token);

            return ResponseEntity.ok(loginResponse);

        } catch (AuthenticationException authExc) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No valid credentials"
            );
        }
    }



    @GetMapping("/user")
    public ResponseEntity<CustomUser> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        CustomUser customUser = userDAO.findByEmail(userEmail);
        return ResponseEntity.ok(customUser);
    }


    @PutMapping("/user")
    public ResponseEntity<CustomUser> updateUser(@RequestBody CustomUser updatedUser) {
        CustomUser existingUser = userDAO.findByEmail(updatedUser.getEmail());

        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden");
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setInfix(updatedUser.getInfix());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());


        CustomUser savedUser = userDAO.save(existingUser);
        return ResponseEntity.ok(savedUser);
    }


    @DeleteMapping("/user/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable("email") String email) {
        CustomUser existingUser = userDAO.findByEmail(email);
        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden voor e-mail: " + email);
        }
        userDAO.delete(existingUser);
        return ResponseEntity.noContent().build();
    }

}
