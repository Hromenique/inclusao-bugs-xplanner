package br.com.hrom.xplanner.controlador.utils;

import java.util.List;

import br.com.hrom.xplanner.modelo.dao.interfaces.IClassificacaoDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.ICriticidadeDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.IStatusDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.ITipoDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.ClassificacaoDefeito;
import br.com.hrom.xplanner.modelo.entidades.CriticidadeDefeito;
import br.com.hrom.xplanner.modelo.entidades.StatusDefeito;
import br.com.hrom.xplanner.modelo.entidades.TipoDefeito;

/**
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */


public class ComboHelper {
	
	private ICriticidadeDefeitoDAO criticidadeDAO;
	private IStatusDefeitoDAO statusDAO;
	private ITipoDefeitoDAO tipoDAO;
	private IClassificacaoDefeitoDAO classificacaoDAO;
	
	private List<CriticidadeDefeito> criticidadesDefeito;
	private List<StatusDefeito> statusDefeito;
	private List<TipoDefeito> tiposDefeito;
	private List<ClassificacaoDefeito> classificacoesDefeito;
	
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
	}
	
	public List<CriticidadeDefeito> getCriticidadesDefeito(){
		return criticidadesDefeito;
	}
	
	public List<StatusDefeito> getStatusDefeito(){
		return statusDefeito;
	}
	
	public List<TipoDefeito> getTiposDefeito(){
		return tiposDefeito;
	}
	
	public List<ClassificacaoDefeito> getClassificacoesDefeito(){
		return classificacoesDefeito;
	}
}
