package org.model.repositories;

import java.util.List;

import org.model.entities.ClienteEntity;

public interface ClienteRepository {
	ClienteEntity buscarPorId(Integer id);
	List<ClienteEntity> buscarPorNome(String nome);
	void salvar(ClienteEntity cliente);
    void atualizar(ClienteEntity cliente);
    void excluir(Integer id);
}
