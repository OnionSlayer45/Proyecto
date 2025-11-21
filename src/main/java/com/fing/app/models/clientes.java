package com.fing.app.models;
import jakarta.persistence.*;


@Entity
public class clientes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cliente_id;
	
	@Column
	private String cliente_nombre;
	
	@Column 
	private String cliente_apellido;
	
	
	@Column
	private String direccion;
	
	 @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
	    private Carrito carrito;
	
	
	public clientes() {}
	
	
	public clientes(String nom, String ape, String dir) {
	
		cliente_nombre = nom;
		cliente_apellido=ape;
		direccion=dir;
		
	}
	

	public Long getId() {
		return cliente_id;
	}

	public void setId(Long id) {
		this.cliente_id = id;
	}

	public String getNombre() {
		return cliente_nombre;
	}

	public void setNombre(String nombre) {
		this.cliente_nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setPrecio(String dire) {
		this.direccion= dire;
	}

	
	
}
