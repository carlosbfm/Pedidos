package org.action;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.AppManager;
import org.enums.UnidadeDeMedida;
import org.mentawai.core.BaseAction;
import org.model.entities.ItemEntity;
import org.model.exceptions.NegocioException;
import org.model.repositories.ItemRepository;
import org.model.repositories.impl.ItemRepositoryImpl;
import org.model.services.ItemService;
import org.model.utils.Page;

public class ItemAction extends BaseAction {

	public String exibir() {
	    int paginaAtual = input.getInt("page", 1);
	    int limite = 5;

	    try (Connection conn = AppManager.getConnection()) {
	        ItemRepository repo = new ItemRepositoryImpl(conn);
	        ItemService service = new ItemService(repo);

	        Page<ItemEntity> pagina = service.listarPaginado(paginaAtual, limite);

	        output.setValue("lista", pagina.getRegistros());
	        output.setValue("paginaAtual", pagina.getPaginaAtual());
	        output.setValue("totalPaginas", pagina.getTotalPaginas());
	        output.setValue("totalRegistros", pagina.getTotalRegistros());

	        return SUCCESS;
	    } catch (NegocioException e) {
	        addError("erro", e.getMessage());
	        return ERROR;
	    } catch (Exception e) {
	        addError("erro", "Erro ao carregar itens: " + e.getMessage());
	        return ERROR;
	    }
	}

    public String cadastro() {
        output.setValue("listaTipoUnd", UnidadeDeMedida.values());
        String nome = input.getString("itemNome");
        String precoUnitarioStr = input.getString("itemPreco");
        String tipoUnidade = input.getString("tipoUnidade");
        String quantidadeStr = input.getString("itemQuantidade");

        if (isEmpty(nome)) {
            addError("erro", "O nome do produto é obrigatório.");
            return ERROR;
        }

        if (isEmpty(precoUnitarioStr)) {
            addError("erro", "O preço unitário do produto é obrigatório.");
            return ERROR;
        }

        BigDecimal precoUnitario;
        try {
            String formatado = precoUnitarioStr.replace("R$", "").replace(",", ".").trim();
            precoUnitario = new BigDecimal(formatado);
        } catch (NumberFormatException e) {
            addError("erro", "O formato do preço unitário informado é inválido.");
            return ERROR;
        }

        UnidadeDeMedida tipo;
        try {
            if (isEmpty(tipoUnidade)) {
                addError("erro", "A unidade de medida é obrigatória.");
                return ERROR;
            }
            tipo = UnidadeDeMedida.fromString(tipoUnidade.trim());
        } catch (Exception e) {
            addError("erro", "A unidade de medida selecionada é inválida.");
            return ERROR;
        }

        int quantidade;
        try {
            if (isEmpty(quantidadeStr)) {
                addError("erro", "A quantidade é obrigatória.");
                return ERROR;
            }
            quantidade = Integer.parseInt(quantidadeStr.trim());
        } catch (NumberFormatException e) {
            addError("erro", "A quantidade informada deve ser um número inteiro válido.");
            return ERROR;
        }

        try (Connection conn = AppManager.getConnection()) {
            ItemRepository repo = new ItemRepositoryImpl(conn);
            ItemService service = new ItemService(repo);

            ItemEntity item = new ItemEntity(nome, precoUnitario, tipo, quantidade);
            service.cadastrar(item);

            output.setValue("item", item);
            output.setValue("mensagem", "Item cadastrado com sucesso!");
            return SUCCESS;
        } catch (NegocioException e) {
            addError("erro", e.getMessage());
            return ERROR;
        } catch (Exception e) {
            addError("erro", "Falha técnica ao gravar item: " + e.getMessage());
            return ERROR;
        }
    }

    public String exibirItem() {
        output.setValue("listaTipoUnd", UnidadeDeMedida.values());
        String idStr = input.getString("id");

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

        try (Connection conn = AppManager.getConnection()) {
            ItemRepository repo = new ItemRepositoryImpl(conn);
            ItemService service = new ItemService(repo);

            ItemEntity item = service.buscarPorId(id);

            output.setValue("id", item.getId());
            output.setValue("nomeItem", item.getNomeItem());
            output.setValue("precoItem", item.getPrecoItem());
            output.setValue("quantidadeDoItem", item.getQuantidadeDoItem());
            output.setValue("tipoUnidadeDeMedida", item.getTipoUnidadeDeMedida());
            output.setValue("item", item);

            return SUCCESS;
        } catch (NegocioException e) {
            addError("erro", e.getMessage());
            return ERROR;
        } catch (Exception e) {
            addError("erro", "Erro ao recuperar dados do item: " + e.getMessage());
            return ERROR;
        }
    }

    public String atualizarItem() {
        output.setValue("listaTipoUnd", UnidadeDeMedida.values());

        Long id = input.getLong("id");
        String nome = input.getString("nomeItem");
        String precoUnitarioStr = input.getString("precoItem");
        String tipoUnidade = input.getString("tipoUnidadeDeMedida");
        int novaQuantidade = input.getInt("quantidadeDoItem", -1);

        if (id == null || isEmpty(nome) || isEmpty(precoUnitarioStr) || novaQuantidade < 0) {
            addError("erro", "Todos os campos devem ser preenchidos corretamente.");
            return ERROR;
        }

        BigDecimal novoPreco;
        try {
            String formatado = precoUnitarioStr.replace("R$", "").replace(",", ".").trim();
            novoPreco = new BigDecimal(formatado);
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

        try (Connection conn = AppManager.getConnection()) {
            ItemRepository repo = new ItemRepositoryImpl(conn);
            ItemService service = new ItemService(repo);

            ItemEntity item = service.buscarPorId(id);
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

            service.atualizar(item);

            output.setValue("mensagem", "Item atualizado com sucesso!");
            return SUCCESS;
        } catch (NegocioException e) {
            addError("erro", e.getMessage());
            return ERROR;
        } catch (Exception e) {
            addError("erro", "Erro ao salvar alterações do item: " + e.getMessage());
            return ERROR;
        }
    }

    public String excluir() {
        String idStr = input.getString("id");

        if (idStr == null || isEmpty(idStr)) {
            addError("erro", "Identificador do item não fornecido.");
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
            ItemRepository repo = new ItemRepositoryImpl(conn);
            ItemService service = new ItemService(repo);

            service.excluir(id);

            output.setValue("mensagem", "Item excluído com sucesso!");
            return SUCCESS;
        } catch (NegocioException e) {
            addError("erro", e.getMessage());
            return ERROR;
        } catch (Exception e) {
            addError("erro", "Erro ao excluir o item da base de dados: " + e.getMessage());
            return ERROR;
        }
    }
}