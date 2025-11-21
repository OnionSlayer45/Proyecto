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
	
	@Column
	private int cantidad;
	
	@OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<EntradaProdFactura> listaProductos = new ArrayList<>();
	
	
	@Column 
	private double total;
	
	
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	


	public Double getTotal() {
        return listaProductos.stream()
                .mapToDouble(EntradaProdFactura::getSubtotal)
                .sum();
    }


	


	public List<EntradaProdFactura> getListaProductos() {
		return listaProductos;
	}


	public void setListaProductos(ArrayList<EntradaProdFactura> listaProductos) {
		this.listaProductos = listaProductos;
	}
	
	
}
