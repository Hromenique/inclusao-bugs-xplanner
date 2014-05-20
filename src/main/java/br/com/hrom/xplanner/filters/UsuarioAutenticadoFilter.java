package br.com.hrom.xplanner.filters;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.technoetic.xplanner.security.SecurityHelper;

/**
 * Filtro que recupera o usuário autenticado e o coloca no objeto HttpRequest para ser recuperado nas páginas xhtml<b/>
 *<b>Observação: </b>As páginas .jsp (versão antiga do xplanner) fazer este processo por meio de tags customizadas
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
@WebFilter("*.xhtml")
public class UsuarioAutenticadoFilter implements Filter{

	//TODO fazer testes
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {		
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		try {
			setUsuarioAutenticado(request);
		} catch (JspException e) {
			//TODO inclusão de log
			e.printStackTrace();
		}
		
		chain.doFilter(request, response);
		
	}
	
	@Override
	public void destroy() {
	}

	private void setUsuarioAutenticado(ServletRequest request) throws JspException {
		try {
			if (SecurityHelper.isUserAuthenticated((HttpServletRequest) request)) {
				Principal userPrincipal = SecurityHelper.getUserPrincipal((HttpServletRequest) request);
				String usuario = userPrincipal.getName();
				request.setAttribute("usuario", usuario);
			}else{
				request.setAttribute("usuario", null);
			}
		} 
		catch (Exception e) {
	         throw new JspException(e.getMessage());
		}
	}	
}


