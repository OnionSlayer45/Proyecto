package com.fing.app.repos;

import org.springframework.data.repository.CrudRepository;
import com.fing.app.models.Factura;
import com.fing.app.models.clientes;
import java.util.List;

public interface RepoFacturas extends CrudRepository<Factura, Long> {
    List<Factura> findByCliente(clientes cliente);
}