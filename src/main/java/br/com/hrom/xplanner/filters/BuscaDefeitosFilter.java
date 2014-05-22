package br.com.hrom.xplanner.filters;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import br.com.hrom.xplanner.interceptors.Transacional;
import br.com.hrom.xplanner.modelo.dao.interfaces.IDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.IStoryDAO;
import br.com.hrom.xplanner.modelo.entidades.Defeito;
import br.com.hrom.xplanner.modelo.entidades.UserStory;

/**
 * Filtro que busca Defeitos a partir de uma estória de usuário (user story)
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
//@WebFilter("/do/view/userstory")
public class BuscaDefeitosFilter implements Filter{

	//TODO FAZER TESTES
	//TODO INCLUIR LOGS
	@Inject
	private IDefeitoDAO defeitoDAO;
	@Inject
	private IStoryDAO storyDAO;
	
	public BuscaDefeitosFilter(){
		
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {		
		
	}

	@Override
	@Transacional
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		List<Defeito> defeitos = null;
		String oid = request.getParameter("oid");
		long idStory = Long.valueOf(oid);
		
		UserStory userStory = storyDAO.buscaPorId(idStory);
		if (userStory != null) {
			defeitos = defeitoDAO.buscaDefeitosPorUserStory(userStory);
		}
	
		request.setAttribute("defeitos", defeitos);		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {		
		
	}

}
