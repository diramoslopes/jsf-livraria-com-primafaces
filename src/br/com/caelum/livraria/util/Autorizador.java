package br.com.caelum.livraria.util;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import br.com.caelum.livraria.modelo.Usuario;

public class Autorizador implements PhaseListener {

	@Override
	public void afterPhase(PhaseEvent event) {
		// EXECUTA DEPOIS DA FASE
		
		FacesContext context = event.getFacesContext();
		String nomePagina = context.getViewRoot().getViewId();
		
		System.out.println(nomePagina);
		
		if("/login-css.xhtml".equals(nomePagina)) {
			return;
		}
		
		Usuario usuarioLogado = (Usuario) context.getExternalContext()
				.getSessionMap().get("usuarioLogado");
		
		if(usuarioLogado != null) {
			return;
		}
		
		NavigationHandler handler = context.getApplication().getNavigationHandler();
		handler.handleNavigation(context, null, "/login-css?faces-redirect=true");
		
		context.getRenderResponse();
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		// ANTES DA FASE
		
	}

	@Override
	public PhaseId getPhaseId() {
		// TODO Auto-generated method stub
		return PhaseId.RESTORE_VIEW;
	}

}
