package br.com.hrom.xplanner.modelo.dao.implementacoes;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.hrom.xplanner.modelo.dao.interfaces.ICriticidadeDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.CriticidadeDefeito;

/**
 * 
 * Classe com as funções de DAO para a entidade (tabela) criticidade
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class CriticidadeDefeitoDAO extends AbstractDAO<CriticidadeDefeito> implements ICriticidadeDefeitoDAO{

	@Inject
	public CriticidadeDefeitoDAO(EntityManager entityManager) {
		super(entityManager);		
	}
}
