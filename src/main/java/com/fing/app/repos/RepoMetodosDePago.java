package com.fing.app.repos;

import org.springframework.data.repository.CrudRepository;

import com.fing.app.models.MetodosDePago;
import com.fing.app.models.Pedidos;
import com.fing.app.models.Producto;

public interface RepoMetodosDePago extends CrudRepository<Producto, Long> {
	
	MetodosDePago findByNombre(String n);
	

}