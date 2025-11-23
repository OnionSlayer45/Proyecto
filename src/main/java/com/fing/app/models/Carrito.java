package com.fing.app.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "cliente_id")
    private clientes cliente;
    
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<EntradaProdFactura> listaProductos = new ArrayList<>();
    
    public Carrito() {}
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public clientes getCliente() {
        return cliente;
    }

    public void setCliente(clientes cliente) {
        this.cliente = cliente;
    }

    public Double getTotal() {
        return listaProductos.stream()
                .mapToDouble(EntradaProdFactura::getSubtotal)
                .sum();
    }

    public List<EntradaProdFactura> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<EntradaProdFactura> listaProductos) {
        this.listaProductos = listaProductos;
    }
}