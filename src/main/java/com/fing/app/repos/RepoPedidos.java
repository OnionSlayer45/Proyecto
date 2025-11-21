package com.fing.app.repos;

import org.springframework.data.repository.CrudRepository;

import com.fing.app.models.Pedidos;
import com.fing.app.models.Producto;

public interface RepoPedidos extends CrudRepository<Producto, Long> {
	
	Pedidos findByNombre(String n);
	
	<S extends Pedidos> S save(S p);
}