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
	 
	 
	 @Column
	 private String password;
	 
	 
	 @Column
	 private String userName;
	
	
	public clientes() {}
	
	
	public clientes(String nom, String ape, String dir) {
	
		cliente_nombre = nom;
		cliente_apellido=ape;
		direccion=dir;
		
	}


	public Long getCliente_id() {
		return cliente_id;
	}


	public void setCliente_id(Long cliente_id) {
		this.cliente_id = cliente_id;
	}


	public String getCliente_nombre() {
		return cliente_nombre;
	}


	public void setCliente_nombre(String cliente_nombre) {
		this.cliente_nombre = cliente_nombre;
	}


	public String getCliente_apellido() {
		return cliente_apellido;
	}


	public void setCliente_apellido(String cliente_apellido) {
		this.cliente_apellido = cliente_apellido;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public Carrito getCarrito() {
		return carrito;
	}


	public void setCarrito(Carrito carrito) {
		this.carrito = carrito;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
	


