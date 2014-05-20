package br.com.hrom.xplanner.modelo.dao.implementacoes;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.hrom.xplanner.modelo.dao.interfaces.IDAO;

/**
 * Classe abstrata com as funções básicas do DAO
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 * @param <T> Classe que representa a entidade (tabela) do banco de dados
 */
public class AbstractDAO<T> implements IDAO<T> {

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
	
	/**
	 * Cria um objeto do tipo TypedQuery (query que retorna um determinado tipo de objeto)
	 * 
	 * @param jpql String que representa uma consulta JPQL
	 * @param parametros Map de parâmetros para a consulta. Caso a consulta não possua parâmetros, utilizar null
	 * @return Query representada pela consulta jpql e parâmetros
	 */
	protected TypedQuery<T> criaTypedQuery(String jpql, Map<String, Object> parametros){
		TypedQuery<T> query = this.entityManager.createQuery(jpql, entidadePersistida);		
		
		if (parametros != null ? !parametros.isEmpty() : false) {
			for (Entry<String, Object> entry : parametros.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		
		return query;
	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}
}
