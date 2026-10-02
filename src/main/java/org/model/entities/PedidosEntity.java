package org.model.entities;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.enums.StatusPedido;

public class PedidosEntity {

    private Long id;
    private ClienteEntity cliente;
    private final List<ItemEntity> itens = new ArrayList<>();
    private StatusPedido status;
    private Integer quantidadeComprada;
    private PagamentoEntity formaPagamento;
    private LocalDateTime dataHoraEmissao;

    private static final DateTimeFormatter FMT_TIMESTAMP_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public PedidosEntity() {
    }

    public PedidosEntity(ClienteEntity cliente, PagamentoEntity formaPagamento, Integer quantidadeComprada) {
        this.cliente = cliente;
        this.formaPagamento = formaPagamento;
        this.quantidadeComprada = (quantidadeComprada != null && quantidadeComprada > 0) ? quantidadeComprada : 1;
        this.dataHoraEmissao = LocalDateTime.now();
    }

    public PedidosEntity(Long id, ClienteEntity cliente, PagamentoEntity formaPagamento, 
                         StatusPedido status, Integer quantidadeComprada, LocalDateTime dataHoraEmissao) {
        this.id = id;
        this.cliente = cliente;
        this.formaPagamento = formaPagamento;
        this.status = status;
        this.quantidadeComprada = quantidadeComprada;
        this.dataHoraEmissao = dataHoraEmissao;
    }

    public BigDecimal getValorTotalPedido() {
        if (itens.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return itens.stream()
                .map(ItemEntity::getValorTotalEstoque)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getDataHoraEmissaoFormatada() {
        if (this.dataHoraEmissao == null) {
            return "";
        }
        return this.dataHoraEmissao.format(FMT_TIMESTAMP_BR);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public List<ItemEntity> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void adicionarItem(ItemEntity item) {
        if (item != null) {
            this.itens.add(item);
        }
    }

    public void removerItem(Long itemId) {
        this.itens.removeIf(item -> item.getId() != null && item.getId().equals(itemId));
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Integer getQuantidadeComprada() {
        return quantidadeComprada;
    }

    public void setQuantidadeComprada(Integer quantidadeComprada) {
        this.quantidadeComprada = quantidadeComprada;
    }

    public PagamentoEntity getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(PagamentoEntity formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public LocalDateTime getDataHoraEmissao() {
        return dataHoraEmissao;
    }

    public void setDataHoraEmissao(LocalDateTime dataHoraEmissao) {
        this.dataHoraEmissao = dataHoraEmissao;
    }
}