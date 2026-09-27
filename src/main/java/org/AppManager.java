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

		
	}
}
