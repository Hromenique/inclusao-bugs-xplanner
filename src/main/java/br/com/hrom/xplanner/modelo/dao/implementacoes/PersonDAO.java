package br.com.hrom.xplanner.modelo.dao.implementacoes;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.hrom.xplanner.modelo.dao.interfaces.IPersonDAO;
import br.com.hrom.xplanner.modelo.entidades.Person;

/**
 * 
 * Classe com as funções de DAO para a entidade (tabela) person
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class PersonDAO extends AbstractDAO<Person> implements IPersonDAO {

	@Inject
	public PersonDAO(EntityManager entityManager) {
		super(entityManager);		
	}
}
