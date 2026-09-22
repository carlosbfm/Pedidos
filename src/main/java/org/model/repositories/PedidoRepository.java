package org.model.repositories;

import org.model.entities.PedidoEntity;

public interface PedidoRepository {
    PedidoEntity buscarPorId(Integer id);
    void salvar(PedidoEntity pedido);
    void atualizar(PedidoEntity pedido);
    void excluir(Integer id);
}