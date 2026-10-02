package org.model.repositories;

import java.util.List;


import org.enums.StatusPedido;
import org.model.entities.PedidosEntity;

public interface PedidoRepository {
    PedidosEntity buscarPorId(Long id);
    PedidosEntity buscarPorIdItens(Long id);
    List<PedidosEntity> buscarPorStatus(StatusPedido status);
    List<PedidosEntity> buscarPorClienteId(Long clienteId);
    List<PedidosEntity> listarTodos();
    List<PedidosEntity> listarPaginado(int limit, int offset);
    int contarTotal();
    void salvar(PedidosEntity pedido);
    void atualizar(PedidosEntity pedido);
    void salvarOuAtualizar(PedidosEntity pedido);
    void excluir(Long id);
    void removerItemDoPedido(Long pedidoId, Long itemId);
}