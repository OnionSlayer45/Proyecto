package com.fing.app.controllers;

// camel
import org.apache.camel.ProducerTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.fing.app.models.Carrito;
import com.fing.app.models.DetallePedido;
import com.fing.app.models.EntradaProdFactura;
import com.fing.app.models.Pedidos;
import com.fing.app.models.clientes;
import com.fing.app.repos.RepoCarrito;
import com.fing.app.repos.RepoClientes;
import com.fing.app.repos.RepoPedidos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidosController {

    @Autowired
    private RepoPedidos repoPedidos;

    @Autowired
    private RepoClientes repoClientes;

    @Autowired
    private RepoCarrito repoCarrito;

    // 2. INYECTAR EL DISPARADOR DE CAMEL (NUEVO)
    @Autowired
    private ProducerTemplate producerTemplate;

    @PostMapping("/crear")
    public Map<String, Object> crearPedido(@RequestBody Map<String, String> request, Authentication auth) {
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

            // Crear pedido
            Pedidos pedido = new Pedidos();
            pedido.setFolio(generarFolio());
            pedido.setCliente(cliente);
            pedido.setDireccionEntrega(request.getOrDefault("direccion", cliente.getDireccion()));
            pedido.setNotasAdicionales(request.getOrDefault("notas", ""));
            
            double subtotal = 0;
            for (EntradaProdFactura item : carrito.getListaProductos()) {
                DetallePedido detalle = new DetallePedido(
                    pedido,
                    item.producto,
                    item.getCanti()
                );
                pedido.getDetalles().add(detalle);
                subtotal += detalle.getImporte();
            }

            pedido.setSubtotal(subtotal);
            pedido.setIva(subtotal * 0.16);
            pedido.setTotal(pedido.getSubtotal() + pedido.getIva());
            pedido.setEstado("PENDIENTE");

            // Guardar pedido
            repoPedidos.save(pedido);

            // 3. ¡AQUÍ DISPARAMOS A CAMEL! (NUEVO)
            // Usamos el mismo canal "direct:facturacion" que escucha tu ruta ProcesarFacturaRoute
            String mensaje = "Pedido Creado - Folio: " + pedido.getFolio() + " - Cliente: " + username;
            producerTemplate.sendBody("direct:facturacion", mensaje);

            // Limpiar carrito
            carrito.getListaProductos().clear();
            repoCarrito.save(carrito);

            response.put("success", true);
            response.put("message", "Pedido creado exitosamente");
            response.put("folio", pedido.getFolio());
            response.put("total", pedido.getTotal());
            response.put("pedidoId", pedido.getPedidoId());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear el pedido: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    // ... (El resto de métodos @GetMapping y @PutMapping se quedan igual, no los borres) ...
    // Para no hacer el archivo gigante aquí, solo asegúrate de no borrar el resto de tu clase original.
    
    // Si copias y pegas todo este archivo, asegúrate de incluir los métodos obtenerHistorial, obtenerDetallePedido, cancelarPedido y generarFolio que ya tenías.
    // Solo agregué las partes marcadas como (NUEVO).

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

            List<Pedidos> pedidos = repoPedidos.findByClienteOrderByFechaPedidoDesc(cliente);
            
            // Convertir a formato más amigable para el frontend
            List<Map<String, Object>> pedidosDTO = pedidos.stream().map(p -> {
                Map<String, Object> pedidoMap = new HashMap<>();
                pedidoMap.put("id", p.getPedidoId());
                pedidoMap.put("folio", p.getFolio());
                pedidoMap.put("fecha", p.getFechaPedido().toString());
                pedidoMap.put("estado", p.getEstado());
                pedidoMap.put("total", p.getTotal());
                pedidoMap.put("subtotal", p.getSubtotal());
                pedidoMap.put("iva", p.getIva());
                pedidoMap.put("direccion", p.getDireccionEntrega());
                pedidoMap.put("cantidadProductos", p.getDetalles().size());
                return pedidoMap;
            }).collect(Collectors.toList());

            response.put("success", true);
            response.put("pedidos", pedidosDTO);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping("/detalle/{pedidoId}")
    public Map<String, Object> obtenerDetallePedido(@PathVariable Long pedidoId, Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = auth.getName();
            clientes cliente = repoClientes.findByUserName(username);
            
            Pedidos pedido = repoPedidos.findById(pedidoId).orElse(null);
            
            if (pedido == null) {
                response.put("success", false);
                response.put("message", "Pedido no encontrado");
                return response;
            }

            // Verificar que el pedido pertenece al cliente
            if (!pedido.getCliente().getCliente_id().equals(cliente.getCliente_id())) {
                response.put("success", false);
                response.put("message", "No tienes permiso para ver este pedido");
                return response;
            }

            Map<String, Object> pedidoDetalle = new HashMap<>();
            pedidoDetalle.put("folio", pedido.getFolio());
            pedidoDetalle.put("fecha", pedido.getFechaPedido().toString());
            pedidoDetalle.put("estado", pedido.getEstado());
            pedidoDetalle.put("subtotal", pedido.getSubtotal());
            pedidoDetalle.put("iva", pedido.getIva());
            pedidoDetalle.put("total", pedido.getTotal());
            pedidoDetalle.put("direccion", pedido.getDireccionEntrega());
            pedidoDetalle.put("notas", pedido.getNotasAdicionales());
            
            // Detalles de productos
            List<Map<String, Object>> productos = pedido.getDetalles().stream().map(d -> {
                Map<String, Object> prod = new HashMap<>();
                prod.put("nombre", d.getProducto().getNombre());
                prod.put("cantidad", d.getCantidad());
                prod.put("precioUnitario", d.getPrecioUnitario());
                prod.put("importe", d.getImporte());
                return prod;
            }).collect(Collectors.toList());
            
            pedidoDetalle.put("productos", productos);

            response.put("success", true);
            response.put("pedido", pedidoDetalle);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @PutMapping("/cancelar/{pedidoId}")
    public Map<String, Object> cancelarPedido(@PathVariable Long pedidoId, Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = auth.getName();
            clientes cliente = repoClientes.findByUserName(username);
            
            Pedidos pedido = repoPedidos.findById(pedidoId).orElse(null);
            
            if (pedido == null) {
                response.put("success", false);
                response.put("message", "Pedido no encontrado");
                return response;
            }

            if (!pedido.getCliente().getCliente_id().equals(cliente.getCliente_id())) {
                response.put("success", false);
                response.put("message", "No tienes permiso para cancelar este pedido");
                return response;
            }

            if (pedido.getEstado().equals("CANCELADO")) {
                response.put("success", false);
                response.put("message", "El pedido ya está cancelado");
                return response;
            }

            if (pedido.getEstado().equals("ENTREGADO")) {
                response.put("success", false);
                response.put("message", "No se puede cancelar un pedido entregado");
                return response;
            }

            pedido.setEstado("CANCELADO");
            repoPedidos.save(pedido);

            response.put("success", true);
            response.put("message", "Pedido cancelado exitosamente");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }
        return new HashMap<>(); 
    }
    
   
    private String generarFolio() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "PED-" + now.format(formatter);
    }
}