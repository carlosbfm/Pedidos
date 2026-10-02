package org.model.services;

import java.math.BigDecimal;
import java.util.List;

import org.enums.FormaDePagamento;
import org.enums.StatusPedido;
import org.model.entities.ItemEntity;
import org.model.entities.PagamentoEntity;
import org.model.entities.PedidosEntity;
import org.model.exceptions.NegocioException;
import org.model.repositories.PedidoRepository;
import org.model.utils.Page;

public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemService itemService;
    private final ClienteService clienteService;

    public PedidoService(PedidoRepository pedidoRepository, ItemService itemService, ClienteService clienteService) {
        this.pedidoRepository = pedidoRepository;
        this.itemService = itemService;
        this.clienteService = clienteService;
    }

    public Page<PedidosEntity> listarPaginado(int pagina, int tamanhoPagina) {
        if (pagina < 1) pagina = 1;
        if (tamanhoPagina <= 0) tamanhoPagina = 5;

        int totalRegistros = pedidoRepository.contarTotal();
        int offset = (pagina - 1) * tamanhoPagina;

        List<PedidosEntity> pedidos = pedidoRepository.listarPaginado(tamanhoPagina, offset);
        return new Page<>(pedidos, pagina, tamanhoPagina, totalRegistros);
    }

    public PedidosEntity buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new NegocioException("Identificador de pedido inválido.");
        }
        PedidosEntity pedido = pedidoRepository.buscarPorId(id);
        if (pedido == null) {
            throw new NegocioException("Pedido com identificador " + id + " não encontrado.");
        }
        return pedido;
    }

    //rever
    public PedidosEntity buscarPorIdDetalhado(Long id) {
        if (id == null || id <= 0) {
            throw new NegocioException("Identificador de pedido inválido.");
        }
        PedidosEntity pedido = pedidoRepository.buscarPorIdItens(id);
        if (pedido == null) {
            throw new NegocioException("Pedido com identificador " + id + " não encontrado.");
        }
        return pedido;
    }

    public void criarPedido(PedidosEntity pedido) {
        validarDadosIniciais(pedido);

        pedido.setStatus(StatusPedido.CRIADO);

        for (ItemEntity itemPedido : pedido.getItens()) {
            ItemEntity itemEstoque = itemService.buscarPorId(itemPedido.getId());
            int qtdSolicitada = itemPedido.getQuantidadeDoItem();

            if (itemEstoque.getQuantidadeDoItem() < qtdSolicitada) {
                throw new NegocioException("Estoque insuficiente para o produto: " + itemEstoque.getNomeItem()
                        + ". Disponível: " + itemEstoque.getQuantidadeDoItem() + ", Solicitado: " + qtdSolicitada);
            }
            itemService.removerEstoque(itemEstoque.getId(), qtdSolicitada);
        }

        pedidoRepository.salvar(pedido);
    }

   
    public void cancelarPedido(Long pedidoId) {
        PedidosEntity pedido = buscarPorIdDetalhado(pedidoId);
        StatusPedido statusAtual = pedido.getStatus();

        if (statusAtual == StatusPedido.CONCLUIDO) {
            throw new NegocioException("Não é permitido cancelar um pedido já concluído.");
        }

        if (statusAtual == StatusPedido.CANCELADO) {
            throw new NegocioException("Este pedido já se encontra cancelado.");
        }

        if (statusAtual != StatusPedido.CRIADO && statusAtual != StatusPedido.PENDENTE) {
            throw new NegocioException("Status atual [" + statusAtual.getDescricao() + "] não permite cancelamento.");
        }

        for (ItemEntity item : pedido.getItens()) {
            itemService.adicionarEstoque(item.getId(), item.getQuantidadeDoItem());
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.atualizar(pedido);
    }

   
    public void excluirItemDoPedido(Long pedidoId, Long itemId) {
        PedidosEntity pedido = buscarPorIdDetalhado(pedidoId);

        if (pedido.getStatus() == StatusPedido.CONCLUIDO || pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new NegocioException("Itens não podem ser excluídos de pedidos no status " + pedido.getStatus().getDescricao());
        }

        List<ItemEntity> itens = pedido.getItens();
        if (itens.size() <= 1) {
            throw new NegocioException("O pedido possui apenas um item. Para retirá-lo, cancele o pedido por completo.");
        }

        ItemEntity itemParaRemover = itens.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NegocioException("Item ID " + itemId + " não pertence a este pedido."));

        itemService.adicionarEstoque(itemParaRemover.getId(), itemParaRemover.getQuantidadeDoItem());

        pedidoRepository.removerItemDoPedido(pedidoId, itemId);
    }

   
    public void concluirPedido(Long pedidoId, Integer quantidadeConferida, BigDecimal valorConferido, FormaDePagamento formaPagamentoConferida) {
        PedidosEntity pedido = buscarPorIdDetalhado(pedidoId);

        if (pedido.getStatus() == StatusPedido.CONCLUIDO) {
            throw new NegocioException("O pedido já está concluído.");
        }

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new NegocioException("Não é possível concluir um pedido cancelado.");
        }

        int quantidadeReal = pedido.getItens().stream()
                .mapToInt(ItemEntity::getQuantidadeDoItem)
                .sum();

        if (quantidadeConferida == null || quantidadeConferida != quantidadeReal) {
            throw new NegocioException("Divergência na quantidade de itens! Quantidade real no pedido: " 
                    + quantidadeReal + ", Quantidade informada na conferência: " + quantidadeConferida);
        }

        BigDecimal valorTotalReal = pedido.getItens().stream()
                .map(item -> item.getPrecoItem().multiply(BigDecimal.valueOf(item.getQuantidadeDoItem())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (valorConferido == null || valorConferido.compareTo(valorTotalReal) != 0) {
            throw new NegocioException("Divergência de valores financeiros! Total esperado: R$ " 
                    + valorTotalReal + ", Valor apresentado na conferência: R$ " + valorConferido);
        }

        PagamentoEntity pagamentoRegistrado = pedido.getFormaPagamento();
        if (formaPagamentoConferida == null) {
            throw new NegocioException("A forma de pagamento para conferência é obrigatória.");
        }

        if (pagamentoRegistrado == null || pagamentoRegistrado.getFormaDePagamento() != formaPagamentoConferida) {
            throw new NegocioException("Inconsistência no meio de pagamento! Registrado no pedido: " 
                    + (pagamentoRegistrado != null ? pagamentoRegistrado.getFormaDePagamento() : "NENHUM") 
                    + ", Informado na conferência: " + formaPagamentoConferida);
        }

        pedido.setStatus(StatusPedido.CONCLUIDO);
        pedidoRepository.atualizar(pedido);
    }

    private void validarDadosIniciais(PedidosEntity pedido) {
        if (pedido == null) {
            throw new NegocioException("Os dados do pedido não foram informados.");
        }

        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new NegocioException("Um cliente válido deve ser associado ao pedido.");
        }

        clienteService.buscarPorId(pedido.getCliente().getId());

        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new NegocioException("O pedido deve conter pelo menos um item.");
        }

        if (pedido.getFormaPagamento() == null || pedido.getFormaPagamento().getFormaDePagamento() == null) {
            throw new NegocioException("A forma de pagamento deve ser informada.");
        }
    }
}