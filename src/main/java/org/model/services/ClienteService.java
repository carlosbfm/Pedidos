package org.model.services;

import java.time.LocalDate;
import java.util.List;
import org.model.entities.ClienteEntity;
import org.model.exceptions.NegocioException;
import org.model.repositories.ClienteRepository;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    
    public List<ClienteEntity> listarTodos() {
        return clienteRepository.listarTodos();
    }

    public ClienteEntity buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new NegocioException("ID inválido para busca.");
        }
        ClienteEntity cliente = clienteRepository.buscarPorId(id);
        if (cliente == null) {
            throw new NegocioException("Cliente com ID " + id + " não encontrado.");
        }
        return cliente;
    }

    public List<ClienteEntity> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new NegocioException("O nome para busca não pode estar vazio.");
        }
        return clienteRepository.buscarPorNome(nome.trim());
    }

    public void cadastrar(ClienteEntity cliente) {
        validarCliente(cliente);
        clienteRepository.salvar(cliente);
    }

    public void atualizar(ClienteEntity cliente) {
        if (cliente.getId() == null || cliente.getId() <= 0) {
            throw new NegocioException("ID obrigatório para atualizar o cliente.");
        }
        
        buscarPorId(cliente.getId());
        validarCliente(cliente);

        clienteRepository.atualizar(cliente);
    }

    public void excluir(Long id) {
        if (id == null || id <= 0) {
            throw new NegocioException("ID inválido para exclusão.");
        }
        
        buscarPorId(id);
        clienteRepository.excluir(id);
    }

    private void validarCliente(ClienteEntity cliente) {
        if (cliente == null) {
            throw new NegocioException("Entidade cliente não pode ser nula.");
        }

        if (cliente.getNomeCliente() == null || cliente.getNomeCliente().trim().length() < 3) {
            throw new NegocioException("O nome do cliente deve possuir pelo menos 3 caracteres.");
        }

        if (cliente.getDataNascimento() != null && cliente.getDataNascimento().isAfter(LocalDate.now())) {
            throw new NegocioException("A data de nascimento não pode estar no futuro.");
        }
    }
}