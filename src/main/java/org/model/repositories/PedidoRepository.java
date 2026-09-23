package org.model.repositories;

import java.util.List;

import org.enums.StatusPedido;
import org.model.entities.PedidosEntity;

public interface PedidoRepository {
    PedidosEntity buscarPorId(Integer id);
    PedidosEntity buscarPorIdCarregandoItens(Integer id);
    List<PedidosEntity> buscarPorStatus(StatusPedido status);
    List<PedidosEntity> buscarPorClienteId(Integer clienteId);
    void salvar(PedidosEntity pedido);
    void atualizar(PedidosEntity pedido);
    void salvarOuAtualizar(PedidosEntity pedido);
    void excluir(Integer id);
}