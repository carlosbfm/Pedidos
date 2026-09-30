package org.model.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.enums.UnidadeDeMedida;
import org.model.entities.ItemEntity;
import org.model.repositories.ItemRepository;

public class ItemRepositoryImpl implements ItemRepository {

    private final Connection connection;

    public ItemRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ItemEntity buscarPorId(Long id) {
        String sql = "SELECT id, nome_item, preco_unitario, quantidade, tipo_unidade, data_criacao "
                   + "FROM itens WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearItem(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar item pelo ID: " + id, e);
        }

        return null;
    }

    @Override
    public List<ItemEntity> buscarPorNomeItem(String nome) {
        String sql = "SELECT id, nome_item, preco_unitario, quantidade, tipo_unidade, data_criacao "
                   + "FROM itens WHERE LOWER(nome_item) LIKE LOWER(?) ORDER BY nome_item ASC";
        List<ItemEntity> itens = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itens.add(mapearItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens por nome: " + nome, e);
        }

        return itens;
    }

    @Override
    public List<ItemEntity> listarTodos() {
        String sql = "SELECT id, nome_item, preco_unitario, quantidade, tipo_unidade, data_criacao "
                   + "FROM itens ORDER BY id DESC";
        List<ItemEntity> itens = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                itens.add(mapearItem(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todos os itens", e);
        }

        return itens;
    }
    
    @Override
    public List<ItemEntity> listarPaginado(int limit, int offset) {
        String sql = "SELECT id, nome_item, preco_unitario, quantidade, tipo_unidade, data_criacao "
                   + "FROM itens ORDER BY id DESC LIMIT ? OFFSET ?";
        List<ItemEntity> itens = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itens.add(mapearItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens paginados", e);
        }
        return itens;
    }

    @Override
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM itens";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar total de itens", e);
        }
        return 0;
    }

    @Override
    public void salvar(ItemEntity item) {
        String sql = "INSERT INTO itens (nome_item, preco_unitario, quantidade, tipo_unidade,valor_total ,data_criacao) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, item.getNomeItem());
            stmt.setBigDecimal(2, item.getPrecoItem());
            stmt.setInt(3, item.getQuantidadeDoItem() != null ? item.getQuantidadeDoItem() : 0);
            stmt.setString(4, item.getTipoUnidadeDeMedida() != null ? item.getTipoUnidadeDeMedida().name() : null);
            stmt.setBigDecimal(5, item.getValorTotalEstoque());
            stmt.setTimestamp(6, item.getDataHoraEmissao() != null 
                    ? Timestamp.valueOf(item.getDataHoraEmissao()) 
                    : new Timestamp(System.currentTimeMillis()));

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar item: " + item.getNomeItem(), e);
        }
    }

    @Override
    public void atualizar(ItemEntity item) {
        String sql = "UPDATE itens SET nome_item = ?, preco_unitario = ?, quantidade = ?, tipo_unidade = ?, valor_total = ? "
                   + "WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getNomeItem());
            stmt.setBigDecimal(2, item.getPrecoItem());
            stmt.setInt(3, item.getQuantidadeDoItem() != null ? item.getQuantidadeDoItem() : 0);
            stmt.setString(4, item.getTipoUnidadeDeMedida() != null ? item.getTipoUnidadeDeMedida().name() : null);
            stmt.setBigDecimal(5, item.getValorTotalEstoque());
            stmt.setLong(6, item.getId());
            

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar item ID: " + item.getId(), e);
        }
    }

    @Override
    public void excluir(Long id) {
        String sql = "DELETE FROM itens WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir item ID: " + id, e);
        }
    }

    private ItemEntity mapearItem(ResultSet rs) throws SQLException {
        String unidadeStr = rs.getString("tipo_unidade");
        UnidadeDeMedida unidade = null;
        if (unidadeStr != null && !unidadeStr.trim().isEmpty()) {
            try {
                unidade = UnidadeDeMedida.valueOf(unidadeStr.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                unidade = null;
            }
        }

        Timestamp timestamp = rs.getTimestamp("data_criacao");

        return new ItemEntity(
            rs.getLong("id"),
            rs.getString("nome_item"),
            rs.getBigDecimal("preco_unitario"),
            rs.getInt("quantidade"),
            unidade,
            timestamp != null ? timestamp.toLocalDateTime() : null
        );
    }
}