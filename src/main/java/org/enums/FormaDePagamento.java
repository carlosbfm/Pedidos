package org.enums;

public enum FormaDePagamento {
	PIX("PIX"),
	DEBITO("CARTÃO DE DÉBITO"),
	CREDITO("CARTÃO DE CRÉDITO"),
	DINHEIRO("DINHEIRO");
	
	private String descricao;
	
	
	FormaDePagamento(String descricao ) {
		this.descricao = descricao;
	}


	public String getDesccricao() {
		return descricao;
	}
	
	@Override
	public String toString(){
		return descricao;
	}
	
	
	
	
}
