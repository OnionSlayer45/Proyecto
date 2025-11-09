package com.fing.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class EntradaProdFactura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "producto_id") // Foreign key in Employee table
	public Producto producto;
	
	@ManyToOne
	@JoinColumn(name = "factura_id") // Foreign key in Employee table
	public Factura factura;
		
	@Column
	public int canti;
	
	@Column
	public double importe;
	
	
	public EntradaProdFactura() {}
	
	
	public EntradaProdFactura(Factura factura, int c, Producto p) {
		
		this.factura = factura; 
		canti = c;
		producto = p;
		
		importe = canti * producto.getPrecio();
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	//public Producto getProd() {
	//	return prod;
	//}

	//public void setProd(Producto prod) {
	//	this.prod = prod;
	//}

	public int getCanti() {
		return canti;
	}

	public void setCanti(int canti) {
		this.canti = canti;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}
	
	
	
	
}
