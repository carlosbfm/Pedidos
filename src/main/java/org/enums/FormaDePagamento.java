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
	
	public static FormaDePagamento fromString(String statusTexto) {
        if (statusTexto == null || statusTexto.trim().isEmpty()) {
            return null;
        }
        for (FormaDePagamento sp : values()) {
            if (sp.name().equalsIgnoreCase(statusTexto.trim())) {
                return sp;
            }
        }
        throw new IllegalArgumentException("Status desconhecido: " + statusTexto);
    }
	
	
}
