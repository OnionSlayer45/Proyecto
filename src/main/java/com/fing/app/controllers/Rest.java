package com.fing.app.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.fing.app.models.Carrito;
import com.fing.app.models.Factura;
import com.fing.app.models.Producto;
import com.fing.app.repos.RepoClientes;
import com.fing.app.repos.RepoFacturas;
import com.fing.app.repos.RepoMetodosDePago;
import com.fing.app.repos.RepoPedidos;
import com.fing.app.repos.RepoProductos;

@RestController
public class Rest {
	
	@Autowired
	RepoProductos repoProductos;
	
	@Autowired
	RepoClientes repoClientes;
	
	@Autowired
	RepoFacturas repoFacturas;
	
	@Autowired
	RepoMetodosDePago repoMetodosDePago;
	
	@Autowired
	RepoPedidos repoPedidos;
	
	
	
	
	
	@GetMapping("/inventario")
	public Factura raiz() {
		
		
		Producto prod1 = repoProductos.findByNombre("Papas");
		
		Producto prod2 = repoProductos.findByNombre("Limonada");
		
		Factura f01 = new Factura();
		
		f01.addProd(2, prod1);
		f01.addProd(2, prod2);
		
		
		return f01;
	}
	
	
	
	@GetMapping("/lista")
	public List<Producto> lista() {
		
		ArrayList<Producto> miList = (ArrayList<Producto>) repoProductos.findAll();
			
		return miList;
	}
	
	
	
	@GetMapping("/addProd")
	public Producto addProd() {
		
		Producto prod1 = new Producto("Papas", 20);
		
		
		Producto prod2 = new Producto("Limonada", 15);
		
		repoProductos.save(prod1);
		repoProductos.save(prod2);
		
		return prod1;
	}
	
	
	@GetMapping("/carrito")
	public List<Producto> carrito() {
		ArrayList<Producto> miList = (ArrayList<Producto>) repoProductos.findAll();
		return miList;
	}
	
	
	@GetMapping("/producto")
	public Producto infoProducto(Producto prueba) {
		Producto pruebas = new Producto("Pizza",20,"Pizza casera rica");
		repoProductos.save(pruebas);
		return pruebas ;
	}
	
	 
	 @GetMapping("/Productos")
	 public Producto nuevo() {
		 Producto pro = new Producto("RX 7600",5200);
		 repoProductos.save(pro);
		 
		 return pro;
		 
	 }
	 
	 
	 
	 @GetMapping("/Carrito")
	 public Carrito carr() {
		 
		 Carrito carrito = new Carrito();
		 return carrito;
	 }
}


