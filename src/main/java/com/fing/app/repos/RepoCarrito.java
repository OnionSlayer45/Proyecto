package com.fing.app.repos;

import org.springframework.data.repository.CrudRepository;
import com.fing.app.models.Carrito;

public interface RepoCarrito extends CrudRepository<Carrito, Long> {
}