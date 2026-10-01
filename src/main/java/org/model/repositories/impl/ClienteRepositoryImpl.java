package org.model.repositories.impl;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.model.entities.ClienteEntity;
import org.model.repositories.ClienteRepository;

public class ClienteRepositoryImpl implements ClienteRepository {

    private final Connection connection;

    public ClienteRepositoryImpl(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public List<ClienteEntity> listarPaginado(int limit, int offset) {
        String sql = "SELECT id, nome, data_nascimento, data_criacao "
                   + "FROM cliente ORDER BY id DESC LIMIT ? OFFSET ?";
        List<ClienteEntity> clientes = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearCliente(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar clientes paginados", e);
        }
        return clientes;
    }

    @Override
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM cliente";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar total de clientes", e);
        }
        return 0;
    }

    @Override
    public ClienteEntity buscarPorId(Long id) {
        String sql = "SELECT id, nome,data_nascimento,data_criacao FROM cliente WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por ID: " + id, e);
        }
        
        return null;
    }

    @Override
    public List<ClienteEntity> buscarPorNome(String nome) {
        String sql = "SELECT id, nome,data_nascimento,data_criacao FROM cliente WHERE LOWER(nome) LIKE LOWER(?)";
        List<ClienteEntity> lista = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCliente(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar clientes por nome: " + nome, e);
        }
        
        return lista;
    }
    
    @Override
    public List<ClienteEntity> listarTodos() {
        String sql = "SELECT id, nome, data_nascimento, data_criacao FROM cliente ORDER BY id DESC";
        List<ClienteEntity> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todos os clientes", e);
        }

        return lista;
    }

    @Override
    public void salvar(ClienteEntity cliente) {
        String sql = "INSERT INTO cliente (nome, data_nascimento, data_criacao) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNomeCliente());

            if (cliente.getDataNascimento() != null) {
                stmt.setDate(2, Date.valueOf(cliente.getDataNascimento()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }

            LocalDateTime dataCriacao = cliente.getDataCadastroCliente() != null 
                    ? cliente.getDataCadastroCliente() 
                    : LocalDateTime.now();
            stmt.setTimestamp(3, Timestamp.valueOf(dataCriacao));

            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente", e);
        }
    }

    @Override
    public void atualizar(ClienteEntity cliente) {
        String sql = "UPDATE cliente SET nome = ?, data_nascimento = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNomeCliente());

            if (cliente.getDataNascimento() != null) {
                stmt.setDate(2, Date.valueOf(cliente.getDataNascimento()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }

            stmt.setLong(3, cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente ID: " + cliente.getId(), e);
        }
    }

    @Override
    public void excluir(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente ID: " + id, e);
        }
    }

    
    private ClienteEntity mapearCliente(ResultSet rs) throws SQLException {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(rs.getLong("id"));
        cliente.setNomeCliente(rs.getString("nome"));

        Date dataNascSql = rs.getDate("data_nascimento");
        if (dataNascSql != null) {
            cliente.setDataNascimento(dataNascSql.toLocalDate());
        }

        Timestamp dataCriacaoSql = rs.getTimestamp("data_criacao");
        if (dataCriacaoSql != null) {
            cliente.setDataCadastroCliente(dataCriacaoSql.toLocalDateTime());
        }

        return cliente;
    }
}