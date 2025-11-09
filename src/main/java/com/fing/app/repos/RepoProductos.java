package com.fing.app.repos;

import org.springframework.data.repository.CrudRepository;
import com.fing.app.models.Producto;

public interface RepoProductos extends CrudRepository<Producto, Long> {
	
	Producto findByNombre(String n);

}