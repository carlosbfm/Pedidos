package org;

import org.action.ItemAction;
import org.mentawai.core.ApplicationManager;

public class AppManager extends ApplicationManager {
	
	@Override
	public void loadActions() {
		
		action("/novo-item", ItemAction.class, "listarTipoDeUnidades")
        .on(SUCCESS, fwd("/jsp/index.jsp"));
		
		action("/cadastro-item", ItemAction.class, "cadastrar")
		.on(SUCCESS, fwd("jsp/hello.jsp"))
		.on(ERROR, fwd("jsp/index.jsp"));
	}
}
