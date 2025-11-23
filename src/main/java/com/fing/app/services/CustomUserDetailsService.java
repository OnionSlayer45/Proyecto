package com.fing.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fing.app.models.clientes;
import com.fing.app.repos.RepoClientes;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private RepoClientes repoClientes;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        clientes cliente = repoClientes.findByUserName(username);
        
        if (cliente == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        return User.builder()
                .username(cliente.getUsername())
                .password(cliente.getPassword())
                .roles("USER")
                .build();
    }
}