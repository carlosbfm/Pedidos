package org.model.repositories;

import java.util.List;

import org.model.entities.ItemEntity;

public interface ItemRepository {
    ItemEntity buscarPorId(Integer id);
    List<ItemEntity> buscarPorNomeItem(String nome);
    void salvar(ItemEntity item);
    void atualizar(ItemEntity item);
    void excluir(Integer id);
}