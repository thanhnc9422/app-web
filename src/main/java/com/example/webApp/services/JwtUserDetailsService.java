package com.example.webApp.services;

import java.util.Collections;


import com.example.webApp.util.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import repositories.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    public Crypto crypto;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.webApp.models.User c = userRepository.findOneByUserName(username);
        if (c != null) {
            try {
                return new User(c.getUserName(), new BCryptPasswordEncoder().encode(crypto.decrypt(c.getPassword())),
                        Collections.singleton(new SimpleGrantedAuthority(c.getRole())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}