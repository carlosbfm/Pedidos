package org.model.entities;

import java.math.BigDecimal;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

import org.enums.UnidadeDeMedida;

public class ItemEntity {
	private Long id;
	private String nomeItem;
	private BigDecimal precoItem;
	private Integer quantidadeDoItem = 0;
	private UnidadeDeMedida tipoUnidadeDeMedida;
	private final LocalDateTime dataHoraEmissao;
	
	private static final AtomicLong GERADOR_ID = new AtomicLong(0);
	private static final DateTimeFormatter FMT_TIMESTAMP_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	public ItemEntity() {
		this.id = GERADOR_ID.incrementAndGet();
		this.dataHoraEmissao = LocalDateTime.now();
	}

	public ItemEntity( String nomeItem, BigDecimal precoItem, UnidadeDeMedida tipoUnidadeDeMedida,Integer quantidade) {
		this();
		this.nomeItem = nomeItem;
		this.precoItem = precoItem;
		this.quantidadeDoItem = (quantidade != null) ? quantidade : 0;
		this.tipoUnidadeDeMedida = tipoUnidadeDeMedida;
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
		if( quantidadeAdicionada <= 0 ) {
			throw new IllegalArgumentException("Quantidade adicionado não ser menor ou igual a zero");
		}
		quantidadeDoItem += quantidadeAdicionada;
	}

	public void removeQuantidade(int quantidadeRemovida) {
		if( quantidadeRemovida <= 0 ) {
			throw new IllegalArgumentException("Quantidade removida deve ser maior que zero");
		}

		if(quantidadeDoItem < quantidadeRemovida) {
			throw new  IllegalArgumentException("Saldo Insufuciente. Estoque Atual: " + quantidadeDoItem );
		}
		quantidadeDoItem -= quantidadeRemovida;
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

}
