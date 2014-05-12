package br.com.hrom.xplanner.modelo.dao.implementacoes;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.hrom.xplanner.modelo.dao.interfaces.IStoryDAO;
import br.com.hrom.xplanner.modelo.entidades.UserStory;

/**
 * 
 * Classe com as funções de DAO para a entidade (tabela) story
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */

public class StoryDao extends AbstractDAO<UserStory> implements IStoryDAO{

	@Inject 	
	public StoryDao(EntityManager entityManager) {
		super(entityManager);
	}
}
