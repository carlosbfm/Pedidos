package org;

import org.action.ItemAction;
import org.action.PedidosAction;

import java.sql.SQLException;

import org.action.ClienteAction;

import org.mentawai.core.ApplicationManager;
import java.sql.Connection;
import org.mentawai.core.Props;
import org.mentawai.db.BoneCPConnectionHandler;
import org.mentawai.db.ConnectionHandler;
import org.mentawai.filter.PaginationFilter;

public class AppManager extends ApplicationManager {

	@Override
	public void loadActions() {

		action("/itens", ItemAction.class, "exibir")
		.filter(new PaginationFilter("lista", 5))
		.on(SUCCESS, fwd("jsp/itens.jsp"));

		action("/clientes", ClienteAction.class, "exibir")
		.filter(new PaginationFilter("lista", 5))
		.on(SUCCESS, fwd("jsp/clientes.jsp"));

		action("/clientes", ClienteAction.class, "exibirCliente")
		.on(SUCCESS, fwd("jsp/clientes-update.jsp")) 
		.addConsequence(ERROR, fwd("jsp/clientes.jsp"));

		action("/clientes", ClienteAction.class, "atualizarCliente")
		.on(SUCCESS, redir("clientes.exibir.mtw")) 
		.addConsequence(ERROR, fwd("jsp/clientes-update.jsp"));

		action("/itens", ItemAction.class, "exibirItem")
		.on(SUCCESS, fwd("jsp/itens-update.jsp")) 
		.addConsequence(ERROR, fwd("jsp/itens.jsp"));

		action("/itens", ItemAction.class, "atualizarItem")
		.on(SUCCESS, redir("itens.exibir.mtw")) 
		.addConsequence(ERROR, fwd("jsp/itens-update.jsp"));

		action("/itens", ItemAction.class, "cadastro")
		.on(SUCCESS, fwd("jsp/itens-form.jsp"))
		.addConsequence(ERROR, fwd("jsp/itens-form.jsp"));

		action("/itens", ItemAction.class, "excluir")
		.on(SUCCESS, redir("itens.exibir.mtw"))
		.addConsequence(ERROR, fwd("jsp/itens.jsp"));

		action("/clientes", ClienteAction.class, "excluir")
		.on(SUCCESS, redir("clientes.exibir.mtw"))
		.addConsequence(ERROR, fwd("jsp/clientes.jsp"));

		action("/clientes", ClienteAction.class, "cadastro")
		.on(SUCCESS, fwd("jsp/clientes-form.jsp"))
		.addConsequence(ERROR, fwd("jsp/clientes-form.jsp"));

		action("/selecionar", PedidosAction.class, "clientes")
		.on(SUCCESS, fwd("jsp/pedidos-selecionar-cliente.jsp"));
	}

	private static ConnectionHandler pool;

	@Override
	public ConnectionHandler createConnectionHandler() {
		Props props = getProps();
		String driver = props.getString("jdbc.driver");
		String url = props.getString("jdbc.url");
		String user = props.getString("jdbc.user");
		String pass = props.getString("jdbc.pass");

		pool = new BoneCPConnectionHandler(driver, url, user, pass);
		return pool;
	}

	public static Connection getConnection() throws SQLException {
		if (pool == null) {
			Props props = new Props();
			String driver = props.getString("jdbc.driver");
			String url = props.getString("jdbc.url");
			String user = props.getString("jdbc.user");
			String pass = props.getString("jdbc.pass");
			pool = new BoneCPConnectionHandler(driver, url, user, pass);
		}
		return pool.getConnection();
	}

	@Override
    public void init() {
        super.init();
        System.out.println(">>> PASSOU NO INIT DO APPMANAGER <<<");
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println(">>> [DB SUCCESS] Conexão com MariaDB estabelecida com sucesso!");
                System.out.println(">>> [DB INFO] DBMS: " + conn.getMetaData().getDatabaseProductName() + " " + conn.getMetaData().getDatabaseProductVersion());
            }
        } catch (Exception e) {
            System.err.println(">>> [DB ERROR] Falha ao testar conexão no init:");
            e.printStackTrace();
        }
    }


}