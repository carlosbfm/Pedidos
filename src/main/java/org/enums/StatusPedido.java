package org.enums;

public enum StatusPedido {
    CRIADO("Criado"),
    PENDENTE("Preparando o pedido"),
    CANCELADO("Cancelado"),
    CONCLUIDO("Concluído");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public static StatusPedido fromString(String statusTexto) {
        if (statusTexto == null || statusTexto.trim().isEmpty()) {
            return null;
        }
        for (StatusPedido sp : values()) {
            if (sp.name().equalsIgnoreCase(statusTexto.trim())) {
                return sp;
            }
        }
        throw new IllegalArgumentException("Status desconhecido: " + statusTexto);
    }
    
    public boolean podeTransitarPara(StatusPedido novoStatus) {
        if (novoStatus == null) {
            return false;
        }
        
        switch (this) {
            case CRIADO: 
                return novoStatus == PENDENTE || novoStatus == CANCELADO;
            case PENDENTE: 
                return novoStatus == CONCLUIDO || novoStatus == CANCELADO;
            case CONCLUIDO: 
            case CANCELADO: 
                return false; 
            default: 
                return false;
        }
    }
}
