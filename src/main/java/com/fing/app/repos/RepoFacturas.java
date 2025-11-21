package com.fing.app.repos;

import org.springframework.data.repository.CrudRepository;

import com.fing.app.models.Factura;
import com.fing.app.models.Producto;

public interface RepoFacturas extends CrudRepository<Producto, Long> {
	
	Factura findByNombre(String n);
	
	<S extends Factura> S save(S p);
}