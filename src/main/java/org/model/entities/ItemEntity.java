package org.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.enums.UnidadeDeMedida;

public class ItemEntity {

    private Long id;
    private String nomeItem;
    private BigDecimal precoItem;
    private Integer quantidadeDoItem = 0;
    private UnidadeDeMedida tipoUnidadeDeMedida;
    private LocalDateTime dataHoraEmissao;

    private static final DateTimeFormatter FMT_TIMESTAMP_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public ItemEntity() {
    }

   
    public ItemEntity(String nomeItem, BigDecimal precoItem, UnidadeDeMedida tipoUnidadeDeMedida, Integer quantidade) {
        this.nomeItem = nomeItem;
        this.precoItem = precoItem;
        this.tipoUnidadeDeMedida = tipoUnidadeDeMedida;
        this.quantidadeDoItem = (quantidade != null && quantidade >= 0) ? quantidade : 0;
        this.dataHoraEmissao = LocalDateTime.now();
    }

    public ItemEntity(Long id, String nomeItem, BigDecimal precoItem, Integer quantidade, 
                      UnidadeDeMedida tipoUnidadeDeMedida, LocalDateTime dataHoraEmissao) {
        this.id = id;
        this.nomeItem = nomeItem;
        this.precoItem = precoItem;
        this.quantidadeDoItem = (quantidade != null) ? quantidade : 0;
        this.tipoUnidadeDeMedida = tipoUnidadeDeMedida;
        this.dataHoraEmissao = dataHoraEmissao;
    }

    public String getDataCadastroFormatada() {
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

    public String getNomeItem() {
        return nomeItem;
    }

    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }

    public BigDecimal getPrecoItem() {
        return precoItem;
    }

    public void setPrecoItem(BigDecimal precoItem) {
        this.precoItem = precoItem;
    }

    public Integer getQuantidadeDoItem() {
        return quantidadeDoItem;
    }

    public void addQuantidade(int quantidadeAdicionada) {
        if (quantidadeAdicionada <= 0) {
            throw new IllegalArgumentException("Quantidade adicionada deve ser maior que zero.");
        }
        this.quantidadeDoItem += quantidadeAdicionada;
    }

    public void removeQuantidade(int quantidadeRemovida) {
        if (quantidadeRemovida <= 0) {
            throw new IllegalArgumentException("Quantidade removida deve ser maior que zero.");
        }
        if (this.quantidadeDoItem < quantidadeRemovida) {
            throw new IllegalArgumentException("Saldo insuficiente. Estoque atual: " + this.quantidadeDoItem);
        }
        this.quantidadeDoItem -= quantidadeRemovida;
    }
    
    public BigDecimal getValorTotalEstoque() {
        if (this.precoItem == null || this.quantidadeDoItem == null) {
            return BigDecimal.ZERO;
        }
        return this.precoItem.multiply(BigDecimal.valueOf(this.quantidadeDoItem));
    }

    public UnidadeDeMedida getTipoUnidadeDeMedida() {
        return tipoUnidadeDeMedida;
    }

    public void setTipoUnidadeDeMedida(UnidadeDeMedida tipoUnidadeDeMedida) {
        this.tipoUnidadeDeMedida = tipoUnidadeDeMedida;
    }

    public LocalDateTime getDataHoraEmissao() {
        return dataHoraEmissao;
    }

    public void setDataHoraEmissao(LocalDateTime dataHoraEmissao) {
        this.dataHoraEmissao = dataHoraEmissao;
    }
}