package br.com.hrom.xplanner.controlador.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.hrom.xplanner.modelo.entidades.ClassificacaoDefeito;
import br.com.hrom.xplanner.modelo.entidades.CriticidadeDefeito;
import br.com.hrom.xplanner.modelo.entidades.Defeito;
import br.com.hrom.xplanner.modelo.entidades.Person;
import br.com.hrom.xplanner.modelo.entidades.StatusDefeito;
import br.com.hrom.xplanner.modelo.entidades.TipoDefeito;
import br.com.hrom.xplanner.modelo.entidades.UserStory;
import br.com.hrom.xplanner.servico.DefeitoService;

/**
 * Managed Bean (Controlador) para operações que envolvem a entidade/classe Defeito
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */

@Named
@SessionScoped
public class DefeitoMB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Defeito defeitoEdicao;
	private List<Defeito> defeitos;
	
	@Inject
	private DefeitoService defeitoService;
	
	public DefeitoMB(){
		
		this.defeitos = new ArrayList<>();
		
		Person responsavel = new Person(80, "usr_tst", "usuario_teste", "no@reply.com", null, "TST");
		CriticidadeDefeito criticidade = new CriticidadeDefeito(1, "Baixa");
		TipoDefeito tipo = new TipoDefeito(1, "Requisitos");
		ClassificacaoDefeito classificacao = new ClassificacaoDefeito(1,"Impeditivo");
		StatusDefeito status = new StatusDefeito(2, "Em Andamento") ;
		UserStory estoria = new UserStory(800, "teste1", "descrição teste1", responsavel);
		Defeito defeito = new Defeito(1, "defeito1", "teste defeito1", estoria , responsavel , criticidade, tipo, classificacao, status);
		
		this.defeitos.add(defeito);
		
		responsavel = new Person(81, "usr_tst2", "usuario_teste2", "no@reply.com", null, "TST2");				
		criticidade = new CriticidadeDefeito(3, "Alta");
		tipo = new TipoDefeito(3, "Codificação");
		classificacao = new ClassificacaoDefeito(2,"Funcional");
		status = new StatusDefeito(3, "Concluído") ;
		estoria = new UserStory(801, "teste2", "descrição teste2", responsavel);
		defeito = new Defeito(2, "defeito2", "teste defeito2", estoria , responsavel , criticidade, tipo, classificacao, status);
		
		this.defeitos.add(defeito);		
	}	

	public Defeito getDefeitoEdicao() {
		return defeitoEdicao;
	}

	public void setDefeitoEdicao(Defeito defeitoEdicao) {
		this.defeitoEdicao = defeitoEdicao;
	}

	public List<Defeito> getDefeitos() {
		return defeitos;
	}

	public void setDefeitos(List<Defeito> defeitos) {
		this.defeitos = defeitos;
	}	
}
