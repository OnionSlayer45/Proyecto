package com.fing.app.repos;

import org.springframework.data.repository.CrudRepository;

import com.fing.app.models.Pedidos;
import com.fing.app.models.Producto;

public interface RepoPedidos extends CrudRepository<Pedidos, Long> {
	
	
	
	
}