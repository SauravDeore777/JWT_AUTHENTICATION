package com.restapi.assignment.services;

import com.restapi.assignment.entity.User;
import com.restapi.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository theUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user= theUserRepository.findByEmail(username).orElseThrow(()-> new RuntimeException("User not found"));
        //load user from database

        return user;
    }
}
