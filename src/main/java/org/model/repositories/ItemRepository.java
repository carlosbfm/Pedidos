package org.model.repositories;

import java.util.List;

import org.model.entities.ItemEntity;

public interface ItemRepository {
    ItemEntity buscarPorId(Long id);
    List<ItemEntity> buscarPorNomeItem(String nome);
    List<ItemEntity> listarTodos();
    List<ItemEntity> listarPaginado(int limit, int offset);
    int contarTotal();
    void salvar(ItemEntity item);
    void atualizar(ItemEntity item);
    void excluir(Long id);
}