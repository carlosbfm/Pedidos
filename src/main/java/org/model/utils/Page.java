package org.model.utils;

import java.util.Collections;
import java.util.List;

public class Page<T> {
    private final List<T> registros;
    private final int paginaAtual;
    private final int registrosPorPagina;
    private final int totalRegistros;
    private final int totalPaginas;

    public Page(List<T> registros, int paginaAtual, int registrosPorPagina, int totalRegistros) {
        this.registros = (registros != null) ? registros : Collections.emptyList();
        this.paginaAtual = Math.max(paginaAtual, 1);
        this.registrosPorPagina = registrosPorPagina;
        this.totalRegistros = totalRegistros;
        this.totalPaginas = (registrosPorPagina > 0 && totalRegistros > 0)
                ? (int) Math.ceil((double) totalRegistros / registrosPorPagina)
                : 1;
    }

    public List<T> getRegistros() { return registros; }
    public int getPaginaAtual() { return paginaAtual; }
    public int getRegistrosPorPagina() { return registrosPorPagina; }
    public int getTotalRegistros() { return totalRegistros; }
    public int getTotalPaginas() { return totalPaginas; }
}