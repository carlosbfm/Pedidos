package org.action;

import java.math.BigDecimal;


import org.enums.UnidadeDeMedida;
import org.mentawai.core.BaseAction;
import org.model.entities.ItemEntity;

public class ItemAction extends BaseAction {
	
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
		
		
		if(precoUnitarioStr.isEmpty()) {
			output.setValue("erro", "O preço unitário do produto é obrigatório ser preenchido");
			return ERROR;
		}
		
		precoUnitarioStr = precoUnitarioStr.replace("R$", "").replace(".", "").replace(",", ".").trim();
		
		
		BigDecimal precoUnitario;
		
        try {
            precoUnitario = new BigDecimal(precoUnitarioStr);
        } catch (NumberFormatException e) {
            output.setValue("erro", "O formato do preço unitário informado é inválido.");
            return ERROR;
        }
		
		if(precoUnitario.compareTo(BigDecimal.ZERO) <= 0 || precoUnitario == null) {
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
		
		int quantidade = input.getInt("itemQuantidade");
		
		
		if(quantidade <= 0) {
			output.setValue("erro", "A quantidade do produto deve ser maior ou igual a zero");
			return ERROR;
		}
		
		ItemEntity item = new ItemEntity();
		
		System.out.println("ID gerado: " + item.getId());
		item.setNomeItem(nome);
		item.setPrecoItem(precoUnitario);
		item.setTipoUnidadeDeMedida(tipo);
		item.addQuantidade(quantidade);
		System.out.println(">>> SUCESSO: Chegou ao final do método -> retornando SUCCESS");
		output.setValue("item", item);
        output.setValue("mensagem", "Item cadastrado com sucesso!");
		
		return SUCCESS;
	}
}
