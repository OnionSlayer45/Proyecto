package com.fing.app.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ProcesarFacturaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Escuchamos el canal "direct:facturacion"
        from("direct:facturacion")
            .routeId("ProcesoFacturacion")
            
            // Log inicial
            .log("CAMEL: Recibida solicitud de facturación -> ${body}")
            
            // Simulamos generación de PDF y Timbrado SAT
            .process(exchange -> {
                System.out.println("Generando XML :)");
                Thread.sleep(500); 
                System.out.println("Creando PDF");
                Thread.sleep(500);
            })
            
            // Log final
            .log("Factura generada y enviada por correo exitosamente");
    }
}
