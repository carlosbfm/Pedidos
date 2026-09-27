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
		
		action("/clientes", ClienteAction.class, "exibir")
		.filter(new PaginationFilter("lista", 5))
		.on(SUCCESS,fwd("jsp/clientes.jsp"));

		action("/itens", ItemAction.class, "cadastro")
		.on(SUCCESS, fwd("jsp/itens-form.jsp"))
		.on(ERROR, fwd("jsp/itens-form.jsp"));

		action("/clientes", ClienteAction.class, "cadastro")
		.on(SUCCESS, fwd("jsp/clientes-form.jsp"))
		.on(ERROR, fwd("jsp/clientes-form.jsp"));

		
	}
}
