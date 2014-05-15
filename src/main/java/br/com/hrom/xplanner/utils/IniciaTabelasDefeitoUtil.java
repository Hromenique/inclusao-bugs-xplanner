package br.com.hrom.xplanner.utils;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.hrom.xplanner.modelo.dao.implementacoes.ClassificacaoDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.implementacoes.CriticidadeDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.implementacoes.TipoDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.IClassificacaoDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.ICriticidadeDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.ITipoDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.ClassificacaoDefeito;
import br.com.hrom.xplanner.modelo.entidades.CriticidadeDefeito;
import br.com.hrom.xplanner.modelo.entidades.TipoDefeito;

/**
 * 
 * Inicia as tabelas tipo_defeito, classificacao_defeito, criticidade_defeito do banco de dados do Xplanner caso as mesmas não tenha sido inicializadas
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class IniciaTabelasDefeitoUtil {
	// TODO Incluir log;
	
	private ICriticidadeDefeitoDAO criticiadadeDAO;
	private ITipoDefeitoDAO tipoDAO;
	private IClassificacaoDefeitoDAO classificacaoDAO;
	private EntityManager entityManager;

	public IniciaTabelasDefeitoUtil(EntityManager entityManager){
		this.entityManager = entityManager;
		
		criticiadadeDAO = new CriticidadeDefeitoDAO(entityManager);
		tipoDAO = new TipoDefeitoDAO(entityManager);
		classificacaoDAO = new ClassificacaoDefeitoDAO(entityManager);
	}	
	
	/**
	 * Inicia as tabelas tipo_defeito, classificacao_defeito, criticidade_defeito do banco de dados do Xplanner caso as mesmas não tenha sido inicializadas
	 */
	
	public void iniciaTabelas() {

		entityManager.getTransaction().begin();
		
		try{
			if (!existeDadosCriticidade()) {
				// Entidades
				CriticidadeDefeito c1 = new CriticidadeDefeito(1, "Baixa", true);
				CriticidadeDefeito c2 = new CriticidadeDefeito(2, "Média", true);
				CriticidadeDefeito c3 = new CriticidadeDefeito(3, "Alta", true);
				
				criticiadadeDAO.salva(c1);
				criticiadadeDAO.salva(c2);
				criticiadadeDAO.salva(c3);
			}
			
			if (!existeDadosTipo()) {
				TipoDefeito t1 = new TipoDefeito(1, "Requisito", true);
				TipoDefeito t2 = new TipoDefeito(2, "Análise", true);
				TipoDefeito t3 = new TipoDefeito(3, "Codificação", true);
				TipoDefeito t4 = new TipoDefeito(4, "Testes", true);
				
				tipoDAO.salva(t1);
				tipoDAO.salva(t2);
				tipoDAO.salva(t3);
				tipoDAO.salva(t4);
			}
			
			if (!existeDadosClassificacao()) {
				ClassificacaoDefeito cl1 = new ClassificacaoDefeito(1, "Impeditivo", true);
				ClassificacaoDefeito cl2 = new ClassificacaoDefeito(1, "Funcional", true);
				ClassificacaoDefeito cl3 = new ClassificacaoDefeito(1, "Interface", true);
				ClassificacaoDefeito cl4 = new ClassificacaoDefeito(1, "Texto", true);
				ClassificacaoDefeito cl5 = new ClassificacaoDefeito(1, "Melhoria", true);
				
				classificacaoDAO.salva(cl1);
				classificacaoDAO.salva(cl2);
				classificacaoDAO.salva(cl3);
				classificacaoDAO.salva(cl4);
				classificacaoDAO.salva(cl5);			
			}
		}
		catch(Exception e){
			entityManager.getTransaction().rollback();
			entityManager.close();
			// TODO Incluir log;
		}
		
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private boolean existeDadosCriticidade() {
		List<TipoDefeito> tipos = tipoDAO.listaTodos();
		
		return existeDados(tipos);
	}

	private boolean existeDadosTipo(){
		List<CriticidadeDefeito> criticidades = criticiadadeDAO.listaTodos();
		
		return existeDados(criticidades);
	}

	private boolean existeDadosClassificacao(){
		List<ClassificacaoDefeito> classificacoes = classificacaoDAO.listaTodos();
		
		return existeDados(classificacoes);
	}

	@SuppressWarnings("rawtypes")
	private boolean existeDados(List dados) {
		if(dados.size() > 0){
			return true;
		}

		return false;
	}
	
	public static void main(String[] args){
		
		JPAUtil jpaUtil = new JPAUtil();
		EntityManagerFactory factory = jpaUtil.getEntityManagerFactory();
		EntityManager manager = jpaUtil.getEntityManager(factory);
		
		IniciaTabelasDefeitoUtil app = new IniciaTabelasDefeitoUtil(manager);
		app.iniciaTabelas();
		
		factory.close();
		manager.close();
	}
}
