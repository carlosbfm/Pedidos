package org.action;

import org.mentawai.core.BaseAction;

public class HelloAction extends BaseAction{
	public String hi() {
		String msg = input.getString("msg");
		if(isEmpty(msg)) {
			msg = "Mensagem vazia";
		}
		
		output.setValue("msg", msg);
		return SUCCESS;
	}
}
