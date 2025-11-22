package com.fing.app.models;
import jakarta.persistence.*;

@Entity
public class Pedidos {

	
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long pedidoId;
	
@Column
private String fechaDeEnvio;

@Column
private String fechaRecibo;

@Column
private String recibidoPor;


@Column 
private long proveedorId;


public long getPedidoId() {
	return pedidoId;
}


public void setPedidoId(long pedidoId) {
	this.pedidoId = pedidoId;
}


public String getFechaDeEnvio() {
	return fechaDeEnvio;
}


public void setFechaDeEnvio(String fechaDeEnvio) {
	this.fechaDeEnvio = fechaDeEnvio;
}


public String getFechaRecibo() {
	return fechaRecibo;
}


public void setFechaRecibo(String fechaRecibo) {
	this.fechaRecibo = fechaRecibo;
}


public String getRecibidoPor() {
	return recibidoPor;
}


public void setRecibidoPor(String recibidoPor) {
	this.recibidoPor = recibidoPor;
}


public long getProveedorId() {
	return proveedorId;
}


public void setProveedorId(long proveedorId) {
	this.proveedorId = proveedorId;
}





}
