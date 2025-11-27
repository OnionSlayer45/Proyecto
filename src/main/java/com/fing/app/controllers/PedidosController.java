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
        // ... (Tu código original de historial) ...
        // Para simplificar, dejo este método resumido, pero tú usa tu código original
        return new HashMap<>(); 
    }
    
    // (Asegúrate de mantener tu método generarFolio al final)
    private String generarFolio() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "PED-" + now.format(formatter);
    }
}