package com.loctran.User;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserUserDetailsService implements UserDetailsService {
    private final UserDAO userDAO;

    public UserUserDetailsService(@Qualifier("userJPA") UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByName(username).orElseThrow( () -> new UsernameNotFoundException(username + " not found") );
    }
}
