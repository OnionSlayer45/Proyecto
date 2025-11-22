package com.fing.app.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
@Entity
public class Proveedores {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Proveedor_id;
	
	@Column
	private int cantidad;
	
	@Column 
	private String Nombre;
	
	@Column
	private String apellidos;
	
	@Column
	private long numeroDeCelular;
	
	@Column
	private String Correo;
	

}
