package com.fing.app.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    public String folio;
    
    @Column
    public double total;
    
    @Column
    public double iva;
    
    @Column
    public double subTotal;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private clientes cliente;
    
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<EntradaProdFactura> listaProductos = new ArrayList<>();
    
    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public clientes getCliente() {
        return cliente;
    }

    public void setCliente(clientes cliente) {
        this.cliente = cliente;
    }

    public List<EntradaProdFactura> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<EntradaProdFactura> listaProductos) {
        this.listaProductos = listaProductos;
    }
}