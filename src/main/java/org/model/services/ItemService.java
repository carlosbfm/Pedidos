package org.model.services;

import java.math.BigDecimal;
import java.util.List;

import org.model.entities.ItemEntity;
import org.model.exceptions.NegocioException;
import org.model.repositories.ItemRepository;
import org.model.utils.Page;

public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemEntity> listarTodos() {
        return itemRepository.listarTodos();
    }
    
    public Page<ItemEntity> listarPaginado(int pagina, int tamanhoPagina) {
        if (pagina < 1) pagina = 1;
        if (tamanhoPagina <= 0) tamanhoPagina = 5;

        int totalRegistros = itemRepository.contarTotal();
        int offset = (pagina - 1) * tamanhoPagina;

        List<ItemEntity> itens = itemRepository.listarPaginado(tamanhoPagina, offset);
        return new Page<>(itens, pagina, tamanhoPagina, totalRegistros);
    }

    public ItemEntity buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new NegocioException("Identificador inválido para pesquisa.");
        }
        ItemEntity item = itemRepository.buscarPorId(id);
        if (item == null) {
            throw new NegocioException("Item com identificador " + id + " não encontrado.");
        }
        return item;
    }

    public List<ItemEntity> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new NegocioException("O termo de pesquisa não pode estar vazio.");
        }
        return itemRepository.buscarPorNomeItem(nome.trim());
    }

    public void cadastrar(ItemEntity item) {
        validarItem(item);
        itemRepository.salvar(item);
    }

    public void atualizar(ItemEntity item) {
        if (item.getId() == null || item.getId() <= 0) {
            throw new NegocioException("Identificador obrigatório para atualização do item.");
        }

        buscarPorId(item.getId());
        validarItem(item);

        itemRepository.atualizar(item);
    }

    public void excluir(Long id) {
        if (id == null || id <= 0) {
            throw new NegocioException("Identificador inválido para exclusão.");
        }

        buscarPorId(id);
        itemRepository.excluir(id);
    }

    public void adicionarEstoque(Long id, int quantidade) {
        if (quantidade <= 0) {
            throw new NegocioException("A quantidade de entrada em estoque deve ser superior a zero.");
        }

        ItemEntity item = buscarPorId(id);
        item.addQuantidade(quantidade);
        itemRepository.atualizar(item);
    }

    public void removerEstoque(Long id, int quantidade) {
        if (quantidade <= 0) {
            throw new NegocioException("A quantidade de saída de estoque deve ser superior a zero.");
        }

        ItemEntity item = buscarPorId(id);
        try {
            item.removeQuantidade(quantidade);
        } catch (IllegalArgumentException e) {
            throw new NegocioException(e.getMessage());
        }

        itemRepository.atualizar(item);
    }

    public BigDecimal calcularValorTotalEstoque(Long id) {
        ItemEntity item = buscarPorId(id);
        
        /*if(item.getValorTotalEstoque().compareTo(BigDecimal.ZERO) < 0 || item.getValorTotalEstoque() == null ) {
        	throw new NegocioException("O preço total não pode ser menor que zero!");
        }*/
        
        return item.getValorTotalEstoque();
    }

    private void validarItem(ItemEntity item) {
        if (item == null) {
            throw new NegocioException("Os dados do item não podem ser nulos.");
        }

        if (item.getNomeItem() == null || item.getNomeItem().trim().length() < 2) {
            throw new NegocioException("A descrição/nome do item deve conter pelo menos 2 caracteres.");
        }

        if (item.getPrecoItem() == null || item.getPrecoItem().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException("O preço unitário deve ser superior a zero.");
        }

        if (item.getQuantidadeDoItem() == null || item.getQuantidadeDoItem() < 0) {
            throw new NegocioException("A quantidade em estoque não pode ser negativa.");
        }

        if (item.getTipoUnidadeDeMedida() == null) {
            throw new NegocioException("A unidade de medida deve ser obrigatoriamente informada.");
        }
    }
}