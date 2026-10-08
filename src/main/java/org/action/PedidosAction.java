package org.action;

import java.math.BigDecimal;

import java.sql.Connection;
import java.util.List;

import org.AppManager;
import org.enums.FormaDePagamento;
import org.mentawai.core.BaseAction;
import org.model.entities.ClienteEntity;
import org.model.entities.ItemEntity;
import org.model.entities.PagamentoEntity;
import org.model.entities.PedidosEntity;
import org.model.exceptions.NegocioException;
import org.model.repositories.ClienteRepository;
import org.model.repositories.ItemRepository;
import org.model.repositories.PedidoRepository;
import org.model.repositories.impl.ClienteRepositoryImpl;
import org.model.repositories.impl.ItemRepositoryImpl;
import org.model.repositories.impl.PedidoRepositoryImpl;
import org.model.services.ClienteService;
import org.model.services.ItemService;
import org.model.services.PedidoService;
import org.model.utils.Page;

public class PedidosAction extends BaseAction {

	public String exibir() {
		int paginaAtual = input.getInt("page", 1);
		int limite = 5;

		try (Connection conn = AppManager.getConnection()) {
			PedidoService pedidoService = criarPedidoService(conn);

			Page<PedidosEntity> pagina = pedidoService.listarPaginado(paginaAtual, limite);

			output.setValue("lista", pagina.getRegistros());
			output.setValue("paginaAtual", pagina.getPaginaAtual());
			output.setValue("totalPaginas", pagina.getTotalPaginas());
			output.setValue("totalRegistros", pagina.getTotalRegistros());

			return SUCCESS;
		} catch (NegocioException e) {
			addError("erro", e.getMessage());
			return ERROR;
		} catch (Exception e) {
			addError("erro", "Falha técnica ao listar pedidos: " + e.getMessage());
			return ERROR;
		}
	}

	public String exibirDados() {
		try (Connection conn = AppManager.getConnection()) {
			ClienteRepository clienteRepo = new ClienteRepositoryImpl(conn);
			PedidoRepository pedidoRepo = new PedidoRepositoryImpl(conn);
			ClienteService clienteService = new ClienteService(clienteRepo,pedidoRepo);

			ItemRepository itemRepo = new ItemRepositoryImpl(conn);
			ItemService itemService = new ItemService(itemRepo);

			output.setValue("clientes", clienteService.listarTodos());
			output.setValue("itens", itemService.listarTodos());
			output.setValue("formasPagamento", FormaDePagamento.values());

			return SUCCESS;
		} catch (Exception e) {
			addError("erro", "Erro ao carregar dados do formulário: " + e.getMessage());
			return ERROR;
		}
	}


	public String criarPedido() {
	    exibirDados(); 

	    Long clienteId = input.getLong("clienteId");
	    Long itemId = input.getLong("itemId");
	    int quantidade = input.getInt("quantidade", 0);
	    String formaPagamentoStr = input.getString("formaPagamento");

	    if (clienteId == null || clienteId <= 0) {
	        addError("erro", "Selecione um cliente válido.");
	        return ERROR;
	    }

	    if (itemId == null || itemId <= 0) {
	        addError("erro", "Selecione um item válido.");
	        return ERROR;
	    }

	    if (quantidade <= 0) {
	        addError("erro", "A quantidade do item deve ser superior a zero.");
	        return ERROR;
	    }

	    FormaDePagamento formaPagamento;
	    try {
	        if (isEmpty(formaPagamentoStr)) {
	            addError("erro", "A forma de pagamento é obrigatória.");
	            return ERROR;
	        }
	        formaPagamento = FormaDePagamento.valueOf(formaPagamentoStr.trim().toUpperCase());
	    } catch (IllegalArgumentException e) {
	        addError("erro", "Forma de pagamento selecionada é inválida.");
	        return ERROR;
	    }

	    try (Connection conn = AppManager.getConnection()) {
	        PedidoService pedidoService = criarPedidoService(conn);
	        ItemRepository itemRepo = new ItemRepositoryImpl(conn);
	        ItemService itemService = new ItemService(itemRepo);
	        ClienteRepository clienteRepo = new ClienteRepositoryImpl(conn);

	        ItemEntity itemEstoque = itemService.buscarPorId(itemId);

	        ClienteEntity cliente = clienteRepo.buscarPorId(clienteId);

	        ItemEntity itemPedido = new ItemEntity();
	        itemPedido.setId(itemEstoque.getId());
	        itemPedido.setNomeItem(itemEstoque.getNomeItem());
	        itemPedido.setPrecoItem(itemEstoque.getPrecoItem());
	        itemPedido.addQuantidade(quantidade);

	        BigDecimal valorTotalCalculado = itemEstoque.getPrecoItem().multiply(BigDecimal.valueOf(quantidade));
	        PagamentoEntity pagamento = new PagamentoEntity(valorTotalCalculado, formaPagamento);

	        PedidosEntity pedido = new PedidosEntity(cliente, pagamento, quantidade);
	        pedido.adicionarItem(itemPedido);

	        pedidoService.criarPedido(pedido);

	        // evitar fazer a chamada de objeto por questão de seguraça da entidade
	        output.setValue("pedido", pedido);
	        output.setValue("mensagem", "Pedido #" + pedido.getId() + " cadastrado com sucesso!");
	        
	        return SUCCESS;
	    } catch (NegocioException e) {
	        addError("erro", e.getMessage());
	        return ERROR;
	    } catch (Exception e) {
	        addError("erro", "Falha técnica ao cadastrar pedido: " + e.getMessage());
	        return ERROR;
	    }
	}
	
	public String detalhes() {
		
		Long id = input.getLong("id");
		
		String erroParam = input.getString("erro");
		if (erroParam != null && !erroParam.trim().isEmpty()) {
		    addError("erro", erroParam);
		}
	
		
		//System.out.println(input.getString("erro"));
		

		if (id == null || id <= 0) {
			addError("erro", "Identificador de pedido inválido.");
			return ERROR;
		}

		try (Connection conn = AppManager.getConnection()) {
			PedidoService pedidoService = criarPedidoService(conn);

			PedidosEntity pedido = pedidoService.buscarPorIdDetalhado(id);

			int quantidadeTotal = pedido.getItens().stream()
					.mapToInt(ItemEntity::getQuantidadeDoItem)
					.sum();

			// entender o reduce 
			BigDecimal valorTotal = pedido.getItens().stream()
					.map(i -> i.getPrecoItem().multiply(BigDecimal.valueOf(i.getQuantidadeDoItem())))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			
			


			// não ser o objeto no output 
			output.setValue("pedido", pedido);
			output.setValue("idConferencia", id);
			output.setValue("quantidadeTotal", quantidadeTotal);
			output.setValue("valorTotal", valorTotal);
			//output.setValue("formasPagamento", FormaDePagamento.values()); no jsp ele é chamado

			return SUCCESS;
		} catch (NegocioException e) {
			addError("erro", e.getMessage());
			return ERROR;
		} catch (Exception e) {
			addError("erro", "Erro ao carregar detalhes do pedido: " + e.getMessage());
			return ERROR;
		}
	}

	public String concluir() {
		Long id = input.getLong("idPedido");
		
		int quantidadeConferida = input.getInt("quantidadeConferida", -1);
		String valorTotalStr = input.getString("valorConferido");
		String formaPagamentoStr = input.getString("formaPagamentoConferida");

		if (id == null || id <= 0) {
			addError("erro", "Identificador do pedido inválido.");
			return ERROR;
		}

		if (quantidadeConferida <= 0) {
			addError("erro", "Quantidade de itens para conferência não informada ou inválida.");
			return ERROR;
		}

		if (isEmpty(valorTotalStr)) {
			addError("erro", "Valor financeiro para conferência é obrigatório.");
			return ERROR;
		}

		BigDecimal valorConferido;
		try {
			// evitar tratar no back e sim no front
			String formatado = valorTotalStr.replace("R$", "").replace(",", ".").trim();
			valorConferido = new BigDecimal(formatado);
		} catch (NumberFormatException e) {
			addError("erro", "Formato de valor financeiro inválido.");
			return ERROR;
		}

		FormaDePagamento formaPagamentoConferida;
		try {
			if (isEmpty(formaPagamentoStr)) {
				addError("erro", "Forma de pagamento para conferência é obrigatória.");
				return ERROR;
			}
			formaPagamentoConferida = FormaDePagamento.valueOf(formaPagamentoStr.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			addError("erro", "Forma de pagamento para conferência é inválida.");
			return ERROR;
		}

		try (Connection conn = AppManager.getConnection()) {
			PedidoService pedidoService = criarPedidoService(conn);

			pedidoService.concluirPedido(id, quantidadeConferida, valorConferido, formaPagamentoConferida);

			output.setValue("mensagem", "Pedido #" + id + " conferido e concluído com sucesso!");
			return SUCCESS;
		} catch (NegocioException e) {
			addError("erro", e.getMessage());
			return ERROR;
		} catch (Exception e) {
			addError("erro", "Falha técnica ao concluir pedido: " + e.getMessage());
			return ERROR;
		}
	}

	public String cancelar() {
		Long id = input.getLong("idPedido");
		
		

		if (id == null || id <= 0) {
			addError("erro", "Identificador do pedido inválido.");
			return ERROR;
		}

		try (Connection conn = AppManager.getConnection()) {
			PedidoService pedidoService = criarPedidoService(conn);
			
			pedidoService.cancelarPedido(id);

			output.setValue("mensagem", "Pedido #" + id + " cancelado com sucesso e estoque estornado.");
			return SUCCESS;
		} catch (NegocioException e) {
			output.setValue("erro", e.getMessage());
			return ERROR;
		} catch (Exception e) {
			output.setValue("erro", "Erro ao processar cancelamento: " + e.getMessage());
			return ERROR;
		}
	}

	public String excluirItem() {
		Long pedidoId = input.getLong("idPedido");
		output.setValue("id",pedidoId);
		Long itemId = input.getLong("itemId");
		

		try (Connection conn = AppManager.getConnection()) {
			PedidoService pedidoService = criarPedidoService(conn);
			PedidosEntity pedido = pedidoService.buscarPorIdDetalhado(pedidoId);
			
			if (pedidoId == null || pedidoId <= 0 || itemId == null || itemId <= 0) {
				addError("erro", "Identificadores de pedido e item são obrigatórios.");
				return ERROR;
			}
			
	        if (pedido.getItens() == null || pedido.getItens().size() <= 1) {
	            addError("erro", "O pedido possui apenas um item. Para retirá-lo, cancele o pedido por completo.");
	            output.setValue("erro", "O pedido possui apenas um item. Para retirá-lo, cancele o pedido por completo.");

	            return ERROR; 
	            
	        }
			
			output.setValue("mensagem", "Item removido do pedido com sucesso e estoque estornado.");
			pedidoService.excluirItemDoPedido(pedidoId, itemId);

			
			return SUCCESS;
		} catch (NegocioException e) {
			addError("erro", e.getMessage());
			return ERROR;
		} catch (Exception e) {
			addError("erro", "Erro ao excluir item do pedido: " + e.getMessage());
			return ERROR;
		}
	}

	private PedidoService criarPedidoService(Connection conn) {
		PedidoRepository pedidoRepo = new PedidoRepositoryImpl(conn);
		ItemRepository itemRepo = new ItemRepositoryImpl(conn);
		ClienteRepository clienteRepo = new ClienteRepositoryImpl(conn);

		ItemService itemService = new ItemService(itemRepo);
		ClienteService clienteService = new ClienteService(clienteRepo,pedidoRepo);

		return new PedidoService(pedidoRepo, itemService, clienteService);
	}
}