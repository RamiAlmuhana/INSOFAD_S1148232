package com.example.gamewebshop.services;

import com.example.gamewebshop.dao.UserRepository;
import com.example.gamewebshop.models.CustomUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userDAO;

    public UserService(UserRepository userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomUser customUser = userDAO.findByEmail(email);

        if (customUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Voeg hier een controle op de gebruikersrol toe, bijvoorbeeld:
        if (!customUser.getRole().equals("admin") && !customUser.getRole().equals("user")) {
            throw new IllegalStateException("User has invalid role: " + customUser.getRole());
        }

        return new User(
                email,
                customUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + customUser.getRole())));
    }


}
