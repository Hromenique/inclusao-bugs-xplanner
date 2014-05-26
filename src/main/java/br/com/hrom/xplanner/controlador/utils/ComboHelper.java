package br.com.hrom.xplanner.controlador.utils;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import br.com.hrom.xplanner.modelo.dao.interfaces.IClassificacaoDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.ICriticidadeDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.IStatusDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.ITipoDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.ClassificacaoDefeito;
import br.com.hrom.xplanner.modelo.entidades.CriticidadeDefeito;
import br.com.hrom.xplanner.modelo.entidades.StatusDefeito;
import br.com.hrom.xplanner.modelo.entidades.TipoDefeito;

/**
 * Esta classe é responsável por gerar listas de objetos que serão utilizados para popular combos nas páginas web (camada de view)
 * 
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */

@ApplicationScoped
public class ComboHelper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ICriticidadeDefeitoDAO criticidadeDAO;
	private IStatusDefeitoDAO statusDAO;
	private ITipoDefeitoDAO tipoDAO;
	private IClassificacaoDefeitoDAO classificacaoDAO;
	
	private final List<CriticidadeDefeito> criticidadesDefeito;
	private final List<StatusDefeito> statusDefeito;
	private final List<TipoDefeito> tiposDefeito;
	private final List<ClassificacaoDefeito> classificacoesDefeito;
	
	@Inject
	public ComboHelper(ICriticidadeDefeitoDAO criticidadeDAO,
			IStatusDefeitoDAO statusDAO, ITipoDefeitoDAO tipoDAO,
			IClassificacaoDefeitoDAO classificacaoDAO) {
		this.classificacaoDAO = classificacaoDAO;
		this.statusDAO = statusDAO;
		this.tipoDAO = tipoDAO;
		this.criticidadeDAO = criticidadeDAO;
		
		this.criticidadesDefeito= this.criticidadeDAO.listaTodos();
		this.classificacoesDefeito = this.classificacaoDAO.listaTodos();
		this.statusDefeito = this.statusDAO.listaTodos();
		this.tiposDefeito = this.tipoDAO.listaTodos();
		
		System.out.println("--------------criou ComboHelper---------------------");
	}
	
	@Produces
	public List<CriticidadeDefeito> getCriticidadesDefeito(){
		return criticidadesDefeito;
	}
	
	@Produces
	public List<StatusDefeito> getStatusDefeito(){
		return statusDefeito;
	}
	
	@Produces
	public List<TipoDefeito> getTiposDefeito(){
		return tiposDefeito;
	}
	
	@Produces
	public List<ClassificacaoDefeito> getClassificacoesDefeito(){
		return classificacoesDefeito;
	}
}
