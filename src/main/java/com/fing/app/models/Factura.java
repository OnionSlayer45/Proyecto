package com.fing.app.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;



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
	
		
	//Lista de productos que estoy facturando
	@OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<EntradaProdFactura> listaProductos = new ArrayList<>();
	
	
	public void addProd(int canti, Producto prod) {
		
		//EntradaProdFactura entradaProdFactura = new EntradaProdFactura(this, canti, prod);
		EntradaProdFactura entradaProdFactura = new EntradaProdFactura();
		
		listaProductos.add(entradaProdFactura);
		
		subTotal+= entradaProdFactura.importe;
		iva = subTotal * 0.16;
		total = iva + subTotal;
		
	}


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


	public List<EntradaProdFactura> getListaProductos() {
		return listaProductos;
	}


	public void setListaProductos(ArrayList<EntradaProdFactura> listaProductos) {
		this.listaProductos = listaProductos;
	}
	
	
	
	
	
	
}
