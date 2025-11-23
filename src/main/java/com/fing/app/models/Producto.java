package com.fing.app.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String nombre;
	
	@Column
	private double precio;
	
	@Column
	private String descri = "";
	
	 @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "proveedor_id")
	    private Proveedores proveedor;
	
	
	public Producto() {}
	
	
	public Producto(String nom, double precio) {
	
		nombre = nom;
		this.precio = precio;
	}
	
	public Producto(String nom, double precio, String str) {
		
		nombre = nom;
		this.precio = precio;
		descri=str;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}
	
	
}
