package com.fing.app.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
@Entity
public class Proveedores {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Proveedor_id;
	
	 @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    private List<Producto> productos;
	
	@Column 
	private String Nombre;
	
	@Column
	private String apellidos;
	
	@Column
	private long numeroDeCelular;
	
	@Column
	private String Correo;

	public Long getProveedor_id() {
		return Proveedor_id;
	}

	public void setProveedor_id(Long proveedor_id) {
		Proveedor_id = proveedor_id;
	}

	

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public long getNumeroDeCelular() {
		return numeroDeCelular;
	}

	public void setNumeroDeCelular(long numeroDeCelular) {
		this.numeroDeCelular = numeroDeCelular;
	}

	public String getCorreo() {
		return Correo;
	}

	public void setCorreo(String correo) {
		Correo = correo;
	}
	

}
