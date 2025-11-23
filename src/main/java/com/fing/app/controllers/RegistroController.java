package com.fing.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.fing.app.models.Carrito;
import com.fing.app.models.clientes;
import com.fing.app.repos.RepoCarrito;
import com.fing.app.repos.RepoClientes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegistroController {

    @Autowired
    private RepoClientes repoClientes;

    @Autowired
    private RepoCarrito repoCarrito;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registro")
    public Map<String, Object> registrarUsuario(@RequestBody clientes nuevoCliente) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar que el usuario no exista
            if (repoClientes.findByUserName(nuevoCliente.getUsername()) != null) {
                response.put("success", false);
                response.put("message", "El usuario ya existe");
                return response;
            }

            // Encriptar contraseña
            nuevoCliente.setPassword(passwordEncoder.encode(nuevoCliente.getPassword()));
            
            // Crear carrito para el cliente
            Carrito carrito = new Carrito();
            carrito.setCliente(nuevoCliente);
            nuevoCliente.setCarrito(carrito);
            
            // Guardar cliente (cascade guardará el carrito)
            repoClientes.save(nuevoCliente);
            
            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al registrar: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}