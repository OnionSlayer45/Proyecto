package com.fing.app.repos;

import org.springframework.data.repository.CrudRepository;

import com.fing.app.models.Pedidos;
import com.fing.app.models.Producto;
import com.fing.app.models.clientes;

public interface RepoClientes extends CrudRepository<Producto, Long> {
	
	clientes findByNombre(String n);
	
	
}