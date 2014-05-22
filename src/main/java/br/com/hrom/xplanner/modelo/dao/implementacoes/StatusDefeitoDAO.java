package br.com.hrom.xplanner.modelo.dao.implementacoes;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.hrom.xplanner.modelo.dao.interfaces.IStatusDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.StatusDefeito;

/**
 * 
 * Classe com as funções de DAO para a entidade (tabela) status_defeito
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class StatusDefeitoDAO extends AbstractDAO<StatusDefeito> implements
		IStatusDefeitoDAO {

	@Inject
	public StatusDefeitoDAO(EntityManager entityManager) {
		super(entityManager);		
	}
}
