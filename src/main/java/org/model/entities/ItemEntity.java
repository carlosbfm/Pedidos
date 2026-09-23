package org.model.entities;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import org.enums.UnidadeDeMedida;

public class ItemEntity {
	private Integer id;
	private String nomeItem;
	private BigDecimal precoItem;
	private Integer quantidadeDoItem = 0;
	private UnidadeDeMedida tipoUnidadeDeMedida;
	private final LocalDateTime dataHoraEmissao;
	
	public ItemEntity() {
		this.dataHoraEmissao = LocalDateTime.now();
	}

	public ItemEntity(Integer id, String nomeItem, BigDecimal precoItem, UnidadeDeMedida tipoUnidadeDeMedida,Integer quantidade, LocalDateTime dataHora) {
		this.id = id;
		this.nomeItem = nomeItem;
		this.precoItem = precoItem;
		this.tipoUnidadeDeMedida = tipoUnidadeDeMedida;
		this.quantidadeDoItem = (quantidade != null) ? quantidade : 0;
		this.dataHoraEmissao = (dataHora != null) ? dataHora : LocalDateTime.now();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
