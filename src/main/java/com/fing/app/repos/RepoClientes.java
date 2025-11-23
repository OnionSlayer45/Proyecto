package com.fing.app.repos;

import org.springframework.data.repository.CrudRepository;
import com.fing.app.models.clientes;

public interface RepoClientes extends CrudRepository<clientes, Long> {
    clientes findByUserName(String username);
}