package com.fing.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.fing.app.models.Carrito;
import com.fing.app.models.EntradaProdFactura;
import com.fing.app.models.Factura;
import com.fing.app.models.clientes;
import com.fing.app.repos.RepoCarrito;
import com.fing.app.repos.RepoClientes;
import com.fing.app.repos.RepoFacturas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/factura")
public class FacturaController {

    @Autowired
    private RepoFacturas repoFacturas;

    @Autowired
    private RepoClientes repoClientes;

    @Autowired
    private RepoCarrito repoCarrito;

    @PostMapping("/procesar")
    public Map<String, Object> procesarCompra(Authentication auth) {
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
            
            if (carrito.getListaProductos().isEmpty()) {
                response.put("success", false);
                response.put("message", "El carrito está vacío");
                return response;
            }

            // Crear factura
            Factura factura = new Factura();
            factura.setFolio(generarFolio());
            factura.setCliente(cliente);
            
            // Calcular totales
            double subtotal = 0;
            for (EntradaProdFactura item : carrito.getListaProductos()) {
                // Crear nueva entrada para la factura
                EntradaProdFactura nuevaEntrada = new EntradaProdFactura();
                nuevaEntrada.setFactura(factura);
                nuevaEntrada.producto = item.producto;
                nuevaEntrada.setCanti(item.getCanti());
                nuevaEntrada.setImporte(item.getImporte());
                
                factura.getListaProductos().add(nuevaEntrada);
                subtotal += item.getImporte();
            }

            factura.setSubTotal(subtotal);
            factura.setIva(subtotal * 0.16);
            factura.setTotal(factura.getSubTotal() + factura.getIva());

            // Guardar factura
            repoFacturas.save(factura);

            // Limpiar carrito
            carrito.getListaProductos().clear();
            repoCarrito.save(carrito);

            response.put("success", true);
            response.put("message", "Compra procesada exitosamente");
            response.put("folio", factura.getFolio());
            response.put("total", factura.getTotal());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al procesar la compra: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping("/historial")
    public Map<String, Object> obtenerHistorial(Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = auth.getName();
            clientes cliente = repoClientes.findByUserName(username);
            
            if (cliente == null) {
                response.put("success", false);
                response.put("message", "Cliente no encontrado");
                return response;
            }

            response.put("success", true);
            response.put("facturas", repoFacturas.findByCliente(cliente));
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }

    private String generarFolio() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "FAC-" + now.format(formatter);
    }
}