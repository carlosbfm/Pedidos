package org.model.entities;

import java.math.BigDecimal;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import org.enums.FormaDePagamento;


public class PagamentoEntity {

    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private FormaDePagamento formaDePagamento;

    public PagamentoEntity() {
    }

    public PagamentoEntity(BigDecimal valor, FormaDePagamento formaDePagamento) {
        this.valor = valor;
        this.formaDePagamento = formaDePagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public FormaDePagamento getFormaDePagamento() {
        return formaDePagamento;
    }

    public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
        this.formaDePagamento = formaDePagamento;
    }
}