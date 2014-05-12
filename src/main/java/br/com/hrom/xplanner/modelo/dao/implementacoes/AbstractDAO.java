package br.com.hrom.xplanner.modelo.dao.implementacoes;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.hrom.xplanner.modelo.dao.interfaces.IDao;

/**
 * Classe abstrata com as funções básicas do DAO
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 * @param <T> Classe que representa a entidade (tabela) do banco de dados
 */
public class AbstractDAO<T> implements IDao<T> {

	private Class<T> entidadePersistida;
	private EntityManager entityManager;
	
	@Inject
	public AbstractDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.entidadePersistida = (Class) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Override
	public List<T> listaTodos() {
		String entidade = this.entidadePersistida.getSimpleName();
		String sql = "SELECT entidade FROM " + entidade + " entidade";

		TypedQuery<T> query = entityManager.createQuery(sql, entidadePersistida);
		 List<T> resultList = query.getResultList();

		return resultList;	
	}

	@Override
	public T buscaPorId(Object id) {
		T entidade = entityManager.find(entidadePersistida, id);
		return entidade;
	}

	@Override
	public void salva(T entidade) {
		entityManager.persist(entidade);		
	}

	@Override
	public void atualiza(T entidade) {
		entityManager.merge(entidade);	
	}

	@Override
	public void exclui(Object id) {
		entityManager.remove(id);		
	}
}
