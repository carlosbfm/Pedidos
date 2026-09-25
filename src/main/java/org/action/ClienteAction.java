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
			output.setValue("erro", "O preenchimento do nome do cliente é obrigatório");
			return ERROR;
		}

		String data = input.getString("dataNascimento");

		LocalDate dataFmt = null;
		try {
			if (data == null || data.trim().isEmpty()) {
		        output.setValue("erro", "O preenchimento da data de nascimento é obrigatório");
		        return ERROR;
		    }
			dataFmt = LocalDate.parse(data);
		} catch (DateTimeParseException e1) {
			try {
				dataFmt = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (DateTimeParseException e2) {
				output.setValue("erro", "Formato de data inválido. Use aaaa-mm-dd ou dd/mm/aaaa");
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
		output.setValue("mensagem", "Novo cliente cadastrado!");

		return SUCCESS;
	}
	
	@Override
    public String execute() throws Exception {
        return cadastro();
    }
	
	public String index() throws Exception {
	    output.setValue("lista", lista);
	    return SUCCESS;
	}
}
