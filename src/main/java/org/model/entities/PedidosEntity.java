package org.model.entities;

import java.util.ArrayList;
import java.util.List;

import org.interfaces.StatusPedido;

public class PedidosEntity {
	private Integer id;
	private ClienteEntity cliente;
	private final List <ItemEntity> itens = new ArrayList<>();
	private String status;
	
	public PedidosEntity(Integer id, ClienteEntity cliente) {
		this.id = id;
		this.cliente = cliente;
		this.status = StatusPedido.CRIADO;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
