package br.com.hrom.xplanner.modelo.dao.implementacoes;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.hrom.xplanner.modelo.dao.interfaces.IDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.Defeito;

/**
 * 
 * Classe com as funções de DAO para a entidade (tabela) defeito
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class DefeitoDAO extends AbstractDAO<Defeito> implements IDefeitoDAO {

	@Inject
	public DefeitoDAO(EntityManager entityManager) {
		super(entityManager);
	}
}
