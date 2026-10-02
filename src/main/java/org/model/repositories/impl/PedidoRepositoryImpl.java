package org.model.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.enums.FormaDePagamento;
import org.enums.StatusPedido;
import org.model.entities.ClienteEntity;
import org.model.entities.ItemEntity;
import org.model.entities.PagamentoEntity;
import org.model.entities.PedidosEntity;
import org.model.repositories.PedidoRepository;

public class PedidoRepositoryImpl implements PedidoRepository {

    private final Connection connection;

    public PedidoRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PedidosEntity buscarPorId(Long id) {
        String sql = "SELECT p.id, p.status, p.data_pedido, "
                   + "       c.id AS cliente_id, c.nome, c.data_nascimento, c.data_criacao "
                   + "FROM pedidos p "
                   + "INNER JOIN cliente c ON p.id_cliente = c.id "
                   + "WHERE p.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPedidoCabecalho(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido por ID: " + id, e);
        }

        return null;
    }

    @Override
    public PedidosEntity buscarPorIdItens(Long id) {
        PedidosEntity pedido = buscarPorId(id);
        if (pedido == null) {
            return null;
        }

        String sql = "SELECT id_item, nomeItem, preco_do_item, quantidade_comprada, "
                   + "       forma_pagamento, valor_total "
                   + "FROM mov_pedidos WHERE id_pedido = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemEntity item = new ItemEntity();
                    item.setId(rs.getLong("id_item"));
                    item.setNomeItem(rs.getString("nomeItem"));
                    item.setPrecoItem(rs.getBigDecimal("preco_do_item"));
                    item.addQuantidade(rs.getInt("quantidade_comprada"));

                    pedido.adicionarItem(item);

                    if (pedido.getFormaPagamento() == null) {
                        String formaStr = rs.getString("forma_pagamento");
                        if (formaStr != null && !formaStr.trim().isEmpty()) {
                            FormaDePagamento forma = FormaDePagamento.valueOf(formaStr.trim().toUpperCase());
                            pedido.setFormaPagamento(new PagamentoEntity(rs.getBigDecimal("valor_total"), forma));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar itens do pedido: " + id, e);
        }

        return pedido;
    }

    @Override
    public List<PedidosEntity> buscarPorStatus(StatusPedido status) {
        String sql = "SELECT p.id, p.status, p.data_pedido, "
                   + "       c.id AS cliente_id, c.nome, c.data_nascimento, c.data_criacao "
                   + "FROM pedidos p "
                   + "INNER JOIN cliente c ON p.id_cliente = c.id "
                   + "WHERE p.status = ? ORDER BY p.id DESC";
        List<PedidosEntity> pedidos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status != null ? status.name() : "");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedidoCabecalho(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedidos por status: " + status, e);
        }

        return pedidos;
    }

    @Override
    public List<PedidosEntity> buscarPorClienteId(Long clienteId) {
        String sql = "SELECT p.id, p.status, p.data_pedido, "
                   + "       c.id AS cliente_id, c.nome, c.data_nascimento, c.data_criacao "
                   + "FROM pedidos p "
                   + "INNER JOIN cliente c ON p.id_cliente = c.id "
                   + "WHERE p.id_cliente = ? ORDER BY p.id DESC";
        List<PedidosEntity> pedidos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, clienteId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedidoCabecalho(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedidos do cliente ID: " + clienteId, e);
        }

        return pedidos;
    }

    @Override
    public List<PedidosEntity> listarTodos() {
        String sql = "SELECT p.id, p.status, p.data_pedido, "
                   + "       c.id AS cliente_id, c.nome, c.data_nascimento, c.data_criacao "
                   + "FROM pedidos p "
                   + "INNER JOIN cliente c ON p.id_cliente = c.id "
                   + "ORDER BY p.id DESC";
        List<PedidosEntity> pedidos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pedidos.add(mapearPedidoCabecalho(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todos os pedidos", e);
        }

        return pedidos;
    }

    @Override
    public List<PedidosEntity> listarPaginado(int limit, int offset) {
        String sql = "SELECT p.id, p.status, p.data_pedido, "
                   + "       c.id AS cliente_id, c.nome, c.data_nascimento, c.data_criacao "
                   + "FROM pedidos p "
                   + "INNER JOIN cliente c ON p.id_cliente = c.id "
                   + "ORDER BY p.id DESC LIMIT ? OFFSET ?";
        List<PedidosEntity> pedidos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedidoCabecalho(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pedidos paginados", e);
        }

        return pedidos;
    }

    @Override
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM pedidos";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar total de pedidos", e);
        }
        return 0;
    }

    @Override
    public void salvar(PedidosEntity pedido) {
        String sqlPedido = "INSERT INTO pedidos (id_cliente, status, data_pedido) VALUES (?, ?, ?)";
        String sqlMov = "INSERT INTO mov_pedidos (id_pedido, id_item, forma_pagamento, quantidade_comprada, "
                      + "preco_do_item, nomeItem, valor_total, status_pedido, data_pedido) "
                      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        boolean autoCommitOriginal = true;
        try {
            autoCommitOriginal = connection.getAutoCommit();
            connection.setAutoCommit(false); 

            try (PreparedStatement stmtPedido = connection.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmtPedido.setLong(1, pedido.getCliente().getId());
                stmtPedido.setString(2, pedido.getStatus() != null ? pedido.getStatus().name() : StatusPedido.CRIADO.name());
                stmtPedido.setTimestamp(3, pedido.getDataHoraEmissao() != null 
                        ? Timestamp.valueOf(pedido.getDataHoraEmissao()) 
                        : new Timestamp(System.currentTimeMillis()));

                stmtPedido.executeUpdate();

                try (ResultSet generatedKeys = stmtPedido.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pedido.setId(generatedKeys.getLong(1));
                    }
                }
            }

            try (PreparedStatement stmtMov = connection.prepareStatement(sqlMov)) {
                String formaPagto = (pedido.getFormaPagamento() != null && pedido.getFormaPagamento().getFormaDePagamento() != null)
                        ? pedido.getFormaPagamento().getFormaDePagamento().name()
                        : "A_VISTA";

                for (ItemEntity item : pedido.getItens()) {
                    stmtMov.setLong(1, pedido.getId());
                    if (item.getId() != null) {
                        stmtMov.setLong(2, item.getId());
                    } else {
                        stmtMov.setNull(2, java.sql.Types.BIGINT);
                    }
                    stmtMov.setString(3, formaPagto);
                    int qtd = (pedido.getQuantidadeComprada() != null && pedido.getQuantidadeComprada() > 0)
                            ? pedido.getQuantidadeComprada()
                            : item.getQuantidadeDoItem();
                    stmtMov.setInt(4, qtd);
                    stmtMov.setBigDecimal(5, item.getPrecoItem());
                    stmtMov.setString(6, item.getNomeItem());
                    stmtMov.setBigDecimal(7, item.getPrecoItem().multiply(java.math.BigDecimal.valueOf(qtd)));
                    stmtMov.setString(8, pedido.getStatus() != null ? pedido.getStatus().name() : StatusPedido.CRIADO.name());
                    stmtMov.setTimestamp(9, Timestamp.valueOf(pedido.getDataHoraEmissao()));

                    stmtMov.addBatch();
                }
                stmtMov.executeBatch();
            }

            connection.commit(); 
        } catch (SQLException e) {
            try {
                connection.rollback(); 
            } catch (SQLException ex) {
            	
            }
            throw new RuntimeException("Erro ao salvar pedido de forma transacional: " + e.getMessage(), e);
        } finally {
            try {
                connection.setAutoCommit(autoCommitOriginal);
            } catch (SQLException e) {
            
            }
        }
    }

    @Override
    public void atualizar(PedidosEntity pedido) {
        String sql = "UPDATE pedidos SET status = ?, id_cliente = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pedido.getStatus().name());
            stmt.setLong(2, pedido.getCliente().getId());
            stmt.setLong(3, pedido.getId());

            stmt.executeUpdate();

            try (PreparedStatement stmtMov = connection.prepareStatement("UPDATE mov_pedidos SET status_pedido = ? WHERE id_pedido = ?")) {
                stmtMov.setString(1, pedido.getStatus().name());
                stmtMov.setLong(2, pedido.getId());
                stmtMov.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status do pedido ID: " + pedido.getId(), e);
        }
    }

    @Override
    public void salvarOuAtualizar(PedidosEntity pedido) {
        if (pedido.getId() == null || pedido.getId() <= 0) {
            salvar(pedido);
        } else {
            atualizar(pedido);
        }
    }

    @Override
    public void excluir(Long id) {
        String sql = "DELETE FROM pedidos WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir pedido ID: " + id, e);
        }
    }
    
    @Override
    public void removerItemDoPedido(Long pedidoId, Long itemId) {
        String sql = "DELETE FROM mov_pedidos WHERE id_pedido = ? AND id_item = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, pedidoId);
            stmt.setLong(2, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover item do pedido: " + e.getMessage(), e);
        }
    }

    private PedidosEntity mapearPedidoCabecalho(ResultSet rs) throws SQLException {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(rs.getLong("cliente_id"));
        cliente.setNomeCliente(rs.getString("nome"));
        
        java.sql.Date dataNasc = rs.getDate("data_nascimento");
        if (dataNasc != null) {
            cliente.setDataNascimento(dataNasc.toLocalDate());
        }

        StatusPedido status = null;
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            try {
                status = StatusPedido.valueOf(statusStr.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                status = StatusPedido.CRIADO;
            }
        }

        Timestamp dataPedido = rs.getTimestamp("data_pedido");

        PedidosEntity pedido = new PedidosEntity();
        pedido.setId(rs.getLong("id"));
        pedido.setCliente(cliente);
        pedido.setStatus(status);
        pedido.setDataHoraEmissao(dataPedido != null ? dataPedido.toLocalDateTime() : null);

        return pedido;
    }
}