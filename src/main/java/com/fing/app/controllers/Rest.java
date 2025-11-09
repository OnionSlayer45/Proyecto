package com.fing.app.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fing.app.models.Factura;
import com.fing.app.models.Producto;
import com.fing.app.repos.RepoProductos;

@RestController
public class Rest {
	
	@Autowired
	RepoProductos repoProductos;
	
	
	@RequestMapping("/inventario")
	public Factura raiz() {
		
		
		Producto prod1 = repoProductos.findByNombre("Papas");
		
		Producto prod2 = repoProductos.findByNombre("Limonada");
		
		Factura f01 = new Factura();
		
		f01.addProd(2, prod1);
		f01.addProd(2, prod2);
		
		
		return f01;
	}
	
	
	
	@RequestMapping("/lista")
	public List<Producto> lista() {
		
		ArrayList<Producto> miList = (ArrayList<Producto>) repoProductos.findAll();
			
		return miList;
	}
	
	
	
	@RequestMapping("/addProd")
	public Producto addProd() {
		
		Producto prod1 = new Producto("Papas", 20);
		
		
		Producto prod2 = new Producto("Limonada", 15);
		
		repoProductos.save(prod1);
		repoProductos.save(prod2);
		
		return prod1;
	}
	
	
	@RequestMapping("/Inicio")
	public Boolean guardar()
	{
		return true;
	}
	
	@RequestMapping("/carrito")
	public List<Producto> carrito() {
		ArrayList<Producto> miList = (ArrayList<Producto>) repoProductos.findAll();
		return miList;
	}
	@RequestMapping("/producto")
	public Producto infoProducto(Producto prueba) {
		Producto pruebas = new Producto("Pizza",20,"Pizza casera rica");
		repoProductos.save(pruebas);
		return pruebas ;
	}
	
	 @GetMapping("/api/data")
	    public String getData() {
	        return "{\"message\": \"Hello from Spring Boot!\"}";
	    }
}


