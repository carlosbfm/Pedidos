package org.action;


import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.mentawai.core.BaseAction;
import org.model.entities.ClienteEntity;

public class ClienteAction extends BaseAction{
	private static final List<ClienteEntity> lista = new ArrayList<>();

	public String cadastro() throws Exception {
		String nomeCliente = input.getString("nomeCliente");

		if (isEmpty(nomeCliente)) {
			addError("erro", "O preenchimento do nome do cliente é obrigatório");
			return ERROR;
		}

		String data = input.getString("dataNascimento");

		LocalDate dataFmt = null;
		try {
			if (data == null || data.trim().isEmpty()) {
				addError("erro", "O preenchimento da data de nascimento é obrigatório");
				return ERROR;
			}
			dataFmt = LocalDate.parse(data);
		} catch (DateTimeParseException e1) {
			try {
				dataFmt = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (DateTimeParseException e2) {
				addError("erro", "Formato de data inválido. Use aaaa-mm-dd ou dd/mm/aaaa");
				return ERROR;
			}
		}

		ClienteEntity cliente = new ClienteEntity(nomeCliente, dataFmt);

		System.out.println("ID gerado: " + cliente.getId());
		System.out.println("Data cadastro: " + cliente.getDataCadastroFormatada());
		System.out.println("Data nascimento: " + cliente.getDataNascimentoFormatada());

		lista.add(cliente);

		output.setValue("cliente", cliente);
		output.setValue("lista", lista);
		output.setValue("exibirMensagem", true);
		output.setValue("mensagem", "Novo cliente cadastrado!");

		return SUCCESS;
	}

	@Override
	public String execute() throws Exception {
		return cadastro();
	}

	public String exibir() throws Exception {
		int paginaAtual = input.getInt("page", 1);
		if (paginaAtual < 1) {
			paginaAtual = 1;
		}

		int registrosPorPagina = 5;
		int totalRegistros = lista.size();

		int totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);
		if (totalPaginas == 0) {
			totalPaginas = 1;
		}
		if (paginaAtual > totalPaginas) {
			paginaAtual = totalPaginas;
		}

		int inicio = (paginaAtual - 1) * registrosPorPagina;
		int fim = Math.min(inicio + registrosPorPagina, totalRegistros);

		List<ClienteEntity> itensPaginados = (inicio < totalRegistros) 
				? lista.subList(inicio, fim) 
						: new ArrayList<>();

				output.setValue("lista", itensPaginados);
				output.setValue("paginaAtual", paginaAtual);
				output.setValue("totalPaginas", totalPaginas);
				output.setValue("totalRegistros", totalRegistros);

				return SUCCESS;
	}
	
	public String excluir() {
		String idStr = input.getString("id");

		if (idStr == null || isEmpty(idStr)) {
			addError("erro", "Identificador do cliente não foi fornecido.");
			return ERROR;
		}

		try {
			Long id = Long.parseLong(idStr);

			boolean removido = lista.removeIf(item -> item.getId().equals(id));

			if (!removido) {
				addError("erro", "Cliente não encontrado para exclusão.");
				return ERROR;
			}

		} catch (NumberFormatException e) {
			addError("erro", "Formato de identificador inválido.");
			return ERROR;
		}
		System.out.println("Exclusão ok do cliente com id: " + idStr);

		return SUCCESS;
	}
	
	
	public String exibirCliente() {
	    String idStr = input.getString("id");
	    
	    System.out.println("item id = " + idStr);
	    if (idStr == null || idStr.trim().isEmpty()) {
	        addError("erro", "ID não informado para edição.");
	        return ERROR;
	    }
	    
	    ClienteEntity cliente = null;
	    try {
	        Long id = Long.parseLong(idStr);
	        
	        System.out.println("item id = " + id);
	         cliente = lista.stream()
	                                   .filter(x -> x.getId().equals(id))
	                                   .findFirst()
	                                   .orElse(null);
	                                   
	        if (cliente == null) {
	            addError("erro", "Item não encontrado no sistema.");
	            return ERROR;
	        }
	        
	        output.setValue("id", cliente.getId());
	        output.setValue("nomeCliente", cliente.getNomeCliente());
	        output.setValue("dataNascimento", cliente.getDataNascimentoFormatada());
	        
	        output.setValue("cliente", cliente);
	        
	    } catch (NumberFormatException e) {
	        addError("erro", "Formato de ID inválido.");
	        return ERROR;
	    }
	    
	    
	    
	    return SUCCESS;
	}
	
	public String atualizarCliente() {

        Long id = input.getLong("id");
        String nomeCliente = input.getString("nomeCliente");
        String dataNascimento = input.getString("dataNascimento");
        

        if (id == null || isEmpty(nomeCliente) || dataNascimento == null || dataNascimento.trim().isEmpty() ) {
            addError("erro", "Todos os campos devem ser preenchidos corretamente.");
            return ERROR;
        }

        
        LocalDate dataFmt = null;
        try {
        	dataFmt = LocalDate.parse(dataNascimento);
        } catch (DateTimeParseException e1) {
			try {
				dataFmt = LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (DateTimeParseException e2) {
				addError("erro", "Formato de data inválido. Use aaaa-mm-dd ou dd/mm/aaaa");
				return ERROR;
			}
		}

        ClienteEntity cliente = lista.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (cliente == null) {
            addError("erro", "Item não localizado para atualização.");
            return ERROR;
        }

        cliente.setNomeCliente(nomeCliente);
        cliente.setDataNascimento(dataFmt);

        return SUCCESS;
    }
}
