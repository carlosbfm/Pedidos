package org.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.enums.UnidadeDeMedida;
import org.mentawai.core.BaseAction;
import org.model.entities.ItemEntity;

public class ItemAction extends BaseAction {
	private static final List<ItemEntity> lista = new ArrayList<>(); 
	public String listarTipoDeUnidades() {
		output.setValue("listaTipoUnd", UnidadeDeMedida.values());
		return SUCCESS;
	}
	
	public String cadastrar() {
		System.out.println(">>> ENTROU NO CADASTRAR DA ITEMACTION <<<");
		output.setValue("listaTipoUnd", UnidadeDeMedida.values());
		String nome = input.getString("itemNome");
		
		if(isEmpty(nome)) {
			output.setValue("erro", "O nome do produto é obrigatório ser preenchido");
			return ERROR;
		}
		
		String precoUnitarioStr = input.getString("itemPreco");
		
		
		if(isEmpty(precoUnitarioStr)) {
			output.setValue("erro", "O preço unitário do produto é obrigatório ser preenchido");
			return ERROR;
		}
		
		precoUnitarioStr = precoUnitarioStr.replace("R$", "").replace(",", ".").trim();
		
		
		BigDecimal precoUnitario;
		
        try {
            precoUnitario = new BigDecimal(precoUnitarioStr);
        } catch (NumberFormatException e) {
            output.setValue("erro", "O formato do preço unitário informado é inválido.");
            return ERROR;
        }
		
		if(precoUnitario == null || precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
			output.setValue("erro", "O preço unitário do produto deve ser maior ou igual a zero");
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
                output.setValue("erro", "A unidade de medida selecionada é inválida.");
                return ERROR;
            }
        } else {
            output.setValue("erro", "A unidade de medida é obrigatória.");
            return ERROR;
        }
		
		String quantidadeStr = input.getString("itemQuantidade");
		
		if (quantidadeStr == null || quantidadeStr.trim().isEmpty()) {
		    output.setValue("erro", "A quantidade é obrigatória.");
		    return ERROR;
		}
		
		
		int quantidade;
		try {
		    quantidade = Integer.parseInt(quantidadeStr);
		    if (quantidade <= 0) {
		        output.setValue("erro", "A quantidade deve ser maior que zero.");
		        return ERROR;
		    }
		} catch (NumberFormatException e) {
		    output.setValue("erro", "A quantidade informada é inválida.");
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
}
