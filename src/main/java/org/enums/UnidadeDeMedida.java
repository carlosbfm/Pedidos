package org.enums;

public enum UnidadeDeMedida {
	UND("UNIDADE"),
	PCT("PACOTE"),
	CX("CAIXA"),
	AMP("ÂMPOLA");
	
    private final String descricao;
    
    UnidadeDeMedida(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

}
