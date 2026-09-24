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
    
    public static UnidadeDeMedida fromString(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        for (UnidadeDeMedida u : values()) {
            if (u.name().equalsIgnoreCase(texto.trim()) || 
                u.descricao.equalsIgnoreCase(texto.trim())) {
                return u;
            }
        }
        throw new IllegalArgumentException("Nenhuma unidade de medida encontrada para o valor: " + texto);
    }

}
