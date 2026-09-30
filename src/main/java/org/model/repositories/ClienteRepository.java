package org.model.repositories;

import java.util.List;


import org.model.entities.ClienteEntity;

public interface ClienteRepository {
	ClienteEntity buscarPorId(Long id);
	List<ClienteEntity> buscarPorNome(String nome);
	List<ClienteEntity> listarTodos();
	List<ClienteEntity> listarPaginado(int limit, int offset);
    int contarTotal();
	void salvar(ClienteEntity cliente);
    void atualizar(ClienteEntity cliente);
    void excluir(Long id);
}
