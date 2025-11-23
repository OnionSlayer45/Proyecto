package com.fing.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.fing.app.models.Carrito;
import com.fing.app.models.EntradaProdFactura;
import com.fing.app.models.Producto;
import com.fing.app.models.clientes;
import com.fing.app.repos.RepoCarrito;
import com.fing.app.repos.RepoClientes;
import com.fing.app.repos.RepoProductos;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private RepoClientes repoClientes;

    @Autowired
    private RepoCarrito repoCarrito;

    @Autowired
    private RepoProductos repoProductos;

    @GetMapping("/obtener")
    public Map<String, Object> obtenerCarrito(Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = auth.getName();
            clientes cliente = repoClientes.findByUserName(username);
            
            if (cliente == null || cliente.getCarrito() == null) {
                response.put("success", false);
                response.put("message", "Carrito no encontrado");
                return response;
            }

            response.put("success", true);
            response.put("items", cliente.getCarrito().getListaProductos());
            response.put("total", cliente.getCarrito().getTotal());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }

    @PostMapping("/agregar")
    public Map<String, Object> agregarProducto(@RequestBody Map<String, Long> request, Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = auth.getName();
            clientes cliente = repoClientes.findByUserName(username);
            Long productoId = request.get("productoId");
            
            if (cliente == null) {
                response.put("success", false);
                response.put("message", "Cliente no encontrado");
                return response;
            }

            Producto producto = repoProductos.findById(productoId).orElse(null);
            if (producto == null) {
                response.put("success", false);
                response.put("message", "Producto no encontrado");
                return response;
            }

            Carrito carrito = cliente.getCarrito();
            if (carrito == null) {
                carrito = new Carrito();
                carrito.setCliente(cliente);
                cliente.setCarrito(carrito);
            }

            // Buscar si el producto ya está en el carrito
            EntradaProdFactura entrada = carrito.getListaProductos().stream()
                .filter(e -> e.producto != null && e.producto.getId().equals(productoId))
                .findFirst()
                .orElse(null);

            if (entrada != null) {
                entrada.canti = entrada.canti + 1;
                entrada.importe = entrada.canti * producto.getPrecio();
            } else {
                entrada = new EntradaProdFactura();
                entrada.carrito = carrito;
                entrada.producto = producto;
                entrada.canti = 1;
                entrada.importe = producto.getPrecio();
                carrito.getListaProductos().add(entrada);
            }

            repoCarrito.save(carrito);

            response.put("success", true);
            response.put("message", "Producto agregado");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @PutMapping("/actualizar")
    public Map<String, Object> actualizarCantidad(@RequestBody Map<String, Object> request, Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = auth.getName();
            clientes cliente = repoClientes.findByUserName(username);
            Long productoId = Long.valueOf(request.get("productoId").toString());
            Integer cantidad = Integer.valueOf(request.get("cantidad").toString());
            
            if (cliente == null || cliente.getCarrito() == null) {
                response.put("success", false);
                response.put("message", "Carrito no encontrado");
                return response;
            }

            Carrito carrito = cliente.getCarrito();
            EntradaProdFactura entrada = carrito.getListaProductos().stream()
                .filter(e -> e.producto != null && e.producto.getId().equals(productoId))
                .findFirst()
                .orElse(null);

            if (entrada != null) {
                entrada.canti = cantidad;
                entrada.importe = cantidad * entrada.producto.getPrecio();
                repoCarrito.save(carrito);
                response.put("success", true);
            } else {
                response.put("success", false);
                response.put("message", "Producto no encontrado en carrito");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }

    @DeleteMapping("/eliminar/{productoId}")
    public Map<String, Object> eliminarProducto(@PathVariable Long productoId, Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = auth.getName();
            clientes cliente = repoClientes.findByUserName(username);
            
            if (cliente == null || cliente.getCarrito() == null) {
                response.put("success", false);
                response.put("message", "Carrito no encontrado");
                return response;
            }

            Carrito carrito = cliente.getCarrito();
            carrito.getListaProductos().removeIf(e -> e.producto != null && e.producto.getId().equals(productoId));
            repoCarrito.save(carrito);

            response.put("success", true);
            response.put("message", "Producto eliminado");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }
}