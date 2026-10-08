package org.action;

import java.sql.Connection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.AppManager;
import org.mentawai.core.BaseAction;
import org.model.entities.ClienteEntity;
import org.model.exceptions.NegocioException;
import org.model.repositories.ClienteRepository;
import org.model.repositories.PedidoRepository;
import org.model.repositories.impl.ClienteRepositoryImpl;
import org.model.repositories.impl.PedidoRepositoryImpl;
import org.model.services.ClienteService;
import org.model.utils.Page;

public class ClienteAction extends BaseAction {

	private static final DateTimeFormatter FMT_DATA_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter FMT_DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");


	public String exibir() {
		int paginaAtual = input.getInt("page", 1);
		int limite = 5;

		try (Connection conn = AppManager.getConnection()) {
			ClienteRepository clienteRepo = new ClienteRepositoryImpl(conn);
			PedidoRepository pedidoRepo = new PedidoRepositoryImpl(conn);
			ClienteService clienteService = new ClienteService(clienteRepo, pedidoRepo);

			Page<ClienteEntity> pagina = clienteService.listarPaginado(paginaAtual, limite);

			output.setValue("lista", pagina.getRegistros());
			output.setValue("paginaAtual", pagina.getPaginaAtual());
			output.setValue("totalPaginas", pagina.getTotalPaginas());
			output.setValue("totalRegistros", pagina.getTotalRegistros());

			return SUCCESS;
		} catch (NegocioException e) {
			output.setValue("erro", e.getMessage());
			return ERROR;
		} catch (Exception e) {
			output.setValue("erro", "Erro ao carregar clientes: " + e.getMessage());
			return ERROR;
		}
	}

	public String salvar() {


		if(isPost()) {
			String nomeCliente = input.getString("nomeCliente");
			String data = input.getString("dataNascimento");

			if (isEmpty(nomeCliente)) {
				addError("erro", "O preenchimento do nome do cliente é obrigatório.");
				return ERROR;
			}

			LocalDate dataFmt;
			try {
				dataFmt = converterData(data);
			} catch (DateTimeParseException e) {
				addError("erro", "Formato de data inválido. Utilize aaaa-mm-dd ou dd/mm/aaaa.");
				return ERROR;
			}

			try (Connection conn = AppManager.getConnection()) {
				ClienteRepository repo = new ClienteRepositoryImpl(conn);
				PedidoRepository pedidoRepo = new PedidoRepositoryImpl(conn);
				ClienteService clienteService = new ClienteService(repo,pedidoRepo);

				ClienteEntity cliente = new ClienteEntity(nomeCliente, dataFmt);
				clienteService.salvar(cliente);

				output.setValue("exibirMensagem", true);
				output.setValue("mensagem", "Cliente cadastrado com sucesso!");
				return SUCCESS;
			} catch (NegocioException e) {
				addError("erro", e.getMessage());
				return ERROR;
			} catch (Exception e) {
				addError("erro", "Falha ao gravar no banco: " + e.getMessage());
				return ERROR;
			}

		}
		return SUCCESS;

	}


	// estou usando input para enviar os output para no fim utilizar da action de atualizar
	public String exibirCliente() {
		String idStr = input.getString("id");

		if (idStr == null || idStr.trim().isEmpty()) {
			addError("erro", "O identificador do cliente não foi fornecido.");
			return ERROR;
		}

		Long id;
		try {
			id = Long.parseLong(idStr.trim());
		} catch (NumberFormatException e) {
			addError("erro", "O formato do identificador deve ser numérico.");
			return ERROR;
		}

		try (Connection conn = AppManager.getConnection()) {
			ClienteRepository clienteRepo = new ClienteRepositoryImpl(conn);
			PedidoRepository pedidoRepo = new PedidoRepositoryImpl(conn);
			ClienteService service = new ClienteService(clienteRepo,pedidoRepo);

			ClienteEntity cliente = service.buscarPorId(id);

			output.setValue("id", cliente.getId());
			output.setValue("nomeCliente", cliente.getNomeCliente());
			output.setValue("dataNascimento", cliente.getDataNascimentoFormatada());

			return SUCCESS;
		} catch (NegocioException e) {
			addError("erro", e.getMessage());
			return ERROR;
		} catch (Exception e) {
			addError("erro", "Falha técnica ao consultar o cliente na base de dados: " + e.getMessage());
			return ERROR;
		}
	}

	public String atualizarCliente() {


		String idStr = input.getString("id");

		String nomeCliente = input.getString("nomeCliente");
		String dataNascimento = input.getString("dataNascimento");

		if (idStr == null || isEmpty(nomeCliente) || isEmpty(dataNascimento)) {
			addError("erro", "Todos os campos obrigatórios devem ser preenchidos.");
			return ERROR;
		}



		if (idStr == null || isEmpty(idStr)) {
			addError("erro", "Identificador do item não informado.");
			return ERROR;
		}

		Long id;
		try {
			id = Long.parseLong(idStr.trim());
		} catch (NumberFormatException e) {
			addError("erro", "Formato de identificador inválido.");
			return ERROR;
		}

		LocalDate dataFmt = null;
		try {
			if (dataNascimento != null && !dataNascimento.trim().isEmpty()) {
				dataFmt = converterData(dataNascimento);
			}
		} catch (DateTimeParseException e) {
			addError("erro", "Formato de data inválido. Utilize aaaa-mm-dd ou dd/mm/aaaa.");
			return ERROR;
		}

		try (Connection conn = AppManager.getConnection()) {
			ClienteRepository clienteRepo = new ClienteRepositoryImpl(conn);
			PedidoRepository pedidoRepo = new PedidoRepositoryImpl(conn);
			ClienteService clienteService = new ClienteService(clienteRepo,pedidoRepo);

			ClienteEntity cliente = clienteService.buscarPorId(id); 
			cliente.setNomeCliente(nomeCliente);
			cliente.setDataNascimento(dataFmt);

			clienteService.atualizar(cliente);

			output.setValue("exibirMensagem", true);
			output.setValue("mensagem", "Cliente atualizado com sucesso!");
			return SUCCESS;
		} catch (NegocioException e) {
			addError("erro", e.getMessage());
			return ERROR;
		} catch (Exception e) {
			addError("erro", "Erro ao atualizar dados do cliente: " + e.getMessage());
			return ERROR;
		}


	}

	public String excluir() {
		String idStr = input.getString("id");

		if (idStr == null || isEmpty(idStr)) {
			output.setValue("erro", "Identificador do cliente não fornecido.");
			return ERROR;
		}

		Long id;
		try {
			id = Long.parseLong(idStr.trim());
		} catch (NumberFormatException e) {
			addError("erro", "Formato de identificador numérico inválido.");
			return ERROR;
		}

		try (Connection conn = AppManager.getConnection()) {
			ClienteRepository clienteRepo = new ClienteRepositoryImpl(conn);
			PedidoRepository pedidoRepo = new PedidoRepositoryImpl(conn);
			ClienteService clienteService = new ClienteService(clienteRepo, pedidoRepo);

			clienteService.excluir(id);

			output.setValue("exibirMensagem", true);
			output.setValue("mensagem", "Cliente excluído com sucesso!");
			return SUCCESS;
		} catch (NumberFormatException e) {
			output.setValue("erro", "Formato de identificador numérico inválido.");
			return ERROR;
		} catch (NegocioException e) {
			output.setValue("erro", e.getMessage());
			return ERROR;
		} catch (Exception e) {
			output.setValue("erro", "Erro ao remover registo da base de dados: " + e.getMessage());
			return ERROR;
		}
	}

	private LocalDate converterData(String data) {
		if (data == null || data.trim().isEmpty()) {
			return null;
		}
		try {
			return LocalDate.parse(data.trim(), FMT_DATA_ISO);
		} catch (DateTimeParseException e) {
			return LocalDate.parse(data.trim(), FMT_DATA_BR);
		}
	}
}