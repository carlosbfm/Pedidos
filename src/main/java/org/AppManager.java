package org;

import org.action.ItemAction;

import org.action.ClienteAction;

import org.mentawai.core.ApplicationManager;
import org.mentawai.filter.PaginationFilter;
public class AppManager extends ApplicationManager {

	@Override
	public void loadActions() {
		
		action("/itens", ItemAction.class, "exibir")
		.filter(new PaginationFilter("lista", 5))
		.on(SUCCESS, fwd("jsp/itens.jsp"));

		action("/novo-item", ItemAction.class, "listarTipoDeUnidades")
		.on(SUCCESS, fwd("/jsp/index.jsp"));
		

		action("/cadastro-item", ItemAction.class, "cadastrar")
		.on(SUCCESS, fwd("jsp/hello.jsp"))
		.on(ERROR, fwd("jsp/index.jsp"));
		
		action("novo-cliente", ClienteAction.class, "cadastro")
        .on(SUCCESS, fwd("/jsp/clientes.jsp"))
        .on(ERROR, fwd("/jsp/clientes.jsp"));

		action("/novo-cliente", ClienteAction.class, "cadastro")
		.on(SUCCESS, fwd("jsp/clientes.jsp"))
		.on(ERROR, fwd("jsp/clientes.jsp"));

		action("/cliente", ClienteAction.class, "index")
		.on(SUCCESS, fwd("jsp/clientes.jsp"));

		action("cliente", ClienteAction.class, "index")
		.on(SUCCESS, fwd("jsp/clientes.jsp"));
	}
}
