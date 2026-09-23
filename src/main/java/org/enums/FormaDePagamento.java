package org.enums;

public enum FormaDePagamento {
	PIX("PIX"),
	DEBITO("CARTÃO DE DÉBITO"),
	CREDITO("CARTÃO DE CRÉDITO"),
	DINHEIRO("DINHEIRO");
	
	private String desccricao;
	
	
	FormaDePagamento(String descricao ) {
		this.desccricao = descricao;
	}


	public String getDesccricao() {
		return desccricao;
	}
	
	@Override
	public String toString(){
		return desccricao;
	}
	
	
}
