package org.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.enums.UnidadeDeMedida;
import org.mentawai.core.BaseAction;
import org.mentawai.tag.IsEmpty;
import org.model.entities.ItemEntity;

public class ItemAction extends BaseAction {
	private static final List<ItemEntity> lista = new ArrayList<>(); 

	public String cadastro() {
		System.out.println(">>> ENTROU NO CADASTRAR DA ITEMACTION <<<");
		output.setValue("listaTipoUnd", UnidadeDeMedida.values());
		String nome = input.getString("itemNome");

		if(isEmpty(nome)) {
			addError("erro", "O nome do produto é obrigatório ser preenchido");
			return ERROR;
		}

		String precoUnitarioStr = input.getString("itemPreco");


		if(isEmpty(precoUnitarioStr)) {
			addError("erro", "O preço unitário do produto é obrigatório ser preenchido");
			return ERROR;
		}

		precoUnitarioStr = precoUnitarioStr.replace("R$", "").replace(",", ".").trim();


		BigDecimal precoUnitario;

		try {
			precoUnitario = new BigDecimal(precoUnitarioStr);
		} catch (NumberFormatException e) {
			addError("erro", "O formato do preço unitário informado é inválido.");
			return ERROR;
		}

		if(precoUnitario == null || precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
			addError("erro", "O preço unitário do produto deve ser maior ou igual a zero");
			return ERROR;
		}

		String tipoUnidade = input.getString("tipoUnidade");
		UnidadeDeMedida tipo = null;

		tipoUnidade = input.getString("tipoUnidade");
		System.out.println(">>> VALOR EXATO RECEBIDO NO BACKEND: [" + tipoUnidade + "] <<<");
		if (!isEmpty(tipoUnidade)) {
			try {
				tipo = UnidadeDeMedida.fromString(tipoUnidade.trim());
			} catch (IllegalArgumentException e) {
				addError("erro", "A unidade de medida selecionada é inválida.");
				return ERROR;
			}
		} else {
			output.setValue("erro", "A unidade de medida é obrigatória.");
			return ERROR;
		}

		String quantidadeStr = input.getString("itemQuantidade");

		if (quantidadeStr == null || quantidadeStr.trim().isEmpty()) {
			addError("erro", "A quantidade é obrigatória.");
			return ERROR;
		}


		int quantidade;
		try {
			quantidade = Integer.parseInt(quantidadeStr);
			if (quantidade <= 0) {
				addError("erro", "A quantidade deve ser maior que zero.");
				return ERROR;
			}
		} catch (NumberFormatException e) {
			addError("erro", "A quantidade informada é inválida.");
			return ERROR;
		}

		ItemEntity item = new ItemEntity(nome,precoUnitario,tipo,quantidade);

		System.out.println("ID gerado: " + item.getId());
		lista.add(item);
		System.out.println(">>> SUCESSO: Chegou ao final do método -> retornando SUCCESS");
		output.setValue("item", item);
		output.setValue("lista", lista);
		output.setValue("mensagem", "Item cadastrado com sucesso!");

		return SUCCESS;
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

		List<ItemEntity> itensPaginados = (inicio < totalRegistros) 
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
			addError("erro", "Identificador do item não foi fornecido.");
			return ERROR;
		}

		try {
			Long id = Long.parseLong(idStr);

			boolean removido = lista.removeIf(item -> item.getId().equals(id));

			if (!removido) {
				addError("erro", "Item não encontrado para exclusão.");
				return ERROR;
			}

		} catch (NumberFormatException e) {
			addError("erro", "Formato de identificador inválido.");
			return ERROR;
		}
		System.out.println("Exclusão ok do id: " + idStr);

		return SUCCESS;
	}
	
	public String exibirItem() {
		output.setValue("listaTipoUnd", UnidadeDeMedida.values());
	    String idStr = input.getString("id");
	    
	    System.out.println("item id = " + idStr);
	    if (idStr == null || idStr.trim().isEmpty()) {
	        addError("erro", "ID não informado para edição.");
	        return ERROR;
	    }
	    
	    ItemEntity item = null;
	    try {
	        Long id = Long.parseLong(idStr);
	        
	        System.out.println("item id = " + id);
	         item = lista.stream()
	                                   .filter(x -> x.getId().equals(id))
	                                   .findFirst()
	                                   .orElse(null);
	                                   
	        if (item == null) {
	            addError("erro", "Item não encontrado no sistema.");
	            return ERROR;
	        }
	        
	        output.setValue("id", item.getId());
	        output.setValue("nomeItem", item.getNomeItem());
	        output.setValue("precoItem", item.getPrecoItem());
	        output.setValue("quantidadeDoItem", item.getQuantidadeDoItem());
	        output.setValue("tipoUnidadeDeMedida", item.getTipoUnidadeDeMedida());
	        
	    } catch (NumberFormatException e) {
	        addError("erro", "Formato de ID inválido.");
	        return ERROR;
	    }
	    
	    output.setValue("item", item);
	    
	    return SUCCESS;
	}


	public String atualizarItem() {
        output.setValue("listaTipoUnd", UnidadeDeMedida.values());

        Long id = input.getLong("id");
        String nome = input.getString("nomeItem");
        String precoUnitarioStr = input.getString("precoItem");
        String tipoUnidade = input.getString("tipoUnidadeDeMedida");
        int novaQuantidade = input.getInt("quantidadeDoItem", 0);

        if (id == null || isEmpty(nome) || isEmpty(precoUnitarioStr) || novaQuantidade <= 0) {
            addError("erro", "Todos os campos devem ser preenchidos corretamente.");
            return ERROR;
        }

        BigDecimal novoPreco;
        try {
            String formatado = precoUnitarioStr.replace("R$", "").replace(",", ".").trim();
            novoPreco = new BigDecimal(formatado);
            if (novoPreco.compareTo(BigDecimal.ZERO) <= 0) {
                addError("erro", "O preço deve ser superior a zero.");
                return ERROR;
            }
        } catch (NumberFormatException e) {
            addError("erro", "O preço unitário informado possui formato inválido.");
            return ERROR;
        }

        UnidadeDeMedida unidade;
        try {
            unidade = UnidadeDeMedida.fromString(tipoUnidade.trim());
        } catch (Exception e) {
            addError("erro", "A unidade de medida selecionada é inválida.");
            return ERROR;
        }

        ItemEntity item = lista.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (item == null) {
            addError("erro", "Item não localizado para atualização.");
            return ERROR;
        }

        item.setNomeItem(nome);
        item.setPrecoItem(novoPreco);
        item.setTipoUnidadeDeMedida(unidade);

        int quantidadeAtual = item.getQuantidadeDoItem();
        int diferenca = novaQuantidade - quantidadeAtual;

        if (diferenca > 0) {
            item.addQuantidade(diferenca);
        } else if (diferenca < 0) {
            item.removeQuantidade(Math.abs(diferenca)); 
        }

        return SUCCESS;
    }
}
