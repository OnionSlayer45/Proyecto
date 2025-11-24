package com.fing.app.repos;

import org.springframework.data.repository.CrudRepository;
import com.fing.app.models.Pedidos;
import com.fing.app.models.clientes;
import java.util.List;

public interface RepoPedidos extends CrudRepository<Pedidos, Long> {
    
    // Encontrar todos los pedidos de un cliente
    List<Pedidos> findByClienteOrderByFechaPedidoDesc(clientes cliente);
    
    // Encontrar pedidos por estado
    List<Pedidos> findByEstado(String estado);
    
    // Encontrar pedidos de un cliente por estado
    List<Pedidos> findByClienteAndEstado(clientes cliente, String estado);
    
    // Encontrar por folio
    Pedidos findByFolio(String folio);
}