package org.model.entities;

import java.util.ArrayList;
import java.util.List;
import org.enums.StatusPedido;

public class PedidosEntity {

    private Integer id;
    private ClienteEntity cliente;
    private final List<ItemEntity> itens = new ArrayList<>();
    private StatusPedido status;

   
    public PedidosEntity() {
        this.status = StatusPedido.CRIADO;
    }

    public PedidosEntity(Integer id, ClienteEntity cliente) {
        this();
        this.id = id;
        this.cliente = cliente;
    }


    public void adicionarItem(ItemEntity item) {
        if (item != null) {
            this.itens.add(item);
        }
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public List<ItemEntity> getItens() {
        return itens;
    }
}